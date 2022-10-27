package com.highalchighlight;

import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

import com.highalchighlight.config.FireRuneSource;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class HighAlcHighlightOverlay extends WidgetItemOverlay
{
	private final Client client;
    private final ItemManager itemManager;
    private final HighAlcHighlightConfig config;
    @Inject
    private HighAlcHighlightOverlay(Client client, ItemManager itemManager, HighAlcHighlightPlugin plugin, HighAlcHighlightConfig config)
    {
    	this.client = client;
        this.itemManager = itemManager;
        this.config = config;
        showOnInventory();
        showOnBank();
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget)
    {
        if (checkInterfaceIsHighlightable(itemWidget)) {
            int gePrice = itemManager.getItemPrice(itemId);

            int profitPerCast = getProfit(itemId, gePrice);
            boolean isSellable = isSellable(gePrice);

            if ((profitPerCast > 0) && (isSellable || config.highlightUnsellables())) {
                Color colorToUse = getColor(profitPerCast, isSellable);

                Rectangle bounds = itemWidget.getCanvasBounds();
                final BufferedImage outline = itemManager.getItemOutline(itemId, itemWidget.getQuantity(), colorToUse);
                graphics.drawImage(outline, (int) bounds.getX(), (int) bounds.getY(), null);
            }
        }
    }

	private boolean checkInterfaceIsHighlightable(WidgetItem itemWidget)
	{
		if (config.getHighlightLocation() != HighAlcHighlightConfig.HighlightLocationType.BOTH)
		{
			Widget bankWidget = client.getWidget(WidgetInfo.BANK_CONTAINER);
			if (bankWidget != null && config.getHighlightLocation() != HighAlcHighlightConfig.HighlightLocationType.BANK)
			{
				return false;
			}
			Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
			if (inventoryWidget != null)
			{
				return inventoryWidget.getId() != itemWidget.getWidget().getId() || config.getHighlightLocation() == HighAlcHighlightConfig.HighlightLocationType.INVENTORY;
			}
		}
		return true;
	}

    private int getProfit(int itemId, int gePrice)
    {

        ItemComposition itemDef = itemManager.getItemComposition(itemId);

        int haPrice = itemDef.getHaPrice();

        int fireRunePrice = itemManager.getItemPrice(ItemID.FIRE_RUNE);
        int natureRunePrice;
        if (config.useGE())
        {
            natureRunePrice = itemManager.getItemPrice(ItemID.NATURE_RUNE);
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

        return haPrice - gePrice - castCost;
    }

    private boolean isSellable(int gePrice) { return (gePrice > 0); }

    private Color getColor(int profitPerCast, boolean isSellable)
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
