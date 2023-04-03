package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.Miners;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class FluidAbsorberRecipe implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String fluid;


    public FluidAbsorberRecipe(ResourceLocation id, String fluid) {
        this.id = id;
        this.fluid = fluid;
    }

    public String getFluid() {
        return fluid;
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
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FluidAbsorberRecipe.Serializer.INSTANCE;
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

    public static class Serializer implements RecipeSerializer<FluidAbsorberRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Miners.MOD_ID,"fluid_absorber");

        @Override
        public FluidAbsorberRecipe fromJson(ResourceLocation id, JsonObject json) {

            String fluid = GsonHelper.getAsString(json, "fluid");

            return new FluidAbsorberRecipe(id, fluid);
        }

        @Override
        public FluidAbsorberRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

            String fluid = buf.readUtf();

            return new FluidAbsorberRecipe(id, fluid);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluidAbsorberRecipe recipe) {

            buf.writeUtf(recipe.getFluid());

        }


        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        public ResourceLocation getRegistryName() {
            return ID;
        }

        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }




}