package com.benbenlaw.miners.multiblock;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
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
    }

    static {
        MINERS.register(
                "miners:iron",
                Miners.IRON,
                BlockPatternBuilder.start()
                        .aisle("C   C", "     ", "     ", "     ", "C   C")
                        .aisle("0   0", "     ", "  *  ", "     ", "0   0")
                        .aisle("00000", "0   0", "0   0", "0   0", "00000")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .where('0', a -> a.getState().is(Blocks.IRON_BLOCK))
                        .where('C', a -> a.getState().is(ModBlocks.ENDER_SCRAMBLER.get()))
                        .where('*', a -> a.getState().is(com.benbenlaw.miners.block.ModBlocks.MINER.get()))
                        .build()
        );

        MINERS.register(
                "miners:gold",
                Miners.WOOD,
                BlockPatternBuilder.start()
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("00000", "0   0", "0   0", "0   0", "00000")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .where('0', a -> a.getState().is(Blocks.GOLD_BLOCK))
                        .where(' ', a -> a.getState().is(Blocks.AIR))
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
