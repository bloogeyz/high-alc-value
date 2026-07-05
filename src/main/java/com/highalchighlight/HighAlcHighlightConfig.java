package com.highalchighlight;

import lombok.AllArgsConstructor;
import net.runelite.client.config.*;
import com.highalchighlight.config.FireRuneSource;
import com.highalchighlight.config.HighlightStyle;

import java.awt.Color;

@ConfigGroup(HighAlcHighlightConfig.GROUP)
public interface HighAlcHighlightConfig extends Config
{
	String GROUP = "highalchighlight";@AllArgsConstructor
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

	@ConfigSection(
			name = "Highlighting Colour and Style",
			description = "Highlighting config",
			position = 0
	)
	String highlighting = "Highlighting";

	@ConfigItem(
		position = 1,
		keyName = "useGradientMode",
		name = "Gradient Mode",
		description = "Enabling this setting will cause items to be highlighted in a gradient color from your Highlight Colour to your High-Profit Colour based on profitability.",
		section = highlighting
	)
	default boolean useGradientMode() {return true;}

	@ConfigItem(
		position = 2,
		keyName = "HighlightColour",
		name = "Highlight Colour",
		description = "Highlight colour of profitable items",
		section = highlighting
	)
	default Color getColour() {return Color.WHITE;}

	@ConfigItem(
		position = 3,
		keyName = "highProfitValue",
		name = "High-Profit Threshold",
		description = "The price for high-profit highlighting.",
		section = highlighting
	)
	default int highProfitValue() { return 300; }

	@ConfigItem(
		position = 4,
		keyName = "highProfitColour",
		name = "High-Profit Colour",
		description = "Highlight colour of items that are high-profit.",
		section = highlighting
	)
	default Color getHighProfitColour() {return Color.GREEN;}

	@ConfigItem(
		position = 5,
		keyName = "unsellableHighlightColour",
		name = "Unsellables Colour",
		description = "Colour to show if Highlight Unsellables is checked",
		section = highlighting
	)
	default Color getUnsellableColour() {return Color.YELLOW;}


	@ConfigItem(
		position = 6,
		keyName = "highlightStyle",
		name = "Highlight Style",
		description = "How profitable items are highlighted.",
		section = highlighting
	)
	default HighlightStyle highlightStyle() {return HighlightStyle.OUTLINE;}

	@Range(max = 255)
	@ConfigItem(
		position = 7,
		keyName = "fillOpacity",
		name = "Fill Opacity",
		description = "Opacity of the fill highlight (only used when Highlight Style is Fill).",
		section = highlighting
	)
	default int fillOpacity() {return 50;}

	@ConfigItem(
		position = 8,
		keyName = "respectInventoryTags",
		name = "Respect Inventory Tags",
		description = "Skip highlighting items that have an Inventory Tag.",
		section = highlighting
	)
	default boolean respectInventoryTags() {return true;}

	@ConfigItem(
			position = 9,
			keyName = "usingBryo",
			name = "Using Bryophyta's Staff",
			description = "Configures if you are using Bryophyta's Staff."
	)
	default boolean useBryoStaff() {return false;}

	@ConfigItem(
			position = 10,
			keyName = "fireRuneSource",
			name = "Source of Fire Runes",
			description = "Configures what source of fire runes you are using."
	)
	default FireRuneSource fireRuneSource() { return FireRuneSource.STAFF; }

	@ConfigItem(
			position = 11,
			keyName = "useGE",
			name = "Use GE Price for Nature Runes",
			description = "Fetch the price of Nature Runes from the GE (Ironmen should set this to off)"
	)
	default boolean useGE() {return true;}

	@ConfigItem(
			position = 12,
			keyName = "useGEPrices",
			name = "Use GE Price for Item",
			description = "Fetch the price of the item for High Alc calculation, otherwise just uses HA Value - Cast Cost"
	)
	default boolean useGEPrices() {return true;}

	@ConfigItem(
			position = 13,
			keyName = "overridePrice",
			name = "Nature rune cost",
			description = "If the Nature Rune GE price is not used this is the price for Nature Runes that will be used."
	)
	default int overridePrice() {return 203;}

	@ConfigItem(
			position = 14,
			keyName = "highlightUnsellables",
			name = "Highlight Unsellables",
			description = "If enabled, highlights items that would make a profit but cannot be sold on the GE"
	)
	default boolean highlightUnsellables() {return true;}

	@ConfigItem(
			position = 15,
			keyName = "highlightLocation",
			name = "Highlight",
			description = "Whether to highlight items in the bank, inventory, or both."
	)
	default HighlightLocationType getHighlightLocation() {return HighlightLocationType.BOTH;}

	@ConfigItem(
			position = 16,
			keyName = "neverHighlightInventory",
			name = "Never Highlight Inventory",
			description = "If enabled, items will never be highlighted in your inventory, even when the bank is closed."
	)
	default boolean neverHighlightInventory() {return false;}

	@ConfigItem(
		position = 17,
		keyName = "exceptions",
		name = "Exception List",
		description = "Comma-separated list of item names to never highlight. Use Shift+right-click on an item to add it. Wildcarded items must be edited directly"
	)
	default String exceptions()
	{
		return "";
	}
}
