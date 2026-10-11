package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.miners.data.custom.MinerStructureRecipeBuilder.minerStructure;

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
            return Miners.MOD_ID + " Recipes";
        }
    }

    @Override
    protected void buildRecipes() {

        minerStructure(Miners.identifier("cobble_miner"), List.of(Identifier.parse("minecraft:blocks/cobblestone")), 100, 1, 20)
                .save(output, "cobble_miner");

        minerStructure(Miners.identifier("cobble_miner"), List.of(Identifier.parse("minecraft:blocks/stone")), 100, 1, 20)
                .save(output, "stone_miner");

    }
}
