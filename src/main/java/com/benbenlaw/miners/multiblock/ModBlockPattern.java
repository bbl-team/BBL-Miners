package com.benbenlaw.miners.multiblock;

import net.minecraft.world.level.block.state.pattern.BlockPattern;

public record ModBlockPattern<T>(String ID, T data, BlockPattern structure) { }
