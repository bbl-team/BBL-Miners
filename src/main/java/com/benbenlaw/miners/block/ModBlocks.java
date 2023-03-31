package com.benbenlaw.miners.block;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.custom.*;
import com.benbenlaw.miners.block.entities.custom.MinerBaseBlockEntity;
import com.benbenlaw.miners.item.ModItems;
import com.benbenlaw.miners.item.custom.ModCreativeModTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Miners.MOD_ID);

    public static final RegistryObject<Block> TREE_ABSORBER = registerBlock("tree_absorber",
            () -> new TreeAbsorberBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> WOODEN_SUPPORT_FRAME = registerBlock("wooden_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0f, 3f).sound(SoundType.WOOD).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> STONE_SUPPORT_FRAME = registerBlock("stone_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 3f).sound(SoundType.STONE).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> LAPIS_SUPPORT_FRAME = registerBlock("lapis_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> COPPER_SUPPORT_FRAME = registerBlock("copper_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> IRON_SUPPORT_FRAME = registerBlock("iron_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> GOLD_SUPPORT_FRAME = registerBlock("gold_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> DIAMOND_SUPPORT_FRAME = registerBlock("diamond_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> EMERALD_SUPPORT_FRAME = registerBlock("emerald_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> NETHERITE_SUPPORT_FRAME = registerBlock("netherite_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> ULTIMATE_SUPPORT_FRAME = registerBlock("ultimate_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> MINER_BASE_BLOCK = registerBlock("miner_base_block",
            () -> new MinerBaseBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL)),
            ModCreativeModTab.MINERS);

    public static final RegistryObject<Block> FLUID_ABSORBER = registerBlock("fluid_absorber",
            () -> new FluidAbsorberBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3f).sound(SoundType.METAL)),
            ModCreativeModTab.MINERS);









    //Light Level When Interacted With

    private static ToIntFunction<BlockState> litBlockEmission(int p_50760_) {
        return (p_50763_) -> {
            return p_50763_.getValue(BlockStateProperties.LIT) ? p_50760_ : 0;
        };
    }




    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModCreativeModTab.MINERS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}