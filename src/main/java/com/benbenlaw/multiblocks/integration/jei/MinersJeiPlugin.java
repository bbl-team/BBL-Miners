package com.benbenlaw.multiblocks.integration.jei;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock;
import com.benbenlaw.multiblocks.client.ClientCraftingPreviewCache;
import com.benbenlaw.multiblocks.client.ClientPreviewCache;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class MinersJeiPlugin implements IModPlugin {

    private MinerStructureCategory category;
    private final Map<Identifier, CraftingControllerCategory> craftingCategories = new LinkedHashMap<>();
    private IRecipeManager recipeManager;
    private List<MinerStructureCategory.Entry> registeredEntries = List.of();
    private final Map<Identifier, List<CraftingControllerCategory.Entry>> registeredCraftingEntries = new HashMap<>();

    @Override
    public Identifier getPluginUid() {
        return Multiblocks.identifier("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        category = new MinerStructureCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(category);

        // one category per crafting controller block, so a custom controller gets its own tab
        craftingCategories.clear();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (!(block instanceof CraftingControllerBlock controllerBlock)) continue;

            Identifier id = BuiltInRegistries.BLOCK.getKey(block);
            CraftingControllerCategory craftingCategory = new CraftingControllerCategory(registration.getJeiHelpers().getGuiHelper(), id, controllerBlock);
            craftingCategories.put(id, craftingCategory);
            registration.addRecipeCategories(craftingCategory);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registeredEntries = buildEntries();
        registration.addRecipes(MinerStructureCategory.TYPE, registeredEntries);

        craftingCategories.forEach((id, craftingCategory) -> {
            List<CraftingControllerCategory.Entry> entries = buildCraftingEntries(id);
            registeredCraftingEntries.put(id, entries);
            registration.addRecipes(CraftingControllerCategory.typeFor(id), entries);
        });
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MinerStructureCategory.TYPE, MultiblocksBlocks.MINER_CONTROLLER.get());
        craftingCategories.forEach((id, craftingCategory) ->
                registration.addCraftingStation(CraftingControllerCategory.typeFor(id), craftingCategory.block()));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        recipeManager = jeiRuntime.getRecipeManager();
        // Previews can arrive after JEI has started (and again on /reload), so swap the entries in when they do
        ClientPreviewCache.setListener(this::refreshEntries);
        ClientCraftingPreviewCache.setListener(this::refreshCraftingEntries);
    }

    @Override
    public void onRuntimeUnavailable() {
        recipeManager = null;
        ClientPreviewCache.setListener(() -> {});
        ClientCraftingPreviewCache.setListener(() -> {});
        if (category != null) category.closeRenderers();
        craftingCategories.values().forEach(CraftingControllerCategory::closeRenderers);
    }

    private void refreshEntries() {
        if (recipeManager == null) return;

        List<MinerStructureCategory.Entry> newEntries = buildEntries();
        recipeManager.hideRecipes(MinerStructureCategory.TYPE, registeredEntries);
        recipeManager.addRecipes(MinerStructureCategory.TYPE, newEntries);
        registeredEntries = newEntries;
    }

    private void refreshCraftingEntries() {
        if (recipeManager == null) return;

        craftingCategories.keySet().forEach(id -> {
            List<CraftingControllerCategory.Entry> newEntries = buildCraftingEntries(id);
            recipeManager.hideRecipes(CraftingControllerCategory.typeFor(id), registeredCraftingEntries.getOrDefault(id, List.of()));
            recipeManager.addRecipes(CraftingControllerCategory.typeFor(id), newEntries);
            registeredCraftingEntries.put(id, newEntries);
        });
    }

    private static List<CraftingControllerCategory.Entry> buildCraftingEntries(Identifier controller) {
        return ClientCraftingPreviewCache.all().stream()
                .filter(preview -> preview.recipe().controller().equals(controller))
                .map(CraftingControllerCategory.Entry::new).toList();
    }

    private static List<MinerStructureCategory.Entry> buildEntries() {
        return ClientPreviewCache.all().stream().map(MinerStructureCategory.Entry::new).toList();
    }
}
