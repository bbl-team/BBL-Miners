package com.benbenlaw.multiblocks.data;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.util.MinersTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MinersBlockTagsProvider extends BlockTagsProvider {

    MinersBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Multiblocks.MOD_ID);
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
                .add(MultiblocksBlocks.WOODEN_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_2_FRAME)
                .addTag(MinersTags.Blocks.TIER_3_FRAME)
                .addTag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MultiblocksBlocks.STONE_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_3_FRAME)
                .addTag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MultiblocksBlocks.COPPER_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_4_FRAME)
                .addTag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MultiblocksBlocks.IRON_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_5_FRAME)
                .addTag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MultiblocksBlocks.GOLD_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_6_FRAME)
                .addTag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MultiblocksBlocks.DIAMOND_SUPPORT_FRAME.get());

        tag(MinersTags.Blocks.TIER_7_FRAME)
                .add(MultiblocksBlocks.NETHERITE_SUPPORT_FRAME.get());
        
        
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MultiblocksBlocks.CRAFTING_CONTROLLER.get())
        ;

        MultiblocksBlocks.CAPS.forEach(cap -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(cap.get()));

        SupportFrames.ALL.forEach(frame -> tag(SupportFrames.isWooden(frame)
                ? BlockTags.MINEABLE_WITH_AXE
                : BlockTags.MINEABLE_WITH_PICKAXE).add(frame.get()));
    }


    @Override
    public @NotNull String getName() {
        return Multiblocks.MOD_ID + " Block Tags";
    }
}
