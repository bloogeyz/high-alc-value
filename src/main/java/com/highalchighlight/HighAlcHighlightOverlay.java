package com.highalchighlight;

import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
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
    private final ItemManager itemManager;
    private final HighAlcHighlightConfig config;

    @Inject
    private HighAlcHighlightOverlay(ItemManager itemManager, HighAlcHighlightPlugin plugin, HighAlcHighlightConfig config)
    {
        this.itemManager = itemManager;
        this.config = config;
        showOnInventory();
        showOnBank();
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget)
    {
        Color colorToUse = config.getColour();
        boolean isProfit = false;
        ProfitStatus profitStatus = checkProfitability(itemId);
        switch (profitStatus)
        {
            case HIGH_PROFIT:
                colorToUse = config.getHighProfitColour();
                isProfit = true;
                break;
            case PROFIT:
                colorToUse = config.getColour();
                isProfit = true;
                break;
            case UNSELLABLE_PROFIT:
                colorToUse = config.getUnsellableColour();
                isProfit = true;
                break;
            default:
                break;
        }
        if(isProfit)
        {
            Rectangle bounds = itemWidget.getCanvasBounds();
            final BufferedImage outline = itemManager.getItemOutline(itemId, itemWidget.getQuantity(), colorToUse);
            graphics.drawImage(outline, (int) bounds.getX(), (int) bounds.getY(), null);
        }
    }

    private ProfitStatus checkProfitability(int itemId)
    {
        ItemComposition itemDef = itemManager.getItemComposition(itemId);

        int gePrice = itemManager.getItemPrice(itemId);
        int haPrice = itemDef.getHaPrice();

        // Only account for the price of fire runes if runes are being used instead of a staff or tome.
        int fireRunePrice = 0;
        if (config.fireRuneSource() == FireRuneSource.RUNES) {
            fireRunePrice = 5 * itemManager.getItemPrice(ItemID.FIRE_RUNE);
        }

        // If Bryophyta's Staff is in use, decrease cost of nature runes.
        int natureRunePrice = itemManager.getItemPrice(ItemID.NATURE_RUNE);
        if (config.useBryoStaff()) {
            natureRunePrice = (int) Math.ceil(natureRunePrice*0.9375);
        }

        if (gePrice > 0 && haPrice - natureRunePrice - fireRunePrice > gePrice + config.highProfitValue())
        {
            return ProfitStatus.HIGH_PROFIT;
        } else  if (gePrice > 0 && haPrice - natureRunePrice - fireRunePrice > gePrice)
        {
            return ProfitStatus.PROFIT;
        }
        else if (gePrice == 0 && config.highlightUnsellables() && haPrice - natureRunePrice - fireRunePrice > 0)
        {
            return ProfitStatus.UNSELLABLE_PROFIT;
        }
        return ProfitStatus.LOSS;
    }

}
