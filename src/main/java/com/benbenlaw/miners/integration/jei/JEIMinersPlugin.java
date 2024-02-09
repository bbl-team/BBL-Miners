package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.CrusherRecipe;
import com.benbenlaw.miners.recipe.FluidAbsorberRecipe;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import com.benbenlaw.opolisutilities.integration.jei.UpgradeRecipeUtilCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class JEIMinersPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Miners.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MINER.get()), MinerRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TREE_ABSORBER.get()), TreeAbsorberRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_ABSORBER.get()), FluidAbsorberRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRUSHER.get()), CrusherRecipeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TREE_ABSORBER.get()), UpgradeRecipeUtilCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRUSHER.get()), UpgradeRecipeUtilCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MINER.get()), UpgradeRecipeUtilCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_ABSORBER.get()), UpgradeRecipeUtilCategory.RECIPE_TYPE);


    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new
                MinerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                TreeAbsorberRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                FluidAbsorberRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new
                CrusherRecipeCategory(registration.getJeiHelpers().getGuiHelper()));


        slotDrawable = guiHelper.getSlotDrawable();

    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<MinerRecipe> minersRecipes = rm.getAllRecipesFor(MinerRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(MinerRecipeCategory.UID, MinerRecipe.class), minersRecipes);

        List<TreeAbsorberRecipe> treeRecipes = rm.getAllRecipesFor(TreeAbsorberRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(TreeAbsorberRecipeCategory.UID, TreeAbsorberRecipe.class), treeRecipes);

        List<FluidAbsorberRecipe> fluidAbsorberRecipes = rm.getAllRecipesFor(FluidAbsorberRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(FluidAbsorberRecipeCategory.UID, FluidAbsorberRecipe.class), fluidAbsorberRecipes);

        List<CrusherRecipe> crusherRecipes = rm.getAllRecipesFor(CrusherRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(CrusherRecipeCategory.UID, CrusherRecipe.class), crusherRecipes);


    }
}
