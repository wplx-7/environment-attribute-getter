package net.environment.attribute.client.gui.components.debug;


import net.environment.attribute.client.mixin.DebugScreenEntriesMixin;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;

public class EnvironmentAttributeEntries {
    public static final DebugEntryCategory ENVIRONMENT_ATTRIBUTE = new DebugEntryCategory(Component.translatable("debug.options.category.environment_attribute"), 3.0f);
    public static final Identifier FOG_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FOG_COLOR);
    public static final Identifier FOG_START_DISTANCE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FOG_START_DISTANCE);
    public static final Identifier FOG_END_DISTANCE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FOG_END_DISTANCE);
    public static final Identifier SKY_FOG_END_DISTANCE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_FOG_END_DISTANCE);
    public static final Identifier CLOUD_FOG_END_DISTANCE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE);
    public static final Identifier WATER_FOG_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_FOG_COLOR);
    public static final Identifier WATER_FOG_START_DISTANCE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_FOG_START_DISTANCE);
    public static final Identifier WATER_FOG_END_DISTANCE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_FOG_END_DISTANCE);
    public static final Identifier SKY_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_COLOR);
    public static final Identifier SUNRISE_SUNSET_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SUNRISE_SUNSET_COLOR);
    public static final Identifier CLOUD_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CLOUD_COLOR);
    public static final Identifier CLOUD_HEIGHT = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CLOUD_HEIGHT);
    public static final Identifier SUN_ANGLE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SUN_ANGLE);
    public static final Identifier MOON_ANGLE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.MOON_ANGLE);
    public static final Identifier STAR_ANGLE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.STAR_ANGLE);
    public static final Identifier MOON_PHASE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.MOON_PHASE);
    public static final Identifier STAR_BRIGHTNESS = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.STAR_BRIGHTNESS);
    public static final Identifier BLOCK_LIGHT_TINT = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.BLOCK_LIGHT_TINT);
    public static final Identifier SKY_LIGHT_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_LIGHT_COLOR);
    public static final Identifier SKY_LIGHT_FACTOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_LIGHT_FACTOR);
    public static final Identifier NIGHT_VISION_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.NIGHT_VISION_COLOR);
    public static final Identifier AMBIENT_LIGHT_COLOR = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.AMBIENT_LIGHT_COLOR);
    public static final Identifier DEFAULT_DRIPSTONE_PARTICLE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier BACKGROUND_MUSIC = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.BACKGROUND_MUSIC, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier MUSIC_VOLUME = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.MUSIC_VOLUME);
    public static final Identifier AMBIENT_SOUNDS = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.AMBIENT_SOUNDS, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier FIREFLY_BUSH_SOUNDS = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS);
    public static final Identifier SKY_LIGHT_LEVEL = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_LIGHT_LEVEL);
    public static final Identifier WATER_EVAPORATES = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_EVAPORATES);
    public static final Identifier FAST_LAVA = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FAST_LAVA);
    public static final Identifier PIGLINS_ZOMBIFY = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.PIGLINS_ZOMBIFY);
    public static final Identifier CREAKING_ACTIVE = EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CREAKING_ACTIVE);

    public static Identifier registerEnvironmentAttributeEntry(EnvironmentAttribute<?> attribute, boolean enabled){
        if (!enabled) {
            return Identifier.parse(attribute.toString());
        }
        return EnvironmentAttributeEntries.registerEnvironmentAttributeEntry(attribute);
    }

    public static Identifier registerEnvironmentAttributeEntry(EnvironmentAttribute<?> attribute){
        return DebugScreenEntriesMixin.invokeRegister(Identifier.parse(attribute.toString()), new EnvironmentAttributeDebugEntry(attribute));
    }

    public static void onInitialize(){}
}
