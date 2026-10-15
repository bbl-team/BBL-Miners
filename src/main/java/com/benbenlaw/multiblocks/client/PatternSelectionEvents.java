package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.item.PatternMakerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * Handles the Pattern Maker clicks on the client so a corner can be a block or a point in the air, the way the Roomopolis
 * placer does it. Only the finished selection goes to the server.
 */
@EventBusSubscriber(modid = Multiblocks.MOD_ID, value = Dist.CLIENT)
public class PatternSelectionEvents {

    private static final int ACTION_COOLDOWN_TICKS = 5;

    private static long lastActionTick = -ACTION_COOLDOWN_TICKS;

    @SubscribeEvent
    public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null || event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(player.getMainHandItem().getItem() instanceof PatternMakerItem)) return;

        if (event.isUseItem()) {
            event.setCanceled(true);
            event.setSwingHand(false);

            if (player.isShiftKeyDown()) {
                cancel(player);
            } else {
                handleClick(mc, player);
            }
        } else if (event.isAttack() && PatternSelectionState.isActive()) {
            event.setCanceled(true);
            event.setSwingHand(false);
            cancel(player);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null || (PatternSelectionState.isActive() && !(player.getMainHandItem().getItem() instanceof PatternMakerItem))) {
            PatternSelectionState.clear();
        }
    }

    private static void handleClick(Minecraft mc, LocalPlayer player) {
        long now = mc.level.getGameTime();
        if (now - lastActionTick < ACTION_COOLDOWN_TICKS) return;

        if (!player.isCreative()) {
            player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.pattern.creative_only").withStyle(ChatFormatting.RED));
            return;
        }

        BlockPos target = PatternSelectionState.lookedAt();
        if (target == null) return;

        lastActionTick = now;

        BlockPos first = PatternSelectionState.first();
        if (first == null) {
            PatternSelectionState.start(target);
            player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.pattern.first_corner", target.toShortString()).withStyle(ChatFormatting.GREEN));
        } else {
            PatternSelectionState.clear();
            mc.setScreen(new CreatePatternScreen(first, target));
        }
    }

    private static void cancel(LocalPlayer player) {
        if (!PatternSelectionState.isActive()) return;

        PatternSelectionState.clear();
        player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.pattern.cancelled").withStyle(ChatFormatting.YELLOW));
    }
}
