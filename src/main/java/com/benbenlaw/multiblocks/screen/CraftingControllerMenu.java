package com.benbenlaw.multiblocks.screen;

import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import com.benbenlaw.core.screen.util.slot.ResultSlot;
import com.benbenlaw.multiblocks.block.entity.CraftingControllerBlockEntity;
import com.benbenlaw.multiblocks.config.MultiblocksConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Slot positions follow crafter_gui.png (176 x 196). */
public class CraftingControllerMenu extends SimpleAbstractContainerMenu {

    public static final int IMAGE_WIDTH = 176;
    public static final int IMAGE_HEIGHT = 196;

    public static final int GRID_X = 29;
    public static final int GRID_Y = 16;
    public static final int OUTPUT_X = 116;
    public static final int OUTPUT_Y = 16;
    public static final int RESEARCH_X = 8;
    public static final int RESEARCH_Y = 83;
    public static final int FUEL_X = 92;
    public static final int FUEL_Y = 52;
    public static final int PLAYER_INVENTORY_Y = 114;
    public static final int HOTBAR_Y = 172;

    public static final int BUTTON_TOGGLE_LOCK = 0;

    /** The grid and research slots by inventory index, so the screen can show what a locked slot accepts. */
    public final Slot[] lockableSlots = new Slot[CraftingControllerBlockEntity.LOCKABLE_SLOTS];

    public final CraftingControllerBlockEntity blockEntity;
    public final ContainerData data;

    public CraftingControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, extraData.readBlockPos(), new SimpleContainerData(7));
    }

    public CraftingControllerMenu(int containerId, Inventory inventory, BlockPos pos, ContainerData data) {
        super(MultiblocksMenuTypes.CRAFTING_CONTROLLER_MENU.get(), containerId, inventory, pos,
                CraftingControllerBlockEntity.SLOT_COUNT - (MultiblocksConfig.CRAFTING_FUEL_SLOT.get() ? 0 : 1));

        Level level = inventory.player.level();
        this.blockEntity = (CraftingControllerBlockEntity) level.getBlockEntity(pos);
        this.data = data;

        assert blockEntity != null;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                int index = CraftingControllerBlockEntity.GRID_START + column + row * 3;
                lockableSlots[index] = this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set,
                        index, GRID_X + column * 18, GRID_Y + row * 18));
            }
        }

        for (int i = 0; i < CraftingControllerBlockEntity.EXTRA_SIZE; i++) {
            int index = CraftingControllerBlockEntity.EXTRA_START + i;
            lockableSlots[index] = this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set,
                    index, RESEARCH_X + i * 18, RESEARCH_Y));
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                this.addSlot(new ResultSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set,
                        CraftingControllerBlockEntity.OUTPUT_START + column + row * 3, OUTPUT_X + column * 18, OUTPUT_Y + row * 18));
            }
        }

        if (MultiblocksConfig.CRAFTING_FUEL_SLOT.get()) {
            this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set,
                    CraftingControllerBlockEntity.FUEL_SLOT, FUEL_X, FUEL_Y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return level.fuelValues().isFuel(stack);
                }
            });
        }

        addDataSlots(data);
    }

    @Override
    protected void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9,
                        8 + column * 18, PLAYER_INVENTORY_Y + row * 18));
            }
        }
    }

    @Override
    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, HOTBAR_Y));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == BUTTON_TOGGLE_LOCK) {
            blockEntity.toggleLock();
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    public boolean isLocked() {
        return data.get(6) == 1;
    }

    public int getScaledProgress(int pixels) {
        int max = data.get(1);
        return max == 0 ? 0 : data.get(0) * pixels / max;
    }

    public int getScaledEnergy(int pixels) {
        int max = data.get(3);
        return max == 0 ? 0 : (int) ((long) data.get(2) * pixels / max);
    }

    /** One of the CraftingControllerBlockEntity.STRUCTURE_* values. */
    public int getStructureState() {
        return data.get(4);
    }

    public int getEfficiencyLevels() {
        return data.get(5);
    }
}
