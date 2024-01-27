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

import java.util.HashMap;
import java.util.Map;

public class CrusherRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String pattern;
    private final ItemStack outputItem;
    private final int RFPerTick;
    private final int duration;

    private final ItemStack inputItem;
    private final Map<ItemStack, Integer> chanceOutputs;

    public CrusherRecipe(ResourceLocation id, String pattern, ItemStack inputItem, ItemStack outputItem, Map<ItemStack, Integer> chanceOutputs, int RFPerTick, int duration) {
        this.id = id;
        this.pattern = pattern;
        this.outputItem = outputItem;
        this.RFPerTick = RFPerTick;
        this.duration = duration;
        this.inputItem = inputItem;
        this.chanceOutputs = chanceOutputs;
    }

    public String getPattern() {
        return pattern;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public Map<ItemStack, Integer> getChanceOutputs() {
        return chanceOutputs;
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
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"output"));
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");
            int duration = GsonHelper.getAsInt(json, "duration");

            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"input"));

            JsonObject chancesJson = GsonHelper.getAsJsonObject(json, "chances");
            Map<ItemStack, Integer> chanceOutputs = new HashMap<>();
            for (String key : chancesJson.keySet()) {
                JsonObject chanceObj = chancesJson.getAsJsonObject(key);
                ItemStack chanceItem = ShapedRecipe.itemStackFromJson(chanceObj);
                int chance = GsonHelper.getAsInt(chanceObj, "chance");
                chanceOutputs.put(chanceItem, chance);
            }

            return new CrusherRecipe(id, pattern, input, output, chanceOutputs, RFPerTick, duration);
        }

        @Override
        public CrusherRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            ItemStack inputItem = buf.readItem();
            ItemStack outputItem = buf.readItem();
            int RFPerTick = buf.readInt();
            int duration = buf.readInt();

            int chanceOutputsSize = buf.readInt();
            Map<ItemStack, Integer> chanceOutputs = new HashMap<>();
            for (int i = 0; i < chanceOutputsSize; i++) {
                ItemStack chanceItem = buf.readItem();
                int chance = buf.readInt();
                chanceOutputs.put(chanceItem, chance);
            }

            return new CrusherRecipe(id, pattern, inputItem, outputItem, chanceOutputs, RFPerTick, duration);
        }


        @Override
        public void toNetwork(FriendlyByteBuf buf, CrusherRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);
            buf.writeItemStack(recipe.outputItem, false);
            buf.writeInt(recipe.getRFPerTick());
            buf.writeInt(recipe.getDuration());
            buf.writeItemStack(recipe.inputItem, false);
            buf.writeInt(recipe.getChanceOutputs().size());
            for (Map.Entry<ItemStack, Integer> entry : recipe.getChanceOutputs().entrySet()) {
                buf.writeItemStack(entry.getKey(), false);
                buf.writeInt(entry.getValue());
            }
        }
    }
}
