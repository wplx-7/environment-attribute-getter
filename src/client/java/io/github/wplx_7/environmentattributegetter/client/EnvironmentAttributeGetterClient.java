package io.github.wplx_7.environmentattributegetter.client;

import io.github.wplx_7.environmentattributegetter.client.gui.components.debug.EnvironmentAttributeDebugScreenEntries;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

public class EnvironmentAttributeGetterClient implements ClientModInitializer {
	public static final String MOD_ID = "environment_attribute_getter";

	@Override
	public void onInitializeClient() {
		EnvironmentAttributeDebugScreenEntries.onInitialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}