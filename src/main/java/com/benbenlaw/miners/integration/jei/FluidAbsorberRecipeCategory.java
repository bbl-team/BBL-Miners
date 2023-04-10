package com.benbenlaw.miners.integration.jei;

import java.util.List;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.FluidAbsorberRecipe;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.util.ModTags;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class FluidAbsorberRecipeCategory implements IRecipeCategory<FluidAbsorberRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "fluid_absorber");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_fluid_absorber.png");

///jei_miners.png");

    static final RecipeType<FluidAbsorberRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "fluid_absorber",
            FluidAbsorberRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FluidAbsorberRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FLUID_ABSORBER.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<FluidAbsorberRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.FLUID_ABSORBER_SERIALIZER.getId(), FluidAbsorberRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Fluid Absorber");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidAbsorberRecipe recipe, @NotNull IFocusGroup focusGroup) {

        String fluidString = recipe.getFluid();
        Fluid fluidStack = Registry.FLUID.get(new ResourceLocation(fluidString));

        builder.addSlot(RecipeIngredientRole.INPUT, 135, 24).addItemStack(new ItemStack(ModBlocks.FLUID_ABSORBER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 43).addFluidStack(fluidStack, 36000).setFluidRenderer(36000, true, 16,16);
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 62).addItemStack(new ItemStack(ModBlocks.IRON_SUPPORT_FRAME.get(), 32));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 43).addFluidStack(fluidStack, 36000);


        //    builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()));
    //    builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(2));




    }

    @Override
    public void draw(@NotNull FluidAbsorberRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_2"), 5, 15, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_3"), 5, 23, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.fa_line_1"), 5, 31, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.fa_line_2"), 5, 39, Color.black.getRGB());


    }
}
