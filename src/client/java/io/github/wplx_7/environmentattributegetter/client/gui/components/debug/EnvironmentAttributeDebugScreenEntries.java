package io.github.wplx_7.environmentattributegetter.client.gui.components.debug;


import io.github.wplx_7.environmentattributegetter.client.mixin.DebugScreenEntriesMixin;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;

public class EnvironmentAttributeDebugScreenEntries {
    public static final DebugEntryCategory ENVIRONMENT_ATTRIBUTE = new DebugEntryCategory(Component.translatable("debug.options.category.environment_attribute"), 20.0f);
    public static final Identifier FOG_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FOG_COLOR);
    public static final Identifier FOG_START_DISTANCE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FOG_START_DISTANCE);
    public static final Identifier FOG_END_DISTANCE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FOG_END_DISTANCE);
    public static final Identifier SKY_FOG_END_DISTANCE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_FOG_END_DISTANCE);
    public static final Identifier CLOUD_FOG_END_DISTANCE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE);
    public static final Identifier WATER_FOG_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_FOG_COLOR);
    public static final Identifier WATER_FOG_START_DISTANCE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_FOG_START_DISTANCE);
    public static final Identifier WATER_FOG_END_DISTANCE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_FOG_END_DISTANCE);
    public static final Identifier SKY_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_COLOR);
    public static final Identifier SUNRISE_SUNSET_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SUNRISE_SUNSET_COLOR);
    public static final Identifier CLOUD_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CLOUD_COLOR);
    public static final Identifier CLOUD_HEIGHT = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CLOUD_HEIGHT);
    public static final Identifier SUN_ANGLE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SUN_ANGLE);
    public static final Identifier MOON_ANGLE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.MOON_ANGLE);
    public static final Identifier STAR_ANGLE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.STAR_ANGLE);
    public static final Identifier MOON_PHASE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.MOON_PHASE);
    public static final Identifier STAR_BRIGHTNESS = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.STAR_BRIGHTNESS);
    public static final Identifier BLOCK_LIGHT_TINT = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.BLOCK_LIGHT_TINT);
    public static final Identifier SKY_LIGHT_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_LIGHT_COLOR);
    public static final Identifier SKY_LIGHT_FACTOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_LIGHT_FACTOR);
    public static final Identifier NIGHT_VISION_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.NIGHT_VISION_COLOR);
    public static final Identifier AMBIENT_LIGHT_COLOR = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.AMBIENT_LIGHT_COLOR);
    public static final Identifier DEFAULT_DRIPSTONE_PARTICLE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier AMBIENT_PARTICLES = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.AMBIENT_PARTICLES, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier BACKGROUND_MUSIC = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.BACKGROUND_MUSIC, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier MUSIC_VOLUME = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.MUSIC_VOLUME);
    public static final Identifier AMBIENT_SOUNDS = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.AMBIENT_SOUNDS, SharedConstants.IS_RUNNING_IN_IDE);
    public static final Identifier FIREFLY_BUSH_SOUNDS = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS);
    public static final Identifier SKY_LIGHT_LEVEL = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.SKY_LIGHT_LEVEL);
    public static final Identifier WATER_EVAPORATES = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.WATER_EVAPORATES);
    public static final Identifier FAST_LAVA = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.FAST_LAVA);
    public static final Identifier PIGLINS_ZOMBIFY = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.PIGLINS_ZOMBIFY);
    public static final Identifier CREAKING_ACTIVE = EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(EnvironmentAttributes.CREAKING_ACTIVE);

    public static Identifier registerEnvironmentAttributeEntry(EnvironmentAttribute<?> attribute, boolean enabled){
        if (!enabled) {
            return Identifier.parse(attribute.toString());
        }
        return EnvironmentAttributeDebugScreenEntries.registerEnvironmentAttributeEntry(attribute);
    }

    public static Identifier registerEnvironmentAttributeEntry(EnvironmentAttribute<?> attribute){
        return DebugScreenEntriesMixin.invokeRegister(Identifier.parse(attribute.toString()), new DebugEntryEnvironmentAttribute(attribute));
    }

    public static void onInitialize(){}
}
