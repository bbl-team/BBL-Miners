package com.benbenlaw.multiblocks.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.SizedIngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.function.RecipeFunctionInstance;
import dev.latvian.mods.kubejs.util.TinyMap;
import com.benbenlaw.multiblocks.recipe.CraftingResult;
import com.benbenlaw.multiblocks.recipe.ExtraItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public interface ControllerCraftingRecipeJS {

    RecipeKey<CraftingResult> RESULT = CraftingResultComponent.RESULT.outputKey("result");
    RecipeKey<List<String>> PATTERN = StringComponent.STRING.asList().otherKey("pattern").defaultOptional();
    RecipeKey<TinyMap<Character, SizedIngredient>> KEY = SizedIngredientComponent.SIZED_INGREDIENT.asPatternKey().inputKey("key").defaultOptional();
    RecipeKey<List<ExtraItem>> EXTRA_ITEMS = ExtraItemComponent.EXTRA_ITEM.asList().inputKey("extra_items").functionNames("extraItems").defaultOptional();
    RecipeKey<String> CONTROLLER = StringComponent.OPTIONAL_STRING.otherKey("controller").defaultOptional();
    RecipeKey<String> STRUCTURE_ID = StringComponent.OPTIONAL_STRING.otherKey("structure_id").functionNames("structureId").defaultOptional();
    RecipeKey<Integer> BASE_DURATION = NumberComponent.INT.otherKey("base_duration").functionNames("baseDuration").optional(100);
    RecipeKey<Integer> ENERGY_PER_TICK = NumberComponent.INT.otherKey("energy_per_tick").functionNames("energyPerTick").optional(20);

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, PATTERN, KEY, EXTRA_ITEMS, CONTROLLER, STRUCTURE_ID, BASE_DURATION, ENERGY_PER_TICK)
            .uniqueId(RESULT)
            .addToListOpFunction("extra", EXTRA_ITEMS)
            .function(new RecipeFunctionInstance("extraItems", new ExtraItemsFunction()));
}
