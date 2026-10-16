package com.benbenlaw.multiblocks.recipe;

import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.multiblocks.Multiblocks;
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


public record ControllerCraftingRecipe(SizedShapedPattern pattern, List<ExtraItem> extraItems, CraftingResult result,
                                       Optional<Identifier> structureId, int baseDuration, int energyPerTick,
                                       Identifier controller) implements Recipe<ControllerCraftingInput> {

    public static final int MAX_EXTRAS = 9;

    /** The controller a recipe is for when it doesn't name one: the block id of the built-in Crafting Controller. */
    public static final Identifier DEFAULT_CONTROLLER = Multiblocks.identifier("crafting_controller");

    public static final MapCodec<ControllerCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedShapedPattern.MAP_CODEC.forGetter(ControllerCraftingRecipe::pattern),
            ExtraItem.CODEC.listOf(0, MAX_EXTRAS).optionalFieldOf("extra_items", List.of()).forGetter(ControllerCraftingRecipe::extraItems),
            CraftingResult.CODEC.fieldOf("result").forGetter(ControllerCraftingRecipe::result),
            Identifier.CODEC.optionalFieldOf("structure_id").forGetter(ControllerCraftingRecipe::structureId),
            Codec.INT.optionalFieldOf("base_duration", 100).forGetter(ControllerCraftingRecipe::baseDuration),
            Codec.INT.optionalFieldOf("energy_per_tick", 20).forGetter(ControllerCraftingRecipe::energyPerTick),
            Identifier.CODEC.optionalFieldOf("controller", DEFAULT_CONTROLLER).forGetter(ControllerCraftingRecipe::controller)
    ).apply(instance, ControllerCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ControllerCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(SizedShapedPattern.CODEC), ControllerCraftingRecipe::pattern,
            ExtraItem.LIST_STREAM_CODEC, ControllerCraftingRecipe::extraItems,
            CraftingResult.STREAM_CODEC, ControllerCraftingRecipe::result,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), ControllerCraftingRecipe::structureId,
            ByteBufCodecs.VAR_INT, ControllerCraftingRecipe::baseDuration,
            ByteBufCodecs.VAR_INT, ControllerCraftingRecipe::energyPerTick,
            Identifier.STREAM_CODEC, ControllerCraftingRecipe::controller,
            ControllerCraftingRecipe::new
    );

    public static final RecipeType<ControllerCraftingRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<ControllerCraftingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(@NotNull ControllerCraftingInput input, @NotNull Level level) {
        return gridAmounts(input.grid()) != null && assignExtras(input.extras()) != null;
    }

    public int @Nullable [] gridAmounts(List<ItemStack> grid) {
        return pattern.amounts(grid);
    }

    public int @Nullable [] assignExtras(List<ItemStack> extraSlots) {
        List<Integer> required = new ArrayList<>();
        for (int i = 0; i < extraItems.size(); i++) {
            if (extraItems.get(i).required()) required.add(i);
        }

        int[] requiredSlots = ExtraAssignment.assign(required.size(), extraSlots.size(), (needed, slot) -> {
            ItemStack stack = extraSlots.get(slot);
            return !stack.isEmpty() && extraItems.get(required.get(needed)).test(stack);
        });
        if (requiredSlots == null) return null;

        int[] slots = new int[extraItems.size()];
        java.util.Arrays.fill(slots, -1);
        boolean[] used = new boolean[extraSlots.size()];
        for (int i = 0; i < requiredSlots.length; i++) {
            slots[required.get(i)] = requiredSlots[i];
            used[requiredSlots[i]] = true;
        }

        for (int i = 0; i < extraItems.size(); i++) {
            if (extraItems.get(i).required()) continue;

            for (int slot = 0; slot < used.length; slot++) {
                if (!used[slot] && extraItems.get(i).test(extraSlots.get(slot))) {
                    used[slot] = true;
                    slots[i] = slot;
                    break;
                }
            }
        }
        return slots;
    }

    public EffectTotals totals(int[] slots) {
        EffectTotals totals = EffectTotals.NONE;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] < 0) continue;
            for (ItemEffect effect : extraItems.get(i).effects()) totals = totals.with(effect);
        }
        return totals;
    }

    /** The results after extra items have replaced them: a replace_result effect swaps the whole list for that one item. */
    private List<ChanceResult> effectiveResults(int[] slots) {
        List<ChanceResult> results = result instanceof CraftingResult.Items items ? items.results() : List.of();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] < 0) continue;
            for (ItemEffect effect : extraItems.get(i).effects()) {
                if (effect instanceof ItemEffect.ReplaceResult replace) results = List.of(new ChanceResult(replace.item(), 1.0f));
            }
        }
        return results;
    }

    private static ItemStack scaled(ItemStack stack, EffectTotals totals) {
        if (stack.isEmpty()) return stack;
        stack.setCount(Math.min(stack.getMaxStackSize(), totals.outputCount(stack.getCount())));
        return stack;
    }

    /** The most this craft can produce (every chance succeeding), used to check the output slots have room. Empty for a loot table result. */
    public List<ItemStack> potentialResults(EffectTotals totals, int[] slots) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ChanceResult chance : effectiveResults(slots)) stacks.add(scaled(chance.template().create(), totals));
        return stacks;
    }

    /** What this craft actually produces, with each result's chance rolled. Empty for a loot table result. */
    public List<ItemStack> rollResults(EffectTotals totals, int[] slots, RandomSource random) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ChanceResult chance : effectiveResults(slots)) {
            ItemStack rolled = scaled(chance.rollOutput(random), totals);
            if (!rolled.isEmpty()) stacks.add(rolled);
        }
        return stacks;
    }

    public List<ItemStack> bonusOutputs(int[] slots, RandomSource random) {
        List<ItemStack> bonuses = new ArrayList<>();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] < 0) continue;
            for (ItemEffect effect : extraItems.get(i).effects()) {
                if (effect instanceof ItemEffect.BonusOutput bonus && (bonus.chance() >= 1 || random.nextDouble() < bonus.chance())) {
                    bonuses.add(bonus.item().create());
                }
            }
        }
        return bonuses;
    }

    @Override
    public @NonNull ItemStack assemble(ControllerCraftingInput input) {
        return result instanceof CraftingResult.Items items && !items.results().isEmpty()
                ? items.results().getFirst().template().create() : ItemStack.EMPTY;
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
