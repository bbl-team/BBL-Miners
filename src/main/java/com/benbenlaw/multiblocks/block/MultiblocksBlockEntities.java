package com.benbenlaw.multiblocks.block;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.entity.CraftingControllerBlockEntity;
import com.benbenlaw.multiblocks.block.entity.MinerControllerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MultiblocksBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Multiblocks.MOD_ID);

    public static final Supplier<BlockEntityType<MinerControllerBlockEntity>> MINER_CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("miner_controller_block_entity", () ->
                    new BlockEntityType<>(MinerControllerBlockEntity::new, MultiblocksBlocks.MINER_CONTROLLER.get()));

    public static final Supplier<BlockEntityType<CraftingControllerBlockEntity>> CRAFTING_CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crafting_controller_block_entity", () ->
                    new BlockEntityType<>(CraftingControllerBlockEntity::new, MultiblocksBlocks.CRAFTING_CONTROLLER.get()));
}
