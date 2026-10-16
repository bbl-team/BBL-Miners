package com.benbenlaw.multiblocks.data;

import com.benbenlaw.multiblocks.Multiblocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.multiblocks.data.custom.ControllerCraftingRecipeBuilder.controllerCrafting;
import static com.benbenlaw.multiblocks.data.custom.ControllerCraftingRecipeBuilder.controllerLoot;

public class MinersRecipeProvider extends RecipeProvider {

    public MinersRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new MinersRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return Multiblocks.MOD_ID + " Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        addSimpleMinerRecipe("cobblestone", 200, 20);
        addSimpleMinerRecipe("stone", 200, 30);
        addSimpleMinerRecipe("granite", 200, 40);
        addSimpleMinerRecipe("andesite", 200, 40);
        addSimpleMinerRecipe("diorite", 200, 40);

        //Crafting Controller examples
        controllerCrafting(Items.DIAMOND)
                .define('C', Items.COAL_BLOCK)
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .baseDuration(200)
                .energyPerTick(40)
                .optional(Items.EMERALD, o -> o.consumeChance(0.25).bonusOutput(Items.DIAMOND, 1, 0.5))
                .save(output, "demo_diamond");

        controllerCrafting(Items.EMERALD)
                .define('G', Items.GOLD_INGOT)
                .define('D', Items.DIAMOND)
                .pattern("GGG")
                .pattern("GDG")
                .pattern("GGG")
                .extra(Items.REDSTONE, 4)
                .optional(Items.GLOWSTONE_DUST, o -> o.duration(0.5).energy(1.25))
                .optional(Items.NETHER_STAR, o -> o.keep().extraOutput(2))
                .structure(Multiblocks.identifier("crafting_demo"))
                .baseDuration(400)
                .energyPerTick(60)
                .save(output, "demo_emerald");

        controllerCrafting(Items.BLAST_FURNACE)
                .define('F', Items.FURNACE, 10)
                .pattern("F")
                .baseDuration(300)
                .energyPerTick(40)
                .save(output, "demo_blast_furnace");

        addShowcaseRecipe();
    }

    /**
     * Everything the Crafting Controller can do in one recipe: sized ingredients in the grid, in the required research
     * slots and in the optional items, an ingredient that accepts several items, a required structure, and optional items
     * that change the speed, the energy use, the output count, add bonus drops or change the result entirely.
     */
    private void addShowcaseRecipe() {
        controllerCrafting(Items.BEACON)
                // grid: 20 obsidian in each of three slots, then gold blocks around a nether star, then diamond blocks
                .define('O', Items.OBSIDIAN, 20)
                .define('G', Items.GOLD_BLOCK, 4)
                .define('N', Items.NETHER_STAR, 1)
                .define('D', Items.DIAMOND_BLOCK, 2)
                .pattern("OOO")
                .pattern("GNG")
                .pattern("DDD")
                // required research items, each in its own slot
                .extra(Items.ENDER_EYE, 4)
                .extra(Items.BLAZE_POWDER, 16)
                .extra(Items.AMETHYST_SHARD, e -> e.count(8).keep().duration(0.9)) // required, but not used up
                .extra(Ingredient.of(Items.COAL, Items.CHARCOAL), 32)
                // optional items
                .optional(Items.GLOWSTONE_DUST, o -> o.count(16).duration(0.5).energy(1.5))
                .optional(Items.ECHO_SHARD, o -> o.count(4).keep().outputMultiplier(2))
                .optional(Items.EMERALD_BLOCK, o -> o.count(2).consumeChance(0.5)
                        .bonusOutput(Items.DIAMOND, 8, 0.75)
                        .bonusOutput(Items.EMERALD, 4, 0.25))
                .optional(Items.HEART_OF_THE_SEA, o -> o.keep().energy(0.5))
                .optional(Items.DRAGON_BREATH, o -> o.count(4).replaceResult(Items.END_CRYSTAL, 4).extraOutput(2))
                .structure(Multiblocks.identifier("showcase_altar"))
                .baseDuration(1200)
                .energyPerTick(200)
                .save(output, "showcase_beacon");
    }

    private void addSimpleMinerRecipe(String name, int baseDuration, int energyPerTick) {
        controllerLoot(List.of(Identifier.parse("minecraft:blocks/" + name)), 1)
                .structure(Multiblocks.identifier(name + "_miner"))
                .baseDuration(baseDuration)
                .energyPerTick(energyPerTick)
                .save(output, name);
    }
}
