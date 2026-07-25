package net.environmentattributegetter.client.gui.components.debug;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public class EnvironmentAttributeDebugEntry implements DebugScreenEntry {
    public static final Identifier GROUP = Identifier.fromNamespaceAndPath("environment_attribute_getter", "value");
    private final EnvironmentAttribute<?> attribute;

    public EnvironmentAttributeDebugEntry(EnvironmentAttribute<?> attribute){
        this.attribute = attribute;
    }

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        if (serverOrClientLevel == null || Minecraft.getInstance().getCameraEntity() == null) {
            return;
        }
        StringBuilder content = new StringBuilder(attribute.toString());
        content.append(": ");
        if (!attribute.isSyncable() && serverChunk == null) {
            content.append("??");
        } else {
            content.append(this.getText());
        }
        displayer.addToGroup(GROUP, content.toString());
    }

    @Override
    public DebugEntryCategory category() {
        return EnvironmentAttributeDebugEntries.ENVIRONMENT_ATTRIBUTE;
    }

    private <Value> String getText(){
        EnvironmentAttribute<Value> attribute = (EnvironmentAttribute<Value>)this.attribute;
        Value value = Minecraft.getInstance().gameRenderer.mainCamera().attributeProbe().getValue(attribute, 1.0f);
        JsonElement json = (JsonElement)attribute.valueCodec().encodeStart((DynamicOps) JsonOps.INSTANCE, value).getOrThrow();
        return GsonHelper.toStableString(json);
    }
}