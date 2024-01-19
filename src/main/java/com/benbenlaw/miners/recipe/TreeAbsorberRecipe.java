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

public class TreeAbsorberRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String pattern;
    private final String logLoottable;
    private final String leafLoottable;
    private final int RFPerTick;
    private final int duration;

    public TreeAbsorberRecipe(ResourceLocation id, String pattern, String logLoottable, String leafLoottable, int RFPerTick, int duration) {
        this.id = id;
        this.pattern = pattern;
        this.logLoottable = logLoottable;
        this.leafLoottable = leafLoottable;
        this.RFPerTick = RFPerTick;
        this.duration = duration;
    }

    public String getPattern() {
        return pattern;
    }

    public String getLogLoottable() {
        return logLoottable;
    }
    public String getLeafLoottable() {
        return leafLoottable;
    }

    public int getRFPerTick() {
        return RFPerTick;
    }
    public int getDuration() {
        return duration;
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
    public static class Type implements RecipeType<TreeAbsorberRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "tree_absorber";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<TreeAbsorberRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID, "tree_absorber");

        @Override
        public TreeAbsorberRecipe fromJson(ResourceLocation id, JsonObject json) {

            String pattern = GsonHelper.getAsString(json, "pattern");
            String logLoottable = GsonHelper.getAsString(json, "log_loottable");
            String leafLoottable = GsonHelper.getAsString(json, "leaf_loottable");
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");
            int duration = GsonHelper.getAsInt(json, "duration");

            return new TreeAbsorberRecipe(id, pattern, logLoottable, leafLoottable, RFPerTick, duration);
        }

        @Override
        public TreeAbsorberRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            String logLoottable = buf.readUtf();
            String leafLoottable = buf.readUtf();
            int RFPerTick = buf.readInt();
            int duration = buf.readInt();

            return new TreeAbsorberRecipe(id, pattern, logLoottable, leafLoottable, RFPerTick, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TreeAbsorberRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);
            buf.writeUtf(recipe.getLogLoottable(), Short.MAX_VALUE);
            buf.writeUtf(recipe.getLeafLoottable(), Short.MAX_VALUE);
            buf.writeInt(recipe.getRFPerTick());
            buf.writeInt(recipe.getDuration());
        }
    }
}
