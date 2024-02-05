package com.benbenlaw.miners.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import org.mangorage.mangomultiblock.core.IMultiBlockPattern;

import java.util.ArrayList;

public class MultiBlockManagerBeta<T> {
    private final ArrayList<ModBlockPattern<T>> structures = new ArrayList<>();

    public MultiBlockManagerBeta() {
    }

    public void register(String ID, T data, IMultiBlockPattern blockPattern) {
        structures.add(new ModBlockPattern<T>(ID, data, blockPattern));
    }

    public ModBlockPattern<T> findStructure(Level level, BlockPos pos, Rotation rotation) {
        for (ModBlockPattern<T> structure : structures)
            if (structure.structure().matches(level, pos, rotation))
                return structure;
        return null;
    }
}


