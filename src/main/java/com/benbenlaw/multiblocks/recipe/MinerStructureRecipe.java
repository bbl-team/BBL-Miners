package com.benbenlaw.multiblocks.recipe;

import com.benbenlaw.core.recipe.NoInventoryRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record MinerStructureRecipe(Identifier structureId, List<Identifier> lootTables,
                                   int baseDuration, int baseRolls, int energyPerTick) implements Recipe<NoInventoryRecipe> {

    public static final MapCodec<MinerStructureRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("structure_id").forGetter(MinerStructureRecipe::structureId),
            Identifier.CODEC.listOf().fieldOf("loot_tables").forGetter(MinerStructureRecipe::lootTables),
            Codec.INT.optionalFieldOf("base_duration", 100).forGetter(MinerStructureRecipe::baseDuration),
            Codec.INT.optionalFieldOf("base_rolls", 1).forGetter(MinerStructureRecipe::baseRolls),
            Codec.INT.optionalFieldOf("energy_per_tick", 20).forGetter(MinerStructureRecipe::energyPerTick)
    ).apply(instance, MinerStructureRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MinerStructureRecipe> STREAM_CODEC =
            ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public static final RecipeType<MinerStructureRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<MinerStructureRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(@NotNull NoInventoryRecipe input, @NotNull Level level) {
        return true;
    }

    //Boiler Plate
    @Override
    public @NonNull ItemStack assemble(NoInventoryRecipe recipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<NoInventoryRecipe>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<NoInventoryRecipe>> getType() {
        return TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }
}
