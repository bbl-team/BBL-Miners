package com.benbenlaw.miners.util;

import com.benbenlaw.miners.Miners;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {



    public static class Blocks {


//        public static final TagKey<Block> MINEABLE_WITH_HAMMER = tag("mineable_with_hammer");
        public static final TagKey<Block> MINER_TIER_1 = tag("miner_tier_1");
        public static final TagKey<Block> MINER_TIER_2 = tag("miner_tier_2");
        public static final TagKey<Block> MINER_TIER_3 = tag("miner_tier_3");
        public static final TagKey<Block> MINER_TIER_4 = tag("miner_tier_4");
        public static final TagKey<Block> MINER_TIER_5 = tag("miner_tier_5");
        public static final TagKey<Block> MINER_TIER_6 = tag("miner_tier_6");
        public static final TagKey<Block> MINER_TIER_7 = tag("miner_tier_7");
        public static final TagKey<Block> MINER_TIER_8 = tag("miner_tier_8");
        public static final TagKey<Block> MINER_TIER_9 = tag("miner_tier_9");
        public static final TagKey<Block> MINER_TIER_10 = tag("miner_tier_10");

        public static final TagKey<Block> TREE_ABSORBER_LEAVES = tag("tree_absorber_leaves");

        public static final TagKey<Block> WOODEN_SUPPORT_FRAME = tag("wooden_support_frame");
        public static final TagKey<Block> STONE_SUPPORT_FRAME = tag("stone_support_frame");
        public static final TagKey<Block> LAPIS_SUPPORT_FRAME = tag("lapis_support_frame");
        public static final TagKey<Block> COPPER_SUPPORT_FRAME = tag("copper_support_frame");
        public static final TagKey<Block> IRON_SUPPORT_FRAME = tag("iron_support_frame");
        public static final TagKey<Block> GOLD_SUPPORT_FRAME = tag("gold_support_frame");
        public static final TagKey<Block> DIAMOND_SUPPORT_FRAME = tag("diamond_support_frame");
        public static final TagKey<Block> EMERALD_SUPPORT_FRAME = tag("emerald_support_frame");
        public static final TagKey<Block> NETHERITE_SUPPORT_FRAME = tag("netherite_support_frame");
        public static final TagKey<Block> ULTIMATE_SUPPORT_FRAME = tag("ultimate_support_frame");
        public static final TagKey<Block> EMPTY = tag("empty");

        public static final TagKey<Block> WOODEN_SUPPORT_CAP = tag("wooden_support_cap");
        public static final TagKey<Block> STONE_SUPPORT_CAP = tag("stone_support_cap");
        public static final TagKey<Block> LAPIS_SUPPORT_CAP = tag("lapis_support_cap");
        public static final TagKey<Block> COPPER_SUPPORT_CAP = tag("copper_support_cap");
        public static final TagKey<Block> IRON_SUPPORT_CAP = tag("iron_support_cap");
        public static final TagKey<Block> GOLD_SUPPORT_CAP = tag("gold_support_cap");
        public static final TagKey<Block> DIAMOND_SUPPORT_CAP = tag("diamond_support_cap");
        public static final TagKey<Block> EMERALD_SUPPORT_CAP = tag("emerald_support_cap");
        public static final TagKey<Block> NETHERITE_SUPPORT_CAP = tag("netherite_support_cap");
        public static final TagKey<Block> ULTIMATE_SUPPORT_CAP = tag("ultimate_support_cap");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Miners.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }




    public static class Items {

        public static final TagKey<Item> EMPTY = tag("empty");
        public static final TagKey<Item> WOODEN_SUPPORT_FRAME = tag("wooden_support_frame");
        public static final TagKey<Item> STONE_SUPPORT_FRAME = tag("stone_support_frame");
        public static final TagKey<Item> LAPIS_SUPPORT_FRAME = tag("lapis_support_frame");
        public static final TagKey<Item> COPPER_SUPPORT_FRAME = tag("copper_support_frame");
        public static final TagKey<Item> IRON_SUPPORT_FRAME = tag("iron_support_frame");
        public static final TagKey<Item> GOLD_SUPPORT_FRAME = tag("gold_support_frame");
        public static final TagKey<Item> DIAMOND_SUPPORT_FRAME = tag("diamond_support_frame");
        public static final TagKey<Item> EMERALD_SUPPORT_FRAME = tag("emerald_support_frame");
        public static final TagKey<Item> NETHERITE_SUPPORT_FRAME = tag("netherite_support_frame");
        public static final TagKey<Item> ULTIMATE_SUPPORT_FRAME = tag("ultimate_support_frame");

        public static final TagKey<Item> TREE_ABSORBER_LEAVES = tag("tree_absorber_leaves");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Miners.MOD_ID, name));

        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }

    public static class Fluids {

        public static final TagKey<Fluid> FLUID_ABSORBER_FLUID = tag("fluid_absorber_fluid");

        private static TagKey<Fluid> tag(String name) {
            return FluidTags.create(new ResourceLocation(Miners.MOD_ID, name));
        }

        private static TagKey<Fluid> forgeTag(String name) {
            return FluidTags.create(new ResourceLocation("forge", name));
        }

    }

}
