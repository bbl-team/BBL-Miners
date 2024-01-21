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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidAbsorberRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String pattern;
    private final String outputFluid;
    private final int outputAmount;
    private final int RFPerTick;
    private final int duration;

    public FluidAbsorberRecipe(ResourceLocation id, String pattern, String outputFluid, int outputAmount, int RFPerTick, int duration) {
        this.id = id;
        this.pattern = pattern;
        this.outputFluid = outputFluid;
        this.outputAmount = outputAmount;
        this.RFPerTick = RFPerTick;
        this.duration = duration;
    }

    public String getPattern() {
        return pattern;
    }

    public String getOutputFluid() {
        return outputFluid;
    }

    public int getOutputAmount() { return outputAmount; }

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
    public static class Type implements RecipeType<FluidAbsorberRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "fluid_absorber";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<FluidAbsorberRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID, "fluid_absorber");

        @Override
        public FluidAbsorberRecipe fromJson(ResourceLocation id, JsonObject json) {

            String pattern = GsonHelper.getAsString(json, "pattern");
            String output = GsonHelper.getAsString(json,"output");
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");
            int duration = GsonHelper.getAsInt(json, "duration");
            int outputAmount = GsonHelper.getAsInt(json, "output_amount");

            return new FluidAbsorberRecipe(id, pattern, output, outputAmount, RFPerTick, duration);
        }

        @Override
        public FluidAbsorberRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            String output = buf.readUtf();
            int RFPerTick = buf.readInt();
            int duration = buf.readInt();
            int outputAmount = buf.readInt();

            return new FluidAbsorberRecipe(id, pattern, output, outputAmount, RFPerTick, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluidAbsorberRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);
            buf.writeUtf(recipe.outputFluid);
            buf.writeInt(recipe.getOutputAmount());
            buf.writeInt(recipe.getRFPerTick());
            buf.writeInt(recipe.getDuration());
        }
    }
}
