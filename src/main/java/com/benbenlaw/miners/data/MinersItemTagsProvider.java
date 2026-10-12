package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.item.MinersItems;
import com.benbenlaw.miners.util.MinersTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MinersItemTagsProvider extends ItemTagsProvider {

    MinersItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Miners.MOD_ID);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        //Frames
        tag(MinersTags.Items.TIER_1_FRAME)
                .addTag(MinersTags.Items.TIER_2_FRAME)
                .addTag(MinersTags.Items.TIER_3_FRAME)
                .addTag(MinersTags.Items.TIER_4_FRAME)
                .addTag(MinersTags.Items.TIER_5_FRAME)
                .addTag(MinersTags.Items.TIER_6_FRAME)
                .addTag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.WOODEN_SUPPORT_FRAME.asItem());
        
        tag(MinersTags.Items.TIER_2_FRAME)
                .addTag(MinersTags.Items.TIER_3_FRAME)
                .addTag(MinersTags.Items.TIER_4_FRAME)
                .addTag(MinersTags.Items.TIER_5_FRAME)
                .addTag(MinersTags.Items.TIER_6_FRAME)
                .addTag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.STONE_SUPPORT_FRAME.asItem());
        
        tag(MinersTags.Items.TIER_3_FRAME)
                .addTag(MinersTags.Items.TIER_4_FRAME)
                .addTag(MinersTags.Items.TIER_5_FRAME)
                .addTag(MinersTags.Items.TIER_6_FRAME)
                .addTag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.COPPER_SUPPORT_FRAME.asItem());
        
        tag(MinersTags.Items.TIER_4_FRAME)
                .addTag(MinersTags.Items.TIER_5_FRAME)
                .addTag(MinersTags.Items.TIER_6_FRAME)
                .addTag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.IRON_SUPPORT_FRAME.asItem());
        
        tag(MinersTags.Items.TIER_5_FRAME)
                .addTag(MinersTags.Items.TIER_6_FRAME)
                .addTag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.GOLD_SUPPORT_FRAME.asItem());
        
        tag(MinersTags.Items.TIER_6_FRAME)
                .addTag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.DIAMOND_SUPPORT_FRAME.asItem());
        
        tag(MinersTags.Items.TIER_7_FRAME)
                .add(MinersBlocks.NETHERITE_SUPPORT_FRAME.asItem());

    }

}