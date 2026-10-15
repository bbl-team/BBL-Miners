package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.structure.BlockMatcher;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.block.custom.CapBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import com.benbenlaw.multiblocks.structure.CompiledStructure;
import com.benbenlaw.multiblocks.structure.StructurePattern;
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

    public static Result build(StructurePattern pattern, Block controller, long timeMillis) {
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
                controller.defaultBlockState()));

        for (CompiledStructure.Entry entry : entries) {
            BlockState state = stateFor(entry.block(), cycle);
            if (state == null) continue;
            blocks.add(new StructurePreviewRenderState.PreviewBlock(
                    new BlockPos(entry.dx() - minX, entry.dy() - minY, entry.dz() - minZ), state));
        }

        List<Vec3i> caps = pattern.caps();
        for (int i = 0; i < caps.size(); i++) {
            Vec3i cap = caps.get(i);
            blocks.add(new StructurePreviewRenderState.PreviewBlock(
                    new BlockPos(cap.getX() - minX, cap.getY() - minY, cap.getZ() - minZ),
                    capState(cycle + i)));
        }

        return new Result(blocks, new Vec3i(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1));
    }

    /** Cycles through every registered cap block; slots are offset so neighbouring slots show different caps. */
    private static BlockState capState(int cycle) {
        List<DeferredBlock<CapBlock>> caps = MultiblocksBlocks.CAPS;
        if (caps.isEmpty()) return Blocks.AIR.defaultBlockState();
        return caps.get(cycle % caps.size()).get().defaultBlockState();
    }

    private static BlockState stateFor(BlockMatcher location, int cycle) {
        if (!location.tag()) {
            return location.toState();
        }

        List<Holder<Block>> members = new ArrayList<>();
        BuiltInRegistries.BLOCK.getTagOrEmpty(TagKey.create(Registries.BLOCK, location.id())).forEach(members::add);
        if (members.isEmpty()) return null;

        return location.applyProperties(members.get(cycle % members.size()).value().defaultBlockState());
    }
}
