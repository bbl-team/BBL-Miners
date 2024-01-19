package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.recipe.ModRecipes;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class MinerRecipeCategory implements IRecipeCategory<MinerRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "miners");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_miner.png");

    static final RecipeType<MinerRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "miners",
            MinerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public MinerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MINER.get()));
    }

    @Override
    public RecipeType<MinerRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.MINER_SERIALIZER.getId(), MinerRecipe.class);
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
    public void setRecipe(IRecipeLayoutBuilder builder, MinerRecipe recipe, @NotNull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 5, 13).addItemStack(recipe.getOutputItem());
    }

    @Override
    public void draw(MinerRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        int fuelDuration = recipe.getDuration();
        String id = recipe.getPattern().replace("miners:", "");
        int rfPerTick = recipe.getRFPerTick();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei_pattern.miner." + id), 2, 0, Color.darkGray.getRGB(), false);


        boolean durationArea = mouseX >= 67 && mouseX <= 171 && mouseY >= 16 && mouseY <= 25;
        boolean rfArea = mouseX >= 67 && mouseX <= 171 && mouseY >= 29 && mouseY <= 38;

        if (durationArea) {
            guiGraphics.drawString(minecraft.font.self(), Component.literal( fuelDuration/20 + " Seconds"), 68, 17, Color.darkGray.getRGB(), false);
        } else {
            guiGraphics.drawString(minecraft.font.self(), Component.literal(fuelDuration + " Ticks"), 68, 17, Color.darkGray.getRGB(), false);

        }
        if (rfArea) {
            guiGraphics.drawString(minecraft.font.self(), Component.literal( "RF Per Tick: " + rfPerTick), 68, 30, Color.white.getRGB(), false);
        } else {
            guiGraphics.drawString(minecraft.font.self(), Component.literal("Total RF: " + (rfPerTick * fuelDuration) + " RF"), 68, 30, Color.white.getRGB(), false);

        }

    }
}
