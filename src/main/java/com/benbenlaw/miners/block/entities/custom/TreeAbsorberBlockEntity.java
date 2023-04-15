package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.block.ModBlocks;
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
    private boolean hasCaps = false;
    private boolean isValidStructure = false;

    public TreeAbsorberBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.TREE_ABSORBER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {

        Level pLevel = this.level;
        BlockPos blockPos = this.worldPosition;
        assert pLevel != null;
        TreeAbsorberBlockEntity entity = this;
        entity.counter++;
        int tickRate = 220;

        Block logBlock = Blocks.AIR;
        Block leafBlock = Blocks.AIR;
        ItemStack extraItem1 = null;
        double extraItem1Chance = 0.0;
        ItemStack extraItem2 = null;
        double extraItem2Chance = 0.0;
        ItemStack extraItem3 = null;
        double extraItem3Chance = 0.0;

        //Check for Cap and apply correct tickrate
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
                                hasCaps = true;
                            }
                        }
                    }
                }
            }
        }

        //Check Structure

        if (entity.counter % 5 == 0) {

            for (TreeAbsorberBlocksRecipe treeAbsorberBlocksRecipe : level.getRecipeManager().getRecipesFor(TreeAbsorberBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String logBlockString = treeAbsorberBlocksRecipe.getLogBlock();
                String leafBlockString = treeAbsorberBlocksRecipe.getLeafBlock();
                logBlock = Registry.BLOCK.get(new ResourceLocation(logBlockString));
                leafBlock = Registry.BLOCK.get(new ResourceLocation(leafBlockString));
                extraItem1 = treeAbsorberBlocksRecipe.getExtraItem1();
                extraItem1Chance = treeAbsorberBlocksRecipe.getExtraItem1Chance();
                extraItem2 = treeAbsorberBlocksRecipe.getExtraItem2();
                extraItem2Chance = treeAbsorberBlocksRecipe.getExtraItem2Chance();
                extraItem3 = treeAbsorberBlocksRecipe.getExtraItem3();
                extraItem3Chance = treeAbsorberBlocksRecipe.getExtraItem3Chance();

                if (level.getBlockState(blockPos.above(4)).is(logBlock) && level.getBlockState(blockPos.above(6)).is(leafBlock)) {
                    isValidStructure = level.getBlockState(blockPos.north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1)).is(Blocks.DIRT) && level.getBlockState(blockPos.above(2)).is(logBlock) && level.getBlockState(blockPos.above(3)).is(logBlock) && level.getBlockState(blockPos.above(4)).is(logBlock) && level.getBlockState(blockPos.above(5)).is(logBlock) && level.getBlockState(blockPos.above(4).north(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1).east(1)).is(leafBlock) &&level.getBlockState(blockPos.above(5).north(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(2)).is(leafBlock) && level.getBlockState(blockPos.above(6)).is(leafBlock) && level.getBlockState(blockPos.above(6).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(6).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(6).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(6).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(7)).is(leafBlock) && level.getBlockState(blockPos.above(7).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(7).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(7).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(7).east(1)).is(leafBlock);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, true));
                    break;
                }
            }
        }

        //Particle

        if (level.getBlockState(blockPos).is(ModBlocks.TREE_ABSORBER.get()) && MinersConfigFile.showParticlesOnMachines.get()){
            if (hasCaps) {
                level.addParticle(ParticleTypes.CRIMSON_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
            if (level.getBlockState(blockPos).getValue(TreeAbsorberBlock.LIT)) {
                level.addParticle(ParticleTypes.WARPED_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, false));
            }
        }

        //Generate Drops

        if (entity.counter % tickRate == 0){ //tickRate - 1) {

            if (level.getBlockState(blockPos).is(ModBlocks.TREE_ABSORBER.get())) {

                    if (level.getBlockState(blockPos.north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(3).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(4).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(5).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(6).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(7).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(8).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(10).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(9).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) && level.getBlockState(blockPos.above(1)).is(Blocks.DIRT) && level.getBlockState(blockPos.above(2)).is(logBlock) && level.getBlockState(blockPos.above(3)).is(logBlock) && level.getBlockState(blockPos.above(4)).is(logBlock) && level.getBlockState(blockPos.above(5)).is(logBlock) && level.getBlockState(blockPos.above(4).north(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(4).south(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1).east(1)).is(leafBlock) &&level.getBlockState(blockPos.above(5).north(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).east(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1).west(2)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(2).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(2).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(5).south(2)).is(leafBlock) && level.getBlockState(blockPos.above(6)).is(leafBlock) && level.getBlockState(blockPos.above(6).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(6).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(6).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(6).east(1)).is(leafBlock) && level.getBlockState(blockPos.above(7)).is(leafBlock) && level.getBlockState(blockPos.above(7).north(1)).is(leafBlock) && level.getBlockState(blockPos.above(7).south(1)).is(leafBlock) && level.getBlockState(blockPos.above(7).west(1)).is(leafBlock) && level.getBlockState(blockPos.above(7).east(1)).is(leafBlock)){

                        if (level.getBlockEntity(blockPos.below()) != null) {

                            BlockEntity ent = level.getBlockEntity(blockPos.below());
                            assert ent != null;

                            ItemStack finalExtraItem1 = extraItem1;
                            double finalExtraItem1Chance = extraItem1Chance;
                            ItemStack finalExtraItem2 = extraItem2;
                            double finalExtraItem2Chance = extraItem2Chance;
                            ItemStack finalExtraItem3 = extraItem3;
                            double finalExtraItem3Chance = extraItem3Chance;
                            Block finalLogBlock = logBlock;
                            Block finalLeafBlock = leafBlock;

                            ent.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {

                                        ItemStack logAsItem = new ItemStack(finalLogBlock.asItem());
                                        ItemStack leafAsItem = new ItemStack(finalLeafBlock.asItem());

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


                                       // if (finalExtraItem1 !=null) {
                                            if (Math.random() <= finalExtraItem1Chance) {
                                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                    if (itemHandler.isItemValid(i, finalExtraItem1) && itemHandler.insertItem(i, finalExtraItem1, false).isEmpty()) {
                                                        itemHandler.insertItem(i, finalExtraItem1, false);
                                                        break;
                                                    }
                                                }
                                            }
                                   //     }
                                   //     if (finalExtraItem2 !=null) {
                                            if (Math.random() <= finalExtraItem2Chance) {
                                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                    if (itemHandler.isItemValid(i, finalExtraItem2) && itemHandler.insertItem(i, finalExtraItem2, false).isEmpty()) {
                                                        itemHandler.insertItem(i, finalExtraItem2, false);
                                                        break;
                                                    }
                                                }
                                            }
                                    //    }
                                   //     if (finalExtraItem3 !=null) {
                                            if (Math.random() <= finalExtraItem3Chance) {
                                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                    if (itemHandler.isItemValid(i, finalExtraItem3) && itemHandler.insertItem(i, finalExtraItem3, false).isEmpty()) {
                                                        itemHandler.insertItem(i, finalExtraItem3, false);
                                                        break;
                                                    }
                                                }
                                            }
                                  //      }
                                    }
                            );
                        }
                    }
                }
            }
        }
    }
