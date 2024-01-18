package com.benbenlaw.miners.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

import java.util.ArrayList;

public class MultiBlockManagerBeta<T> {
    private final ArrayList<ModBlockPattern<T>> structures = new ArrayList<>();

    public MultiBlockManagerBeta() {
    }

    public void register(String ID, T data, BlockPattern blockPattern) {
        structures.add(new ModBlockPattern<>(ID, data, blockPattern));
    }

    public ModBlockPattern<T> findStructure(Level level, BlockPos pos) {
        for (ModBlockPattern<T> structure : structures) {
            var result = structure.structure().find(level, pos);
            if (result != null) {
                if (structure.structure().matches(level, result.getBlock(0, 0, 0).getPos(), Direction.DOWN, Direction.SOUTH) != null) {
                    return structure;
                }
            }
        }
        return null;
    }
}


