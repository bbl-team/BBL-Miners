package com.benbenlaw.multiblocks.data.custom;

import com.benbenlaw.multiblocks.recipe.OptionalEffect;
import com.benbenlaw.multiblocks.recipe.OptionalItem;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

/** Describes one optional item of a Crafting Controller recipe: how many are needed, whether they are used up, and what they do. */
public class OptionalItemBuilder {

    private final Ingredient ingredient;
    private int count = 1;
    private boolean consume = true;
    private double consumeChance = 1.0;
    private final List<OptionalEffect> effects = new ArrayList<>();

    OptionalItemBuilder(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /** How many must be in the slot for the effects to apply (and how many are used up if consumed). */
    public OptionalItemBuilder count(int count) {
        this.count = count;
        return this;
    }

    /** The item stays in the slot and keeps working. */
    public OptionalItemBuilder keep() {
        this.consume = false;
        return this;
    }

    /** Used up by every craft (the default). */
    public OptionalItemBuilder consume() {
        this.consume = true;
        return this;
    }

    /** Used up by a craft only some of the time: 0.25 means one craft in four. */
    public OptionalItemBuilder consumeChance(double chance) {
        this.consume = true;
        this.consumeChance = chance;
        return this;
    }

    /** 0.5 makes the craft twice as fast, 2 makes it twice as slow. */
    public OptionalItemBuilder duration(double multiplier) {
        effects.add(new OptionalEffect.Duration(multiplier));
        return this;
    }

    /** Multiplies the energy used per tick. */
    public OptionalItemBuilder energy(double multiplier) {
        effects.add(new OptionalEffect.Energy(multiplier));
        return this;
    }

    /** Adds to the number of results each craft makes. */
    public OptionalItemBuilder extraOutput(int add) {
        effects.add(new OptionalEffect.OutputCount(add, 1.0));
        return this;
    }

    /** Multiplies the number of results each craft makes. */
    public OptionalItemBuilder outputMultiplier(double multiplier) {
        effects.add(new OptionalEffect.OutputCount(0, multiplier));
        return this;
    }

    /** An additional item produced alongside the result, with a chance from 0 to 1. */
    public OptionalItemBuilder bonusOutput(ItemLike item, int count, double chance) {
        effects.add(new OptionalEffect.BonusOutput(new ItemStackTemplate(item.asItem(), count), chance));
        return this;
    }

    /** The craft produces this item instead of the recipe's result. */
    public OptionalItemBuilder replaceResult(ItemLike item, int count) {
        effects.add(new OptionalEffect.ReplaceResult(new ItemStackTemplate(item.asItem(), count)));
        return this;
    }

    OptionalItem build() {
        return new OptionalItem(ingredient, count, consume, consumeChance, List.copyOf(effects));
    }
}
