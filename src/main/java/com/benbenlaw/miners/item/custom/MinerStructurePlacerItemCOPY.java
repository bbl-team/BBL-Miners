package com.benbenlaw.miners.item.custom;

import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinerStructurePlacerItemCOPY extends Item {
    public MinerStructurePlacerItemCOPY(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        if (pPlayer.isCrouching()) {
            if (nbt.contains("mode") && nbt.getString("mode").equals("Mode: Placing Ore/Resource Blocks")) {
                nbt.put("mode", StringTag.valueOf("Mode: Placing Miner Frames"));
            } else {
                nbt.put("mode", StringTag.valueOf("Mode: Placing Ore/Resource Blocks"));
            }
        }
        itemstack.setTag(nbt);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (stack.hasTag()) {
            components.add(Component.literal(String.valueOf(stack.getTag().getString("mode")))
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
        assert player != null;
        ItemStack offHandItemStack = player.getOffhandItem();
        ItemStack mainHandItemStack = player.getMainHandItem();

        BlockState blockAsState = Blocks.AIR.defaultBlockState();

        if (offHandItemStack.is(ModTags.Items.WOODEN_SUPPORT_FRAME)) {blockAsState = ModBlocks.WOODEN_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.STONE_SUPPORT_FRAME)) {blockAsState = ModBlocks.STONE_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.LAPIS_SUPPORT_FRAME)) {blockAsState = ModBlocks.LAPIS_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.COPPER_SUPPORT_FRAME)) {blockAsState = ModBlocks.COPPER_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.IRON_SUPPORT_FRAME)) {blockAsState = ModBlocks.IRON_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.GOLD_SUPPORT_FRAME)) {blockAsState = ModBlocks.GOLD_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.DIAMOND_SUPPORT_FRAME)) {blockAsState = ModBlocks.DIAMOND_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.EMERALD_SUPPORT_FRAME)) {blockAsState = ModBlocks.EMERALD_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.NETHERITE_SUPPORT_FRAME)) {blockAsState = ModBlocks.NETHERITE_SUPPORT_FRAME.get().defaultBlockState();
        }
        if (offHandItemStack.is(ModTags.Items.ULTIMATE_SUPPORT_FRAME)) {blockAsState = ModBlocks.ULTIMATE_SUPPORT_FRAME.get().defaultBlockState();
        }

        assert mainHandItemStack.getTag() != null;

        if (!level.isClientSide && !player.isCrouching()) {
            if (mainHandItemStack.getTag().getString("mode").equals("Mode: Placing Miner Frames")) {
                if (offHandItemStack.getCount() >= 32) {
                    if (!blockAsState.is(Blocks.AIR)) {

                        if (level.getBlockState(blockPos.north(2).west(2).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).west(2).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).east(2).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).east(2).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).west(2).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).west(2).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).east(2).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).east(2).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }

                        if (level.getBlockState(blockPos.north(2).west(2).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).west(2).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).east(2).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).east(2).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).west(2).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).west(2).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).east(2).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).east(2).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }

                        if (level.getBlockState(blockPos.north(2).west(2).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).west(2).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).east(2).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).east(2).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).west(2).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).west(2).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).east(2).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).east(2).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }

                        if (level.getBlockState(blockPos.north(2).west(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).west(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).east(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).east(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).west(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).west(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).east(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).east(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }

                        if (level.getBlockState(blockPos.north(2).west(2).above(5)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).west(2).above(5), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).east(2).above(5)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).east(2).above(5), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).west(2).above(5)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).west(2).above(5), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).east(2).above(5)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).east(2).above(5), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }


                        if (level.getBlockState(blockPos.north(2).west(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).west(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).east(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).east(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }


                        if (level.getBlockState(blockPos.south(2).west(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).west(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).east(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).east(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }


                        if (level.getBlockState(blockPos.east(2).north(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(2).north(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.east(2).south(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(2).south(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.east(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }

                        if (level.getBlockState(blockPos.west(2).north(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(2).north(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.west(2).south(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(2).south(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.west(2).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(2).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                    }
                }
            }
            if (mainHandItemStack.getTag().getString("mode").equals("Mode: Placing Ore/Resource Blocks")) {
                if (offHandItemStack.getCount() >= 36) {
                    blockAsState = ((BlockItem) offHandItemStack.getItem().asItem()).getBlock().defaultBlockState();

                    if (!blockAsState.is(Blocks.AIR)) {

                        if (level.getBlockState(blockPos.north(1).west(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).west(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).west(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).west(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).east(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).east(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).east(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).east(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.east(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.west(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).above(1)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).above(1), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }


                        if (level.getBlockState(blockPos.north(1).west(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).west(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).west(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).west(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).east(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).east(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).east(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).east(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.east(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.west(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).above(2)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).above(2), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }


                        if (level.getBlockState(blockPos.north(1).west(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).west(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).west(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).west(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).east(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).east(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).east(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).east(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.east(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.west(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).above(3)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).above(3), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }


                        if (level.getBlockState(blockPos.north(1).west(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).west(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).west(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).west(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).east(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).east(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).east(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).east(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.east(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.east(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.north(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.north(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.west(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.west(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                        if (level.getBlockState(blockPos.south(1).above(4)).is(Blocks.AIR)) {
                            level.setBlockAndUpdate(blockPos.south(1).above(4), blockAsState);
                            player.getItemBySlot(EquipmentSlot.OFFHAND).shrink(1);
                        }
                    }
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


