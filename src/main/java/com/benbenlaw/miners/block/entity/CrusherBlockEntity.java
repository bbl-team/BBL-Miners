package com.benbenlaw.miners.block.entity;

import com.benbenlaw.miners.multiblock.Crusher;
import com.benbenlaw.miners.multiblock.MultiBlockManagers;
import com.benbenlaw.miners.networking.ModMessages;
import com.benbenlaw.miners.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.miners.recipe.CrusherRecipe;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.screen.CrusherMenu;
import com.benbenlaw.miners.util.ModEnergyStorage;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.UpgradeRecipeUtil;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrusherBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }

        }


    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> i == 6 && itemHandler.isItemValid(6, s))),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> i == 6 && itemHandler.isItemValid(6, s))),

                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> i == 6 && itemHandler.isItemValid(6, s))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> i == 6 && itemHandler.isItemValid(6, s))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> i == 6 && itemHandler.isItemValid(6, s))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> i == 6 && itemHandler.isItemValid(6, s)))




            );

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    double durationMultiplier = 1.0;
    double RFPerTickMultiplier = 1.0;
    int outputRuns = 0;
    ItemStack upgradeItem = ItemStack.EMPTY;

    protected final ContainerData data;
    public int progress;
    public int maxProgress;
    public ItemStack output;
    public NonNullList<Ingredient> input;
    public final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    public int RFPerTick;
    public int fuelDuration = 0;
    final int maxEnergyStorage = 1000000;
    final int maxEnergyTransfer = 1000000;
    public int maxTransferPerTick = 0;
    public String pattern;
    public boolean hasStructure;
    public boolean hasFuel;
    public boolean hasEnoughPowerStorageAvailable;
    public int tickCounter = 0;
    public int tickBeforeCheck = 10; // ticks before checking for structure again

    public ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(maxEnergyStorage, maxEnergyTransfer) {

            @Override
            public boolean canReceive() {
                return true;
            }

            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }


    public String getPattern() {
        return pattern;
    }

    public boolean getHasStructure() {
        return hasStructure;
    }

    public boolean getHasFuel() {
        return hasStructure;
    }

    public boolean hasEnoughPowerStorageAvailable() {
        return hasStructure;
    }


    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getRFPerTick() {
        return RFPerTick;
    }

    @Override
    public void setHandler(ItemStackHandler handler) {
        copyHandlerContents(handler);
    }

    private void copyHandlerContents(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CRUSHER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            public int getCount() {
                return 1; //Change to 1 from 2 to fix log spam not sure why it causes spam though

            }
        };
    }

    private void updateUpgrades(@NotNull CrusherBlockEntity entity) {

        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        assert level != null;
        List<UpgradeRecipeUtil> upgradeRecipe = level.getRecipeManager().getAllRecipesFor(UpgradeRecipeUtil.Type.INSTANCE);

        List<UpgradeRecipeUtil> matchingUpgradeRecipes = upgradeRecipe.stream()
                .filter(recipe -> recipe.matches(inventory, level))
                .collect(Collectors.toList());


        if (!matchingUpgradeRecipes.isEmpty()) {
            for (UpgradeRecipeUtil matchingUpgrade : matchingUpgradeRecipes) {
                if (itemHandler.getStackInSlot(7).is(matchingUpgrade.getUpgradeItem().getItem())) {
                    durationMultiplier = matchingUpgrade.getDurationMultiplier();
                    RFPerTickMultiplier = matchingUpgrade.getRFPerTick();
                    outputRuns = matchingUpgrade.getOutputIncreaseAmount();
                    upgradeItem = matchingUpgrade.getUpgradeItem();
                    break;
                }
            }
        }

        if (itemHandler.getStackInSlot(7).isEmpty()) {
            upgradeItem = ItemStack.EMPTY;
            durationMultiplier = 1.0;
            outputRuns = 0;
            RFPerTickMultiplier = 1.0;
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Crusher");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new CrusherMenu(containerID, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return directionWrappedHandlerMap.get(side).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        for (Direction dir : Direction.values()) {
            if (directionWrappedHandlerMap.containsKey(dir)) {
                directionWrappedHandlerMap.get(dir).invalidate();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("crusher_generator.progress", progress);
        tag.putInt("crusher_generator.maxProgress", maxProgress);
        tag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        tag.putInt("current_tick", tickCounter);
        tag.putInt("RFPerTick", RFPerTick);
        tag.putInt("fuelDuration", fuelDuration);
        tag.putString("pattern", Objects.requireNonNullElse(pattern, ""));


        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("crusher_generator.progress");
        maxProgress = tag.getInt("crusher_generator.maxProgress");
        ENERGY_STORAGE.setEnergy(tag.getInt("energy"));
        tickCounter = tag.getInt("current_tick");
        RFPerTick = tag.getInt("RFPerTick");
        fuelDuration = tag.getInt("fuelDuration");
        pattern = tag.getString("pattern");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick() {

        tickCounter++;

        updateUpgrades(this);

        assert level != null;
        if (!level.isClientSide()) {

            if (tickCounter % tickBeforeCheck == 0) {
                var result = MultiBlockManagers.CRUSHER.findStructure(level, this.worldPosition, Rotation.NONE);

                if (result != null) {

                    String foundPattern = result.ID();
                    SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
                    for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                        inventory.setItem(i, this.itemHandler.getStackInSlot(i));
                    }
                    assert level != null;

                    for (CrusherRecipe recipe : level.getRecipeManager().getAllRecipesFor(CrusherRecipe.Type.INSTANCE)) {
                        String patternInRecipe = recipe.getPattern();

                        if (foundPattern.equals(patternInRecipe)) {

                            if (hasInputItem(this, recipe)) {

                                //Set Recipe
                                if (hasEnoughEnergyStorage(this, recipe)) {
                                    pattern = foundPattern;
                                    output = recipe.getOutputItem().getItem().getDefaultInstance();
                                    this.RFPerTick = (int) (recipe.getRFPerTick() * RFPerTickMultiplier);
                                    this.maxProgress = (int) (recipe.getDuration() * durationMultiplier);
                                    setChanged(this.level, this.worldPosition, this.getBlockState());
                                    break;
                               }
                            }
                        }
                    }
                }
            }

            if (output != null) {
                progress++;
                if (progress > maxProgress) {

                    this.itemHandler.extractItem(6, 1, false);

                    int remainingOutput = 1 + outputRuns; // Total items to insert
                    for (int slotIndex = 0; slotIndex <= 5 && remainingOutput > 0; slotIndex++) {
                        ItemStack remaining = this.itemHandler.insertItem(slotIndex, new ItemStack (output.copy().getItem().asItem(), 1 + outputRuns), false);
                        int inserted = 1 + outputRuns - remaining.getCount(); // Calculate how many items were inserted
                        remainingOutput -= inserted; // Update the remaining items to insert
                    }

                    resetGenerator();

                }
            }



            if (this.itemHandler.getStackInSlot(0).getCount() < this.itemHandler.getSlotLimit(0) || output == null) {
                this.ENERGY_STORAGE.extractEnergy(RFPerTick, false);
            }
            //reset tick
            if (tickCounter > tickBeforeCheck) {
                tickCounter = 0;
            }
        }
    }


    private void resetGenerator() {
        this.progress = 0;
        this.RFPerTick = 0;
        this.maxProgress = 0;
        output = null;
        pattern = null;
        assert this.level != null;
        setChanged(this.level, this.worldPosition, this.getBlockState());
    }

    private boolean hasInputItem(@NotNull CrusherBlockEntity entity, @NotNull CrusherRecipe recipe) {

        ItemStack[] items = recipe.getIngredients().get(0).getItems();
        ItemStack slotItem = entity.itemHandler.getStackInSlot(6);

        for (ItemStack item : items) {
            if (ItemStack.isSameItem(item, slotItem)) {
                return true;
            }
        }
        return false;
    }

    boolean hasEnoughEnergyStorage (CrusherBlockEntity entity, CrusherRecipe recipe) {
        return entity.getEnergyStorage().getEnergyStored() >= (recipe.getRFPerTick() * maxProgress) + 1 ;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

}
