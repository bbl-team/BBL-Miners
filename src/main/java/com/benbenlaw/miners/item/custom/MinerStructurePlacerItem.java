package com.benbenlaw.miners.item.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.item.ModItems;
import com.benbenlaw.miners.recipe.CapBlocksRecipe;
import com.benbenlaw.miners.recipe.NoInventoryRecipe;
import com.benbenlaw.miners.recipe.TreeAbsorberBlocksRecipe;
import com.benbenlaw.miners.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.LeavesBlock.PERSISTENT;

public class MinerStructurePlacerItem extends Item {
    public MinerStructurePlacerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        if (pPlayer.isCrouching()) {
            if (nbt.contains("mode") && nbt.getString("mode").equals("Mode: Placing Tree Absorber Tree") || !nbt.contains("mode")) {
                nbt.put("mode", StringTag.valueOf("Mode: Placing Miner Frames"));
                pPlayer.displayClientMessage(Component.literal("Mode: Placing Miner Frame"), true);
            }
            else if (nbt.contains("mode") && nbt.getString("mode").equals("Mode: Placing Miner Frames")) {
                nbt.put("mode", StringTag.valueOf("Mode: Placing Tree Absorber Frames"));
                pPlayer.displayClientMessage(Component.literal("Mode: Placing Tree Absorber Frames"), true);

            }
            else if (nbt.contains("mode") && nbt.getString("mode").equals("Mode: Placing Tree Absorber Frames")) {
                nbt.put("mode", StringTag.valueOf("Mode: Placing Tree Absorber Tree"));
                pPlayer.displayClientMessage(Component.literal("Mode: Placing Tree Absorber Tree"), true);

            }
        }
        itemstack.setTag(nbt);
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (stack.hasTag()) {
            components.add(Component.literal(stack.getTag().getString("mode"))
                    .withStyle(ChatFormatting.GREEN));
        }
        if (!stack.hasTag()) {
            components.add(Component.literal("Shift right click in the air to change mode!")
                    .withStyle(ChatFormatting.GREEN));
        }
        super.appendHoverText(stack, level, components, flag);
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {

        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = pContext.getLevel().getBlockState(blockPos);
        BlockState blockStateBelow = pContext.getLevel().getBlockState(blockPos.below());
        assert player != null;
        ItemStack offHandItemStack = player.getOffhandItem();
        ItemStack mainHandItemStack = player.getMainHandItem();
        Inventory inventory = player.getInventory();

        BlockState blockAsState = Blocks.AIR.defaultBlockState();
        BlockState leafBlockState = Blocks.AIR.defaultBlockState();
        BlockState logBlockState = Blocks.AIR.defaultBlockState();

        int i;
        int l;
        int v;

        for (i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(ModTags.Items.WOODEN_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.WOODEN_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.STONE_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.STONE_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.LAPIS_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.LAPIS_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.COPPER_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.COPPER_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.IRON_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.IRON_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.GOLD_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.GOLD_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.DIAMOND_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.DIAMOND_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.EMERALD_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.EMERALD_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.NETHERITE_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.NETHERITE_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
            if (stack.is(ModTags.Items.ULTIMATE_SUPPORT_FRAME)) {
                blockAsState = ModBlocks.ULTIMATE_SUPPORT_FRAME.get().defaultBlockState();
                break;
            }
        }

        assert mainHandItemStack.getTag() != null;

        if (!level.isClientSide && !player.isCrouching()) {
            if (mainHandItemStack.getTag().getString("mode").equals("Mode: Placing Miner Frames")) {
                if (!blockAsState.is(Blocks.AIR)) {

                    if (level.getBlockState(blockPos.north(2).west(2).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(2).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(2).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(2).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).west(2).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(2).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).east(2).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(2).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(2).west(2).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(2).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(2).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(2).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).west(2).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(2).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).east(2).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(2).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(2).west(2).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(2).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(2).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(2).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).west(2).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(2).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).east(2).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(2).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(2).west(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).west(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).east(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(2).west(2).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(2).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(2).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(2).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).west(2).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(2).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).east(2).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(2).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                    if (level.getBlockState(blockPos.north(2).west(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                    if (level.getBlockState(blockPos.south(2).west(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).east(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                    if (level.getBlockState(blockPos.east(2).north(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.east(2).north(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.east(2).south(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.east(2).south(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.east(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.east(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.west(2).north(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.west(2).north(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.west(2).south(1).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.west(2).south(1).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.west(2).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.west(2).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                }
            }
            if (mainHandItemStack.getTag().getString("mode").equals("Mode: Placing Tree Absorber Frames")) {

                if (blockAsState.is(ModBlocks.WOODEN_SUPPORT_FRAME.get())) {

                    if (level.getBlockState(blockPos.north(3).east(3).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(1)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(1), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(2)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(2), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(3)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(3), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(4)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(4), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(5)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(5), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(6)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(6), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(6)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(6), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(6)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(6), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(6)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(6), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(7)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(7), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(7)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(7), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(7)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(7), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(7)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(7), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(8)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(8), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(8)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(8), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(8)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(8), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(8)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(8), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(9)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(9), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(9)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(9), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(9)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(9), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(9)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(9), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.north(3).east(3).above(11)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(3).above(11), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(3).above(11)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(3).above(11), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(3).above(11)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(3).above(11), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(3).above(11)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(3).above(11), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                    if (level.getBlockState(blockPos.south(3).east(2).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(2).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).east(1).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).east(1).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(1).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(1).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(3).west(2).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(3).west(2).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                    if (level.getBlockState(blockPos.north(3).east(2).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(2).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).east(1).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).east(1).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(1).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(1).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(3).west(2).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(3).west(2).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                    if (level.getBlockState(blockPos.south(2).east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(1).east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(1).east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(1).east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(1).east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).east(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).east(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }

                    if (level.getBlockState(blockPos.south(2).west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(2).west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.south(1).west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.south(1).west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(1).west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(1).west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }
                    if (level.getBlockState(blockPos.north(2).west(3).above(10)).is(Blocks.AIR) && player.getInventory().getItem(i).getCount() > 0) {
                        level.setBlockAndUpdate(blockPos.north(2).west(3).above(10), blockAsState);
                        player.getInventory().getItem(i).shrink(1);
                    }


                }
            }

            if (mainHandItemStack.getTag().getString("mode").equals("Mode: Placing Tree Absorber Tree") && blockState.is(BlockTags.DIRT) && blockStateBelow.is(ModBlocks.TREE_ABSORBER.get())) {

                for (v = 0; v < inventory.getContainerSize(); v++) {
                    ItemStack stack = inventory.getItem(v);

                    if (stack.is(ModTags.Items.TREE_ABSORBER_LEAVES)) {
                        leafBlockState = ((BlockItem) inventory.getItem(v).getItem().asItem()).getBlock().defaultBlockState();
                        break;
                    }
                }

                for (l = 0; l < inventory.getContainerSize(); l++) {
                    ItemStack stack = inventory.getItem(l);

                    if (stack.is(ItemTags.LOGS) || stack.is(ItemTags.LOGS_THAT_BURN)) {
                        logBlockState = ((BlockItem) inventory.getItem(l).getItem().asItem()).getBlock().defaultBlockState();
                        break;
                    }
                }

                //Logs

                if (level.getBlockState(blockPos.above(2)).is(Blocks.AIR) && player.getInventory().getItem(l).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(2), logBlockState);
                    player.getInventory().getItem(l).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3)).is(Blocks.AIR) && player.getInventory().getItem(l).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3), logBlockState);
                    player.getInventory().getItem(l).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4)).is(Blocks.AIR) && player.getInventory().getItem(l).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4), logBlockState);
                    player.getInventory().getItem(l).shrink(1);
                }
                if (level.getBlockState(blockPos.above(1)).is(Blocks.AIR) && player.getInventory().getItem(l).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(1), logBlockState);
                    player.getInventory().getItem(l).shrink(1);
                }

                //Leaves

                if (level.getBlockState(blockPos.above(3).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).west(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).west(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).west(1).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).west(1).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).west(2).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).west(2).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).west(1).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).west(1).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).west(2).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).west(2).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).east(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).east(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).east(1).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).east(1).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).east(2).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).east(2).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).east(1).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).east(1).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).east(2).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).east(2).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).north(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).north(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).north(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).north(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).south(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).south(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).south(2).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).south(2).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).south(2).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).south(2).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).south(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).south(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).north(2).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).north(2).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(3).north(2).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(3).north(2).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }


                if (level.getBlockState(blockPos.above(4).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).west(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).west(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).west(1).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).west(1).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).west(2).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).west(2).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).west(1).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).west(1).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).west(2).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).west(2).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).east(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).east(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).east(1).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).east(1).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).east(2).north()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).east(2).north(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).east(1).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).east(1).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).east(2).south()).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).east(2).south(), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).north(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).north(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).north(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).north(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).south(2)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).south(2), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).south(2).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).south(2).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).south(2).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).south(2).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).south(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).south(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).north(2).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).north(2).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(4).north(2).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(4).north(2).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }


                if (level.getBlockState(blockPos.above(5)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(5), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(5).north(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(5).north(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(5).south(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(5).south(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(5).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(5).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(5).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(5).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }

                if (level.getBlockState(blockPos.above(6)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(6), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(6).north(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(6).north(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(6).south(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(6).south(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(6).east(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(6).east(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }
                if (level.getBlockState(blockPos.above(6).west(1)).is(Blocks.AIR) && player.getInventory().getItem(v).getCount() > 0) {
                    level.setBlockAndUpdate(blockPos.above(6).west(1), leafBlockState);
                    player.getInventory().getItem(v).shrink(1);
                }





            }
        }
        return InteractionResult.SUCCESS;
    }
}
/*
            if (mainHandItemStack.getTag().getString("mode").equals("Mode: Placing Ore/Resource Blocks")) {
                    if (offHandItemStack.getCount() >= 36) {
                    if (!offHandItemStack.is(ModTags.Items.WOODEN_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.STONE_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.LAPIS_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.COPPER_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.IRON_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.GOLD_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.DIAMOND_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.EMERALD_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.ULTIMATE_SUPPORT_FRAME) || !offHandItemStack.is(ModTags.Items.NETHERITE_SUPPORT_FRAME)) {

                    player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(36);

                    level.setBlockAndUpdate(blockPos.north(1).west(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).west(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).east(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).east(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.east(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.west(1).above(1), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).above(1), blockInOffHand);

                    level.setBlockAndUpdate(blockPos.north(1).west(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).west(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).east(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).east(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.east(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.west(1).above(2), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).above(2), blockInOffHand);

                    level.setBlockAndUpdate(blockPos.north(1).west(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).west(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).east(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).east(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.east(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.west(1).above(3), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).above(3), blockInOffHand);

                    level.setBlockAndUpdate(blockPos.north(1).west(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).west(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).east(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).east(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.east(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.north(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.west(1).above(4), blockInOffHand);
                    level.setBlockAndUpdate(blockPos.south(1).above(4), blockInOffHand);

                    }
                    }
                    }
                    if (!(offHandItemStack.getCount() >= 36) && !player.isCrouching()) {
                    player.sendSystemMessage(Component.literal("Not Enough Ores/Resources In Your Off Hand"));
                    }
                    }

 */


