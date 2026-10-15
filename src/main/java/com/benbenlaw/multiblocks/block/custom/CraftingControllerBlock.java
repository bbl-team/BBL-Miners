package com.benbenlaw.multiblocks.block.custom;

import com.benbenlaw.core.block.SyncableBlock;
import com.benbenlaw.multiblocks.block.MultiblocksBlockEntities;
import com.benbenlaw.multiblocks.block.entity.CraftingControllerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class CraftingControllerBlock extends SyncableBlock {

    public static final MapCodec<CraftingControllerBlock> CODEC = simpleCodec(CraftingControllerBlock::new);

    public CraftingControllerBlock(Properties properties) {
        super(properties);
    }

    /** Whether the GUI has a fuel slot that turns burnable items into energy. The built-in controller follows the config. */
    public boolean hasFuelSlot() {
        return com.benbenlaw.multiblocks.config.MultiblocksConfig.CRAFTING_FUEL_SLOT.get();
    }

    /** The name of this controller's JEI category, or null to use the block's name. */
    public @Nullable Component jeiTitle() {
        return null;
    }

    /** What the JEI category shows as its icon, or null to use this block. */
    public @Nullable ItemLike jeiIcon() {
        return null;
    }

    @Override
    public @NonNull MapCodec<CraftingControllerBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                                        @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof CraftingControllerBlockEntity controller) {
            player.openMenu(new SimpleMenuProvider(controller, controller.getDisplayName()), pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CraftingControllerBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(),
                (thisLevel, thisPos, thisState, thisEntity) -> thisEntity.tick());
    }
}
