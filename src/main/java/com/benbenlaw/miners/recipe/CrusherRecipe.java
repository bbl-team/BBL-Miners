package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CrusherRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String pattern;
    private final NonNullList<Ingredient> input;
    private final ItemStack outputItem;
    private final int RFPerTick;
    private final int duration;

    public CrusherRecipe(ResourceLocation id, String pattern, NonNullList<Ingredient> input, ItemStack outputItem, int RFPerTick, int duration) {
        this.id = id;
        this.pattern = pattern;
        this.input = input;
        this.outputItem = outputItem;
        this.RFPerTick = RFPerTick;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return input;
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
    public static class Type implements RecipeType<CrusherRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "crusher";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<CrusherRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID, "crusher");

        @Override
        public CrusherRecipe fromJson(ResourceLocation id, JsonObject json) {

            String pattern = GsonHelper.getAsString(json, "pattern");

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "input");
            NonNullList<Ingredient> input = NonNullList.withSize(1, Ingredient.EMPTY);

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"output"));
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");
            int duration = GsonHelper.getAsInt(json, "duration");


            for (int i = 0; i < input.size(); i++) {
                input.set(i, Ingredient.fromJson(ingredients.get(i)));
            }


            return new CrusherRecipe(id, pattern, input, output, RFPerTick, duration);
        }

        @Override
        public CrusherRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            NonNullList<Ingredient> input = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < input.size(); i++) {
                input.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            int RFPerTick = buf.readInt();
            int duration = buf.readInt();

            return new CrusherRecipe(id, pattern, input, output, RFPerTick, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CrusherRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);

            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }

            buf.writeItemStack(recipe.outputItem, false);
            buf.writeInt(recipe.getRFPerTick());
            buf.writeInt(recipe.getDuration());
        }
    }
}
