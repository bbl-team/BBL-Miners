package com.benbenlaw.miners.block.entity;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

public class MinerEnergyHandler extends SimpleEnergyHandler {
    private final SyncableBlockEntity blockEntity;

    public MinerEnergyHandler(int capacity, int maxInsert, SyncableBlockEntity blockEntity) {
        super(capacity, maxInsert, 0);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onEnergyChanged(int previousAmount) {
        blockEntity.setChanged();
        super.onEnergyChanged(previousAmount);
    }
}
