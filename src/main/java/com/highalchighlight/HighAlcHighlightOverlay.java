package com.highalchighlight;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.highalchighlight.config.FireRuneSource;
import com.highalchighlight.config.HighlightStyle;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class HighAlcHighlightOverlay extends WidgetItemOverlay
{
	private final Client client;
    private final ItemManager itemManager;
    private final HighAlcHighlightConfig config;
    private final ConfigManager configManager;
    private final Cache<Long, Image> fillCache;
    private Set<String> exceptionExact = Collections.emptySet();
    private List<Pattern> exceptionPatterns = Collections.emptyList();
    private static final double GE_TAX_RATE = 0.02;
    private static final int GE_TAX_THRESHOLD = 5000000;
    private static final String INVENTORY_TAGS_GROUP = "inventorytags";
    private static final String INVENTORY_TAGS_KEY_PREFIX = "tag_";
    @Inject
    private HighAlcHighlightOverlay(Client client, ItemManager itemManager, HighAlcHighlightConfig config, ConfigManager configManager)
    {
    	this.client = client;
        this.itemManager = itemManager;
        this.config = config;
        this.configManager = configManager;
        fillCache = CacheBuilder.newBuilder()
            .concurrencyLevel(1)
            .maximumSize(32)
            .build();
        showOnInventory();
        showOnBank();
        rebuildExceptionSet();
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget)
    {
        if (checkInterfaceIsHighlightable(itemWidget)) {
            double gePrice = getAdjustedGePrice(itemId);
            int profitPerCast = getProfit(itemId, gePrice);
            boolean isSellable = isSellable(itemId);

            if ((profitPerCast > 0) && (isSellable || config.highlightUnsellables())) {
                if (config.respectInventoryTags() && hasInventoryTag(itemId)) {
                    return;
                }

                final String itemName = itemManager.getItemComposition(itemId).getName();
                if (isException(itemName)) {
                    return;
                }

                Color colorToUse = getColor(profitPerCast, isSellable);
                Rectangle bounds = itemWidget.getCanvasBounds();

                if (config.highlightStyle() == HighlightStyle.OUTLINE) {
                    final BufferedImage outline = itemManager.getItemOutline(itemId, itemWidget.getQuantity(), colorToUse);
                    graphics.drawImage(outline, (int) bounds.getX(), (int) bounds.getY(), null);
                } else if (config.highlightStyle() == HighlightStyle.UNDERLINE) {
                    int y = (int) bounds.getY() + (int) bounds.getHeight() + 2;
                    graphics.setColor(colorToUse);
                    graphics.drawLine((int) bounds.getX(), y, (int) bounds.getX() + (int) bounds.getWidth(), y);
                } else if (config.highlightStyle() == HighlightStyle.FILL) {
                    final Image fill = getFillImage(colorToUse, itemId, itemWidget.getQuantity());
                    graphics.drawImage(fill, (int) bounds.getX(), (int) bounds.getY(), null);
                }
            }
        }
    }

	private boolean checkInterfaceIsHighlightable(WidgetItem itemWidget)
	{
		Widget bankWidget = client.getWidget(InterfaceID.Bankmain.ITEMS);
		Widget inventoryWidget = client.getWidget(InterfaceID.Inventory.ITEMS);
		Widget bankInventoryWidget = client.getWidget(InterfaceID.Bankside.ITEMS);

		boolean isBankItem = bankWidget != null && bankWidget.getId() == itemWidget.getWidget().getId();
		boolean isInventoryItem = inventoryWidget != null && inventoryWidget.getId() == itemWidget.getWidget().getId();
		boolean isBankInventoryItem = bankInventoryWidget != null && bankInventoryWidget.getId() == itemWidget.getWidget().getId();

		if (config.neverHighlightInventory() && (isInventoryItem || isBankInventoryItem))
		{
			return false;
		}

		if (config.getHighlightLocation() == HighAlcHighlightConfig.HighlightLocationType.BANK)
		{
			return isBankItem;
		}

		if (config.getHighlightLocation() == HighAlcHighlightConfig.HighlightLocationType.INVENTORY)
		{
			return isInventoryItem || isBankInventoryItem;
		}

		return true;
	}

    double getAdjustedGePrice(int itemId)
    {
        double gePrice = itemManager.getItemPrice(itemId);
        if (gePrice * GE_TAX_RATE >= 1)
        {
            if (gePrice * GE_TAX_RATE > GE_TAX_THRESHOLD) {
                gePrice -= GE_TAX_THRESHOLD;
            } else {
                gePrice -= (int)(gePrice * GE_TAX_RATE);
            }
        }
        return gePrice;
    }

    int getProfit(int itemId, double gePrice)
    {
        ItemComposition itemDef = itemManager.getItemComposition(itemId);

        int haPrice = itemDef.getHaPrice();

        int fireRunePrice = itemManager.getItemPrice(ItemID.FIRERUNE);
        int natureRunePrice;
        if (config.useGE())
        {
            natureRunePrice = itemManager.getItemPrice(ItemID.NATURERUNE);
        }
        else
        {
            natureRunePrice = config.overridePrice();
        }
        int fireRuneMultiplier = 0;
        if (config.fireRuneSource() == FireRuneSource.RUNES) {
            fireRuneMultiplier = 5;
        }

        double natureRuneMultiplier = 1.0;
        if (config.useBryoStaff()) {
            natureRuneMultiplier = 0.9375;
        }

        int castCost = (fireRunePrice * fireRuneMultiplier) + (int) Math.ceil(natureRunePrice * natureRuneMultiplier);

        if (config.useGEPrices())
        {
            return (int)(haPrice - gePrice - castCost);
        }
        else
        {
            return haPrice - castCost;
        }
    }

    private Image getFillImage(Color color, int itemId, int qty)
    {
        long key = ((long) itemId << 32) | qty;
        Image image = fillCache.getIfPresent(key);
        if (image == null)
        {
            final Color fillColor = ColorUtil.colorWithAlpha(color, config.fillOpacity());
            image = ImageUtil.fillImage(itemManager.getImage(itemId, qty, false), fillColor);
            fillCache.put(key, image);
        }
        return image;
    }

    void invalidateCache()
    {
        fillCache.invalidateAll();
        rebuildExceptionSet();
    }

    private void rebuildExceptionSet()
    {
        String exceptions = config.exceptions();
        if (exceptions.isEmpty())
        {
            exceptionExact = Collections.emptySet();
            exceptionPatterns = Collections.emptyList();
            return;
        }

        Set<String> exact = new HashSet<>();
        List<Pattern> patterns = new ArrayList<>();

        for (String entry : exceptions.split(","))
        {
            String trimmed = entry.trim();
            if (trimmed.isEmpty())
            {
                continue;
            }
            if (trimmed.contains("*"))
            {
                patterns.add(globToPattern(trimmed));
            }
            else
            {
                exact.add(trimmed.toLowerCase());
            }
        }

        exceptionExact = exact;
        exceptionPatterns = patterns;
    }

    private static Pattern globToPattern(String glob)
    {
        String[] parts = glob.toLowerCase().split("\\*", -1);
        StringBuilder regex = new StringBuilder("^");
        for (int i = 0; i < parts.length; i++)
        {
            if (i > 0)
            {
                regex.append(".*");
            }
            regex.append(Pattern.quote(parts[i]));
        }
        regex.append("$");
        return Pattern.compile(regex.toString());
    }

    boolean isExactException(String itemName)
    {
        return exceptionExact.contains(itemName.toLowerCase());
    }

    boolean isException(String itemName)
    {
        String lower = itemName.toLowerCase();
        if (exceptionExact.contains(lower))
        {
            return true;
        }
        for (Pattern pattern : exceptionPatterns)
        {
            if (pattern.matcher(lower).matches())
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasInventoryTag(int itemId)
    {
        String tag = configManager.getConfiguration(INVENTORY_TAGS_GROUP, INVENTORY_TAGS_KEY_PREFIX + itemId);
        return tag != null && !tag.isEmpty();
    }

    boolean isSellable(int itemId) {
        return itemManager.getItemComposition(itemManager.canonicalize(itemId)).isGeTradeable();
    }

    Color getColor(int profitPerCast, boolean isSellable)
    {
        if (!isSellable) {
            return config.getUnsellableColour();
        }

        if (config.useGradientMode()) {
            double percent = Math.min(((double) profitPerCast) / config.highProfitValue(), 1.0);

            return getGradientColor(config.getColour(), config.getHighProfitColour(), percent);
        } else {
            if (profitPerCast >= config.highProfitValue()) {
                return config.getHighProfitColour();
            } else {
                return config.getColour();
            }
        }
    }

    private Color getGradientColor(Color lowColor, Color highColor, double percent)
    {
        int newRed = findStep(lowColor.getRed(), highColor.getRed(), percent);
        int newGreen = findStep(lowColor.getGreen(), highColor.getGreen(), percent);
        int newBlue = findStep(lowColor.getBlue(), highColor.getBlue(), percent);

        return new Color(newRed, newGreen, newBlue);
    }

    private int findStep(int low, int high, double percent)
    {
        return (int) (low + (high-low)*percent);
    }
}
