package com.benbenlaw.miners.multiblock;

import com.benbenlaw.miners.multiblock.core.IMultiBlockPattern;

public record ModBlockPattern<T>(String ID, T data, IMultiBlockPattern structure) { }
