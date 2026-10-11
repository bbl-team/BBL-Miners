package com.benbenlaw.miners.data;

import com.benbenlaw.miners.block.MinersBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.List;

public class SupportFrames {

    public static final List<DeferredBlock<Block>> ALL = List.of(
            MinersBlocks.WOODEN_SUPPORT_FRAME,
            MinersBlocks.STONE_SUPPORT_FRAME,
            MinersBlocks.COPPER_SUPPORT_FRAME,
            MinersBlocks.IRON_SUPPORT_FRAME,
            MinersBlocks.GOLD_SUPPORT_FRAME,
            MinersBlocks.DIAMOND_SUPPORT_FRAME,
            MinersBlocks.NETHERITE_SUPPORT_FRAME
    );

    public static boolean isWooden(DeferredBlock<Block> frame) {
        return frame == MinersBlocks.WOODEN_SUPPORT_FRAME;
    }
}
