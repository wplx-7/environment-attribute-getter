package net.environment.attribute.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttribute;

public class CustomResourceArgument<T> extends ResourceArgument<T> {
    public CustomResourceArgument(CommandBuildContext context, ResourceKey registryKey) {
        super(context, registryKey);
    }

    public static Holder.Reference<EnvironmentAttribute<?>> getEnvironmentAttribute(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, name, Registries.ENVIRONMENT_ATTRIBUTE);
    }
}