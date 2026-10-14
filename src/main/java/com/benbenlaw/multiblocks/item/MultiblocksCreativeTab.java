package com.benbenlaw.multiblocks.item;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MultiblocksCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Multiblocks.MOD_ID);

    public static final Supplier<CreativeModeTab> MINERS_TAB = CREATIVE_MODE_TABS.register(Multiblocks.MOD_ID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> MultiblocksBlocks.MINER_CONTROLLER.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup." + Multiblocks.MOD_ID))
            .displayItems(MultiblocksItems.ITEMS.getEntries()).build());
}
