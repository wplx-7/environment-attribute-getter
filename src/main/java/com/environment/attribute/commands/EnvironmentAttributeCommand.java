package com.environment.attribute.commands;

import com.environment.attribute.commands.arguments.CustomResourceArgument;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.TriState;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;

public class EnvironmentAttributeCommand {
    private static final String TAG_VALUE = "value";
    private static final EnvironmentAttribute<?>[] VISUAL_COLOR_ATTRIBUTE = new EnvironmentAttribute[]{EnvironmentAttributes.SKY_COLOR, EnvironmentAttributes.FOG_COLOR, EnvironmentAttributes.WATER_FOG_COLOR, EnvironmentAttributes.CLOUD_COLOR, EnvironmentAttributes.SUNRISE_SUNSET_COLOR, EnvironmentAttributes.BLOCK_LIGHT_TINT, EnvironmentAttributes.SKY_LIGHT_COLOR, EnvironmentAttributes.NIGHT_VISION_COLOR, EnvironmentAttributes.AMBIENT_LIGHT_COLOR};
    private static final EnvironmentAttribute<?>[] VISUAL_FOG_DISTANCE_ATTRIBUTE = new EnvironmentAttribute[]{EnvironmentAttributes.FOG_START_DISTANCE, EnvironmentAttributes.FOG_END_DISTANCE, EnvironmentAttributes.WATER_FOG_START_DISTANCE, EnvironmentAttributes.WATER_FOG_END_DISTANCE, EnvironmentAttributes.SKY_FOG_END_DISTANCE, EnvironmentAttributes.CLOUD_FOG_END_DISTANCE};

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("environment_attribute")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("get")
                        .then(Commands.argument("environment_attribute", ResourceArgument.resource(context, Registries.ENVIRONMENT_ATTRIBUTE))
                                .executes(c -> EnvironmentAttributeCommand.getValue(c.getSource(), CustomResourceArgument.getEnvironmentAttribute(c, "environment_attribute")))
                                .then(Commands.literal("default")
                                        .executes(c -> EnvironmentAttributeCommand.getDefaultValue(c.getSource(), CustomResourceArgument.getEnvironmentAttribute(c, "environment_attribute")))
                                )
                        )
                ).then(Commands.literal("getvisualcolor")
                        .executes(c -> EnvironmentAttributeCommand.getSetValue(c.getSource(), VISUAL_COLOR_ATTRIBUTE))
                ).then(Commands.literal("getfogdistance")
                        .executes(c -> EnvironmentAttributeCommand.getSetValue(c.getSource(), VISUAL_FOG_DISTANCE_ATTRIBUTE))
                )
        );
    }

    private static int getValue(CommandSourceStack source, Holder.Reference<EnvironmentAttribute<?>> environmentAttribute){
        return EnvironmentAttributeCommand.getValue(source, environmentAttribute.value());
    }

    private static <Value> int getValue(CommandSourceStack source, EnvironmentAttribute<Value> attribute){
        Value value;
        if (attribute.isPositional()) {
            value = (Value)source.getLevel().environmentAttributes().getValue(attribute, source.getPosition());
        } else {
            value = (Value)source.getLevel().environmentAttributes().getDimensionValue(attribute);
        }
        /*
        if (value == null) {
            source.sendFailure(Component.translatable("commands.environment_attribute.cannot_get_value", attribute.toString()));
            return 0;
        }*/
        if (attribute.type() == AttributeTypes.BOOLEAN) {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.success", attribute.toString(), (Boolean)value ? Component.literal("true").withStyle(s -> s.withColor(ChatFormatting.GREEN)) : Component.literal("false").withStyle(s -> s.withColor(ChatFormatting.RED))), true);
        } else if (attribute.type() == AttributeTypes.TRI_STATE) {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.success", attribute.toString(), NbtUtils.toPrettyComponent(StringTag.valueOf(((TriState)value).getSerializedName()))), true);
        } else {
            CompoundTag tag = EnvironmentAttributeCommand.build(attribute.type(), value);
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.success", attribute.toString(), NbtUtils.toPrettyComponent(tag.get(TAG_VALUE))), true);
        }
        if (attribute.type().toFloat() != null) {
            return (int)((Float)value * 1.0f);
        }
        return 1;
    }

    private static <Value> int getDefaultValue(CommandSourceStack source, Holder.Reference<EnvironmentAttribute<?>> environmentAttribute){
        EnvironmentAttribute<Value> attribute = (EnvironmentAttribute<Value>)environmentAttribute.value();
        if (attribute.type() == AttributeTypes.BOOLEAN) {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.default_value", attribute.toString(), (Boolean)attribute.defaultValue() ? Component.literal("true").withStyle(s -> s.withColor(ChatFormatting.GREEN)) : Component.literal("false").withStyle(s -> s.withColor(ChatFormatting.RED))), true);
        } else if (attribute.type() == AttributeTypes.TRI_STATE) {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.default_value", attribute.toString(), NbtUtils.toPrettyComponent(StringTag.valueOf(((TriState)attribute.defaultValue()).getSerializedName()))), true);
        } else {
            CompoundTag tag = EnvironmentAttributeCommand.build(attribute.type(), attribute.defaultValue());
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.default_value", attribute.toString(), NbtUtils.toPrettyComponent(tag.get(TAG_VALUE))), true);
        }
        if (attribute.type().toFloat() != null) {
            return (int)((Float)attribute.defaultValue() * 1.0f);
        }
        return 1;
    }

    private static int getSetValue(CommandSourceStack source, EnvironmentAttribute<?>[] attribute_sets){
        for(EnvironmentAttribute<?> attribute: attribute_sets){
            EnvironmentAttributeCommand.getValue(source, attribute);
        }
        return 1;
    }

    private static <Value> CompoundTag build(AttributeType<Value> attributeType, Value value){
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_VALUE, (Tag)attributeType.valueCodec().encodeStart(NbtOps.INSTANCE, value).getOrThrow());
        return tag;
    }
}
