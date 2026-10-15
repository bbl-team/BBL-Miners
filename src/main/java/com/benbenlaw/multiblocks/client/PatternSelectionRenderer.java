package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.item.PatternMakerItem;
import com.benbenlaw.multiblocks.pattern.PatternExporter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

/** Outlines the position a Pattern Maker will pick, and the box from the first corner to it. */
@EventBusSubscriber(modid = Multiblocks.MOD_ID, value = Dist.CLIENT)
public class PatternSelectionRenderer {

    private static final int CORNER_COLOR = 0xFFFFD700;
    private static final int BOX_COLOR = 0xFF00E5FF;
    private static final int TOO_LARGE_COLOR = 0xFFFF5555;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !(mc.player.getMainHandItem().getItem() instanceof PatternMakerItem)) return;

        BlockPos looked = PatternSelectionState.lookedAt();
        if (looked == null) return;

        BlockPos first = PatternSelectionState.first();
        if (first == null) {
            drawBox(event, looked, looked, CORNER_COLOR);
            return;
        }

        drawBox(event, first, first, CORNER_COLOR);
        drawBox(event, first, looked, PatternExporter.tooLarge(first, looked) ? TOO_LARGE_COLOR : BOX_COLOR);
        drawBox(event, looked, looked, CORNER_COLOR);
    }

    private static void drawBox(RenderLevelStageEvent event, BlockPos a, BlockPos b, int color) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cam = mc.gameRenderer.getMainCamera().position();

        AABB box = new AABB(
                Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()),
                Math.max(a.getX(), b.getX()) + 1, Math.max(a.getY(), b.getY()) + 1, Math.max(a.getZ(), b.getZ()) + 1
        ).inflate(0.005);

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer lines = bufferSource.getBuffer(RenderTypes.lines());

        ShapeRenderer.renderShape(poseStack, lines, Shapes.create(box), -cam.x, -cam.y, -cam.z, color, 3.0f);
        bufferSource.endBatch(RenderTypes.lines());
    }
}
