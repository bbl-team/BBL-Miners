package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MinerBlocksRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String block;
    private final int minerTier;


    public MinerBlocksRecipe(ResourceLocation id, String block, int minerTier){
        this.id = id;
        this.block = block;
        this.minerTier = minerTier;
    }

    public String getBlock() {
        return block;
    }

    public int getMinerTier() {
        return minerTier;
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
        return MinerBlocksRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return MinerBlocksRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<MinerBlocksRecipe> {
        private Type() { }
        public static final MinerBlocksRecipe.Type INSTANCE = new MinerBlocksRecipe.Type();
        public static final String ID = "miner_blocks";
    }

    public static class Serializer implements RecipeSerializer<MinerBlocksRecipe> {
        public static final MinerBlocksRecipe.Serializer INSTANCE = new MinerBlocksRecipe.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID,"miner_blocks");

        @Override
        public MinerBlocksRecipe fromJson(ResourceLocation id, JsonObject json) {
            String block = GsonHelper.getAsString(json, "blockTag");
            int minerTier = GsonHelper.getAsInt(json, "minerTier");

            return new MinerBlocksRecipe(id, block, minerTier);
        }

        @Override
        public MinerBlocksRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String block = buf.readUtf();
            int minerTier = buf.readInt();

            return new MinerBlocksRecipe(id, block, minerTier);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MinerBlocksRecipe recipe) {

            buf.writeUtf(recipe.getBlock(), Short.MAX_VALUE);
            buf.writeInt(recipe.getMinerTier());

        }


        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        public ResourceLocation getRegistryName() {
            return ID;
        }

        public Class<RecipeSerializer<?>> getRegistryType() {
            return MinerBlocksRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }

}
