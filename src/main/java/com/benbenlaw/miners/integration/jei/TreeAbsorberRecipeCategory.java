package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class TreeAbsorberRecipeCategory implements IRecipeCategory<TreeAbsorberRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "tree_absorber");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_miners.png");

///jei_miners.png");

    static final RecipeType<TreeAbsorberRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "tree_absorber",
            TreeAbsorberRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TreeAbsorberRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TREE_ABSORBER.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<TreeAbsorberRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.TREE_ABSORBER_SERIALIZER.getId(), TreeAbsorberRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Tree Absorber");
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
    public void setRecipe(IRecipeLayoutBuilder builder, TreeAbsorberRecipe recipe, @NotNull IFocusGroup focusGroup) {


        builder.addSlot(RecipeIngredientRole.INPUT, 135, 5).addItemStack(new ItemStack(ModBlocks.TREE_ABSORBER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 24).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 43).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 62).addIngredients(recipe.getIngredients().get(2));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(1));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(2));
    //    builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(1));


    }

    @Override
    public void draw(@NotNull TreeAbsorberRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_2"), 5, 15, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_3"), 5, 23, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.ta_line_1"), 5, 31, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.ta_line_2"), 5, 39, Color.black.getRGB());

        minecraft.font.draw(stack, Component.literal("x84"), 154, 28, Color.black.getRGB());
        minecraft.font.draw(stack, Component.literal("x4"), 154, 47, Color.black.getRGB());
        minecraft.font.draw(stack, Component.literal("x30"), 154, 66, Color.black.getRGB());

    }
}
