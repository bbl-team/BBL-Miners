package com.benbenlaw.miners.block.entity;

import com.benbenlaw.miners.multiblock.MultiBlockManagers;
import com.benbenlaw.miners.networking.ModMessages;
import com.benbenlaw.miners.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.miners.recipe.FluidAbsorberRecipe;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import com.benbenlaw.miners.screen.FluidAbsorberMenu;
import com.benbenlaw.miners.screen.TreeAbsorberMenu;
import com.benbenlaw.miners.util.IFluidHandlingBlockEntity;
import com.benbenlaw.miners.util.ModEnergyStorage;
import com.benbenlaw.opolisutilities.recipe.UpgradeRecipeUtil;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import com.benbenlaw.miners.networking.packets.PacketSyncFluidToClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FluidAbsorberBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity, IFluidHandlingBlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }

        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
            } else {
                return super.isItemValid(slot, stack);
            }
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;

    //Upgrades

    double durationMultiplier = 1.0;
    double RFPerTickMultiplier = 1.0;
    ItemStack upgradeItem = ItemStack.EMPTY;

    public String fluid;
    public int progress;
    public int maxProgress;
    public final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    private final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncFluidToClient(this.fluid, worldPosition));
            }
        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    public int RFPerTick;
    public int outputAmount;
    public int fuelDuration = 0;
    final int maxEnergyStorage = 1000000;
    final int maxEnergyTransfer = 1000000;
    public int maxTransferPerTick = 0;
    public boolean hasStructure;
    public boolean hasFuel;
    public boolean hasEnoughPowerStorageAvailable;
    public int tickCounter = 0;
    public int tickBeforeCheck = 10;

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

    private void updateUpgrades(@NotNull FluidAbsorberBlockEntity entity) {

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
                if (itemHandler.getStackInSlot(0).is(matchingUpgrade.getUpgradeItem().getItem())) {
                    durationMultiplier = matchingUpgrade.getDurationMultiplier();
                    RFPerTickMultiplier = matchingUpgrade.getRFPerTick();
                    upgradeItem = matchingUpgrade.getUpgradeItem();
                    break;
                }
            }
        }

        if (itemHandler.getStackInSlot(0).isEmpty()) {
            upgradeItem = ItemStack.EMPTY;
            durationMultiplier = 1.0;
            RFPerTickMultiplier = 1.0;
        }
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

    public FluidAbsorberBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FLUID_ABSORBER_BLOCK_ENTITY.get(), blockPos, blockState);
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

    @Override
    public Component getDisplayName() {
        return Component.literal("Fluid Absorber");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new FluidAbsorberMenu(containerID, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
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
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyItemHandler.invalidate();

    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("fluid_absorber_generator.progress", progress);
        tag.putInt("fluid_absorber_generator.maxProgress", maxProgress);
        tag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        tag.putInt("current_tick", tickCounter);
        tag.putInt("RFPerTick", RFPerTick);
        tag.putInt("fuelDuration", fuelDuration);
        tag.putInt("outputAmount", outputAmount);
        tag = FLUID_TANK.writeToNBT(tag);

        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("fluid_absorber_generator.progress");
        maxProgress = tag.getInt("fluid_absorber_generator.maxProgress");
        ENERGY_STORAGE.setEnergy(tag.getInt("energy"));
        tickCounter = tag.getInt("current_tick");
        RFPerTick = tag.getInt("RFPerTick");
        fuelDuration = tag.getInt("fuelDuration");
        outputAmount = tag.getInt("outputAmount");
        FLUID_TANK.readFromNBT(tag);

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
                var result = MultiBlockManagers.FLUID_ABSORBERS.findStructure(level, this.worldPosition, Rotation.NONE);
                if (result != null) {

                    String foundPattern = result.ID();
                    assert level != null;

                    for (FluidAbsorberRecipe recipe : level.getRecipeManager().getAllRecipesFor(FluidAbsorberRecipe.Type.INSTANCE)) {
                        String patternInRecipe = recipe.getPattern();

                        if (foundPattern.equals(patternInRecipe)) {
                            //Set Recipe
                            if (hasEnoughEnergyStorage(this, recipe)) {
                                fluid = recipe.getOutputFluid();
                                outputAmount = recipe.getOutputAmount();
                                this.RFPerTick = (int) (recipe.getRFPerTick() * RFPerTickMultiplier);
                                this.maxProgress = (int) (recipe.getDuration() * durationMultiplier);
                                setChanged(this.level, this.worldPosition, this.getBlockState());
                                break;
                            }
                        }
                    }
                }
            }

            if (fluid != null) {
                progress++;
                if (progress > maxProgress) {
                    FluidStack fluidStack = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluid))), outputAmount);
                    this.FLUID_TANK.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    resetGenerator();
                }
            }

            //need to check all slots
            if (this.FLUID_TANK.getFluidAmount() < this.FLUID_TANK.getCapacity() || fluid == null) {
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
        fluid = null;
        assert this.level != null;
        setChanged(this.level, this.worldPosition, this.getBlockState());
    }

    boolean hasEnoughEnergyStorage (FluidAbsorberBlockEntity entity, FluidAbsorberRecipe recipe) {
        return entity.getEnergyStorage().getEnergyStored() >= (recipe.getRFPerTick() * maxProgress) + 1 ;
    }

    private boolean hasEnoughOutputSpace(SimpleContainer inventory, ItemStack stack) {
        int emptySlotCounter = 0;
        int occupiedSlotCounter = 0;

        for (int i = 0; i <= 4; i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (itemStack.isEmpty()) {
                emptySlotCounter++;
            } else if (itemStack.getItem() == stack.getItem()) {
                int availableSpace = stack.getCount(); // Use the entire stack size

                if (availableSpace > 0) {
                    // There's enough space for the entire stack, continue checking other slots
                    continue;
                } else {
                    return false; // Item already present in a slot, recipe should not start
                }
            } else {
                occupiedSlotCounter++;
            }
        }

        // Return false if all slots are occupied or if there are no empty slots
        return occupiedSlotCounter != 26 && emptySlotCounter != 0;
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
