package com.benbenlaw.multiblocks.data;

import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.List;

public class SupportFrames {

    public static final List<DeferredBlock<Block>> ALL = List.of(
            MultiblocksBlocks.WOODEN_SUPPORT_FRAME,
            MultiblocksBlocks.STONE_SUPPORT_FRAME,
            MultiblocksBlocks.COPPER_SUPPORT_FRAME,
            MultiblocksBlocks.IRON_SUPPORT_FRAME,
            MultiblocksBlocks.GOLD_SUPPORT_FRAME,
            MultiblocksBlocks.DIAMOND_SUPPORT_FRAME,
            MultiblocksBlocks.NETHERITE_SUPPORT_FRAME
    );

    public static boolean isWooden(DeferredBlock<Block> frame) {
        return frame == MultiblocksBlocks.WOODEN_SUPPORT_FRAME;
    }
}
