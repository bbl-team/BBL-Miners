package com.benbenlaw.miners.block;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.entity.MinerControllerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MinersBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Miners.MOD_ID);

    public static final Supplier<BlockEntityType<MinerControllerBlockEntity>> MINER_CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("miner_controller_block_entity", () ->
                    new BlockEntityType<>(MinerControllerBlockEntity::new, MinersBlocks.MINER_CONTROLLER.get()));
}
