package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.CapBlocksRecipe;
import com.benbenlaw.miners.recipe.ModRecipes;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.awt.*;

public class CapBlocksRecipeCategory implements IRecipeCategory<CapBlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "cap_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_miner_speed_blocks.png");

    static final RecipeType<CapBlocksRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "cap_blocks",
            CapBlocksRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CapBlocksRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MINER_BASE_BLOCK.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<CapBlocksRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.CAP_BLOCKS_SERIALIZER.getId(), CapBlocksRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Cap Blocks");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CapBlocksRecipe recipe, IFocusGroup focusGroup) {

        String blockName = recipe.getBlock();
        Block rgBlock = Registry.BLOCK.get(new ResourceLocation(blockName));
        TagKey<Item> itemTag = ItemTags.create(new ResourceLocation(blockName));

        if (rgBlock == Blocks.AIR) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 5).addIngredients(Ingredient.of(itemTag));
        } else {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 140, 5).addItemStack(new ItemStack(rgBlock.asItem()));

        }

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(rgBlock.asItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(itemTag));

    }

    @Override
    public void draw(CapBlocksRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_2"), 5, 15, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_3"), 5, 23, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.resource_generator_2_speed_blocks_line_4"), 5, 31, Color.black.getRGB());


        minecraft.font.draw(stack, Component.translatable("jei.recipes.miner_tickrate"), 5, 60, Color.black.getRGB());
        minecraft.font.draw(stack, Component.literal(recipe.getTickRate() + ""), 5, 68, Color.black.getRGB());


    }
}
