package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.MinerBlocksRecipe;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.util.ModTags;
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
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Arrays;

public class MinerBlocksRecipeCategory implements IRecipeCategory<MinerBlocksRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "miner_blocks");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_miners.png");

    static final RecipeType<MinerBlocksRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "miner_blocks",
            MinerBlocksRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public MinerBlocksRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MINER_BASE_BLOCK.get()));
    }

    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public RecipeType<MinerBlocksRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.MINER_BLOCKS_SERIALIZER.getId(), MinerBlocksRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Miners");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MinerBlocksRecipe recipe, @NotNull IFocusGroup focusGroup) {

        String blockName = recipe.getBlock();
        TagKey<Item> minerBlockTag = ItemTags.create(new ResourceLocation(blockName));

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 135, 43).addIngredients(Ingredient.of(minerBlockTag));;

        int minerTier = recipe.getMinerTier();
        TagKey<Item> minerTierSupportFrame = ModTags.Items.EMPTY;

        if (minerTier == 1) {
            minerTierSupportFrame = ModTags.Items.WOODEN_SUPPORT_FRAME;
        }
        if (minerTier == 2) {
            minerTierSupportFrame = ModTags.Items.STONE_SUPPORT_FRAME;
        }
        if (minerTier == 3) {
            minerTierSupportFrame = ModTags.Items.LAPIS_SUPPORT_FRAME;
        }
        if (minerTier == 4) {
            minerTierSupportFrame = ModTags.Items.COPPER_SUPPORT_FRAME;
        }
        if (minerTier == 5) {
            minerTierSupportFrame = ModTags.Items.IRON_SUPPORT_FRAME;
        }
        if (minerTier == 6) {
            minerTierSupportFrame = ModTags.Items.GOLD_SUPPORT_FRAME;
        }
        if (minerTier == 7) {
            minerTierSupportFrame = ModTags.Items.DIAMOND_SUPPORT_FRAME;
        }
        if (minerTier == 8) {
            minerTierSupportFrame = ModTags.Items.EMERALD_SUPPORT_FRAME;
        }
        if (minerTier == 9) {
            minerTierSupportFrame = ModTags.Items.NETHERITE_SUPPORT_FRAME;
        }
        if (minerTier == 10) {
            minerTierSupportFrame = ModTags.Items.ULTIMATE_SUPPORT_FRAME;
        }


        builder.addSlot(RecipeIngredientRole.INPUT, 135, 5).addItemStack(new ItemStack(Blocks.CHEST)
                .setHoverName(Component.literal("Any inventory can be used here")));

        builder.addSlot(RecipeIngredientRole.INPUT, 135, 24).addItemStack(new ItemStack(ModBlocks.MINER_BASE_BLOCK.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 62).addIngredients(Ingredient.of(minerTierSupportFrame));

    //    builder.addSlot(RecipeIngredientRole.INPUT, 154, 62).addItemStack(new ItemStack((Holder<Item>) Ingredient.of(minerTierSupportFrame), 50 ));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addIngredients(Ingredient.of(minerBlockTag));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(minerBlockTag));




    }

    @Override
    public void draw(@NotNull MinerBlocksRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {

        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_1"), 5, 7, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_2"), 5, 15, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_3"), 5, 23, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_4"), 5, 31, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_5"), 5, 39, Color.black.getRGB());
        minecraft.font.draw(stack, Component.translatable("jei.recipes.miners_line_6"), 5, 47, Color.black.getRGB());

        minecraft.font.draw(stack, Component.literal("x36"), 154, 47, Color.black.getRGB());
        minecraft.font.draw(stack, Component.literal("x32"), 154, 66, Color.black.getRGB());

    }
}
