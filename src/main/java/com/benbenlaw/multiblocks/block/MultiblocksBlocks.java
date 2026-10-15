package com.benbenlaw.multiblocks.block;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.custom.CapBlock;
import com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock;
import com.benbenlaw.multiblocks.cap.CapType;
import com.benbenlaw.multiblocks.item.MultiblocksItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MultiblocksBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Multiblocks.MOD_ID);

    public static final DeferredBlock<Block> CRAFTING_CONTROLLER = registerBlock("crafting_controller",
            properties -> new CraftingControllerBlock(properties
                    .strength(3.5F)
                    .requiresCorrectToolForDrops()));

    //Caps
    public static final List<DeferredBlock<CapBlock>> CAPS = new ArrayList<>();

    public static final DeferredBlock<CapBlock> SILK_TOUCH_CAP_1 = registerCap(CapType.SILK_TOUCH, 1);
    public static final DeferredBlock<CapBlock> FORTUNE_CAP_1 = registerCap(CapType.FORTUNE, 1);
    public static final DeferredBlock<CapBlock> FORTUNE_CAP_2 = registerCap(CapType.FORTUNE, 2);
    public static final DeferredBlock<CapBlock> FORTUNE_CAP_3 = registerCap(CapType.FORTUNE, 3);
    public static final DeferredBlock<CapBlock> EFFICIENCY_CAP_1 = registerCap(CapType.EFFICIENCY, 1);
    public static final DeferredBlock<CapBlock> EFFICIENCY_CAP_2 = registerCap(CapType.EFFICIENCY, 2);
    public static final DeferredBlock<CapBlock> EFFICIENCY_CAP_3 = registerCap(CapType.EFFICIENCY, 3);

    public static final DeferredBlock<Block> WOODEN_SUPPORT_FRAME = registerBlock("wooden_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> STONE_SUPPORT_FRAME = registerBlock("stone_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> COPPER_SUPPORT_FRAME = registerBlock("copper_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> IRON_SUPPORT_FRAME = registerBlock("iron_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> GOLD_SUPPORT_FRAME = registerBlock("gold_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> DIAMOND_SUPPORT_FRAME = registerBlock("diamond_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> NETHERITE_SUPPORT_FRAME = registerBlock("netherite_support_frame",
            properties -> new Block(properties
                    .strength(2.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    private static DeferredBlock<CapBlock> registerCap(CapType type, int level) {
        DeferredBlock<CapBlock> cap = registerBlock(type.getId() + "_cap_" + level,
                properties -> new CapBlock(properties
                        .strength(3.0F)
                        .requiresCorrectToolForDrops(), type, level));
        CAPS.add(cap);
        return cap;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        MultiblocksItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static ResourceKey<Block> createID(String name) {
        return ResourceKey.create(Registries.BLOCK, Multiblocks.identifier(name));
    }
}
