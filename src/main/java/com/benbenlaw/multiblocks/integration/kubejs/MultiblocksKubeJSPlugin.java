package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.multiblocks.Multiblocks;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import net.minecraft.core.registries.Registries;

public class MultiblocksKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.unit(ExtraItemComponent.EXTRA_ITEM);
        registry.unit(CraftingResultComponent.RESULT);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry event) {
        event.register(Multiblocks.identifier("controller_crafting"), ControllerCraftingRecipeJS.SCHEMA);
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, callback -> {
            callback.add(Multiblocks.identifier("crafting_controller"), CraftingControllerBuilder.class, CraftingControllerBuilder::new);
        });
    }
}
