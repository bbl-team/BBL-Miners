package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER=
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Miners.MOD_ID);


    public static final RegistryObject<RecipeSerializer<MinerRecipe>> MINER_SERIALIZER =
            SERIALIZER.register("miners", () -> MinerRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<TreeAbsorberRecipe>> TREE_ABSORBER_SERIALIZER =
            SERIALIZER.register("tree_absorber", () -> TreeAbsorberRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }


}
