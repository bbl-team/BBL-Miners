package com.benbenlaw.miners.block.entities.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.block.custom.FluidAbsorberBlock;
import com.benbenlaw.miners.block.custom.MinerBaseBlock;
import com.benbenlaw.miners.block.entities.ModBlockEntities;
import com.benbenlaw.miners.recipe.CapBlocksRecipe;
import com.benbenlaw.miners.recipe.FluidAbsorberRecipe;
import com.benbenlaw.miners.recipe.MinerBlocksRecipe;
import com.benbenlaw.miners.recipe.NoInventoryRecipe;
import com.benbenlaw.miners.util.ModTags;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class FluidAbsorberBlockEntity extends BlockEntity {

    // Add a counter variable
    private int counter = 0;
    private static boolean isValidStructure = false;

    public FluidAbsorberBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.FLUID_ABSORBER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(@NotNull Level level, @NotNull BlockPos blockPos, BlockState blockState, @NotNull FluidAbsorberBlockEntity entity) {
        // Increment the counter
        entity.counter++;
        int tickRate = 220;

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
                            }
                        }
                    }
                }
            }
        }

        //CHECK STRUCTURE

        if (entity.counter % 20 == 0) {

            Optional<FluidAbsorberRecipe> fluidAbsorberRecipe = level.getRecipeManager()
                    .getRecipeFor(FluidAbsorberRecipe.Type.INSTANCE,  NoInventoryRecipe.INSTANCE, level);

            String fluidString = fluidAbsorberRecipe.get().getFluid();
            Fluid fluidStack = Registry.FLUID.get(new ResourceLocation(fluidString));
            Fluid fluidInWorld = level.getFluidState(blockPos.below()).getType();

            if (fluidStack == fluidInWorld) {
                isValidStructure = level.getBlockState(blockPos.north(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).north(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(2).north(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(3).north(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(4).north(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.north(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).north(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(2).north(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(3).north(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(4).north(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.south(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).south(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(2).south(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(3).south(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(4).south(2).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.south(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).south(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(2).south(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(3).south(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(4).south(2).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).north(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).north(2).east()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).north(2).west()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).south(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).south(2).east()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).south(2).west()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).east(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).east(2).north()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).east(2).south()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).west(2)).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).west(2).north()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getBlockState(blockPos.below(1).west(2).south()).is(ModBlocks.IRON_SUPPORT_FRAME.get()) && level.getFluidState(blockPos.below(1)).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).north()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).north().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).north().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).south()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).south().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(1).south().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2)).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).north()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).north().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).north().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).south()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).south().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(2).south().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3)).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).north()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).north().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).north().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).south()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).south().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(3).south().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4)).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).north()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).north().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).north().west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).west()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).south()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).south().east()).isSourceOfType(fluidStack) && level.getFluidState(blockPos.below(4).south().west()).isSourceOfType(fluidStack);
            }
        }

        //Particles

        if (level.getBlockState(blockPos).is(ModBlocks.FLUID_ABSORBER.get())) {

            if (tickRate < 201 && blockState.getValue(MinerBaseBlock.LIT)) {
                level.addParticle(ParticleTypes.CRIMSON_SPORE, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.5D, 0.5D, 0.5D);
            }
            if (blockState.getValue(MinerBaseBlock.LIT)) {
                level.addParticle(ParticleTypes.WARPED_SPORE, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.5D, 0.5D, 0.5D);
            }
            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidAbsorberBlock.LIT, false));
            }

            //Produce Items

            if (entity.counter % tickRate == 0) {

                if (level.getBlockState(blockPos).is(ModBlocks.FLUID_ABSORBER.get())) {

                    if (isValidStructure) {

                        if (level.getBlockEntity(blockPos.above()) != null) {

                            level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidAbsorberBlock.LIT, true));

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
                }
            }
        }
    }
}

