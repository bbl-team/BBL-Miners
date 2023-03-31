package com.benbenlaw.miners.recipe;

import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class NoInventoryRecipe extends RecipeWrapper {
    public static final NoInventoryRecipe INSTANCE = new NoInventoryRecipe();

    private NoInventoryRecipe() {
        super(new ItemStackHandler(0));
    }
}

