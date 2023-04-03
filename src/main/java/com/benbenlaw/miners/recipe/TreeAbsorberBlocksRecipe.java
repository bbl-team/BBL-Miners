package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public final class TreeAbsorberBlocksRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String logBlock;
    private final String leafBlock;
    private final ItemStack extraItem1;
    private final ItemStack extraItem2;
    private final ItemStack extraItem3;
    private final double extraItem1Chance;
    private final double extraItem2Chance;
    private final double extraItem3Chance;



    public TreeAbsorberBlocksRecipe(ResourceLocation id, String logBlock, String leafBlock, ItemStack extraItem1, ItemStack extraItem2, ItemStack extraItem3,
                                    double extraItem1Chance, double extraItem2Chance, double extraItem3Chance ){

        this.id = id;
        this.logBlock = logBlock;
        this.leafBlock = leafBlock;
        this.extraItem1 = extraItem1;
        this.extraItem2 = extraItem2;
        this.extraItem3 = extraItem3;
        this.extraItem1Chance = extraItem1Chance;
        this.extraItem2Chance = extraItem2Chance;
        this.extraItem3Chance = extraItem3Chance;
    }

    public String getLogBlock() {
        return logBlock;
    }

    public String getLeafBlock() {
        return leafBlock;
    }

    public ItemStack getExtraItem1() {
        return extraItem1;
    }

    public ItemStack getExtraItem2() {
        return extraItem2;
    }

    public ItemStack getExtraItem3() {
        return extraItem3;
    }

    public double getExtraItem1Chance() {
        return extraItem1Chance;
    }
    public double getExtraItem2Chance() {
        return extraItem2Chance;
    }
    public double getExtraItem3Chance() {
        return extraItem3Chance;
    }

    @Override
    public boolean matches(@NotNull NoInventoryRecipe inv, @NotNull Level pLevel) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull NoInventoryRecipe inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TreeAbsorberBlocksRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TreeAbsorberBlocksRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<TreeAbsorberBlocksRecipe> {
        private Type() { }
        public static final TreeAbsorberBlocksRecipe.Type INSTANCE = new TreeAbsorberBlocksRecipe.Type();
        public static final String ID = "tree_absorber_block";
    }

    public static class Serializer implements RecipeSerializer<TreeAbsorberBlocksRecipe> {
        public static final TreeAbsorberBlocksRecipe.Serializer INSTANCE = new TreeAbsorberBlocksRecipe.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID,"tree_absorber_block");

        @Override
        public TreeAbsorberBlocksRecipe fromJson(ResourceLocation id, JsonObject json) {
            String logBlock = GsonHelper.getAsString(json, "logBlock");
            String leafBlock = GsonHelper.getAsString(json, "leafBlock");
            ItemStack extraItem1 = GsonHelper.getAsItem(json, "extraItem1").getDefaultInstance();
            ItemStack extraItem2 = GsonHelper.getAsItem(json, "extraItem2").getDefaultInstance();
            ItemStack extraItem3 = GsonHelper.getAsItem(json, "extraItem3").getDefaultInstance();

            double extraItem1Chance = GsonHelper.getAsDouble(json, "extraItem1Chance");
            double extraItem2Chance = GsonHelper.getAsDouble(json, "extraItem2Chance");
            double extraItem3Chance = GsonHelper.getAsDouble(json, "extraItem3Chance");

            return new TreeAbsorberBlocksRecipe(id, logBlock, leafBlock, extraItem1, extraItem2, extraItem3, extraItem1Chance, extraItem2Chance, extraItem3Chance);
        }

        @Override
        public TreeAbsorberBlocksRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String logBlock = buf.readUtf();
            String leafBlock = buf.readUtf();
            ItemStack extraItem1 = buf.readItem();
            ItemStack extraItem2 = buf.readItem();
            ItemStack extraItem3 = buf.readItem();

            double extraItem1Chance = buf.readDouble();
            double extraItem2Chance = buf.readDouble();
            double extraItem3Chance = buf.readDouble();

            return new TreeAbsorberBlocksRecipe(id, logBlock, leafBlock, extraItem1, extraItem2, extraItem3, extraItem1Chance, extraItem2Chance, extraItem3Chance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TreeAbsorberBlocksRecipe recipe) {

            buf.writeUtf(recipe.getLogBlock(), Short.MAX_VALUE);
            buf.writeUtf(recipe.getLeafBlock(), Short.MAX_VALUE);

            buf.writeItemStack(recipe.getExtraItem1(), false);
            buf.writeItemStack(recipe.getExtraItem2(), false);
            buf.writeItemStack(recipe.getExtraItem3(), false);

            buf.writeDouble(recipe.getExtraItem1Chance());
            buf.writeDouble(recipe.getExtraItem2Chance());
            buf.writeDouble(recipe.getExtraItem3Chance());

        }

        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        public ResourceLocation getRegistryName() {
            return ID;
        }

        public Class<RecipeSerializer<?>> getRegistryType() {
            return TreeAbsorberBlocksRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }

}
