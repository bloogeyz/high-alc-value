package com.highalchighlight;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HighAlcHighlightTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HighAlcHighlightPlugin.class);
		RuneLite.main(args);
	}
}