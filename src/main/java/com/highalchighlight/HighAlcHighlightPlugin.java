package com.highalchighlight;

import com.google.inject.Provides;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "High Alc Profit",
		description = "Highlights any item in your backpack or bank that would be profit to high alc",
		tags = {"highlight", "items", "overlay"}
)
public class HighAlcHighlightPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private HighAlcHighlightOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HighAlcHighlightConfig config;

	@Provides
	HighAlcHighlightConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(HighAlcHighlightConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(HighAlcHighlightConfig.GROUP))
		{
			overlay.invalidateCache();
		}
	}

	@Subscribe(priority = -1)
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			return;
		}

		final MenuEntry entry = event.getMenuEntry();
		if (!"Examine".equals(entry.getOption()))
		{
			return;
		}

		final int widgetInterfaceId = entry.getParam1() >> 16;
		final int inventoryInterfaceId = InterfaceID.Inventory.ITEMS >> 16;
		final int bankInterfaceId = InterfaceID.Bankmain.ITEMS >> 16;
		final int bankSideInterfaceId = InterfaceID.Bankside.ITEMS >> 16;

		final ItemContainer container;
		if (widgetInterfaceId == bankInterfaceId)
		{
			container = client.getItemContainer(InventoryID.BANK);
		}
		else if (widgetInterfaceId == inventoryInterfaceId || widgetInterfaceId == bankSideInterfaceId)
		{
			container = client.getItemContainer(InventoryID.INVENTORY);
		}
		else
		{
			return;
		}

		if (container == null)
		{
			return;
		}

		final Item item = container.getItem(entry.getParam0());
		if (item == null || item.getId() == -1)
		{
			return;
		}

		int itemId = item.getId();
		final ItemComposition comp = itemManager.getItemComposition(itemId);
		if (comp.getNote() != -1)
		{
			itemId = comp.getLinkedNoteId();
		}

		final String itemName = itemManager.getItemComposition(itemId).getName();
		final boolean exactlyExcepted = overlay.isExactException(itemName);
		final boolean wildcardExcepted = !exactlyExcepted && overlay.isException(itemName);

		// If only a wildcard covers this item, don't offer a toggle — the user must edit the list manually
		if (wildcardExcepted)
		{
			return;
		}

		client.getMenu().createMenuEntry(-1)
			.setOption(exactlyExcepted ? "Highlight" : "Don't highlight")
			.setTarget(entry.getTarget())
			.setType(MenuAction.RUNELITE)
			.onClick(e -> toggleException(itemName));
	}

	private void toggleException(String itemName)
	{
		final String lowerName = itemName.toLowerCase();
		final String current = config.exceptions();

		final List<String> list = Arrays.stream(current.split(","))
			.map(String::trim)
			.filter(s -> !s.isEmpty())
			.collect(Collectors.toList());

		final boolean removed = list.removeIf(s -> s.toLowerCase().equals(lowerName));
		if (!removed)
		{
			list.add(itemName);
		}

		configManager.setConfiguration(HighAlcHighlightConfig.GROUP, "exceptions", String.join(",", list));
	}
}
