package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import com.benbenlaw.opolisutilities.loot.ModLootTables;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeLevel;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class TreeAbsorberRecipeCategory implements IRecipeCategory<TreeAbsorberRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "tree_absorber");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Miners.MOD_ID, "textures/gui/jei_tree_absorber.png");

    static final RecipeType<TreeAbsorberRecipe> RECIPE_TYPE = RecipeType.create(Miners.MOD_ID, "tree_absorber",
            TreeAbsorberRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TreeAbsorberRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TREE_ABSORBER.get()));
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

        Block log = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(recipe.getPattern()));

        assert log != null;
        assert Minecraft.getInstance().player != null;
        List<ItemStack> logDrops = LootTableHelper.getLoot(log, Minecraft.getInstance().player);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 5, 13).addItemStack(logDrops.get(0));
    }


    @Override
    public void draw(TreeAbsorberRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();


        int fuelDuration = recipe.getDuration();
        String id = recipe.getPattern().replace("miners:", "");
        int rfPerTick = recipe.getRFPerTick();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei_pattern.tree_absorber." + id), 2, 0, Color.darkGray.getRGB(), false);


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

    IForgeLevel getLevel(Level level) {
        return level;
    }
}
