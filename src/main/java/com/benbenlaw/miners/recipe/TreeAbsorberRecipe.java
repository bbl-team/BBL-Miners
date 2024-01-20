package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TreeAbsorberRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String pattern;
    private final ItemStack log;
    private final ItemStack leaf;
    private final ItemStack sapling;
    private final ItemStack extraItem;
    private final double extraItemChance;
    private final int RFPerTick;
    private final int duration;

    public TreeAbsorberRecipe(ResourceLocation id, String pattern, ItemStack log, ItemStack leaf, ItemStack sapling, ItemStack extraItem, double extraItemChance, int RFPerTick, int duration) {
        this.id = id;
        this.pattern = pattern;
        this.log = log;
        this.leaf = leaf;
        this.sapling = sapling;
        this.extraItem = extraItem;
        this.extraItemChance = extraItemChance;
        this.RFPerTick = RFPerTick;
        this.duration = duration;
    }

    public String getPattern() {
        return pattern;
    }

    public ItemStack getLog() {
        return log.copy();
    }
    public ItemStack getLeaf() {
        return leaf.copy();
    }
    public ItemStack getSapling() {
        return sapling.copy();
    }

    public ItemStack getExtraItem() {
        return extraItem.copy();
    }

    public double getExtraItemChance() {
        return extraItemChance;
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
            ItemStack log =  ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "log"));
            ItemStack leaf = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "leaf"));
            ItemStack sapling = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "sapling"));

            ItemStack extra_item;

            if (json.has("extra_item")) {
                    extra_item = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "extra_item"));
            }
            else {
                extra_item = ItemStack.EMPTY;
            }

            double extra_item_chance = GsonHelper.getAsDouble(json, "extra_item_chance", 0.0);
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");
            int duration = GsonHelper.getAsInt(json, "duration");

            return new TreeAbsorberRecipe(id, pattern, log, leaf, sapling, extra_item, extra_item_chance, RFPerTick, duration);
        }

        @Override
        public TreeAbsorberRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            ItemStack log = buf.readItem();
            ItemStack leaf = buf.readItem();
            ItemStack sapling = buf.readItem();
            ItemStack extra_item = buf.readItem();
            double extra_item_chance = buf.readDouble();
            int RFPerTick = buf.readInt();
            int duration = buf.readInt();

            return new TreeAbsorberRecipe(id, pattern, log, leaf, sapling, extra_item, extra_item_chance, RFPerTick, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TreeAbsorberRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);
            buf.writeItemStack(recipe.getLog(), false);
            buf.writeItemStack(recipe.getLeaf(), false);
            buf.writeItemStack(recipe.getSapling(), false);
            buf.writeItemStack(recipe.getExtraItem(), false);
            buf.writeDouble(recipe.getExtraItemChance());
            buf.writeInt(recipe.getRFPerTick());
            buf.writeInt(recipe.getDuration());
        }
    }
}
