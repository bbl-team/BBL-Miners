package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.block.custom.TreeAbsorberBlock;
import com.benbenlaw.miners.block.entities.ModBlockEntities;
import com.benbenlaw.miners.config.MinersConfigFile;
import com.benbenlaw.miners.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
        // Increment the counter
        entity.counter++;

        // Only execute the rest of the code if the counter is a multiple of 20

        int tickRate = 220;

        //Check For Caps

        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP)) {

            tickRate = 200;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP)) {

            tickRate = 180;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP)) {

            tickRate = 160;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP)) {

            tickRate = 140;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP)) {

            tickRate = 120;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP)) {

            tickRate = 100;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP)) {

            tickRate = 80;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP)) {

            tickRate = 60;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP)) {

            tickRate = 40;
        }
        if (level.getBlockState(blockPos.above(11).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(11).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP)) {

            tickRate = 20;
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

                //CHECK THE STRUCTURE

                if (
                        //FRAMES

                        level.getBlockState(blockPos.north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(1).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(1).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(1).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(1).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(3).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(3).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(3).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(3).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(4).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(4).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(4).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(4).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(5).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(5).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(5).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(5).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(6).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(6).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(6).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(6).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(7).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(7).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(7).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(7).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(8).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(8).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(8).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(8).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(10).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(10).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(10).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(10).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        level.getBlockState(blockPos.above(9).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&
                        level.getBlockState(blockPos.above(9).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_FRAME) &&

                        //TREE LAYER LOGS

                        level.getBlockState(blockPos.above(1)).is(Blocks.DIRT) &&
                        level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(BlockTags.LOGS) &&
                        level.getBlockState(blockPos.above(3)).getBlockHolder().containsTag(BlockTags.LOGS) &&
                        level.getBlockState(blockPos.above(4)).getBlockHolder().containsTag(BlockTags.LOGS) &&
                        level.getBlockState(blockPos.above(5)).getBlockHolder().containsTag(BlockTags.LOGS) &&

                        //TREE LAYER LEAVES 1

                        level.getBlockState(blockPos.above(4).north(2).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).north(2).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).north(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(4).north(1).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).north(1).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).north(1).east(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).north(1).west(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).north(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(4).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).east(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).west(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(4).south(1).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).south(1).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).south(1).east(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).south(1).west(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).south(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(4).south(2).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).south(2).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(4).south(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        //TREE LAYER LEAVES 2

                        level.getBlockState(blockPos.above(5).north(2).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).north(2).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).north(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(5).north(1).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).north(1).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).north(1).east(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).north(1).west(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).north(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(5).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).east(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).west(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(5).south(1).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).south(1).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).south(1).east(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).south(1).west(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).south(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        level.getBlockState(blockPos.above(5).south(2).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).south(2).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(5).south(2)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        //TREE LAYER LEAVES 3

                        level.getBlockState(blockPos.above(6)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(6).north(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(6).south(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(6).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(6).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&

                        //TREE LAYER LEAVES 4

                        level.getBlockState(blockPos.above(7)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(7).north(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(7).south(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(7).west(1)).getBlockHolder().containsTag(BlockTags.LEAVES) &&
                        level.getBlockState(blockPos.above(7).east(1)).getBlockHolder().containsTag(BlockTags.LEAVES)

                ) {

                    BlockState notWorkingState = level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, false);
                    level.setBlockAndUpdate(blockPos, notWorkingState);

                    if (level.getBlockEntity(blockPos.below()) != null) {

                        BlockState workingState = level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, true);
                        level.setBlockAndUpdate(blockPos, workingState);
                        level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1, 1, false);

                        BlockEntity ent = level.getBlockEntity(blockPos.below());
                        Block blockAboveLog = level.getBlockState(blockPos.above(2)).getBlock();
                        Block blockAboveLeaves = level.getBlockState(blockPos.above(6)).getBlock();
                        assert ent != null;
                        ent.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {

                                    ItemStack log = new ItemStack(blockAboveLog.asItem());
                                    ItemStack leaves = new ItemStack(blockAboveLeaves.asItem());
                                    ItemStack sticks = new ItemStack(Items.STICK);

                                    //Wood

                                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                                        if (itemHandler.isItemValid(i, log) && itemHandler.insertItem(i, log, true).isEmpty()) {
                                            itemHandler.insertItem(i, log, false);
                                            break;
                                        }
                                    }

                                    //Leaves

                                    if (Math.random() > MinersConfigFile.leavesDropChanceFromTreeAbsorber.get()) {
                                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                                            if (itemHandler.isItemValid(i, leaves) && itemHandler.insertItem(i, leaves, true).isEmpty()) {
                                                itemHandler.insertItem(i, leaves, false);
                                                break;
                                            }
                                        }
                                    }

                                    //Sticks

                                    if (Math.random() > MinersConfigFile.sticksDropChanceFromTreeAbsorber.get()) {
                                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                                            if (itemHandler.isItemValid(i, sticks) && itemHandler.insertItem(i, sticks, true).isEmpty()) {
                                                itemHandler.insertItem(i, sticks, false);
                                                break;
                                            }
                                        }
                                    }

                                    //Oak Drops Apples

                                    if (level.getBlockState(blockPos.above(6)).getBlock().equals(Blocks.OAK_LEAVES)) {
                                        if (Math.random() > MinersConfigFile.applesDropChanceFromTreeAbsorberOakTrees.get()) {
                                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                if (itemHandler.isItemValid(i, new ItemStack(Items.APPLE)) && itemHandler.insertItem(i, new ItemStack(Items.APPLE), true).isEmpty()) {
                                                    itemHandler.insertItem(i, new ItemStack(Items.APPLE), false);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    //Jungle Drops Cocoa Beans

                                    if (level.getBlockState(blockPos.above(6)).getBlock().equals(Blocks.JUNGLE_LEAVES)) {
                                        if (Math.random() > MinersConfigFile.cocoaDropChanceFromTreeAbsorberJungleTrees.get()) {
                                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                                if (itemHandler.isItemValid(i, new ItemStack(Items.COCOA_BEANS)) && itemHandler.insertItem(i, new ItemStack(Items.COCOA_BEANS), true).isEmpty()) {
                                                    itemHandler.insertItem(i, new ItemStack(Items.COCOA_BEANS), false);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                }


                        );
                    }
                }
                else {
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(TreeAbsorberBlock.LIT, false));
                }
            }
        }
    }
}
