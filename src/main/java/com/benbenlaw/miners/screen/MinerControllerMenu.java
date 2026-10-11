package com.benbenlaw.miners.screen;

import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import com.benbenlaw.core.screen.util.slot.ResultSlot;
import com.benbenlaw.miners.block.entity.MinerControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MinerControllerMenu extends SimpleAbstractContainerMenu {

    public final MinerControllerBlockEntity blockEntity;
    public final ContainerData data;

    public MinerControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, extraData.readBlockPos(), new SimpleContainerData(8));
    }

    public MinerControllerMenu(int containerId, Inventory inventory, BlockPos pos, ContainerData data) {
        super(MinersMenuTypes.MINER_CONTROLLER_MENU.get(), containerId, inventory, pos,
                MinerControllerBlockEntity.LAST_OUTPUT_SLOT + 1);

        Level level = inventory.player.level();
        this.blockEntity = (MinerControllerBlockEntity) level.getBlockEntity(pos);
        this.data = data;

        assert blockEntity != null;

        this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set,
                MinerControllerBlockEntity.FUEL_SLOT, 44, 32) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return level.fuelValues().isFuel(stack);
            }
        });

        for (int i = 0; i < 9; i++) {
            this.addSlot(new ResultSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set,
                    MinerControllerBlockEntity.FIRST_OUTPUT_SLOT + i, 116 + (i % 3) * 18, 16 + (i / 3) * 18));
        }

        addDataSlots(data);
    }

    public static final int BUTTON_SHOW_CAPS = 0;

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == BUTTON_SHOW_CAPS) {
            blockEntity.toggleCapOutlines(player);
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    public int getEfficiencyLevels() {
        return data.get(5);
    }

    public int getFortuneLevels() {
        return data.get(6);
    }

    public boolean hasSilkTouch() {
        return data.get(7) == 1;
    }

    public int getScaledProgress(int pixels) {
        int max = data.get(1);
        return max == 0 ? 0 : data.get(0) * pixels / max;
    }

    public int getScaledEnergy(int pixels) {
        int max = data.get(3);
        return max == 0 ? 0 : (int) ((long) data.get(2) * pixels / max);
    }

    public boolean isStructureValid() {
        return data.get(4) == 1;
    }
}
