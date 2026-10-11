package com.benbenlaw.miners.block.custom;

import com.benbenlaw.miners.cap.CapType;
import net.minecraft.world.level.block.Block;

public class CapBlock extends Block {

    private final CapType capType;
    private final int level;

    public CapBlock(Properties properties, CapType capType, int level) {
        super(properties);
        this.capType = capType;
        this.level = level;
    }

    public CapType getCapType() {
        return capType;
    }

    public int getLevel() {
        return level;
    }
}
