package com.highalchighlight;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup("highalchighlight")
public interface HighAlcHighlightConfig extends Config
{
	@ConfigItem(
			position = 3,
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
			keyName = "highlightUnsellables",
			name = "Highlight Unsellables",
			description = "If enabled, highlights items that would make a profit but cannot be sold on the GE"
	)
	default boolean highlightUnsellables() {return true;}

	@ConfigItem(
			position = 3,
			keyName = "unsellableHighlightColour",
			name = "Unsellables Colour",
			description = "Colour to show if Highlight Unsellables is checked"
	)
	default Color getUnsellableColour() {return Color.YELLOW;}
}
