package com.highalchighlight;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup("highalchighlight")
public interface HighAlcHighlightConfig extends Config
{
	@ConfigItem(
			position = 5,
			keyName = "HighlightColour",
			name = "Highlight Colour",
			description = "Highlight colour of profitable items"
	)
	default Color getColour() {return Color.GREEN;}

	@ConfigItem(
			position = 1,
			keyName = "useStaff",
			name = "Use Fire Staff in calc",
			description = "If enabled, cost of fire runes is not included in profit calculation"
	)
	default boolean useFireStaff() {return true;}
        
        @ConfigItem(
			position = 2,
			keyName = "useGE",
			name = "Use GE Price for Nature Runes",
			description = "Fetch the price of Nature Runes from the GE (Ironmen should set this to off)"
	)
	default boolean useGE() {return true;}
        
        @ConfigItem(
			position = 3,
			keyName = "overridePrice",
			name = "Nature rune cost",
			description = "If the Nature Rune GE price is not used this is the price for Nature Runes that will be used."
	)
	default int overridePrice() {return 203;}


	@ConfigItem(
			position = 4,
			keyName = "highlightUnsellables",
			name = "Highlight Unsellables",
			description = "If enabled, highlights items that would make a profit but cannot be sold on the GE"
	)
	default boolean highlightUnsellables() {return true;}

	@ConfigItem(
			position = 5,
			keyName = "unsellableHighlightColour",
			name = "Unsellables Colour",
			description = "Colour to show if Highlight Unsellables is checked"
	)
	default Color getUnsellableColour() {return Color.YELLOW;}
}
