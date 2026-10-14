package com.benbenlaw.multiblocks.recipe;

import com.benbenlaw.multiblocks.Multiblocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MultiblocksRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Multiblocks.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Multiblocks.MOD_ID);

    public static final Supplier<RecipeSerializer<MinerStructureRecipe>> MINER_STRUCTURE_SERIALIZER =
            SERIALIZER.register("miner_structure", () -> MinerStructureRecipe.SERIALIZER);
    public static final Supplier<RecipeType<MinerStructureRecipe>> MINER_STRUCTURE_TYPE =
            TYPES.register("miner_structure", () -> MinerStructureRecipe.TYPE);

    public static final Supplier<RecipeSerializer<ControllerCraftingRecipe>> CONTROLLER_CRAFTING_SERIALIZER =
            SERIALIZER.register("controller_crafting", () -> ControllerCraftingRecipe.SERIALIZER);
    public static final Supplier<RecipeType<ControllerCraftingRecipe>> CONTROLLER_CRAFTING_TYPE =
            TYPES.register("controller_crafting", () -> ControllerCraftingRecipe.TYPE);
}
