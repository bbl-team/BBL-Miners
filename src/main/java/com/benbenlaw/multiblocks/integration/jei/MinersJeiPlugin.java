package com.benbenlaw.multiblocks.integration.jei;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.client.ClientCraftingPreviewCache;
import com.benbenlaw.multiblocks.client.ClientPreviewCache;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;

import java.util.List;

@JeiPlugin
public class MinersJeiPlugin implements IModPlugin {

    private MinerStructureCategory category;
    private CraftingControllerCategory craftingCategory;
    private IRecipeManager recipeManager;
    private List<MinerStructureCategory.Entry> registeredEntries = List.of();
    private List<CraftingControllerCategory.Entry> registeredCraftingEntries = List.of();

    @Override
    public Identifier getPluginUid() {
        return Multiblocks.identifier("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        category = new MinerStructureCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(category);

        craftingCategory = new CraftingControllerCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(craftingCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registeredEntries = buildEntries();
        registration.addRecipes(MinerStructureCategory.TYPE, registeredEntries);

        registeredCraftingEntries = buildCraftingEntries();
        registration.addRecipes(CraftingControllerCategory.TYPE, registeredCraftingEntries);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MinerStructureCategory.TYPE, MultiblocksBlocks.MINER_CONTROLLER.get());
        registration.addCraftingStation(CraftingControllerCategory.TYPE, MultiblocksBlocks.CRAFTING_CONTROLLER.get());
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
        if (craftingCategory != null) craftingCategory.closeRenderers();
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

        List<CraftingControllerCategory.Entry> newEntries = buildCraftingEntries();
        recipeManager.hideRecipes(CraftingControllerCategory.TYPE, registeredCraftingEntries);
        recipeManager.addRecipes(CraftingControllerCategory.TYPE, newEntries);
        registeredCraftingEntries = newEntries;
    }

    private static List<CraftingControllerCategory.Entry> buildCraftingEntries() {
        return ClientCraftingPreviewCache.all().stream().map(CraftingControllerCategory.Entry::new).toList();
    }

    private static List<MinerStructureCategory.Entry> buildEntries() {
        return ClientPreviewCache.all().stream().map(MinerStructureCategory.Entry::new).toList();
    }
}
