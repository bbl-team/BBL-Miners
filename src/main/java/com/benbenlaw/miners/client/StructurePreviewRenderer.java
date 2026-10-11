package com.benbenlaw.miners.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public class StructurePreviewRenderer extends PictureInPictureRenderer<StructurePreviewRenderState> {

    private final ModelBlockRenderer blockRenderer;
    private final String textureLabel;

    public StructurePreviewRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);

        this.textureLabel = "bblminers_structure_preview_" + System.identityHashCode(this);
        this.blockRenderer = new ModelBlockRenderer(
                Minecraft.getInstance().options.ambientOcclusion().get(),
                false,
                Minecraft.getInstance().getBlockColors()
        );
    }

    @Override
    public Class<StructurePreviewRenderState> getRenderStateClass() {
        return StructurePreviewRenderState.class;
    }

    @Override
    protected void renderToTexture(StructurePreviewRenderState state, PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || state.blocks().isEmpty()) return;

        Vec3i size = state.size();
        float maxDim = Math.max(size.getX(), Math.max(size.getY(), size.getZ()));
        float scale = state.inViewScale() / Math.max(1.0f, maxDim);

        poseStack.pushPose();
        poseStack.translate(0, 0, 100.0f);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(210f));
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotationTime() / 3));
        poseStack.translate(-size.getX() / 2.0f, -size.getY() / 2.0f, -size.getZ() / 2.0f);

        for (StructurePreviewRenderState.PreviewBlock block : state.blocks()) {
            renderBlock(mc.level, block.state(), block.pos(), poseStack);
        }

        poseStack.popPose();
        this.bufferSource.endBatch();
    }

    private void renderBlock(ClientLevel level, BlockState state, BlockPos pos, PoseStack poseStack) {
        poseStack.pushPose();
        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        int lightCoords = 15728880;

        var renderType = model.hasMaterialFlag(BakedQuad.FLAG_TRANSLUCENT)
                ? Sheets.translucentBlockSheet()
                : Sheets.cutoutBlockSheet();

        VertexConsumer buffer = this.bufferSource.getBuffer(renderType);

        BlockQuadOutput output = (_, _, _, quad, instance) -> {
            instance.setLightCoords(lightCoords);
            buffer.putBakedQuad(poseStack.last(), quad, instance);
        };

        this.blockRenderer.tesselateBlock(output, 0, 0, 0, new GhostRenderState(level, pos, state), pos, state, model, 0);

        poseStack.popPose();
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0F;
    }

    @Override
    protected String getTextureLabel() {
        return this.textureLabel;
    }
}
