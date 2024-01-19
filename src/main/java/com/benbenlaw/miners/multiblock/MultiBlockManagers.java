package com.benbenlaw.miners.multiblock;

import com.benbenlaw.miners.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;

public class MultiBlockManagers {


    public static final MultiBlockManagerBeta<Miners> MINERS = new MultiBlockManagerBeta<>();
    public static final MultiBlockManagerBeta<TreeAbsorbers> TREE_ABSORBERS = new MultiBlockManagerBeta<>();

    static {
        TREE_ABSORBERS.register (
                "miners:oak",
                TreeAbsorbers.OAK,
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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
                BlockPatternBuilder.start()
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

        /*
        patterns.add(
                new ModBlockPattern(
                        "miners:cap_test",
                        1,
                        BlockPatternBuilder.start()
                                .aisle("0   0", "     ", "     ", "     ", "0   0")
                                .where('0', a -> a.getState().is(Blocks.BEACON))
                                .build()
                )
        );
         */
    }

}
