package com.benbenlaw.miners.multiblock;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.multiblock.core.IMultiBlockPattern;
import com.benbenlaw.miners.multiblock.core.IMultiBlockPatternBuilder;
import com.benbenlaw.miners.multiblock.core.SimpleMultiBlockPattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class MultiBlockManagers {


    public static final MultiBlockManagerBeta<Miners> MINERS = new MultiBlockManagerBeta<>();
    public static final MultiBlockManagerBeta<TreeAbsorbers> TREE_ABSORBERS = new MultiBlockManagerBeta<>();
    public static final MultiBlockManagerBeta<FluidAbsorbers> FLUID_ABSORBERS = new MultiBlockManagerBeta<>();

    static {
        TREE_ABSORBERS.register (
                "miners:oak",
                TreeAbsorbers.OAK,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.OAK_LOG))
                        .where('V', a -> a.getState().is(Blocks.OAK_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:acacia",
                TreeAbsorbers.ACACIA,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.ACACIA_LOG))
                        .where('V', a -> a.getState().is(Blocks.ACACIA_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:birch",
                TreeAbsorbers.BIRCH,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.BIRCH_LOG))
                        .where('V', a -> a.getState().is(Blocks.BIRCH_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:spruce",
                TreeAbsorbers.SPRUCE,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.SPRUCE_LOG))
                        .where('V', a -> a.getState().is(Blocks.SPRUCE_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:jungle",
                TreeAbsorbers.JUNGLE,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.JUNGLE_LOG))
                        .where('V', a -> a.getState().is(Blocks.JUNGLE_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:dark_oak",
                TreeAbsorbers.DARK_OAK,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.DARK_OAK_LOG))
                        .where('V', a -> a.getState().is(Blocks.DARK_OAK_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:cherry",
                TreeAbsorbers.CHERRY,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.CHERRY_LOG))
                        .where('V', a -> a.getState().is(Blocks.CHERRY_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );

        TREE_ABSORBERS.register (
                "miners:mangrove",
                TreeAbsorbers.MANGROVE,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("WWWWWWW","W     W","W     W","W     W","W     W", "W     W","WWWWWWW")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","       ","       ", "       ","W     W")
                        .aisle("W     W","       ","   V   ","  VLV  ","   V   ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   L   ","       ", "       ","W     W")
                        .aisle("W     W","       ","       ","   *   ","       ", "       ","W     W")
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .where('W', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.WOODEN_SUPPORT_FRAME.get()))
                        .where('L', a -> a.getState().is(Blocks.MANGROVE_LOG))
                        .where('V', a -> a.getState().is(Blocks.MANGROVE_LEAVES))
                        .where(' ', a -> !a.getState().is(com.benbenlaw.miners.block.ModBlocks.TREE_ABSORBER.get()))
                        .build()
        );
    }

    static {
        MINERS.register(
                "miners:iron",
                Miners.IRON,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_IRON))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:gold",
                Miners.GOLD,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_GOLD))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:redstone",
                Miners.REDSTONE,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_REDSTONE))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:lapis",
                Miners.LAPIS,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_LAPIS))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:diamond",
                Miners.DIAMOND,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_DIAMOND))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:copper",
                Miners.COPPER,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_COPPER))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:emerald",
                Miners.EMERALD,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_EMERALD))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:quartz",
                Miners.QUARTZ,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_QUARTZ))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:coal",
                Miners.COAL,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Tags.Blocks.ORES_COAL))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        TagKey<Block> oreTin = BlockTags.create(new ResourceLocation("forge","ores/tin"));

        MINERS.register(
                "miners:tin",
                Miners.TIN,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(oreTin))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );

        TagKey<Block> oreRuby = BlockTags.create(new ResourceLocation("forge","ores/ruby"));
        MINERS.register(
                "miners:ruby",
                Miners.TIN,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(oreRuby))
                        .where('*', a -> a.getState().is(ModBlocks.MINER.get()))
                        .build()
        );



        /*
        patterns.add(
                new ModBlockPattern(
                        "miners:cap_test",
                        1,
                        SimpleMultiBlockPattern.AisleBuilder.start()
                                .aisle("0   0", "     ", "     ", "     ", "0   0")
                                .where('0', a -> a.getState().is(Blocks.BEACON))
                                .build()
                )
        );
         */
    }

    static {
        FLUID_ABSORBERS.register(
                "miners:water",
                FluidAbsorbers.WATER,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Blocks.WATER))
                        .where('*', a -> a.getState().is(ModBlocks.FLUID_ABSORBER.get()))
                        .build()
        );

        FLUID_ABSORBERS.register(
                "miners:lava",
                FluidAbsorbers.LAVA,
                SimpleMultiBlockPattern.AisleBuilder.start()
                        .aisle("F   F", "     ", "  *  ", "     ", "F   F")
                        .aisle("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .aisle("F   F", " OOO ", " OOO ", " OOO ", "F   F")
                        .where('F', a -> a.getState().is(ModBlocks.STONE_SUPPORT_FRAME.get()))
                        .where('O', a -> a.getState().is(Blocks.LAVA))
                        .where('*', a -> a.getState().is(ModBlocks.FLUID_ABSORBER.get()))
                        .build()
        );
    }
}
