package com.highalchighlight;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
		name = "High Alc Profit",
		description = "Highlights any item in your backpack or bank that would be profit to high alc",
		tags = {"highlight", "items", "overlay"},
		enabledByDefault = false
)
public class HighAlcHighlightPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private HighAlcHighlightConfig config;

	@Inject
	private MenuManager menuManager;

	@Inject
	private HighAlcHighlightOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Provides
	HighAlcHighlightConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(HighAlcHighlightConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
	}
}
