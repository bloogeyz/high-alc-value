package com.highalchighlight;

import lombok.AllArgsConstructor;
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
			keyName = "useGE",
			name = "Use GE Price for Nature Runes",
			description = "Fetch the price of Nature Runes from the GE (Ironmen should set this to off)"
	)
	default boolean useGE() {return true;}

	@ConfigItem(
			position = 4,
			keyName = "overridePrice",
			name = "Nature rune cost",
			description = "If the Nature Rune GE price is not used this is the price for Nature Runes that will be used."
	)
	default int overridePrice() {return 203;}

	@ConfigItem(
		position = 5,
		keyName = "useGradientMode",
		name = "Gradient Mode",
		description = "Enabling this setting will cause items to be highlighted in a gradient color from your Highlight Colour to your High-Profit Colour based on profitability."
	)
	default boolean useGradientMode() {return true;}

	@ConfigItem(
		position = 6,
		keyName = "HighlightColour",
		name = "Highlight Colour",
		description = "Highlight colour of profitable items"
	)
	default Color getColour() {return Color.WHITE;}

	@ConfigItem(
			position = 7,
			keyName = "highProfitValue",
			name = "High-Profit Threshold",
			description = "The price for high-profit highlighting."
	)
	default int highProfitValue() { return 300; }

	@ConfigItem(
			position = 8,
			keyName = "highProfitColour",
			name = "High-Profit Colour",
			description = "Highlight colour of items that are high-profit."
	)
	default Color getHighProfitColour() {return Color.GREEN;}

	@ConfigItem(
			position = 9,
			keyName = "highlightUnsellables",
			name = "Highlight Unsellables",
			description = "If enabled, highlights items that would make a profit but cannot be sold on the GE"
	)
	default boolean highlightUnsellables() {return true;}

	@ConfigItem(
		position = 10,
		keyName = "unsellableHighlightColour",
		name = "Unsellables Colour",
		description = "Colour to show if Highlight Unsellables is checked"
	)
	default Color getUnsellableColour() {return Color.YELLOW;}

	@AllArgsConstructor
	enum HighlightLocationType
	{
		BOTH("Both"),
		BANK("Bank"),
		INVENTORY("Inventory");

		private final String value;

		@Override
		public String toString()
		{
			return value;
		}
	}

	@ConfigItem(
		position = 3,
		keyName = "highlightLocation",
		name = "Highlight",
		description = "Colour to show if Highlight Unsellables is checked"
	)
	default HighlightLocationType getHighlightLocation() {return HighlightLocationType.BOTH;}
}
