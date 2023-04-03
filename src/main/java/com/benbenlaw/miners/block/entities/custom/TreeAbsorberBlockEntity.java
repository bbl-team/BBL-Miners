package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.block.custom.TreeAbsorberBlock;
import com.benbenlaw.miners.block.entities.ModBlockEntities;
import com.benbenlaw.miners.config.MinersConfigFile;
import com.benbenlaw.miners.recipe.CapBlocksRecipe;
import com.benbenlaw.miners.recipe.MinerBlocksRecipe;
import com.benbenlaw.miners.recipe.NoInventoryRecipe;
import com.benbenlaw.miners.recipe.TreeAbsorberBlocksRecipe;
import com.benbenlaw.miners.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;


public class TreeAbsorberBlockEntity extends BlockEntity {

    // Add a counter variable
    private int counter = 0;

    public TreeAbsorberBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.TREE_ABSORBER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(@NotNull Level level, @NotNull BlockPos blockPos, BlockState blockState, @NotNull TreeAbsorberBlockEntity entity) {

        entity.counter++;

        int tickRate = 220;

        if(!level.getBlockState(blockPos.above(11).south(3).west(3)).is(Blocks.AIR) && !level.getBlockState(blockPos.above(11).south(3).east(3)).is(Blocks.AIR) &&
                !level.getBlockState(blockPos.above(11).north(3).east(3)).is(Blocks.AIR) && !level.getBlockState(blockPos.above(11).north(3).west(3)).is(Blocks.AIR)) {

            for (CapBlocksRecipe match : level.getRecipeManager().getRecipesFor(CapBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName = match.getBlock();
                Block speedBlock = Registry.BLOCK.get(new ResourceLocation(blockName));
                TagKey<Block> blockTag = BlockTags.create(new ResourceLocation(blockName));

                if (level.getBlockState(blockPos.above(11).south(3).west(3)).is(speedBlock) ||
                        level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().is(blockTag)) {

                    if (level.getBlockState(blockPos.above(11).south(3).east(3)).is(speedBlock) ||
                            level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().is(blockTag)) {

                        if (level.getBlockState(blockPos.above(11).north(3).east(3)).is(speedBlock) ||
                                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().is(blockTag)) {

                            if (level.getBlockState(blockPos.above(11).north(3).west(3)).is(speedBlock) ||
                                    level.getBlockState(blockPos.above(11).north(3).west(3)).is(blockTag)) {
                                tickRate = match.getTickRate();
                            }
                        }
                    }
                }
            }
        }

        if (level.getBlockState(blockPos).is(ModBlocks.TREE_ABSORBER.get())) {

            if (tickRate < 201) {
                level.addParticle(ParticleTypes.CRIMSON_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }

            if (level.getBlockState(blockPos).getValue(TreeAbsorberBlock.LIT)) {
                level.addParticle(ParticleTypes.WARPED_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
        }

        if (entity.counter % tickRate == 0) {

            if (level.getBlockState(blockPos).is(ModBlocks.TREE_ABSORBER.get())) {

                for (TreeAbsorberBlocksRecipe match : level.getRecipeManager().getRecipesFor(TreeAbsorberBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                    String logBlock = match.getLogBlock();
                    String leafBlock = match.getLeafBlock();
                    ItemStack extraItem1 = match.getExtraItem1();
                    ItemStack extraItem2 = match.getExtraItem2();
                    ItemStack extraItem3 = match.getExtraItem3();

                    Block logBlockBlock = Registry.BLOCK.get(new ResourceLocation(logBlock));
                    Block leafBlockBlock = Registry.BLOCK.get(new ResourceLocation(leafBlock));

                    //CHECK THE STRUCTURE

                    if (level.getBlockState(blockPos.north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1)).is(Blocks.DIRT) && level.getBlockState(blockPos.above(2)).is(logBlockBlock) && level.getBlockState(blockPos.above(3)).is(logBlockBlock) && level.getBlockState(blockPos.above(4)).is(logBlockBlock) && level.getBlockState(blockPos.above(5)).is(logBlockBlock) && level.getBlockState(blockPos.above(4).north(2).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(2).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(1).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(1).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(1).east(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(1).west(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).north(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).east(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).west(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(1).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(1).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(1).east(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(1).west(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(2).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(2).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(4).south(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(2).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(2).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(1).east(1)).is(leafBlockBlock) &&level.getBlockState(blockPos.above(5).north(1).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(1).east(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(1).west(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).north(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).east(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).west(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(1).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(1).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(1).east(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(1).west(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(2).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(2).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(5).south(2)).is(leafBlockBlock) && level.getBlockState(blockPos.above(6)).is(leafBlockBlock) && level.getBlockState(blockPos.above(6).north(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(6).south(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(6).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(6).east(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(7)).is(leafBlockBlock) && level.getBlockState(blockPos.above(7).north(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(7).south(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(7).west(1)).is(leafBlockBlock) && level.getBlockState(blockPos.above(7).east(1)).is(leafBlockBlock)){

                        BlockState notWorkingState = level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, false);
                        level.setBlockAndUpdate(blockPos, notWorkingState);

                        if (level.getBlockEntity(blockPos.below()) != null) {

                            BlockState workingState = level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, true);
                            level.setBlockAndUpdate(blockPos, workingState);
                            level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1, 1, false);

                            BlockEntity ent = level.getBlockEntity(blockPos.below());
                            assert ent != null;
                            ent.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {

                                        ItemStack logAsItem = new ItemStack(logBlockBlock.asItem());
                                        ItemStack leafAsItem = new ItemStack(leafBlockBlock.asItem());

                                        //Wood

                                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                                            if (itemHandler.isItemValid(i, logAsItem) && itemHandler.insertItem(i, logAsItem, true).isEmpty()) {
                                                itemHandler.insertItem(i, logAsItem, false);
                                                break;
                                            }
                                        }

                                        //Leaves

                                        if (Math.random() > MinersConfigFile.leavesDropChanceFromTreeAbsorber.get()) {
                                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                if (itemHandler.isItemValid(i, leafAsItem) && itemHandler.insertItem(i, leafAsItem, true).isEmpty()) {
                                                    itemHandler.insertItem(i, leafAsItem, false);
                                                    break;
                                                }
                                            }
                                        }

                                        if (!extraItem1.isEmpty()) {
                                            if (Math.random() <= match.getExtraItem1Chance()) {
                                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                    if (itemHandler.isItemValid(i, extraItem1) && itemHandler.insertItem(i, extraItem1, true).isEmpty()) {
                                                        itemHandler.insertItem(i, extraItem1, false);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if (!extraItem2.isEmpty()) {
                                            if (Math.random() <= match.getExtraItem2Chance()) {
                                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                    if (itemHandler.isItemValid(i, extraItem2) && itemHandler.insertItem(i, extraItem2, true).isEmpty()) {
                                                        itemHandler.insertItem(i, extraItem2, false);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if (!extraItem3.isEmpty()) {
                                            if (Math.random() <= match.getExtraItem3Chance()) {
                                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                    if (itemHandler.isItemValid(i, extraItem3) && itemHandler.insertItem(i, extraItem3, true).isEmpty()) {
                                                        itemHandler.insertItem(i, extraItem3, false);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                            );
                        }
                    }
                }
            }
        }
    }
}
