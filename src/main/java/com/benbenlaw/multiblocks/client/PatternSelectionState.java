package com.benbenlaw.multiblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/** The first corner of a Pattern Maker selection, and which position the player is pointing at. */
public class PatternSelectionState {

    private static final double BLOCK_REACH = 24.0;
    private static final double AIR_DISTANCE = 6.0;

    private static @Nullable BlockPos first;

    public static @Nullable BlockPos first() {
        return first;
    }

    public static boolean isActive() {
        return first != null;
    }

    public static void start(BlockPos corner) {
        first = corner;
    }

    public static void clear() {
        first = null;
    }

    /** The block being looked at, or a point a few blocks in front of the player when looking at air. */
    public static @Nullable BlockPos lookedAt() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return null;

        HitResult hit = mc.player.pick(BLOCK_REACH, mc.getDeltaTracker().getGameTimeDeltaPartialTick(false), false);
        if (hit instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) {
            return blockHit.getBlockPos();
        }

        Vec3 point = mc.player.getEyePosition().add(mc.player.getLookAngle().scale(AIR_DISTANCE));
        return BlockPos.containing(point);
    }
}
