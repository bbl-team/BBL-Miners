package com.benbenlaw.multiblocks.block.entity;

import com.benbenlaw.core.block.SyncableBlock;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import com.benbenlaw.multiblocks.block.MultiblocksBlockEntities;
import com.benbenlaw.multiblocks.cap.CapEffects;
import com.benbenlaw.multiblocks.config.MultiblocksConfig;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingInput;
import com.benbenlaw.multiblocks.recipe.CraftingResult;
import com.benbenlaw.multiblocks.recipe.EffectTotals;
import com.benbenlaw.multiblocks.recipe.ExtraItem;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import com.benbenlaw.multiblocks.screen.CraftingControllerMenu;
import com.benbenlaw.multiblocks.structure.StructureMatching;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CraftingControllerBlockEntity extends SyncableBlockEntity implements MenuProvider {

    public static final int GRID_START = 0;
    public static final int GRID_SIZE = 9;
    public static final int EXTRA_START = 9;
    public static final int EXTRA_SIZE = 9;
    public static final int OUTPUT_START = 18;
    public static final int OUTPUT_SIZE = 9;
    public static final int FUEL_SLOT = OUTPUT_START + OUTPUT_SIZE;
    public static final int SLOT_COUNT = FUEL_SLOT + 1;

    public static final int ENERGY_CAPACITY = 100_000;
    public static final int ENERGY_PER_BURN_TICK = 10;
    private static final int REFRESH_INTERVAL = 10;
    private static final int STRUCTURE_SCAN_INTERVAL = 20;

    /** Values for the structure status shown in the GUI. */
    public static final int STRUCTURE_NOT_REQUIRED = 0;
    public static final int STRUCTURE_BUILT = 1;
    public static final int STRUCTURE_MISSING = 2;

    /** The grid and research slots, the ones that can be locked for automation. */
    public static final int LOCKABLE_SLOTS = OUTPUT_START;

    private final SlotLock<ItemStack> slotLock = new SlotLock<>(LOCKABLE_SLOTS, ItemStack.EMPTY, ItemStack::isSameItemSameComponents, ItemStack::isEmpty);

    private final SyncableItemHandler inventory = new SyncableItemHandler(this, SLOT_COUNT,
            (slot, stack) -> slot < OUTPUT_START
                    ? slotLock.allows(slot, stack)
                    : (slot == FUEL_SLOT && hasFuelSlot() && isFuel(stack)),
            slot -> slot >= OUTPUT_START && slot < OUTPUT_START + OUTPUT_SIZE);

    private final MinerEnergyHandler energy = new MinerEnergyHandler(ENERGY_CAPACITY, 1000, this);

    private int progress = 0;
    private int maxProgress = 100;

    private @Nullable RecipeHolder<ControllerCraftingRecipe> recipe;
    private int structureState = STRUCTURE_NOT_REQUIRED;
    private @Nullable Identifier activeStructureId;
    private CapEffects capEffects = CapEffects.NONE;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> energy.getAmountAsInt();
                case 3 -> energy.getCapacityAsInt();
                case 4 -> structureState;
                case 5 -> capEffects.efficiency();
                case 6 -> slotLock.isLocked() ? 1 : 0;
                case 7 -> capEffects.fortune();
                case 8 -> capEffects.silkTouch() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 9;
        }
    };

    public CraftingControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(), pos, state);
    }

    /** The registry id of this controller's block. Only recipes that name it (the Crafting Controller's own by default) are used. */
    public Identifier controllerId() {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
    }

    public boolean hasFuelSlot() {
        return !(getBlockState().getBlock() instanceof com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock block) || block.hasFuelSlot();
    }

    private boolean isFuel(ItemStack stack) {
        return level != null && level.fuelValues().isFuel(stack);
    }

    public void tick() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        burnFuel(serverLevel);

        ControllerCraftingInput input = currentInput();

        if (recipe != null && !recipe.value().matches(input, serverLevel)) {
            recipe = null;
            progress = 0;
        }

        if (recipe == null || serverLevel.getGameTime() % REFRESH_INTERVAL == 0) {
            refreshRecipe(serverLevel, input);
        }

        if (recipe == null || !getBlockState().getValue(SyncableBlock.RUNNING)) {
            resetProgress();
            return;
        }

        ControllerCraftingRecipe active = recipe.value();

        List<ItemStack> research = stacks(EXTRA_START, EXTRA_SIZE);
        int[] extraSlots = active.assignExtras(research);
        if (extraSlots == null) {
            resetProgress();
            return;
        }

        EffectTotals totals = active.totals(extraSlots);

        maxProgress = capEffects.duration(totals.duration(active.baseDuration()));
        int cost = totals.energyPerTick(active.energyPerTick());

        List<ItemStack> potential = active.potentialResults(totals, extraSlots);
        boolean fits = potential.isEmpty() && active.result().isLoot() ? hasOutputSpace() : canOutputAll(potential);
        if (!fits || energy.getAmountAsInt() < cost) return;

        energy.set(energy.getAmountAsInt() - cost);
        progress++;

        if (progress >= maxProgress) {
            progress = 0;
            craft(active);
            sync();
        }
        setChanged();
    }

    private ControllerCraftingInput currentInput() {
        return new ControllerCraftingInput(stacks(GRID_START, GRID_SIZE), stacks(EXTRA_START, EXTRA_SIZE));
    }

    private List<ItemStack> stacks(int start, int count) {
        List<ItemStack> stacks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) stacks.add(ItemUtil.getStack(inventory, start + i));
        return stacks;
    }

    private void refreshRecipe(ServerLevel serverLevel, ControllerCraftingInput input) {
        RecipeHolder<ControllerCraftingRecipe> previous = recipe;
        recipe = null;
        structureState = STRUCTURE_NOT_REQUIRED;

        RecipeHolder<ControllerCraftingRecipe> missingStructure = null;

        // recipes that need a grid come first, so a recipe without inputs (a loot table miner) never shadows them
        search:
        for (boolean withoutInputs : new boolean[]{false, true}) {
            for (RecipeHolder<ControllerCraftingRecipe> holder : serverLevel.getServer().getRecipeManager()
                    .recipeMap().byType(ControllerCraftingRecipe.TYPE)) {
                if (holder.value().pattern().isEmpty() != withoutInputs) continue;
                if (!holder.value().controller().equals(controllerId()) || !holder.value().matches(input, serverLevel)) continue;

                var structureId = holder.value().structureId();
                if (structureId.isEmpty() || StructureMatching.matches(serverLevel, getBlockPos(), structureId.get())) {
                    recipe = holder;
                    structureState = structureId.isPresent() ? STRUCTURE_BUILT : STRUCTURE_NOT_REQUIRED;
                    break search;
                }
                if (missingStructure == null) missingStructure = holder;
            }
        }

        if (recipe == null && missingStructure != null) {
            structureState = STRUCTURE_MISSING;
        }

        if (previous != recipe) {
            progress = 0;
            sync();
        }

        var structureId = recipe == null ? java.util.Optional.<net.minecraft.resources.Identifier>empty() : recipe.value().structureId();
        CapEffects effects = structureId.isPresent()
                ? StructureMatching.capEffects(serverLevel, getBlockPos(), structureId.get())
                : CapEffects.NONE;
        capEffects = effects;

        // The title follows whichever structure is built, not only the one an active recipe needs
        if (structureId.isPresent()) {
            setActiveStructure(structureId.get());
        } else if (serverLevel.getGameTime() % STRUCTURE_SCAN_INTERVAL == 0) {
            setActiveStructure(findBuiltStructure(serverLevel));
        }
    }

    /** The first structure used by any Crafting Controller recipe that is built around this controller, or null. */
    private @Nullable Identifier findBuiltStructure(ServerLevel serverLevel) {
        java.util.Set<Identifier> candidates = new java.util.LinkedHashSet<>();
        for (RecipeHolder<ControllerCraftingRecipe> holder : serverLevel.getServer().getRecipeManager()
                .recipeMap().byType(ControllerCraftingRecipe.TYPE)) {
            if (holder.value().controller().equals(controllerId())) holder.value().structureId().ifPresent(candidates::add);
        }

        for (Identifier candidate : candidates) {
            if (StructureMatching.matches(serverLevel, getBlockPos(), candidate)) return candidate;
        }
        return null;
    }

    /** The structure this controller is currently built as, which the GUI shows as its title. Synced to clients. */
    public @Nullable Identifier getActiveStructureId() {
        return activeStructureId;
    }

    private void setActiveStructure(@Nullable Identifier id) {
        if (java.util.Objects.equals(id, activeStructureId)) return;
        activeStructureId = id;
        setChanged();
        sync();
    }

    private void resetProgress() {
        if (progress != 0) {
            progress = 0;
            setChanged();
        }
    }

    private void burnFuel(ServerLevel serverLevel) {
        if (!hasFuelSlot()) return;

        ItemStack fuel = ItemUtil.getStack(inventory, FUEL_SLOT);
        if (fuel.isEmpty()) return;

        int gain = serverLevel.fuelValues().burnDuration(fuel) * ENERGY_PER_BURN_TICK;
        if (gain <= 0 || energy.getAmountAsInt() + gain > energy.getCapacityAsInt()) return;

        inventory.runInternal(() -> {
            try (Transaction tx = Transaction.open(null)) {
                inventory.extract(FUEL_SLOT, ItemResource.of(fuel), 1, tx);
                tx.commit();
            }
        });
        energy.set(energy.getAmountAsInt() + gain);
    }

    /** True when the output slots have room for all of these stacks at once. */
    private boolean canOutputAll(List<ItemStack> stacks) {
        List<ItemStack> simulated = new ArrayList<>();
        for (int slot = OUTPUT_START; slot < OUTPUT_START + OUTPUT_SIZE; slot++) {
            simulated.add(ItemUtil.getStack(inventory, slot).copy());
        }

        for (ItemStack stack : stacks) {
            int remaining = stack.getCount();

            for (ItemStack output : simulated) {
                if (remaining <= 0) break;
                if (output.isEmpty() || !ItemStack.isSameItemSameComponents(output, stack)) continue;

                int added = Math.min(remaining, output.getMaxStackSize() - output.getCount());
                output.grow(added);
                remaining -= added;
            }

            for (int i = 0; i < simulated.size() && remaining > 0; i++) {
                if (!simulated.get(i).isEmpty()) continue;

                int added = Math.min(remaining, stack.getMaxStackSize());
                simulated.set(i, stack.copyWithCount(added));
                remaining -= added;
            }

            if (remaining > 0) return false;
        }
        return true;
    }

    private void craft(ControllerCraftingRecipe active) {
        int[] gridAmounts = active.gridAmounts(stacks(GRID_START, GRID_SIZE));
        int[] extraSlots = active.assignExtras(stacks(EXTRA_START, EXTRA_SIZE));
        if (gridAmounts == null || extraSlots == null) return;

        EffectTotals totals = active.totals(extraSlots);
        List<ItemStack> outputs = new ArrayList<>(active.rollResults(totals, extraSlots, level.getRandom()));
        if (active.result() instanceof CraftingResult.Loot loot && active.potentialResults(totals, extraSlots).isEmpty()) {
            outputs.addAll(rollLoot(loot, totals));
        }
        outputs.addAll(active.bonusOutputs(extraSlots, level.getRandom()));

        inventory.runInternal(() -> {
            try (Transaction tx = Transaction.open(null)) {
                for (int slot = 0; slot < GRID_SIZE; slot++) take(GRID_START + slot, gridAmounts[slot], tx);
                for (int i = 0; i < extraSlots.length; i++) {
                    ExtraItem extra = active.extraItems().get(i);
                    if (extraSlots[i] < 0 || !extra.consume()) continue;
                    if (extra.consumeChance() >= 1 || level.getRandom().nextDouble() < extra.consumeChance()) {
                        take(EXTRA_START + extraSlots[i], extra.count(), tx);
                    }
                }

                // results fit because the output slots were checked first; loot drops and bonuses are a best effort,
                // whatever doesn't fit in the output slots is lost
                for (ItemStack output : outputs) {
                    int left = output.getCount();
                    for (int slot = OUTPUT_START; slot < OUTPUT_START + OUTPUT_SIZE && left > 0; slot++) {
                        left -= inventory.insert(slot, ItemResource.of(output), left, tx);
                    }
                }
                tx.commit();
            }
        });
    }

    private boolean hasOutputSpace() {
        for (int slot = OUTPUT_START; slot < OUTPUT_START + OUTPUT_SIZE; slot++) {
            ItemStack stack = ItemUtil.getStack(inventory, slot);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) return true;
        }
        return false;
    }

    /** Simulated mining: rolls the loot tables as if the block was broken with the caps' fortune and silk touch. */
    private List<ItemStack> rollLoot(CraftingResult.Loot loot, EffectTotals totals) {
        ServerLevel serverLevel = (ServerLevel) level;
        ItemStack tool = miningTool(serverLevel);
        net.minecraft.util.RandomSource random = serverLevel.getRandom();
        List<ItemStack> drops = new ArrayList<>();

        int rolls = totals.outputCount(loot.rolls());
        for (int i = 0; i < rolls; i++) {
            Identifier tableId = loot.tables().get(random.nextInt(loot.tables().size()));
            net.minecraft.world.level.storage.loot.LootTable table = serverLevel.getServer().reloadableRegistries()
                    .getLootTable(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, tableId));

            net.minecraft.world.level.storage.loot.LootParams params = new net.minecraft.world.level.storage.loot.LootParams.Builder(serverLevel)
                    .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, net.minecraft.world.phys.Vec3.atCenterOf(getBlockPos()))
                    .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL, tool)
                    .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE, blockStateFor(tableId))
                    .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.BLOCK);

            drops.addAll(table.getRandomItems(params, random));
        }
        return drops;
    }

    private ItemStack miningTool(ServerLevel serverLevel) {
        ItemStack tool = new ItemStack(net.minecraft.world.item.Items.DIAMOND_PICKAXE);
        net.minecraft.core.HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchantments =
                serverLevel.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);

        if (capEffects.fortune() > 0) tool.enchant(enchantments.getOrThrow(net.minecraft.world.item.enchantment.Enchantments.FORTUNE), capEffects.fortune());
        if (capEffects.silkTouch()) tool.enchant(enchantments.getOrThrow(net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH), 1);
        return tool;
    }

    private static BlockState blockStateFor(Identifier lootTable) {
        String path = lootTable.getPath();
        if (!path.startsWith("blocks/")) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();

        return net.minecraft.core.registries.BuiltInRegistries.BLOCK
                .getOptional(Identifier.fromNamespaceAndPath(lootTable.getNamespace(), path.substring("blocks/".length())))
                .map(net.minecraft.world.level.block.Block::defaultBlockState)
                .orElse(net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
    }

    /** Sends the cap positions of the structure this controller is built as, so they are outlined in the world. */
    public void toggleCapOutlines(Player player) {
        if (!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return;

        com.benbenlaw.multiblocks.structure.StructurePattern pattern = activeStructureId == null ? null
                : com.benbenlaw.multiblocks.structure.StructureManager.INSTANCE.get(activeStructureId);
        if (pattern == null) {
            player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.build_structure_first"));
            return;
        }

        BlockPos origin = getBlockPos();
        List<BlockPos> positions = new ArrayList<>();
        for (net.minecraft.core.Vec3i cap : pattern.compile().caps()) positions.add(origin.offset(cap));

        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer,
                new com.benbenlaw.multiblocks.network.CapOutlinesPacket(origin, positions));
    }

    private void take(int slot, int amount, Transaction tx) {
        ItemStack stack = ItemUtil.getStack(inventory, slot);
        if (amount > 0 && !stack.isEmpty()) inventory.extract(slot, ItemResource.of(stack), amount, tx);
    }

    /** Locks the grid and research slots to what they hold now, or unlocks them if they were locked. */
    public void toggleLock() {
        if (slotLock.isLocked()) {
            slotLock.unlock();
        } else {
            slotLock.lock(slot -> ItemUtil.getStack(inventory, slot).copyWithCount(1));
        }
        setChanged();
        sync();
    }

    public boolean isLocked() {
        return slotLock.isLocked();
    }

    /** What a locked slot is set to accept, or an empty stack. */
    public ItemStack getLockFilter(int slot) {
        return slotLock.filter(slot);
    }

    public ItemStacksResourceHandler getItemHandler() {
        return inventory;
    }

    public MinerEnergyHandler getEnergyHandler() {
        return energy;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new CraftingControllerMenu(containerId, playerInventory, worldPosition, data);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        inventory.serialize(output.child("inventory"));
        energy.serialize(output.child("energy"));
        output.putInt("progress", progress);
        if (activeStructureId != null) output.putString("structureId", activeStructureId.toString());
        output.putBoolean("slotsLocked", slotLock.isLocked());
        for (int slot = 0; slot < LOCKABLE_SLOTS; slot++) {
            output.store("lockFilter" + slot, ItemStack.OPTIONAL_CODEC, slotLock.filter(slot));
        }
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        inventory.deserialize(input.childOrEmpty("inventory"));
        energy.deserialize(input.childOrEmpty("energy"));
        progress = input.getIntOr("progress", 0);
        activeStructureId = Identifier.tryParse(input.getStringOr("structureId", ""));
        slotLock.load(input.getBooleanOr("slotsLocked", false),
                slot -> input.read("lockFilter" + slot, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        dropInventoryContents(inventory);
    }
}
