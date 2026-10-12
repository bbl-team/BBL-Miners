package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.util.MinersTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MinersBlockTagsProvider extends BlockTagsProvider {

    MinersBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Miners.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {


        tag(MinersTags.Blocks.TIER_1_FRAME)
                .addTag(MinersTags.Blocks.TIER_2_FRAME)
                .addTag(MinersTags.Blocks.TIER_3_FRAME)
                .addTag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.WOODEN_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_2_FRAME)
                .addTag(MinersTags.Blocks.TIER_3_FRAME)
                .addTag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.STONE_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_3_FRAME)
                .addTag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.COPPER_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.IRON_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.GOLD_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.DIAMOND_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MinersBlocks.NETHERITE_SUPPORT_FRAME.get());
        
        
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MinersBlocks.MINER_CONTROLLER.get())
        ;

        MinersBlocks.CAPS.forEach(cap -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(cap.get()));

        SupportFrames.ALL.forEach(frame -> tag(SupportFrames.isWooden(frame)
                ? BlockTags.MINEABLE_WITH_AXE
                : BlockTags.MINEABLE_WITH_PICKAXE).add(frame.get()));
    }


    @Override
    public @NotNull String getName() {
        return Miners.MOD_ID + " Block Tags";
    }
}
