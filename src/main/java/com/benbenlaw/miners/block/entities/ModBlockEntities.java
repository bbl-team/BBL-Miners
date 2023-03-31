package com.benbenlaw.miners.block.entities;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.block.entities.custom.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Miners.MOD_ID);

    public static final RegistryObject<BlockEntityType<TreeAbsorberBlockEntity>> TREE_ABSORBER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("tree_absorber_block_entity", () ->
                    BlockEntityType.Builder.of(TreeAbsorberBlockEntity::new,
                            ModBlocks.TREE_ABSORBER.get()).build(null));

    public static final RegistryObject<BlockEntityType<FluidAbsorberBlockEntity>> FLUID_ABSORBER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fluid_absorber_block_entity", () ->
                    BlockEntityType.Builder.of(FluidAbsorberBlockEntity::new,
                            ModBlocks.FLUID_ABSORBER.get()).build(null));

    public static final RegistryObject<BlockEntityType<MinerBaseBlockEntity>> MINER_BASE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("miner_base_block_entity", () ->
                    BlockEntityType.Builder.of(MinerBaseBlockEntity::new,
                            ModBlocks.MINER_BASE_BLOCK.get()).build(null));





    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
