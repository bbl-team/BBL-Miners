package com.benbenlaw.miners.blockentitys;

import com.benbenlaw.miners.Miners;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

public class FormBlockEntity extends BlockEntity {
    private BlockPattern lastCheck;
    public FormBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(Miners.FORM_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (getLevel() == null) return;
    }
}
