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
		position = 1,
		keyName = "usingBryo",
		name = "Using Bryophyta's Staff",
		description = "Configures if you are using Bryophyta's Staff."
	)
	default boolean useBryoStaff() {return false;}

	@ConfigItem(
		position = 2,
		keyName = "fireRuneSource",
		name = "Source of Fire Runes",
		description = "Configures what source of fire runes you are using."
	)
	default FireRuneSource fireRuneSource() { return FireRuneSource.STAFF; }

	@ConfigItem(
		position = 3,
		keyName = "HighlightColour",
		name = "Highlight Colour",
		description = "Highlight colour of profitable items"
	)
	default Color getColour() {return Color.BLUE;}

	@ConfigItem(
			position = 5,
			keyName = "highProfitValue",
			name = "High-Profit Threshold",
			description = "The starting price for high-profit highlighting."
	)
	default int highProfitValue() { return 300; }

	@ConfigItem(
			position = 6,
			keyName = "highProfitColour",
			name = "High-Profit Colour",
			description = "Highlight colour of items that are high-profit."
	)
	default Color getHighProfitColour() {return Color.GREEN;}

	@ConfigItem(
		position = 4,
		keyName = "highlightUnsellables",
		name = "Highlight Unsellables",
		description = "If enabled, highlights items that would make a profit but cannot be sold on the GE"
	)
	default boolean highlightUnsellables() {return true;}

	@ConfigItem(
		position = 7,
		keyName = "unsellableHighlightColour",
		name = "Unsellables Colour",
		description = "Colour to show if Highlight Unsellables is checked"
	)
	default Color getUnsellableColour() {return Color.YELLOW;}
}
