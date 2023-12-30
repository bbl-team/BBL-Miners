package com.benbenlaw.miners.core;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;

import java.util.ArrayList;

public class MultiblockManagers {


    public static final MultiBlockManagerBeta<Tier> MINERS = new MultiBlockManagerBeta<>();




    static {
        MINERS.register(
                "miners:iron",
                Tier.IRON,
                BlockPatternBuilder.start()
                        .aisle("C   C", "     ", "     ", "     ", "C   C")
                        .aisle("0   0", "     ", "  *  ", "     ", "0   0")
                        .aisle("00000", "0   0", "0   0", "0   0", "00000")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .where('0', a -> a.getState().is(Blocks.IRON_BLOCK))
                        .where('C', a -> a.getState().is(ModBlocks.ENDER_SCRAMBLER.get()))
                        .where('*', a -> a.getState().is(Miners.FORM_BLOCK.get()))
                        .where(' ', a -> a.getState().is(Blocks.AIR))
                        .build()
        );

        MINERS.register(
                "miners:gold",
                Tier.WOOD,
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
