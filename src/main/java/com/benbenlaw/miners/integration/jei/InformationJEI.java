package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class InformationJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Miners.MOD_ID, "information");
    }
     @Override
    public void registerRecipes(IRecipeRegistration reg) {

    //     reg.addIngredientInfo(new ItemStack(ModBlocks.RESOURCE_GENERATOR_2.get()), VanillaTypes.ITEM_STACK,
    //             Component.translatable("jei.opolisutilities.resource_generator"));


/*
         for (int i = 0; i < possibleBlocks.size(); i++) {
             if (itemHandler.isItemValid(i, stack) && itemHandler.insertItem(i, stack, true).isEmpty()) {
                 itemHandler.insertItem(i, stack, false);
                 break;
             }
         }

 */

    }
}
