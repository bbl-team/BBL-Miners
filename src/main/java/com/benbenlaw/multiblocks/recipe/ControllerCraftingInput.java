package com.benbenlaw.multiblocks.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

/** The 3x3 crafting grid followed by the research slots. */
public record ControllerCraftingInput(List<ItemStack> grid, List<ItemStack> extras) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return index < grid.size() ? grid.get(index) : extras.get(index - grid.size());
    }

    @Override
    public int size() {
        return grid.size() + extras.size();
    }
}
