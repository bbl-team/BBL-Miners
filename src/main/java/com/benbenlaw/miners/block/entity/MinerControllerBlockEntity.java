package com.benbenlaw.miners.block.entity;

import com.benbenlaw.core.block.SyncableBlock;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import com.benbenlaw.miners.block.MinersBlockEntities;
import com.benbenlaw.miners.block.custom.CapBlock;
import com.benbenlaw.miners.cap.CapEffects;
import com.benbenlaw.miners.network.CapOutlinesPacket;
import com.benbenlaw.miners.recipe.MinerStructureRecipe;
import com.benbenlaw.miners.screen.MinerControllerMenu;
import com.benbenlaw.miners.structure.CompiledStructure;
import com.benbenlaw.miners.structure.StructureManager;
import com.benbenlaw.miners.structure.StructurePattern;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MinerControllerBlockEntity extends SyncableBlockEntity implements MenuProvider {

    public static final int FUEL_SLOT = 0;
    public static final int FIRST_OUTPUT_SLOT = 1;
    public static final int LAST_OUTPUT_SLOT = 9;

    public static final int ENERGY_CAPACITY = 100_000;
    public static final int ENERGY_PER_BURN_TICK = 10;
    private static final int VALIDATE_INTERVAL = 20;

    private static final Map<StructurePattern, CompiledStructure> COMPILED = Collections.synchronizedMap(new WeakHashMap<>());

    private final SyncableItemHandler inventory = new SyncableItemHandler(this, LAST_OUTPUT_SLOT + 1,
            (slot, stack) -> slot == FUEL_SLOT && isFuel(stack),
            slot -> slot >= FIRST_OUTPUT_SLOT);

    private final MinerEnergyHandler energy = new MinerEnergyHandler(ENERGY_CAPACITY, 1000, this);

    private int progress = 0;
    private int maxProgress = 100;

    private RecipeHolder<MinerStructureRecipe> structure;
    private boolean structureDirty = true;

    private ItemStack miningTool = new ItemStack(Items.DIAMOND_PICKAXE);
    private CapEffects capEffects = CapEffects.NONE;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> energy.getAmountAsInt();
                case 3 -> energy.getCapacityAsInt();
                case 4 -> structure != null ? 1 : 0;
                case 5 -> capEffects.efficiency();
                case 6 -> capEffects.fortune();
                case 7 -> capEffects.silkTouch() ? 1 : 0;
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
            return 8;
        }
    };

    public MinerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(MinersBlockEntities.MINER_CONTROLLER_BLOCK_ENTITY.get(), pos, state);
    }

    private boolean isFuel(ItemStack stack) {
        return level != null && level.fuelValues().isFuel(stack);
    }

    public void tick() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        if (structureDirty || serverLevel.getGameTime() % VALIDATE_INTERVAL == 0) {
            validateStructure(serverLevel);
        }

        burnFuel(serverLevel);

        if (structure == null || !getBlockState().getValue(SyncableBlock.RUNNING)) {
            resetProgress();
            return;
        }

        MinerStructureRecipe recipe = structure.value();
        maxProgress = capEffects.duration(recipe.baseDuration());

        if (!hasOutputSpace() || energy.getAmountAsInt() < recipe.energyPerTick()) return;

        energy.set(energy.getAmountAsInt() - recipe.energyPerTick());
        progress++;

        if (progress >= maxProgress) {
            progress = 0;
            mine(serverLevel, recipe);
            sync();
        }
        setChanged();
    }

    private void resetProgress() {
        if (progress != 0) {
            progress = 0;
            setChanged();
        }
    }

    private void burnFuel(ServerLevel serverLevel) {
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

    private void validateStructure(ServerLevel serverLevel) {
        structureDirty = false;
        RecipeHolder<MinerStructureRecipe> previous = structure;
        structure = null;

        if (previous != null && matches(previous)) {
            structure = previous;
        } else {
            for (RecipeHolder<MinerStructureRecipe> holder : serverLevel.getServer().getRecipeManager()
                    .recipeMap().byType(MinerStructureRecipe.TYPE)) {
                if (matches(holder)) {
                    structure = holder;
                    break;
                }
            }

            if (previous != structure) {
                progress = 0;
                sync();
            }
        }

        updateCaps(serverLevel);
    }

    public void toggleCapOutlines(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        StructurePattern pattern = structure == null ? null : StructureManager.INSTANCE.get(structure.value().structureId());
        if (pattern == null) {
            player.sendOverlayMessage(Component.translatable("message.bblminers.build_structure_first"));
            return;
        }

        BlockPos origin = getBlockPos();
        List<BlockPos> positions = new ArrayList<>();
        for (Vec3i cap : COMPILED.computeIfAbsent(pattern, StructurePattern::compile).caps()) {
            positions.add(origin.offset(cap));
        }

        PacketDistributor.sendToPlayer(serverPlayer, new CapOutlinesPacket(origin, positions));
    }

    private void updateCaps(ServerLevel serverLevel) {
        CapEffects effects = CapEffects.NONE;

        StructurePattern pattern = structure == null ? null : StructureManager.INSTANCE.get(structure.value().structureId());
        if (pattern != null) {
            CompiledStructure compiled = COMPILED.computeIfAbsent(pattern, StructurePattern::compile);
            BlockPos origin = getBlockPos();

            for (Vec3i cap : compiled.caps()) {
                if (serverLevel.getBlockState(origin.offset(cap)).getBlock() instanceof CapBlock capBlock) {
                    effects = effects.with(capBlock.getCapType(), capBlock.getLevel());
                }
            }
        }

        if (!effects.equals(capEffects)) {
            capEffects = effects;
            miningTool = buildMiningTool(serverLevel, effects);
        }
    }

    private static ItemStack buildMiningTool(ServerLevel serverLevel, CapEffects effects) {
        ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
        HolderLookup.RegistryLookup<Enchantment> enchantments = serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        if (effects.fortune() > 0) tool.enchant(enchantments.getOrThrow(Enchantments.FORTUNE), effects.fortune());
        if (effects.silkTouch()) tool.enchant(enchantments.getOrThrow(Enchantments.SILK_TOUCH), 1);
        return tool;
    }

    private boolean matches(RecipeHolder<MinerStructureRecipe> holder) {
        StructurePattern pattern = StructureManager.INSTANCE.get(holder.value().structureId());
        if (pattern == null) return false;

        CompiledStructure compiled = COMPILED.computeIfAbsent(pattern, StructurePattern::compile);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos origin = getBlockPos();

        return compiled.matches(origin.getX(), origin.getY(), origin.getZ(), (x, y, z, location) -> {
            BlockState state = level.getBlockState(cursor.set(x, y, z));
            return location.tag()
                    ? state.is(TagKey.create(Registries.BLOCK, location.id()))
                    : state.is(BuiltInRegistries.BLOCK.getValue(location.id()));
        });
    }

    private boolean hasOutputSpace() {
        for (int slot = FIRST_OUTPUT_SLOT; slot <= LAST_OUTPUT_SLOT; slot++) {
            ItemStack stack = ItemUtil.getStack(inventory, slot);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) return true;
        }
        return false;
    }

    private void mine(ServerLevel serverLevel, MinerStructureRecipe recipe) {
        List<Identifier> lootTables = recipe.lootTables();
        if (lootTables.isEmpty()) return;

        RandomSource random = serverLevel.getRandom();
        List<ItemStack> drops = new ArrayList<>();

        for (int i = 0; i < recipe.baseRolls(); i++) {
            Identifier tableId = lootTables.get(random.nextInt(lootTables.size()));
            LootTable table = serverLevel.getServer().reloadableRegistries()
                    .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tableId));

            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(getBlockPos()))
                    .withParameter(LootContextParams.TOOL, miningTool)
                    .withParameter(LootContextParams.BLOCK_STATE, blockStateFor(tableId))
                    .create(LootContextParamSets.BLOCK);

            drops.addAll(table.getRandomItems(params, random));
        }

        inventory.runInternal(() -> {
            try (Transaction tx = Transaction.open(null)) {
                for (ItemStack drop : drops) {
                    int remaining = drop.getCount();
                    for (int slot = FIRST_OUTPUT_SLOT; slot <= LAST_OUTPUT_SLOT && remaining > 0; slot++) {
                        remaining -= inventory.insert(slot, ItemResource.of(drop), remaining, tx);
                    }
                }
                tx.commit();
            }
        });
    }

    private static BlockState blockStateFor(Identifier lootTable) {
        String path = lootTable.getPath();
        if (!path.startsWith("blocks/")) return Blocks.AIR.defaultBlockState();

        return BuiltInRegistries.BLOCK
                .getOptional(Identifier.fromNamespaceAndPath(lootTable.getNamespace(), path.substring("blocks/".length())))
                .map(Block::defaultBlockState)
                .orElse(Blocks.AIR.defaultBlockState());
    }

    public ItemStacksResourceHandler getItemHandler() {
        return inventory;
    }

    public MinerEnergyHandler getEnergyHandler() {
        return energy;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new MinerControllerMenu(containerId, playerInventory, worldPosition, data);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.bblminers.miner_controller");
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        inventory.serialize(output.child("inventory"));
        energy.serialize(output.child("energy"));
        output.putInt("progress", progress);
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        inventory.deserialize(input.childOrEmpty("inventory"));
        energy.deserialize(input.childOrEmpty("energy"));
        progress = input.getIntOr("progress", 0);
        structureDirty = true;
        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        dropInventoryContents(inventory);
    }
}
