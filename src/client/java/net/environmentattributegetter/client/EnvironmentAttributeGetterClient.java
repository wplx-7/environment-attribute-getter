package net.environmentattributegetter.client;

import net.environmentattributegetter.client.gui.components.debug.EnvironmentAttributeDebugEntries;
import net.fabricmc.api.ClientModInitializer;

public class EnvironmentAttributeGetterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EnvironmentAttributeDebugEntries.onInitialize();
	}
}