package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.util.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public enum MinerTier {

    WOODEN(ModTags.Blocks.WOODEN_SUPPORT_FRAME),
    STONE(ModTags.Blocks.STONE_SUPPORT_FRAME),
    LAPIS(ModTags.Blocks.LAPIS_SUPPORT_FRAME),
    COPPER(ModTags.Blocks.COPPER_SUPPORT_FRAME),
    IRON(ModTags.Blocks.IRON_SUPPORT_FRAME),
    GOLD(ModTags.Blocks.GOLD_SUPPORT_FRAME),
    DIAMOND(ModTags.Blocks.DIAMOND_SUPPORT_FRAME),
    EMERALD(ModTags.Blocks.EMERALD_SUPPORT_FRAME),
    NETHERITE(ModTags.Blocks.NETHERITE_SUPPORT_FRAME),
    ULTIMATE(ModTags.Blocks.ULTIMATE_SUPPORT_FRAME);

    private final TagKey<Block> tierSupportFrame;

    MinerTier(TagKey<Block> tierSupportFrame) {
    this.tierSupportFrame = tierSupportFrame;
    }

    public TagKey<Block> getSupportFrame() {
        return tierSupportFrame;
    }
}