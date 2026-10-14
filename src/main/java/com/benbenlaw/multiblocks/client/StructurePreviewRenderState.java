package com.benbenlaw.multiblocks.client;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record StructurePreviewRenderState(List<PreviewBlock> blocks, Vec3i size, float rotationTime,
                                          int x0, int y0, int x1, int y1, float scale,
                                          @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds,
                                          float inViewScale) implements PictureInPictureRenderState {

    public record PreviewBlock(BlockPos pos, BlockState state) {
    }

    public StructurePreviewRenderState(List<PreviewBlock> blocks, Vec3i size, float rotationTime,
                                       int x0, int y0, int x1, int y1, float scale,
                                       @Nullable ScreenRectangle scissorArea, float inViewScale) {
        this(blocks, size, rotationTime, x0, y0, x1, y1, scale, scissorArea,
                PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea), inViewScale);
    }
}
