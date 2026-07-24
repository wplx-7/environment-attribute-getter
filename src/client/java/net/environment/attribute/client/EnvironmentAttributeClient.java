package net.environment.attribute.client;

import net.environment.attribute.client.gui.components.debug.EnvironmentAttributeEntries;
import net.fabricmc.api.ClientModInitializer;

public class EnvironmentAttributeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EnvironmentAttributeEntries.onInitialize();
	}
}