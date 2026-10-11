package com.benbenlaw.miners.client;

import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.structure.CompiledStructure;
import com.benbenlaw.miners.structure.StructurePattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class StructurePreviewBuilder {

    public record Result(List<StructurePreviewRenderState.PreviewBlock> blocks, Vec3i size) {
    }

    public static Result build(StructurePattern pattern, long timeMillis) {
        List<CompiledStructure.Entry> entries = pattern.compile().entries();

        int minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
        for (CompiledStructure.Entry entry : entries) {
            minX = Math.min(minX, entry.dx());
            minY = Math.min(minY, entry.dy());
            minZ = Math.min(minZ, entry.dz());
            maxX = Math.max(maxX, entry.dx());
            maxY = Math.max(maxY, entry.dy());
            maxZ = Math.max(maxZ, entry.dz());
        }

        for (Vec3i cap : pattern.caps()) {
            minX = Math.min(minX, cap.getX());
            minY = Math.min(minY, cap.getY());
            minZ = Math.min(minZ, cap.getZ());
            maxX = Math.max(maxX, cap.getX());
            maxY = Math.max(maxY, cap.getY());
            maxZ = Math.max(maxZ, cap.getZ());
        }

        int cycle = (int) (timeMillis / 1000);
        List<StructurePreviewRenderState.PreviewBlock> blocks = new ArrayList<>();

        blocks.add(new StructurePreviewRenderState.PreviewBlock(new BlockPos(-minX, -minY, -minZ),
                MinersBlocks.MINER_CONTROLLER.get().defaultBlockState()));

        for (CompiledStructure.Entry entry : entries) {
            BlockState state = stateFor(entry.block(), cycle);
            if (state == null) continue;
            blocks.add(new StructurePreviewRenderState.PreviewBlock(
                    new BlockPos(entry.dx() - minX, entry.dy() - minY, entry.dz() - minZ), state));
        }

        for (Vec3i cap : pattern.caps()) {
            blocks.add(new StructurePreviewRenderState.PreviewBlock(
                    new BlockPos(cap.getX() - minX, cap.getY() - minY, cap.getZ() - minZ),
                    Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState()));
        }

        return new Result(blocks, new Vec3i(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1));
    }

    private static BlockState stateFor(ExtraCodecs.TagOrElementLocation location, int cycle) {
        if (!location.tag()) {
            return BuiltInRegistries.BLOCK.getValue(location.id()).defaultBlockState();
        }

        List<Holder<Block>> members = new ArrayList<>();
        BuiltInRegistries.BLOCK.getTagOrEmpty(TagKey.create(Registries.BLOCK, location.id())).forEach(members::add);
        if (members.isEmpty()) return null;

        return members.get(cycle % members.size()).value().defaultBlockState();
    }
}
