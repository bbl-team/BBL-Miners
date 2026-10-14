package com.benbenlaw.multiblocks.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A shaped 3x3 recipe for the Crafting Controller. Every ingredient is sized, so one slot can ask for several items.
 * Each extra ingredient must be found in a different research slot,
 * and the recipe can also require a structure to be built around the controller.
 */
public record ControllerCraftingRecipe(SizedShapedPattern pattern, List<SizedIngredient> extras, List<OptionalItem> optionalItems, ItemStackTemplate result,
                                       Optional<Identifier> structureId, int baseDuration, int energyPerTick) implements Recipe<ControllerCraftingInput> {

    public static final int MAX_EXTRAS = 9;

    public static final MapCodec<ControllerCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedShapedPattern.MAP_CODEC.forGetter(ControllerCraftingRecipe::pattern),
            SizedShapedPattern.SIZED.listOf(0, MAX_EXTRAS).optionalFieldOf("extras", List.of()).forGetter(ControllerCraftingRecipe::extras),
            OptionalItem.CODEC.listOf(0, MAX_EXTRAS).optionalFieldOf("optional_items", List.of()).forGetter(ControllerCraftingRecipe::optionalItems),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(ControllerCraftingRecipe::result),
            Identifier.CODEC.optionalFieldOf("structure_id").forGetter(ControllerCraftingRecipe::structureId),
            Codec.INT.optionalFieldOf("base_duration", 100).forGetter(ControllerCraftingRecipe::baseDuration),
            Codec.INT.optionalFieldOf("energy_per_tick", 20).forGetter(ControllerCraftingRecipe::energyPerTick)
    ).apply(instance, ControllerCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ControllerCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(SizedShapedPattern.CODEC), ControllerCraftingRecipe::pattern,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), ControllerCraftingRecipe::extras,
            OptionalItem.LIST_STREAM_CODEC, ControllerCraftingRecipe::optionalItems,
            ItemStackTemplate.STREAM_CODEC, ControllerCraftingRecipe::result,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), ControllerCraftingRecipe::structureId,
            ByteBufCodecs.VAR_INT, ControllerCraftingRecipe::baseDuration,
            ByteBufCodecs.VAR_INT, ControllerCraftingRecipe::energyPerTick,
            ControllerCraftingRecipe::new
    );

    public static final RecipeType<ControllerCraftingRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<ControllerCraftingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(@NotNull ControllerCraftingInput input, @NotNull Level level) {
        return gridAmounts(input.grid()) != null && extraSlots(input.extras()) != null;
    }

    /** How many items each of the 9 grid slots gives up, or null if the grid doesn't hold this recipe. */
    public int @Nullable [] gridAmounts(List<ItemStack> grid) {
        return pattern.amounts(grid);
    }

    /** The research slot used for each extra ingredient, or null if they can't all be found. */
    public int @Nullable [] extraSlots(List<ItemStack> researchSlots) {
        return ExtraAssignment.assign(extras.size(), researchSlots.size(), (needed, slot) -> {
            ItemStack stack = researchSlots.get(slot);
            return !stack.isEmpty() && extras.get(needed).test(stack);
        });
    }

    /**
     * The research slot holding each optional item (in order), or -1 when it isn't there. Slots already used by a required
     * extra, or by an earlier optional item, are skipped.
     */
    public int[] optionalSlots(List<ItemStack> researchSlots, int[] extraSlots) {
        boolean[] used = new boolean[researchSlots.size()];
        for (int slot : extraSlots) used[slot] = true;

        int[] found = new int[optionalItems.size()];
        for (int i = 0; i < found.length; i++) {
            found[i] = -1;
            for (int slot = 0; slot < used.length; slot++) {
                if (!used[slot] && optionalItems.get(i).test(researchSlots.get(slot))) {
                    used[slot] = true;
                    found[i] = slot;
                    break;
                }
            }
        }
        return found;
    }

    /** The effects of every optional item that is actually present, added together. */
    public EffectTotals totals(int[] optionalSlots) {
        EffectTotals totals = EffectTotals.NONE;
        for (int i = 0; i < optionalSlots.length; i++) {
            if (optionalSlots[i] < 0) continue;
            for (OptionalEffect effect : optionalItems.get(i).effects()) totals = totals.with(effect);
        }
        return totals;
    }

    /** What one craft produces: the result (or the last replacement), with the output count effects applied. */
    public ItemStack resultStack(EffectTotals totals, int[] optionalSlots) {
        ItemStackTemplate template = result;
        for (int i = 0; i < optionalSlots.length; i++) {
            if (optionalSlots[i] < 0) continue;
            for (OptionalEffect effect : optionalItems.get(i).effects()) {
                if (effect instanceof OptionalEffect.ReplaceResult replace) template = replace.item();
            }
        }

        ItemStack stack = template.create();
        stack.setCount(Math.min(stack.getMaxStackSize(), totals.outputCount(template.count())));
        return stack;
    }

    /** The extra items this craft produces, after rolling each bonus output's chance. */
    public List<ItemStack> bonusOutputs(int[] optionalSlots, RandomSource random) {
        List<ItemStack> bonuses = new ArrayList<>();
        for (int i = 0; i < optionalSlots.length; i++) {
            if (optionalSlots[i] < 0) continue;
            for (OptionalEffect effect : optionalItems.get(i).effects()) {
                if (effect instanceof OptionalEffect.BonusOutput bonus && (bonus.chance() >= 1 || random.nextDouble() < bonus.chance())) {
                    bonuses.add(bonus.item().create());
                }
            }
        }
        return bonuses;
    }

    @Override
    public @NonNull ItemStack assemble(ControllerCraftingInput input) {
        return result.create();
    }

    //Boiler Plate
    @Override
    public @NotNull RecipeSerializer<? extends Recipe<ControllerCraftingInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<ControllerCraftingInput>> getType() {
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
