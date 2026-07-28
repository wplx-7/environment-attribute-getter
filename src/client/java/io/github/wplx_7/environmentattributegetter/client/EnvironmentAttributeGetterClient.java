package io.github.wplx_7.environmentattributegetter.client;

import io.github.wplx_7.environmentattributegetter.client.gui.components.debug.EnvironmentAttributeDebugEntries;
import net.fabricmc.api.ClientModInitializer;

public class EnvironmentAttributeGetterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EnvironmentAttributeDebugEntries.onInitialize();
	}
}