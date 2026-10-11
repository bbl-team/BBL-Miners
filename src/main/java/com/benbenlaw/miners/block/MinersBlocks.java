package com.benbenlaw.miners.block;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.custom.MinerControllerBlock;
import com.benbenlaw.miners.item.MinersItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class MinersBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Miners.MOD_ID);

    public static final DeferredBlock<Block> MINER_CONTROLLER = registerBlock("miner_controller",
            properties -> new MinerControllerBlock(properties
                    .strength(3.5F)
                    .requiresCorrectToolForDrops()));

    //Caps


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        MinersItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static ResourceKey<Block> createID(String name) {
        return ResourceKey.create(Registries.BLOCK, Miners.identifier(name));
    }
}
