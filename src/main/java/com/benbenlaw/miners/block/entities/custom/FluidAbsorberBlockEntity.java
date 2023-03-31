package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.block.custom.FluidAbsorberBlock;
import com.benbenlaw.miners.block.entities.ModBlockEntities;
import com.benbenlaw.miners.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;


public class FluidAbsorberBlockEntity extends BlockEntity {

    // Add a counter variable
    private int counter = 0;

    public FluidAbsorberBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.FLUID_ABSORBER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(@NotNull Level level, @NotNull BlockPos blockPos, BlockState blockState, @NotNull FluidAbsorberBlockEntity entity) {
        // Increment the counter
        entity.counter++;

        // Only execute the rest of the code if the counter is a multiple of 20

        int tickRate = 220;

        //Check For Caps

        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.WOODEN_SUPPORT_CAP)) {

            tickRate = 200;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.STONE_SUPPORT_CAP)) {

            tickRate = 180;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.LAPIS_SUPPORT_CAP)) {

            tickRate = 160;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.COPPER_SUPPORT_CAP)) {

            tickRate = 140;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_CAP)) {

            tickRate = 120;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.GOLD_SUPPORT_CAP)) {

            tickRate = 100;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.DIAMOND_SUPPORT_CAP)) {

            tickRate = 80;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.EMERALD_SUPPORT_CAP)) {

            tickRate = 60;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.NETHERITE_SUPPORT_CAP)) {

            tickRate = 40;
        }
        if (level.getBlockState(blockPos.above(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP) &&
                level.getBlockState(blockPos.above(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.ULTIMATE_SUPPORT_CAP)) {

            tickRate = 20;
        }

        if (level.getBlockState(blockPos).is(ModBlocks.FLUID_ABSORBER.get())) {

            if (tickRate < 201) {
                level.addParticle(ParticleTypes.CRIMSON_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }

            if (level.getBlockState(blockPos).getValue(FluidAbsorberBlock.LIT)) {
                level.addParticle(ParticleTypes.WARPED_SPORE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
        }
        if (entity.counter % tickRate == 0) {

            if (level.getBlockState(blockPos).is(ModBlocks.FLUID_ABSORBER.get())) {

                //CHECK THE STRUCTURE



                if (
                        //FRAMES


                        level.getBlockState(blockPos.north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.above(1).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.above(1).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.above(1).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.above(1).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&


                                level.getBlockState(blockPos.below(1).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(1).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(1).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(1).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(2).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(2).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(2).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(2).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(3).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(3).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(3).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(3).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(4).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(4).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(4).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(4).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(5).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(5).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(5).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(5).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).north(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).north(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).north(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).north(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).south(1).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(1).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).south(2).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(2).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                                level.getBlockState(blockPos.below(6).south(3).east(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(3).east(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(3).east(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(3).west(1)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(3).west(2)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&
                                level.getBlockState(blockPos.below(6).south(3).west(3)).getBlockHolder().containsTag(ModTags.Blocks.IRON_SUPPORT_FRAME) &&

                        //WATER LEVEL

                                level.getFluidState(blockPos.below(1).south(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(1).south(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).south(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(1).north(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(1).north(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(1).north(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(2).south(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(2).south(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).south(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(2).north(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(2).north(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(2).north(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(3).south(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(3).south(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).south(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(3).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(3).north(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(3).north(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(3).north(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(4).south(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(4).south(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).south(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(4).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(4).north(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(4).north(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(4).north(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(5).south(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(5).south(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).south(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&

                                level.getFluidState(blockPos.below(5).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(5).north(1).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(1).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(1).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(1).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(5).north(2).west(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(2).west(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(2).east(1)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                level.getFluidState(blockPos.below(5).north(2).east(2)).is(ModTags.Fluids.FLUID_ABSORBER_FLUID) &&
                                
                                level.getFluidState(blockPos.below(1).south(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(2).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(1).south(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).south(1).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(1).north(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(1).east(2)).isSource() &&   
                                
                                level.getFluidState(blockPos.below(1).north(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(2)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(1).north(2).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(2).south(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(2).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(2).south(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).south(1).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(2).north(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(1).east(2)).isSource() &&   
                                
                                level.getFluidState(blockPos.below(2).north(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(2)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(2).north(2).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(3).south(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(2).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(3).south(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).south(1).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(3).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(3)).isSource() &&
                                level.getFluidState(blockPos.below(3).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(3).north(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(1).east(2)).isSource() &&   
                                
                                level.getFluidState(blockPos.below(3).north(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(2)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(3).north(2).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(4).south(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(2).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(4).south(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).south(1).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(4).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(4)).isSource() &&
                                level.getFluidState(blockPos.below(4).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(4).north(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(1).east(2)).isSource() &&   
                                
                                level.getFluidState(blockPos.below(4).north(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(2)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(4).north(2).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(5).south(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(2).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(5).south(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).south(1).east(2)).isSource() &&

                                level.getFluidState(blockPos.below(5).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(5)).isSource() &&
                                level.getFluidState(blockPos.below(5).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).east(2)).isSource() &&
                                
                                level.getFluidState(blockPos.below(5).north(1).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(1).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(1).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(1).east(2)).isSource() &&   
                                
                                level.getFluidState(blockPos.below(5).north(2).west(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(2).west(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(2)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(2).east(1)).isSource() &&
                                level.getFluidState(blockPos.below(5).north(2).east(2)).isSource()


                )
                {

                    BlockState notWorkingState = level.getBlockState(blockPos).setValue(FluidAbsorberBlock.LIT, false);
                    level.setBlockAndUpdate(blockPos, notWorkingState);

                    if (level.getBlockEntity(blockPos.above()) != null) {

                        BlockState workingState = level.getBlockState(blockPos).setValue(FluidAbsorberBlock.LIT, true);
                        level.setBlockAndUpdate(blockPos, workingState);
                        level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1, 1, false);

                        BlockEntity ent = level.getBlockEntity(blockPos.above());
                        FluidState fluidState = level.getFluidState(blockPos.below());
                        assert ent != null;

                        ent.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN).ifPresent(iFluidHandler -> {
                            FluidStack fluid = new FluidStack(fluidState.getType(), 100);
                            iFluidHandler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                        });
                    }
                }
                else {
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidAbsorberBlock.LIT, false));
                }
            }
        }
    }
}
