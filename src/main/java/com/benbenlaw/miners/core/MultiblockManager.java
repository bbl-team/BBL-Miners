package com.benbenlaw.miners.core;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.blocks.FormBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MultiblockManager {
    public static final ArrayList<ModBlockPattern> patterns = new ArrayList<>();

    public static Predicate<BlockInWorld> getPred(Block block) {
        return a -> a.getState().is(block);
    }

    public static <T extends Comparable<T>> Predicate<BlockInWorld> getPredWithState(Block block, Property<T> property, Supplier<T> requiredValue) {
        return a -> a.getState().is(block) && a.getState().getValue(property).equals(requiredValue);
    }




    static {
        patterns.add(
                new ModBlockPattern(
                        "test",
                        BlockPatternBuilder.start()
                                .aisle("0   0", "     ", "     ", "     ", "0   0")
                                .aisle("00000", "0   0", "0   0", "0   0", "00000")
                                .aisle("0   0", "     ", "     ", "     ", "0   0")
                                .aisle("0   0", "     ", "     ", "     ", "0   0")
                                .aisle("0   0", "     ", "     ", "     ", "0   0")
                                .where('0', a -> a.getState().is(Blocks.IRON_BLOCK))
                                .build()
                )
        );
    }

    public static void init() {

    }

    public static ModBlockPattern findUnformedStructure(Level level, BlockPos pos) {
        for (ModBlockPattern pattern : patterns) {
            var struc = pattern.structure();
            var match = struc.find(level, pos);
            if (match != null) {
                return pattern;
            }
        }
        return null;
    }

}
