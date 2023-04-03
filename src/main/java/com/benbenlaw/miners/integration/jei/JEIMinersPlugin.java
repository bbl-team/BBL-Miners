package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class JEIMinersPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Miners.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TREE_ABSORBER.get()), TreeAbsorberRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_ABSORBER.get()), FluidAbsorberRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MINER_BASE_BLOCK.get()), CapBlocksRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MINER_BASE_BLOCK.get()), MinerBlocksRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new
                TreeAbsorberRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                FluidAbsorberRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                CapBlocksRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new
                MinerBlocksRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<TreeAbsorberBlocksRecipe> treeAbsorber = rm.getAllRecipesFor(TreeAbsorberBlocksRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(TreeAbsorberRecipeCategory.UID, TreeAbsorberBlocksRecipe.class), treeAbsorber);

        List<FluidAbsorberRecipe> fluidAbsorber = rm.getAllRecipesFor(FluidAbsorberRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(FluidAbsorberRecipeCategory.UID, FluidAbsorberRecipe.class), fluidAbsorber);

        List<CapBlocksRecipe> capBlocks = rm.getAllRecipesFor(CapBlocksRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(CapBlocksRecipeCategory.UID, CapBlocksRecipe.class), capBlocks);

        List<MinerBlocksRecipe> minerBlocks = rm.getAllRecipesFor(MinerBlocksRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(MinerBlocksRecipeCategory.UID, MinerBlocksRecipe.class), minerBlocks);

    }
}
