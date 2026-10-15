package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.multiblocks.Multiblocks;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import net.minecraft.core.registries.Registries;

public class MultiblocksKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, callback -> {
            callback.add(Multiblocks.identifier("crafting_controller"), CraftingControllerBuilder.class, CraftingControllerBuilder::new);
            callback.add(Multiblocks.identifier("miner_controller"), MinerControllerBuilder.class, MinerControllerBuilder::new);
        });
    }
}
