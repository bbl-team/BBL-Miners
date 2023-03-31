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

public final class CapBlocksRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String block;
    private final int tickRate;

    public CapBlocksRecipe(ResourceLocation id, String block, int tickRate) {
        this.id = id;
        this.block = block;
        this.tickRate = tickRate;
    }

    public String getBlock() {
        return block;
    }

    public int getTickRate() {
        return tickRate;
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
        return CapBlocksRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return CapBlocksRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<CapBlocksRecipe> {
        private Type() { }
        public static final CapBlocksRecipe.Type INSTANCE = new CapBlocksRecipe.Type();
        public static final String ID = "cap_blocks";
    }

    public static class Serializer implements RecipeSerializer<CapBlocksRecipe> {
        public static final CapBlocksRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID,"cap_blocks");

        @Override
        public CapBlocksRecipe fromJson(ResourceLocation id, JsonObject json) {
            String block = GsonHelper.getAsString(json, "block");
            int tickRate = GsonHelper.getAsInt(json, "tickRate");

            return new CapBlocksRecipe(id, block, tickRate);
        }

        @Override
        public CapBlocksRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String block = buf.readUtf();
            int tickRate = buf.readInt();

            return new CapBlocksRecipe(id, block, tickRate);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CapBlocksRecipe recipe) {

            buf.writeUtf(recipe.getBlock(), Short.MAX_VALUE);
            buf.writeInt(recipe.getTickRate());

        }


        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        public ResourceLocation getRegistryName() {
            return ID;
        }

        public Class<RecipeSerializer<?>> getRegistryType() {
            return CapBlocksRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }

}
