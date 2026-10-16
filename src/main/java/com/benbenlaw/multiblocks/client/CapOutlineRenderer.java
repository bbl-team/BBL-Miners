package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.entity.CraftingControllerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = Multiblocks.MOD_ID, value = Dist.CLIENT)
public class CapOutlineRenderer {

    private static final int OUTLINE_COLOR = ARGB.color(255, 230, 170, 50);

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {
        Minecraft mc = Minecraft.getInstance();
        BlockPos controller = ClientCapOutlines.controller();
        if (controller == null) return;

        if (mc.level == null || !(mc.level.getBlockEntity(controller) instanceof CraftingControllerBlockEntity)) {
            ClientCapOutlines.clear();
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer lines = buffer.getBuffer(RenderTypes.lines());
        Vec3 cam = mc.gameRenderer.getMainCamera().position();

        for (BlockPos pos : ClientCapOutlines.positions()) {
            AABB box = new AABB(pos).inflate(0.002).move(-cam.x, -cam.y, -cam.z);

            ShapeRenderer.renderShape(poseStack, lines, Shapes.create(box), 0, 0, 0, OUTLINE_COLOR, 3F);
        }

        buffer.endBatch();
    }
}
