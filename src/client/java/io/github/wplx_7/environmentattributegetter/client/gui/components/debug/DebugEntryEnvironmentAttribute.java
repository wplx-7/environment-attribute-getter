package io.github.wplx_7.environmentattributegetter.client.gui.components.debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.wplx_7.environmentattributegetter.client.EnvironmentAttributeGetterClient;
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

import java.util.List;
import java.util.stream.Collectors;

public class DebugEntryEnvironmentAttribute implements DebugScreenEntry {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Identifier GROUP = EnvironmentAttributeGetterClient.id("value");
    private final EnvironmentAttribute<?> attribute;

    public DebugEntryEnvironmentAttribute(EnvironmentAttribute<?> attribute){
        this.attribute = attribute;
    }

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        if (serverOrClientLevel == null || Minecraft.getInstance().getCameraEntity() == null) {
            return;
        }
        StringBuilder content = new StringBuilder(attribute.toString()).append(": ");
        List<String> value = this.getText();
        if (!attribute.isSyncable() && serverChunk == null) {
            content.append("??");
        } else {
            if (value.size() == 1) {
                content.append(this.getText());
                displayer.addToGroup(GROUP, content.toString());
            }
            else {
                content.append(value.getFirst());
                displayer.addToGroup(GROUP, content.toString());
                displayer.addToGroup(GROUP, value.stream().skip(1).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public DebugEntryCategory category() {
        return EnvironmentAttributeDebugScreenEntries.ENVIRONMENT_ATTRIBUTE;
    }

    private <Value> List<String> getText(){
        EnvironmentAttribute<Value> attribute = (EnvironmentAttribute<Value>)this.attribute;
        Value value = Minecraft.getInstance().gameRenderer.mainCamera().attributeProbe().getValue(attribute, 1.0f);
        JsonElement json = (JsonElement)attribute.valueCodec().encodeStart((DynamicOps) JsonOps.INSTANCE, value).getOrThrow();
        return List.of(GSON.toJson(JsonParser.parseString(GsonHelper.toStableString(json))).split("\n"));
    }
}