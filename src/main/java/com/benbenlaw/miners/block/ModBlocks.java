package com.benbenlaw.miners.block;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.custom.*;
import com.benbenlaw.miners.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Miners.MOD_ID);

    //New Blocks

    public static final RegistryObject<Block> MINER = registerBlock("miner",
            () -> new MinerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).sound(SoundType.METAL)));

    public static final RegistryObject<Block> TREE_ABSORBER = registerBlock("tree_absorber",
            () -> new TreeAbsorberBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).sound(SoundType.METAL)));

    public static final RegistryObject<Block> FLUID_ABSORBER = registerBlock("fluid_absorber",
            () -> new FluidAbsorberBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).sound(SoundType.METAL)));

    public static final RegistryObject<Block> CRUSHER = registerBlock("crusher",
            () -> new CrusherBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1f).sound(SoundType.METAL)));

    public static final RegistryObject<Block> WOODEN_SUPPORT_FRAME = registerBlock("wooden_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(2.0f, 3f).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> STONE_SUPPORT_FRAME = registerBlock("stone_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.0f, 3f).sound(SoundType.STONE).noOcclusion()));

    public static final RegistryObject<Block> LAPIS_SUPPORT_FRAME = registerBlock("lapis_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> COPPER_SUPPORT_FRAME = registerBlock("copper_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> IRON_SUPPORT_FRAME = registerBlock("iron_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> GOLD_SUPPORT_FRAME = registerBlock("gold_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> DIAMOND_SUPPORT_FRAME = registerBlock("diamond_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> EMERALD_SUPPORT_FRAME = registerBlock("emerald_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> NETHERITE_SUPPORT_FRAME = registerBlock("netherite_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> ULTIMATE_SUPPORT_FRAME = registerBlock("ultimate_support_frame",
            () -> new MinerFrameBlockBase(BlockBehaviour.Properties.copy(Blocks.BEACON).strength(2.0f, 3f).sound(SoundType.METAL).noOcclusion()));



    private static ToIntFunction<BlockState> litBlockEmission(int lightLevel) {
        return (blockState) -> blockState.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, String tooltipKey) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()){
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(Component.literal(tooltipKey));
            }
        });

    }

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));

    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
