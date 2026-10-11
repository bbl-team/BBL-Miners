package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.client.ClientPreviewCache;
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
    private IRecipeManager recipeManager;
    private List<MinerStructureCategory.Entry> registeredEntries = List.of();

    @Override
    public Identifier getPluginUid() {
        return Miners.identifier("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        category = new MinerStructureCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(category);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registeredEntries = buildEntries();
        registration.addRecipes(MinerStructureCategory.TYPE, registeredEntries);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MinerStructureCategory.TYPE, MinersBlocks.MINER_CONTROLLER.get());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        recipeManager = jeiRuntime.getRecipeManager();
        // Previews can arrive after JEI has started (and again on /reload), so swap the entries in when they do
        ClientPreviewCache.setListener(this::refreshEntries);
    }

    @Override
    public void onRuntimeUnavailable() {
        recipeManager = null;
        ClientPreviewCache.setListener(() -> {});
        if (category != null) category.closeRenderers();
    }

    private void refreshEntries() {
        if (recipeManager == null) return;

        List<MinerStructureCategory.Entry> newEntries = buildEntries();
        recipeManager.hideRecipes(MinerStructureCategory.TYPE, registeredEntries);
        recipeManager.addRecipes(MinerStructureCategory.TYPE, newEntries);
        registeredEntries = newEntries;
    }

    private static List<MinerStructureCategory.Entry> buildEntries() {
        return ClientPreviewCache.all().stream().map(MinerStructureCategory.Entry::new).toList();
    }
}
