package io.github.wplx_7.environmentattributegetter.client.gui.components.debug;

import io.github.wplx_7.environmentattributegetter.client.mixin.DebugScreenEntriesMixin;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;

public class EnvironmentAttributeDebugScreenEntries {
    public static final DebugEntryCategory ENVIRONMENT_ATTRIBUTE = new DebugEntryCategory(Component.translatable("debug.options.category.environment_attribute"), 20.0f);

    public static Identifier registerEnvironmentAttributeEntry(EnvironmentAttribute<?> attribute, boolean enabled){
        if (!enabled) {
            return Identifier.parse(attribute.toString());
        }
        return DebugScreenEntriesMixin.invokeRegister(Identifier.parse(attribute.toString()), new DebugEntryEnvironmentAttribute(attribute));
    }

    public static void register(){
        BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.listElements().forEach(e -> {
            EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(e.value(), e.value().isSyncable());
        });
    }

    public static void onInitialize(){
        EnvironmentAttributeDebugScreenEntries.register();
    }
}
