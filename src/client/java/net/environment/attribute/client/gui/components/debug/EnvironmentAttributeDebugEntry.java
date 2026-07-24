package net.environment.attribute.client.gui.components.debug;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public class EnvironmentAttributeDebugEntry implements DebugScreenEntry {
    public static final Identifier GROUP = Identifier.fromNamespaceAndPath("environment_attribute","value");
    private static final String TAG_VALUE = "value";
    private EnvironmentAttribute<?> attribute;

    public EnvironmentAttributeDebugEntry(EnvironmentAttribute<?> attribute){
        this.attribute = attribute;
    }

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        if (serverOrClientLevel == null || Minecraft.getInstance().getCameraEntity() == null) {
            return;
        }
        displayer.addToGroup(GROUP,attribute.toString() + ": " + this.getText());
    }

    @Override
    public DebugEntryCategory category() {
        return EnvironmentAttributeEntries.ENVIRONMENT_ATTRIBUTE;
    }

    private <Value> String getText(){
        EnvironmentAttribute<Value> attribute = (EnvironmentAttribute<Value>)this.attribute;
        Value value = Minecraft.getInstance().gameRenderer.mainCamera().attributeProbe().getValue(attribute, 1.0f);
        if (!attribute.isSyncable()) {
            return "NULL";
        }
        if (attribute.type() == AttributeTypes.BOOLEAN) {
            return (Boolean)value ? "true" : "false";
        }
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_VALUE, attribute.valueCodec().encodeStart(NbtOps.INSTANCE, value).getOrThrow());
        return NbtUtils.prettyPrint(tag.get(TAG_VALUE), true);
    }
}