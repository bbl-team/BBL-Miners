package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.item.ModItems;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.recipe.TreeAbsorberBlocksRecipe;
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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;


public class TreeAbsorberRecipeCategory implements IRecipeCategory<TreeAbsorberBlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "tree_absorber_block");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_tree_absorber.png");

    static final RecipeType<TreeAbsorberBlocksRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "tree_absorber_block",
            TreeAbsorberBlocksRecipe.class);

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
    public RecipeType<TreeAbsorberBlocksRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.TREE_ABSORBER_BLOCK_SERIALIZER.getId(), TreeAbsorberBlocksRecipe.class);
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
    public void setRecipe(IRecipeLayoutBuilder builder, TreeAbsorberBlocksRecipe recipe, @NotNull IFocusGroup focusGroup) {

        String logName = recipe.getLogBlock();
        String leafName = recipe.getLeafBlock();
        Block logBlock = Registry.BLOCK.get(new ResourceLocation(logName));
        Block leafBlock = Registry.BLOCK.get(new ResourceLocation(leafName));


        builder.addSlot(RecipeIngredientRole.INPUT, 135, 5).addItemStack(new ItemStack(ModBlocks.TREE_ABSORBER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 24).addItemStack(new ItemStack(ModBlocks.WOODEN_SUPPORT_FRAME.get(), 64));
        builder.addSlot(RecipeIngredientRole.INPUT, 154, 24).addItemStack(new ItemStack(recipe.getExtraItem1().getItem())
                .setHoverName(Component.literal("Chance: "+ (recipe.getExtraItem1Chance() * 100) +"%")));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 43).addItemStack(new ItemStack(logBlock.asItem(), 4));
        builder.addSlot(RecipeIngredientRole.INPUT, 154, 43).addItemStack(new ItemStack(recipe.getExtraItem2().getItem())
                .setHoverName(Component.literal("Chance: "+ (recipe.getExtraItem2Chance() * 100) +"%")));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 62).addItemStack(new ItemStack(leafBlock.asItem(), 30));
        builder.addSlot(RecipeIngredientRole.INPUT, 154, 62).addItemStack(new ItemStack(recipe.getExtraItem3().getItem())
                .setHoverName(Component.literal("Chance: "+ (recipe.getExtraItem3Chance() * 100) +"%")));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(logBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(leafBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(recipe.getExtraItem1().getItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(recipe.getExtraItem2().getItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(recipe.getExtraItem3().getItem()));
    //    builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(2));
    //    builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(recipe.getIngredients().get(1));


    }

    @Override
    public void draw(@NotNull TreeAbsorberBlocksRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_2"), 5, 15, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_3"), 5, 23, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.ta_line_1"), 5, 31, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.ta_line_2"), 5, 39, Color.black.getRGB());

    //    minecraft.font.draw(stack, Component.literal("x64"), 154, 28, Color.black.getRGB());
    //    minecraft.font.draw(stack, Component.literal("x4"), 154, 47, Color.black.getRGB());
    //    minecraft.font.draw(stack, Component.literal("x30"), 154, 66, Color.black.getRGB());

    }
}

