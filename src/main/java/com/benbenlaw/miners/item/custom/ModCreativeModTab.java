package com.benbenlaw.miners.item.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModTab {
    public static final CreativeModeTab MINERS = new CreativeModeTab("miners") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.MINER_BASE_BLOCK.get());
        }
    };


}
