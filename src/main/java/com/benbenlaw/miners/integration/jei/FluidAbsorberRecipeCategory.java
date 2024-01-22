package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.FluidAbsorberRecipe;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;

public class FluidAbsorberRecipeCategory implements IRecipeCategory<FluidAbsorberRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "fluid_absorber");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_fluid_absorber.png");

    static final RecipeType<FluidAbsorberRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "fluid_absorber",
            FluidAbsorberRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FluidAbsorberRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FLUID_ABSORBER.get()));
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

        @Nullable Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getOutputFluid()));
        assert fluid != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 5, 13).addFluidStack(fluid, 1000).addTooltipCallback(fluidAmount(recipe));

    }


    private IRecipeSlotTooltipCallback fluidAmount(FluidAbsorberRecipe recipe) {
        return (chance, addTooltip) -> {
            String string = ("Amount Per Cycle: " + recipe.getOutputAmount());
            addTooltip.add(Component.literal(string));
        };
    }

    @Override
    public void draw(FluidAbsorberRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        int fuelDuration = recipe.getDuration();
        String id = recipe.getPattern().replace("miners:", "");
        int rfPerTick = recipe.getRFPerTick();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei_pattern.fluid_absorber." + id), 2, 0, Color.darkGray.getRGB(), false);


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
