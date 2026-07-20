package com.environment.attribute.commands;

import com.environment.attribute.commands.arguments.CustomResourceArgument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DynamicOps;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnvironmentAttributeCommand {
    private static final String TAG_VALUE = "value";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
                ).then(Commands.literal("export")
                        .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                        .executes(c -> EnvironmentAttributeCommand.export(c.getSource(), false))
                ).then(Commands.literal("exportdefault")
                        .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                        .executes(c -> EnvironmentAttributeCommand.export(c.getSource(), true))
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

    private static <Value> int export(CommandSourceStack source, boolean exportDefaultValue){
        EnvironmentAttributeMap.Builder generatedAttributes = EnvironmentAttributeMap.builder();
        source.getLevel().registryAccess().lookupOrThrow(Registries.ENVIRONMENT_ATTRIBUTE).listElements().forEach(attribute -> {
            if (exportDefaultValue){
                generatedAttributes.set((EnvironmentAttribute<Value>)attribute.value(), (Value)attribute.value().defaultValue());
            } else {
                if (attribute.value().isPositional()){
                    generatedAttributes.set((EnvironmentAttribute<Value>)attribute.value(), (Value)source.getLevel().environmentAttributes().getValue(attribute.value(), source.getPosition()));
                } else {
                    generatedAttributes.set((EnvironmentAttribute<Value>)attribute.value(), (Value)source.getLevel().environmentAttributes().getDimensionValue(attribute.value()));
                }
            }
        });
        EnvironmentAttributeMap generatedAttributesMap = generatedAttributes.build();
        JsonElement json = (JsonElement)EnvironmentAttributeMap.CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, generatedAttributesMap).getOrThrow();
        Path directory = source.getServer().getFile("debug");
        String filename = "environment-attribute-" + (exportDefaultValue ? "default-" : "") + Util.getFilenameFormattedDateTime() + ".json";
        try {
            Files.createDirectories(directory);
            try (BufferedWriter outputWriter = Files.newBufferedWriter(directory.resolve(filename), StandardCharsets.UTF_8);){
                GSON.toJson(JsonParser.parseString(GsonHelper.toStableString(json)), GSON.newJsonWriter(outputWriter));
            }
        } catch (IOException e){
            if (exportDefaultValue) {
                source.sendFailure(Component.translatable("commands.environment_attribute.export.default.fail"));
            } else {
                source.sendFailure(Component.translatable("commands.environment_attribute.export.fail"));
            }
            return 0;
        }
        if (exportDefaultValue) {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.export.default.success", filename), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.export.success", filename), true);
        }
        return 1;
    }

    private static <Value> CompoundTag build(AttributeType<Value> attributeType, Value value){
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_VALUE, (Tag)attributeType.valueCodec().encodeStart(NbtOps.INSTANCE, value).getOrThrow());
        return tag;
    }
}
