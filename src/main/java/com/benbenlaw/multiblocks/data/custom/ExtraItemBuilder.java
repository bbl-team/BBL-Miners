package com.benbenlaw.multiblocks.data.custom;

import com.benbenlaw.multiblocks.recipe.ExtraItem;
import com.benbenlaw.multiblocks.recipe.ItemEffect;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

/** Describes one extra item of a Crafting Controller recipe: how many, whether it is required, whether it is used up, and what it does. */
public class ExtraItemBuilder {

    private final Ingredient ingredient;
    private boolean required;
    private int count = 1;
    private boolean consume = true;
    private double consumeChance = 1.0;
    private final List<ItemEffect> effects = new ArrayList<>();

    ExtraItemBuilder(Ingredient ingredient, boolean required) {
        this.ingredient = ingredient;
        this.required = required;
    }

    /** How many must be in the slot (and how many are used up if consumed). */
    public ExtraItemBuilder count(int count) {
        this.count = count;
        return this;
    }

    /** The recipe won't run without it. */
    public ExtraItemBuilder required() {
        this.required = true;
        return this;
    }

    /** It only takes part if it happens to be in a slot. */
    public ExtraItemBuilder optional() {
        this.required = false;
        return this;
    }

    /** The item stays in the slot and keeps working. */
    public ExtraItemBuilder keep() {
        this.consume = false;
        return this;
    }

    /** Used up by every craft (the default). */
    public ExtraItemBuilder consume() {
        this.consume = true;
        return this;
    }

    /** Used up by a craft only some of the time: 0.25 means one craft in four. */
    public ExtraItemBuilder consumeChance(double chance) {
        this.consume = true;
        this.consumeChance = chance;
        return this;
    }

    /** 0.5 makes the craft twice as fast, 2 makes it twice as slow. */
    public ExtraItemBuilder duration(double multiplier) {
        effects.add(new ItemEffect.Duration(multiplier));
        return this;
    }

    /** Multiplies the energy used per tick. */
    public ExtraItemBuilder energy(double multiplier) {
        effects.add(new ItemEffect.Energy(multiplier));
        return this;
    }

    /** Adds to the number of results each craft makes. */
    public ExtraItemBuilder extraOutput(int add) {
        effects.add(new ItemEffect.OutputCount(add, 1.0));
        return this;
    }

    /** Multiplies the number of results each craft makes. */
    public ExtraItemBuilder outputMultiplier(double multiplier) {
        effects.add(new ItemEffect.OutputCount(0, multiplier));
        return this;
    }

    /** An additional item produced alongside the result, with a chance from 0 to 1. */
    public ExtraItemBuilder bonusOutput(ItemLike item, int count, double chance) {
        effects.add(new ItemEffect.BonusOutput(new ItemStackTemplate(item.asItem(), count), chance));
        return this;
    }

    /** The craft produces this item instead of the recipe's result. */
    public ExtraItemBuilder replaceResult(ItemLike item, int count) {
        effects.add(new ItemEffect.ReplaceResult(new ItemStackTemplate(item.asItem(), count)));
        return this;
    }

    ExtraItem build() {
        return new ExtraItem(ingredient, count, required, consume, consumeChance, List.copyOf(effects));
    }
}
