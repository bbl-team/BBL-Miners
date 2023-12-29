package com.benbenlaw.miners.core;

import net.minecraft.world.level.block.state.pattern.BlockPattern;

public record ModBlockPattern<T>(String ID, T data, BlockPattern structure) { }
