package com.benbenlaw.miners.block.entity;

import com.benbenlaw.miners.block.custom.CrusherBlock;
import com.benbenlaw.miners.block.custom.FluidAbsorberBlock;
import com.benbenlaw.miners.block.custom.MinerBlock;
import com.benbenlaw.miners.multiblock.MultiBlockManagers;
import com.benbenlaw.miners.networking.ModMessages;
import com.benbenlaw.miners.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.screen.MinerMenu;
import com.benbenlaw.miners.util.ModEnergyStorage;
import com.benbenlaw.opolisutilities.recipe.UpgradeRecipeUtil;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MinerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
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
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack)))
            );

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();


    //Upgrades

    double durationMultiplier = 1.0;
    double RFPerTickMultiplier = 1.0;
    int outputRuns = 0;
    ItemStack upgradeItem = ItemStack.EMPTY;

    protected final ContainerData data;
    public int progress;
    public int maxProgress;
    public ItemStack output;
    public final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    public int RFPerTick;
    public int fuelDuration = 0;
    final int maxEnergyStorage = 1000000;
    final int maxEnergyTransfer = 1000000;
    public String pattern;

    public int maxTransferPerTick = 0;
    public boolean hasStructure;
    public boolean hasFuel;
    public boolean hasEnoughPowerStorageAvailable;
    public int tickCounter = 0;
    public int tickBeforeCheck = 20;

    private void updateUpgrades(@NotNull MinerBlockEntity entity) {

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
                if (itemHandler.getStackInSlot(1).is(matchingUpgrade.getUpgradeItem().getItem())) {
                    durationMultiplier = matchingUpgrade.getDurationMultiplier();
                    RFPerTickMultiplier = matchingUpgrade.getRFPerTick();
                    outputRuns = matchingUpgrade.getOutputIncreaseAmount();
                    upgradeItem = matchingUpgrade.getUpgradeItem();
                    break;
                }
            }
        }

        if (itemHandler.getStackInSlot(1).isEmpty()) {
            upgradeItem = ItemStack.EMPTY;
            durationMultiplier = 1.0;
            outputRuns = 0;
            RFPerTickMultiplier = 1.0;
        }
    }

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

    public MinerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), blockPos, blockState);
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
        return Component.literal("Miner");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new MinerMenu(containerID, inventory, this, this.data);
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
        tag.putInt("miner_generator.progress", progress);
        tag.putInt("miner_generator.maxProgress", maxProgress);
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
        progress = tag.getInt("miner_generator.progress");
        maxProgress = tag.getInt("miner_generator.maxProgress");
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

        if (!level.getBlockState(worldPosition).getValue(MinerBlock.POWERED)) {

            if (!level.isClientSide()) {

                if (tickCounter % tickBeforeCheck == 0) {
                    var result = MultiBlockManagers.MINERS.findStructure(level, this.worldPosition, Rotation.NONE);
                    if (result != null) {

                        String foundPattern = result.ID();
                        assert level != null;

                        for (MinerRecipe recipe : level.getRecipeManager().getAllRecipesFor(MinerRecipe.Type.INSTANCE)) {
                            String patternInRecipe = recipe.getPattern();

                            if (foundPattern.equals(patternInRecipe)) {
                                if (hasEnoughEnergyStorage(this, recipe)) {
                                    pattern = foundPattern;
                                    output = recipe.getOutputItem().getItem().getDefaultInstance();
                                    this.RFPerTick = (int) (recipe.getRFPerTick() * RFPerTickMultiplier);
                                    this.maxProgress = (int) (recipe.getDuration() * durationMultiplier);
                                    level.setBlock(this.worldPosition, this.getBlockState().setValue(MinerBlock.RUNNING, true), 3);
                                    setChanged(this.level, this.worldPosition, this.getBlockState());
                                    break;
                                }
                            }
                        }
                    } else {
                        this.pattern = null;
                        this.output = null;
                        this.RFPerTick = 0;
                        level.setBlock(this.worldPosition, this.getBlockState().setValue(MinerBlock.RUNNING, false), 3);
                        setChanged(this.level, this.worldPosition, this.getBlockState());
                    }
                }

                //Has Enough Energy -> Progress
                if (pattern != null && output != null) {
                    if (this.ENERGY_STORAGE.getEnergyStored() >= RFPerTick) {
                        progress++;
                        this.ENERGY_STORAGE.extractEnergy(RFPerTick, false);
                        if (progress > maxProgress) {
                            this.itemHandler.insertItem(0, output.copyWithCount(1 + outputRuns), false);
                            resetGenerator();
                        }
                    }
                }

                //reset tick
                if (tickCounter > tickBeforeCheck) {
                    tickCounter = 0;
                }
            }

            if (level.isClientSide()) {
                if (this.getBlockState().getValue(CrusherBlock.RUNNING)) {
                    level.addParticle(ParticleTypes.INSTANT_EFFECT,
                            (double) worldPosition.getX() + 0.5D,
                            (double) worldPosition.getY() + 0.5D,
                            (double) worldPosition.getZ() + 0.5D,
                            0.5D, 0.5D, 0.5D);

                    //SOUNDS?
                }
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

    boolean hasEnoughEnergyStorage (MinerBlockEntity entity, MinerRecipe recipe) {
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
