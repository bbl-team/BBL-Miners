package com.benbenlaw.miners.item;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MinersCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Miners.MOD_ID);

    public static final Supplier<CreativeModeTab> MINERS_TAB = CREATIVE_MODE_TABS.register(Miners.MOD_ID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> MinersBlocks.MINER_CONTROLLER.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup." + Miners.MOD_ID))
            .displayItems(MinersItems.ITEMS.getEntries()).build());
}
