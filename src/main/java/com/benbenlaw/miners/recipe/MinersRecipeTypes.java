package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MinersRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Miners.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Miners.MOD_ID);

    public static final Supplier<RecipeSerializer<MinerStructureRecipe>> STRUCTURE_SERIALIZER =
            SERIALIZER.register("structure", () -> MinerStructureRecipe.SERIALIZER);
    public static final Supplier<RecipeType<MinerStructureRecipe>> STRUCTURE_TYPE =
            TYPES.register("structure", () -> MinerStructureRecipe.TYPE);
}
