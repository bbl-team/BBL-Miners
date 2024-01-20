package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.networking.ModMessages;
import com.benbenlaw.miners.recipe.ModRecipes;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import com.benbenlaw.opolisutilities.loot.ModLootTables;
import com.google.gson.JsonParser;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeLevel;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeAbsorberRecipeCategory implements IRecipeCategory<TreeAbsorberRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Miners.MOD_ID, "tree_absorber");
    public ResourceLocation TEXTURE =
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

        builder.addSlot(RecipeIngredientRole.OUTPUT, 5, 13).addItemStack(new ItemStack(recipe.getSapling().getItem()));


        if (recipe.getExtraItem() == ItemStack.EMPTY) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 42).addItemStack(new ItemStack(recipe.getLog().getItem())).addTooltipCallback(chanceLog());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 42).addItemStack(new ItemStack(recipe.getLeaf().getItem())).addTooltipCallback(requiresShearUpgrade());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 42).addItemStack(new ItemStack(recipe.getSapling().getItem())).addTooltipCallback(chanceSapling());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 156, 42).addItemStack(new ItemStack(Items.STICK)).addTooltipCallback(chanceStick());

        } else {

            builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 42).addItemStack(new ItemStack(recipe.getLog().getItem())).addTooltipCallback(chanceLog());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 42).addItemStack(new ItemStack(recipe.getLeaf().getItem())).addTooltipCallback(requiresShearUpgrade());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 42).addItemStack(new ItemStack(recipe.getSapling().getItem())).addTooltipCallback(chanceSapling());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 42).addItemStack(new ItemStack(Items.STICK)).addTooltipCallback(chanceStick());

            builder.addSlot(RecipeIngredientRole.INPUT, 156, 42).addItemStack(new ItemStack(recipe.getExtraItem().getItem()))
                    .setBackground(JEIMinersPlugin.slotDrawable, -89, -1).addTooltipCallback(chanceExtra(recipe));
        }

    }

    private IRecipeSlotTooltipCallback requiresShearUpgrade() {
        return (chance, addTooltip) -> {
            addTooltip.add(Component.literal("Chance: 100% (With Shear Upgrade)"));
        };
    }

    private IRecipeSlotTooltipCallback chanceExtra(TreeAbsorberRecipe recipe) {
        return (chance, addTooltip) -> {
            String string = ("Chance: " + recipe.getExtraItemChance() * 100 + "%").replace("0.0", "");
            addTooltip.add(Component.literal(string));
        };
    }

    private IRecipeSlotTooltipCallback chanceStick() {
        return (chance, addTooltip) -> {
            addTooltip.add(Component.literal("Chance: 5% (Without Shear Upgrade)"));
        };
    }

    private IRecipeSlotTooltipCallback chanceSapling() {
        return (chance, addTooltip) -> {
            addTooltip.add(Component.literal("Chance: 5% (Without Shear Upgrade)"));
        };
    }
    private IRecipeSlotTooltipCallback chanceLog() {
        return (chance, addTooltip) -> {
            addTooltip.add(Component.literal("Chance: 100% (With or Without Shear Upgrade)"));
        };
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
