package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.multiblocks.recipe.ExtraItem;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.function.ResolvedRecipeSchemaFunction;

import java.util.ArrayList;
import java.util.List;

/** extraItems(item, consumed): adds an extra item, and consumed = false means the recipe keeps it (a blueprint, a tool). */
public class ExtraItemsFunction implements ResolvedRecipeSchemaFunction {

    @Override
    public List<RecipeComponent<?>> arguments() {
        return List.of(ExtraItemComponent.EXTRA_ITEM, BooleanComponent.BOOLEAN);
    }

    @Override
    public void execute(RecipeScriptContext cx, List<Object> args) {
        KubeRecipe recipe = cx.recipe();

        ExtraItem item = ExtraItemComponent.EXTRA_ITEM.wrap(cx, args.get(0));
        boolean consumed = !(args.size() > 1 && Boolean.FALSE.equals(args.get(1)));
        if (!consumed) {
            item = new ExtraItem(item.ingredient(), item.count(), item.required(), false, item.consumeChance(), item.effects());
        }

        List<ExtraItem> extras = new ArrayList<>();
        List<ExtraItem> existing = recipe.getValue(ControllerCraftingRecipeJS.EXTRA_ITEMS);
        if (existing != null) extras.addAll(existing);
        extras.add(item);

        recipe.setValue(ControllerCraftingRecipeJS.EXTRA_ITEMS, extras);
    }
}
