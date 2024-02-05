package com.benbenlaw.miners.block.entity;

import com.benbenlaw.miners.multiblock.MultiBlockManagers;
import com.benbenlaw.miners.networking.ModMessages;
import com.benbenlaw.miners.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.miners.recipe.MinerRecipe;
import com.benbenlaw.miners.recipe.TreeAbsorberRecipe;
import com.benbenlaw.miners.screen.TreeAbsorberMenu;
import com.benbenlaw.miners.util.ModEnergyStorage;
import com.benbenlaw.opolisutilities.recipe.UpgradeRecipeUtil;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TreeAbsorberBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
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
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (i) -> i >= 0 && i < 5,
                            (i, s) -> false)),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index >= 0 && index < 5,
                            (index, stack) -> index >= 0 && index < 5 && itemHandler.isItemValid(index, stack))),

                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index >= 0 && index < 5,
                            (index, stack) -> index >= 0 && index < 5 && itemHandler.isItemValid(index, stack))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index >= 0 && index < 5,
                            (index, stack) -> index >= 0 && index < 5 && itemHandler.isItemValid(index, stack))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index >= 0 && index < 5,
                            (index, stack) -> index >= 0 && index < 5 && itemHandler.isItemValid(index, stack))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler,
                            (index) -> index >= 0 && index < 5,
                            (index, stack) -> index >= 0 && index < 5 && itemHandler.isItemValid(index, stack)))
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
    public final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    public int RFPerTick;
    public int fuelDuration = 0;
    final int maxEnergyStorage = 1000000;
    final int maxEnergyTransfer = 1000000;
    public ItemStack log;
    public ItemStack leaf;
    public ItemStack sapling;
    public ItemStack extraItem;
    public double extraItemChance;
    public String pattern;
    public int tickCounter = 0;
    public int tickBeforeCheck = 20;

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

    public TreeAbsorberBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TREE_ABSORBER_BLOCK_ENTITY.get(), blockPos, blockState);
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

    private void updateUpgrades(@NotNull TreeAbsorberBlockEntity entity) {

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
                if (itemHandler.getStackInSlot(5).is(matchingUpgrade.getUpgradeItem().getItem())) {
                    durationMultiplier = matchingUpgrade.getDurationMultiplier();
                    RFPerTickMultiplier = matchingUpgrade.getRFPerTick();
                    outputRuns = matchingUpgrade.getOutputIncreaseAmount();
                    upgradeItem = matchingUpgrade.getUpgradeItem();
                    break;
                }
            }
        }

        if (itemHandler.getStackInSlot(5).isEmpty()) {
            upgradeItem = ItemStack.EMPTY;
            durationMultiplier = 1.0;
            outputRuns = 0;
            RFPerTickMultiplier = 1.0;
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Tree Absorber");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new TreeAbsorberMenu(containerID, inventory, this, this.data);
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
        tag.putInt("tree_absorber_generator.progress", progress);
        tag.putInt("tree_absorber_generator.maxProgress", maxProgress);
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
        progress = tag.getInt("tree_absorber_generator.progress");
        maxProgress = tag.getInt("tree_absorber_generator.maxProgress");
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

            //Structure Check
            if (tickCounter % tickBeforeCheck == 0) {
                var result = MultiBlockManagers.TREE_ABSORBERS.findStructure(level, this.worldPosition, Rotation.NONE);
                if (result != null) {

                    String foundPattern = result.ID();
                    assert level != null;
                    // Move this line here

                    for (TreeAbsorberRecipe recipe : level.getRecipeManager().getAllRecipesFor(TreeAbsorberRecipe.Type.INSTANCE)) {
                        String patternInRecipe = recipe.getPattern();

                        if (foundPattern.equals(patternInRecipe)) {
                            if (hasEnoughEnergy(this, recipe)) {
                                pattern = foundPattern;
                                log = recipe.getLog();
                                leaf = recipe.getLeaf();
                                sapling = recipe.getSapling();
                                extraItem = recipe.getExtraItem();
                                extraItemChance = recipe.getExtraItemChance();
                                this.RFPerTick = (int) (recipe.getRFPerTick() * RFPerTickMultiplier);
                                this.maxProgress = (int) (recipe.getDuration() * durationMultiplier);
                                setChanged(this.level, this.worldPosition, this.getBlockState());
                                break;
                            }
                        }
                    }
                }
            }


            if (pattern != null) {
                if (log != null && leaf != null && sapling != null && extraItem != null) {
                    progress++;
                    if (progress > maxProgress) {

                        //log drops
                        for (int i = 0; i <= 4; i++) {
                            if (this.itemHandler.isItemValid(i, log) && this.itemHandler.insertItem(i, new ItemStack(log.getItem(), 1 + outputRuns), false).isEmpty()) {
                                break;
                            }
                        }

                        //leaf drops
                        if (level.getBlockState(this.getBlockPos().above(9)).is(Blocks.DIAMOND_BLOCK)) {
                            for (int i = 0; i <= 4; i++) {
                                if (this.itemHandler.isItemValid(i, leaf) && this.itemHandler.insertItem(i, new ItemStack(leaf.getItem(), 1 + outputRuns), false).isEmpty()) {
                                    break;
                                }
                            }
                        }

                        //No leaves other drops
                        else {
                            if (0.05 > Math.random()) {
                                for (int i = 0; i <= 4; i++) {
                                    if (this.itemHandler.isItemValid(i, sapling) && this.itemHandler.insertItem(i, new ItemStack(sapling.getItem(), 1 + outputRuns), false).isEmpty()) {
                                        break;
                                    }
                                }
                            }

                            if (0.05 > Math.random()) {
                                for (int i = 0; i <= 4; i++) {
                                    if (this.itemHandler.isItemValid(i, Items.STICK.getDefaultInstance()) && this.itemHandler.insertItem(i, new ItemStack(Items.STICK.getDefaultInstance().getItem(), 1 + outputRuns), false).isEmpty()) {
                                        break;
                                    }
                                }
                            }

                            if (extraItem != null) {
                                if (extraItemChance > Math.random()) {
                                    for (int i = 0; i <= 4; i++) {
                                        if (this.itemHandler.isItemValid(i, extraItem) && this.itemHandler.insertItem(i, new ItemStack(extraItem.getItem(), 1 + outputRuns), false).isEmpty()) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        resetGenerator();
                    }
                }
            }

            this.ENERGY_STORAGE.extractEnergy(RFPerTick, false);

            if (tickCounter > tickBeforeCheck) {
                tickCounter = 0;
            }
        }
    }

    private void resetGenerator() {
        this.progress = 0;
        this.RFPerTick = 0;
        this.maxProgress = 0;
        leaf = null;
        log = null;
        sapling = null;
        extraItem = null;
        extraItemChance = 0;
        pattern = null;
        assert this.level != null;
        setChanged(this.level, this.worldPosition, this.getBlockState());
    }



    boolean hasEnoughEnergy(TreeAbsorberBlockEntity entity, TreeAbsorberRecipe recipe) {
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
