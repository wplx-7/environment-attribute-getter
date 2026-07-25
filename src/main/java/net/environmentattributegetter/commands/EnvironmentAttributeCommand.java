package net.environmentattributegetter.commands;

import net.environmentattributegetter.commands.arguments.CustomResourceArgument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
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
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnvironmentAttributeCommand {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final SimpleCommandExceptionType ERROR_EXPORT_FAILURE = new SimpleCommandExceptionType(Component.translatable("commands.environment_attribute.export.io_failure"));
    private static final String TAG_VALUE = "value";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EnvironmentAttribute<?>[] VISUAL_COLOR_ATTRIBUTE = new EnvironmentAttribute[]{EnvironmentAttributes.SKY_COLOR, EnvironmentAttributes.FOG_COLOR, EnvironmentAttributes.WATER_FOG_COLOR, EnvironmentAttributes.CLOUD_COLOR, EnvironmentAttributes.SUNRISE_SUNSET_COLOR, EnvironmentAttributes.BLOCK_LIGHT_TINT, EnvironmentAttributes.SKY_LIGHT_COLOR, EnvironmentAttributes.NIGHT_VISION_COLOR, EnvironmentAttributes.AMBIENT_LIGHT_COLOR};
    private static final EnvironmentAttribute<?>[] VISUAL_FOG_DISTANCE_ATTRIBUTE = new EnvironmentAttribute[]{EnvironmentAttributes.FOG_START_DISTANCE, EnvironmentAttributes.FOG_END_DISTANCE, EnvironmentAttributes.WATER_FOG_START_DISTANCE, EnvironmentAttributes.WATER_FOG_END_DISTANCE, EnvironmentAttributes.SKY_FOG_END_DISTANCE, EnvironmentAttributes.CLOUD_FOG_END_DISTANCE};

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("environment_attribute")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("query")
                        .then(Commands.argument("environment_attribute", ResourceArgument.resource(context, Registries.ENVIRONMENT_ATTRIBUTE))
                                .executes(c -> EnvironmentAttributeCommand.query(c.getSource(), CustomResourceArgument.getEnvironmentAttribute(c, "environment_attribute"), false))
                        )
                ).then(Commands.literal("querydefault")
                        .then(Commands.argument("environment_attribute", ResourceArgument.resource(context, Registries.ENVIRONMENT_ATTRIBUTE))
                                .executes(c -> EnvironmentAttributeCommand.query(c.getSource(), CustomResourceArgument.getEnvironmentAttribute(c, "environment_attribute"), true))
                        )
                ).then(Commands.literal("queryvisualcolor")
                        .executes(c -> EnvironmentAttributeCommand.queryMulti(c.getSource(), VISUAL_COLOR_ATTRIBUTE))
                ).then(Commands.literal("queryfogdistance")
                        .executes(c -> EnvironmentAttributeCommand.queryMulti(c.getSource(), VISUAL_FOG_DISTANCE_ATTRIBUTE))
                ).then(Commands.literal("exportall")
                        .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                        .executes(c -> EnvironmentAttributeCommand.export(c.getSource(), false))
                ).then(Commands.literal("exportdallefault")
                        .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                        .executes(c -> EnvironmentAttributeCommand.export(c.getSource(), true))
                )
        );
    }

    private static int query(CommandSourceStack source, Holder.Reference<EnvironmentAttribute<?>> environmentAttribute, boolean queryDefaultValue){
        return EnvironmentAttributeCommand.query(source, environmentAttribute.value(), queryDefaultValue);
    }

    private static <Value> int query(CommandSourceStack source, EnvironmentAttribute<Value> attribute, boolean queryDefaultValue){
        String message = queryDefaultValue ? "commands.environment_attribute.query.default": "commands.environment_attribute.query";
        Value value = EnvironmentAttributeCommand.getValue(source, attribute, queryDefaultValue);
        if (attribute.type() == AttributeTypes.BOOLEAN) {
            source.sendSuccess(() -> Component.translatable(message, attribute.toString(), (Boolean)value ? Component.literal("true").withStyle(s -> s.withColor(ChatFormatting.GREEN)) : Component.literal("false").withStyle(s -> s.withColor(ChatFormatting.RED))), true);
        } else if (attribute.type() == AttributeTypes.TRI_STATE) {
            source.sendSuccess(() -> Component.translatable(message, attribute.toString(), NbtUtils.toPrettyComponent(StringTag.valueOf(((TriState)value).getSerializedName()))), true);
        } else {
            CompoundTag tag = EnvironmentAttributeCommand.buildNbt(attribute.type(), value);
            source.sendSuccess(() -> Component.translatable(message, attribute.toString(), NbtUtils.toPrettyComponent(tag.get(TAG_VALUE))), true);
        }
        if (attribute.type().toFloat() != null) {
            return (int)attribute.type().toFloat(value);
        }
        return 1;
    }

    private static int queryMulti(CommandSourceStack source, EnvironmentAttribute<?>[] attribute_sets){
        for(EnvironmentAttribute<?> attribute: attribute_sets){
            EnvironmentAttributeCommand.query(source, attribute, false);
        }
        return 1;
    }

    private static <Value> int export(CommandSourceStack source, boolean exportDefaultValue) throws CommandSyntaxException {
        EnvironmentAttributeMap.Builder generatedAttributes = EnvironmentAttributeMap.builder();
        source.getLevel().registryAccess().lookupOrThrow(Registries.ENVIRONMENT_ATTRIBUTE).listElements().forEach(attribute -> {generatedAttributes.set((EnvironmentAttribute<Value>)attribute.value(), EnvironmentAttributeCommand.getValue(source, (EnvironmentAttribute<Value>)attribute.value(), exportDefaultValue));});
        EnvironmentAttributeMap generatedAttributesMap = generatedAttributes.build();
        JsonElement json = (JsonElement)EnvironmentAttributeMap.CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, generatedAttributesMap).getOrThrow();
        Path directory = source.getServer().getFile("debug");
        String filename = "environment-attribute-" + (exportDefaultValue ? "default-" : "") + Util.getFilenameFormattedDateTime() + ".json";
        try {
            Files.createDirectories(directory);
            try (BufferedWriter outputWriter = Files.newBufferedWriter(directory.resolve(filename), StandardCharsets.UTF_8)){
                GSON.toJson(JsonParser.parseString(GsonHelper.toStableString(json)), GSON.newJsonWriter(outputWriter));
            }
        } catch (IOException e){
            LOGGER.warn("Failed to export environment attribute data at {}", directory.toAbsolutePath(), e);
            throw ERROR_EXPORT_FAILURE.create();
        }
        if (exportDefaultValue) {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.export.default.success", filename), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.environment_attribute.export.success", filename), true);
        }
        return 1;
    }

    private static <Value> Value getValue(CommandSourceStack source, EnvironmentAttribute<Value> attribute, boolean defaultValue) {
        if (defaultValue) {
            return attribute.defaultValue();
        } else {
            if (attribute.isPositional()) {
                return source.getLevel().environmentAttributes().getValue(attribute, source.getPosition());
            } else {
                return source.getLevel().environmentAttributes().getDimensionValue(attribute);
            }
        }
    }

    private static <Value> CompoundTag buildNbt(AttributeType<Value> attributeType, Value value){
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_VALUE, attributeType.valueCodec().encodeStart(NbtOps.INSTANCE, value).getOrThrow());
        return tag;
    }
}
