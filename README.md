# High Alchemy Highlight Plugin
This plugin's primary purpose is to quickly show you what items you can safely high alc for profit.
By default it will highlight any items in your bank or inventory that have a higher HA value (after deducting the cost
of a nature rune) than the current GE value. Additionally, it can highlight items that can't be sold on the GE that
have a high alc value above the cost of a cast.

## Features

### Highlighting
- Highlights profitable items in your **inventory** and/or **bank**
- Configurable highlight style: **Outline**, **Underline**, or **Fill** (with adjustable opacity)
- **Gradient mode**: colour scales smoothly from your base highlight colour up to your high-profit colour based on profit amount
- Separate colour for items that cannot be sold on the GE
- Option to never highlight inventory items (bank-only mode)
- Respects **Inventory Tags** — tagged items can be skipped

### Profit Calculation
- GE tax (2%) is factored into the sell price automatically
- Configurable **nature rune source**: live GE price or a fixed override (useful for Ironmen)
- Configurable **fire rune source**: staff (free) or buying runes
- **Bryophyta's Staff** support (reduces nature rune cost by ~6.25%)
- Option to use the item's HA value minus cast cost only, ignoring GE price entirely

### Exception List
- Items can be excluded from highlighting via a comma-separated **Exception List** in the config
- Supports **wildcards**: e.g. `*boots` will suppress all items whose name ends with "boots"
- **Shift+right-click** any item in your inventory or bank to instantly add or remove it from the exception list
  - If an item is covered only by a wildcard, no toggle is offered — edit the list directly instead

Please feel free to suggest improvements or fork the project yourself.
