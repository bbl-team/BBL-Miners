package com.benbenlaw.multiblocks.data.custom;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import com.benbenlaw.multiblocks.recipe.SizedShapedPattern;
import com.mojang.serialization.JsonOps;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import java.util.Collections;
import java.util.function.Consumer;
import com.benbenlaw.multiblocks.recipe.OptionalItem;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ControllerCraftingRecipeBuilder implements RecipeBuilder {

    private final ItemLike result;
    private final int count;
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, SizedIngredient> key = new LinkedHashMap<>();
    private final List<SizedIngredient> extras = new ArrayList<>();
    private final List<OptionalItem> optionalItems = new ArrayList<>();
    private @Nullable Identifier structureId;
    private int duration = 100;
    private int energyPerTick = 20;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private ControllerCraftingRecipeBuilder(ItemLike result, int count) {
        this.result = result;
        this.count = count;
    }

    public static ControllerCraftingRecipeBuilder controllerCrafting(ItemLike result) {
        return new ControllerCraftingRecipeBuilder(result, 1);
    }

    public static ControllerCraftingRecipeBuilder controllerCrafting(ItemLike result, int count) {
        return new ControllerCraftingRecipeBuilder(result, count);
    }

    public ControllerCraftingRecipeBuilder define(char symbol, ItemLike item) {
        return define(symbol, Ingredient.of(item), 1);
    }

    /** A grid slot that needs several of an item, for example 10 furnaces. */
    public ControllerCraftingRecipeBuilder define(char symbol, ItemLike item, int count) {
        return define(symbol, Ingredient.of(item), count);
    }

    public ControllerCraftingRecipeBuilder define(char symbol, Ingredient ingredient, int count) {
        key.put(symbol, new SizedIngredient(ingredient, count));
        return this;
    }

    public ControllerCraftingRecipeBuilder pattern(String row) {
        rows.add(row);
        return this;
    }

    /** An item that must be present in a research slot. It is used up by the craft. */
    public ControllerCraftingRecipeBuilder extra(ItemLike item) {
        return extra(Ingredient.of(item), 1);
    }

    public ControllerCraftingRecipeBuilder extra(ItemLike item, int count) {
        return extra(Ingredient.of(item), count);
    }

    public ControllerCraftingRecipeBuilder extra(Ingredient ingredient, int count) {
        extras.add(new SizedIngredient(ingredient, count));
        return this;
    }

    /** An item that is never required. While it sits in a research slot its effects apply to the craft. */
    public ControllerCraftingRecipeBuilder optional(ItemLike item, Consumer<OptionalItemBuilder> configure) {
        return optional(Ingredient.of(item), configure);
    }

    public ControllerCraftingRecipeBuilder optional(Ingredient ingredient, Consumer<OptionalItemBuilder> configure) {
        OptionalItemBuilder optional = new OptionalItemBuilder(ingredient);
        configure.accept(optional);
        optionalItems.add(optional.build());
        return this;
    }

    public ControllerCraftingRecipeBuilder structure(Identifier structureId) {
        this.structureId = structureId;
        return this;
    }

    public ControllerCraftingRecipeBuilder baseDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ControllerCraftingRecipeBuilder energyPerTick(int energyPerTick) {
        this.energyPerTick = energyPerTick;
        return this;
    }

    private SizedShapedPattern pattern() {
        SizedShapedPattern pattern = new SizedShapedPattern(List.copyOf(rows), Collections.unmodifiableMap(new LinkedHashMap<>(key)));
        SizedShapedPattern.CODEC.encodeStart(JsonOps.INSTANCE, pattern).getOrThrow();
        return pattern;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return ResourceKey.create(Registries.RECIPE, Multiblocks.identifier("controller_crafting/" + BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()));
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Multiblocks.identifier(id)));
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);

        ControllerCraftingRecipe recipe = new ControllerCraftingRecipe(pattern(), List.copyOf(extras), List.copyOf(optionalItems),
                new ItemStackTemplate(result.asItem(), count), Optional.ofNullable(structureId), duration, energyPerTick);

        recipeOutput.accept(resourceKey, recipe, builder.build(resourceKey.identifier().withPrefix("recipes/controller_crafting/")));
    }
}
