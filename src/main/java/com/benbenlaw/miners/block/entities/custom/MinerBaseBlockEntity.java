package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.block.custom.FluidAbsorberBlock;
import com.benbenlaw.miners.block.custom.MinerBaseBlock;
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
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;


public class MinerBaseBlockEntity extends BlockEntity {

    // Add a counter variable
    private int counter = 0;
    private boolean hasCaps = false;
    private boolean isValidStructure = false;

    public MinerBaseBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.MINER_BASE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {

        Level pLevel = this.level;
        BlockPos blockPos = this.worldPosition;
        assert pLevel != null;
        MinerBaseBlockEntity entity = this;
        entity.counter++;
        int tickRate = 220;
        TagKey<Block> blockTag2 = ModTags.Blocks.EMPTY;
        TagKey<Block> minerTierSupportFrame = ModTags.Blocks.EMPTY;

        //Check for Cap and apply correct tickrate
        if (!level.getBlockState(blockPos.above(1).south(2).west(2)).is(Blocks.AIR) && !level.getBlockState(blockPos.above(1).south(2).east(2)).is(Blocks.AIR) &&
                !level.getBlockState(blockPos.above(1).north(2).east(2)).is(Blocks.AIR) && !level.getBlockState(blockPos.above(1).north(2).west(2)).is(Blocks.AIR)) {

            for (CapBlocksRecipe match : level.getRecipeManager().getRecipesFor(CapBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName = match.getBlock();
                Block speedBlock = Registry.BLOCK.get(new ResourceLocation(blockName));
                TagKey<Block> blockTag = BlockTags.create(new ResourceLocation(blockName));

                if (level.getBlockState(blockPos.above(1).south(2).west(2)).is(speedBlock) ||
                        level.getBlockState(blockPos.above(1).south(2).west(2)).getBlockHolder().is(blockTag)) {

                    if (level.getBlockState(blockPos.above(1).south(2).east(2)).is(speedBlock) ||
                            level.getBlockState(blockPos.above(1).south(2).east(2)).getBlockHolder().is(blockTag)) {

                        if (level.getBlockState(blockPos.above(1).north(2).east(2)).is(speedBlock) ||
                                level.getBlockState(blockPos.above(1).north(2).east(2)).getBlockHolder().is(blockTag)) {

                            if (level.getBlockState(blockPos.above(1).north(2).west(2)).is(speedBlock) ||
                                    level.getBlockState(blockPos.above(1).north(2).west(2)).is(blockTag)) {
                                tickRate = match.getTickRate();
                                hasCaps = true;
                            }
                        }
                    }
                }
            }
        }

        //CHECK STRUCTURE

        if (entity.counter % 5 == 0) {

            for (MinerBlocksRecipe minerBlocks : level.getRecipeManager().getRecipesFor(MinerBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName2 = minerBlocks.getBlock();
                blockTag2 = BlockTags.create(new ResourceLocation(blockName2));
                int minerTier = minerBlocks.getMinerTier();

                minerTierSupportFrame = MinerTier.values()[minerTier - 1].getSupportFrame();

                if (level.getBlockState(blockPos.below()).getBlockHolder().containsTag(blockTag2)) {
                    isValidStructure = level.getBlockState(blockPos.below(1)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).east()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).west()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).east()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).west()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).east(2).north()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).east(2).south()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).west(2).north()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).west(2).south()).getBlockHolder().containsTag(minerTierSupportFrame);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(MinerBaseBlock.LIT, true));
                    break;
                }
            }
        }
        //Particle

        if (level.getBlockState(blockPos).is(ModBlocks.MINER_BASE_BLOCK.get()) && MinersConfigFile.showParticlesOnMachines.get()) {

            if (hasCaps) {
                level.addParticle(ParticleTypes.CRIMSON_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
            if (level.getBlockState(blockPos).getValue(TreeAbsorberBlock.LIT)) {
                level.addParticle(ParticleTypes.WARPED_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(MinerBaseBlock.LIT, false));
            }
        }

        //Generate Drops

        if (entity.counter % tickRate == 0) {

            if (level.getBlockState(blockPos).is(ModBlocks.MINER_BASE_BLOCK.get())) {

                if (level.getBlockState(blockPos.below(1)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4)).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).north()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).south()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).south().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).north().east()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).south().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(1).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(2).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(3).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.below(4).north().west()).getBlockHolder().containsTag(blockTag2) && level.getBlockState(blockPos.north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).north(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).north(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).south(2).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(2).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(3).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(4).south(2).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).east()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).north(2).west()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).east()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).south(2).west()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).east(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).east(2).north()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).east(2).south()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).west(2)).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).west(2).north()).getBlockHolder().containsTag(minerTierSupportFrame) && level.getBlockState(blockPos.below(1).west(2).south()).getBlockHolder().containsTag(minerTierSupportFrame)) {

                    if (level.getBlockEntity(blockPos.above()) != null) {
                        BlockEntity ent = level.getBlockEntity(blockPos.above());
                        Block blockAbove = level.getBlockState(blockPos.below()).getBlock();
                        assert ent != null;

                        ent.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {
                            ItemStack stack = new ItemStack(blockAbove.asItem());

                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                if (itemHandler.isItemValid(i, stack) && itemHandler.insertItem(i, stack, true).isEmpty()) {
                                    itemHandler.insertItem(i, stack, false);
                                    break;
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}

