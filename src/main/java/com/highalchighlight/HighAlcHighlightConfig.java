package com.highalchighlight;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import com.highalchighlight.config.FireRuneSource;

import java.awt.Color;

@ConfigGroup("highalchighlight")
public interface HighAlcHighlightConfig extends Config
{
	@ConfigItem(
		keyName = "usingBryo",
		name = "Using Bryophyta's Staff",
		description = "Configures if you are using Bryophyta's Staff.",
		position = 1
	)
	default boolean useBryoStaff() {return false;}

	@ConfigItem(
			keyName = "fireRuneSource",
			name = "Source of Fire Runes",
			description = "Configures what source of fire runes you are using.",
			position = 2
	)
	default FireRuneSource fireRuneSource() { return FireRuneSource.STAFF; }

	@ConfigItem(
			position = 3,
			keyName = "HighlightColour",
			name = "Highlight Colour",
			description = "Highlight colour of profitable items"
	)
	default Color getColour() {return Color.GREEN;}

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
