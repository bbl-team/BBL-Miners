package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER=
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Miners.MOD_ID);

    public static final RegistryObject<RecipeSerializer<FluidAbsorberRecipe>> FLUID_ABSORBER_SERIALIZER =
            SERIALIZER.register("fluid_absorber", () -> FluidAbsorberRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CapBlocksRecipe>> CAP_BLOCKS_SERIALIZER =
            SERIALIZER.register("cap_blocks", () -> CapBlocksRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<MinerBlocksRecipe>> MINER_BLOCKS_SERIALIZER =
            SERIALIZER.register("miner_blocks", () -> MinerBlocksRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<TreeAbsorberBlocksRecipe>> TREE_ABSORBER_BLOCK_SERIALIZER =
            SERIALIZER.register("tree_absorber_block", () -> TreeAbsorberBlocksRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }


}