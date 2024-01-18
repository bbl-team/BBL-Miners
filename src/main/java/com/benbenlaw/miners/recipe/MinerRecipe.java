package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MinerRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String pattern;
    private final ItemStack outputItem;
    private final int RFPerTick;

    public MinerRecipe(ResourceLocation id, String pattern, ItemStack outputItem, int RFPerTick) {
        this.id = id;
        this.pattern = pattern;
        this.outputItem = outputItem;
        this.RFPerTick = RFPerTick;
    }

    public String getPattern() {
        return pattern;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getRFPerTick() {
        return RFPerTick;
    }

    @Override
    public boolean matches(@NotNull NoInventoryRecipe inv, @NotNull Level pLevel) {
        return true;
    }

    @Override
    public ItemStack assemble(NoInventoryRecipe p_44001_, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<MinerRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "miners";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<MinerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID, "miners");

        @Override
        public MinerRecipe fromJson(ResourceLocation id, JsonObject json) {

            String pattern = GsonHelper.getAsString(json, "pattern");
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"output"));
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");

            return new MinerRecipe(id, pattern, output, RFPerTick);
        }

        @Override
        public MinerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            ItemStack output = buf.readItem();
            int RFPerTick = buf.readInt();

            return new MinerRecipe(id, pattern, output, RFPerTick);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MinerRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);
            buf.writeItemStack(recipe.outputItem, false);
            buf.writeInt(recipe.getRFPerTick());
        }
    }
}
