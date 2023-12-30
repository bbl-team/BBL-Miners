package com.benbenlaw.miners.block.entities;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.core.MultiblockChecker;
import com.benbenlaw.miners.core.MultiblockManagers;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class FormBlockEntity extends BlockEntity {

    public static BlockPattern getMinerPattern(Block oreBlock) {
        return BlockPatternBuilder.start()
                .aisle("0   0", "     ", "  *  ", "     ", "0   0")
                .aisle("00000", "0KKK0", "0KKK0", "0KKK0", "00000")
                .aisle("0   0", " KKK ", " KKK ", " KKK ", "0   0")
                .aisle("0   0", " KKK ", " KKK ", " KKK ", "0   0")
                .aisle("0   0", " KKK ", " KKK ", " KKK ", "0   0")
                .where('0', a -> a.getState().is(Blocks.IRON_BLOCK))
                .where('*', a -> a.getState().is(Miners.FORM_BLOCK.get()))
                .where('K', a -> a.getState().is(oreBlock))
                .where(' ', a -> a.getState().is(Blocks.AIR))
                .build();
    }

    public static BlockPattern getSpeedCapPattern(Block block) {
        return BlockPatternBuilder.start()
                .aisle("0   0", "     ", "     ", "     ", "0   0")
                .where('0', a -> a.getState().is(block))
                .build();
    }


    public static int getSpeedReduction(BlockState blockState) {
        if (blockState.is(Blocks.BEACON)) return 210;
        return 0;
    }

    public static void insertItemStackIntoHandler(IItemHandler itemHandler, ItemStack itemStack, Consumer<ItemStack> remainder) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            itemStack = itemHandler.insertItem(slot, itemStack, false);

            if (itemStack.isEmpty()) {
                break;
            }
        }

        // If there are remaining items, drop them in the world
        if (!itemStack.isEmpty()) {
            remainder.accept(itemStack);
        }
    }

    private int counter = 0;
    private final MultiblockChecker minerCheck = new MultiblockChecker();
    private final MultiblockChecker speedCapCheck = new MultiblockChecker();



    public FormBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(Miners.FORM_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    public void spawnItem(ItemStack stack) {
        if (level == null || level.isClientSide) return;
        ItemEntity itemAsEntity = new ItemEntity(level,
                ((level.random.nextFloat() * 0.1) + 0.5) + this.worldPosition.getX(),
                ((level.random.nextFloat() * 0.1) + 0.5) + this.worldPosition.getY(),
                ((level.random.nextFloat() * 0.1) + 0.5) + this.worldPosition.getZ(),
                stack
        );

        itemAsEntity.setDefaultPickUpDelay();
        itemAsEntity.setDeltaMovement((level.random.nextFloat() * 0.1 - 0.05), (level.random.nextFloat() * 0.1 - 0.03), (level.random.nextFloat() * 0.1 - 0.05));
        level.addFreshEntity(itemAsEntity);
    }



    public void tick() {
        if (getLevel() == null) return;

        FormBlockEntity entity = this;
        entity.counter++;
        int tickRate = 220;

        if (!level.isClientSide()) {
            // Check Speed cap (if minerCheck is valid still)

            var lastMatch = minerCheck.getMatch();
            if (lastMatch != null) {
                var capBlock = getLevel().getBlockState(lastMatch.getFrontTopLeft().above()); // above is where the CapBlock will sit
                var valid = speedCapCheck.isValid(getLevel(), getBlockPos(), () -> getSpeedCapPattern(capBlock.getBlock()));
                if (valid != null) tickRate = tickRate - getSpeedReduction(capBlock);
            }

            if (entity.counter % tickRate == 0) {
                Block block = getLevel().getBlockState(getBlockPos().below()).getBlock();
                var resultItem = block.asItem();
                var result = minerCheck.isValid(getLevel(), getBlockPos(), () -> getMinerPattern(block));

                if (result != null) {

                    var item = new ItemStack(resultItem);
                    var be = getLevel().getBlockEntity(getBlockPos().above());
                    if (be != null) {
                        AtomicBoolean sent = new AtomicBoolean(false);
                        be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
                               sent.set(true);
                               insertItemStackIntoHandler(iItemHandler, item, this::spawnItem);
                        });
                        if (!sent.get()) spawnItem(item);
                    } else {
                        spawnItem(item);
                    }
                } else {

                    //   player.sendSystemMessage(Component.literal("Found no structure!"));
                }
            }

        }

        if (getLevel() == null) return;
    }
}

