package com.benbenlaw.multiblocks.structure;

import com.benbenlaw.multiblocks.block.custom.CapBlock;
import com.benbenlaw.multiblocks.cap.CapEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/** Checks a loaded structure against the blocks around a controller. Shared by every controller. */
public final class StructureMatching {

    private static final Map<StructurePattern, CompiledStructure> COMPILED = Collections.synchronizedMap(new WeakHashMap<>());

    private StructureMatching() {
    }

    public static CompiledStructure compile(StructurePattern pattern) {
        return COMPILED.computeIfAbsent(pattern, StructurePattern::compile);
    }

    /** False when the structure id is unknown or any block around the origin is wrong. */
    public static boolean matches(Level level, BlockPos origin, Identifier structureId) {
        StructurePattern pattern = StructureManager.INSTANCE.get(structureId);
        if (pattern == null) return false;

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        return compile(pattern).matches(origin.getX(), origin.getY(), origin.getZ(), (x, y, z, location) -> {
            BlockState state = level.getBlockState(cursor.set(x, y, z));
            return location.tag()
                    ? state.is(TagKey.create(Registries.BLOCK, location.id()))
                    : state.is(BuiltInRegistries.BLOCK.getValue(location.id()));
        });
    }

    /** The combined effect of every cap block sitting in one of the structure's cap slots. */
    public static CapEffects capEffects(Level level, BlockPos origin, Identifier structureId) {
        StructurePattern pattern = StructureManager.INSTANCE.get(structureId);
        if (pattern == null) return CapEffects.NONE;

        CapEffects effects = CapEffects.NONE;
        for (Vec3i cap : compile(pattern).caps()) {
            if (level.getBlockState(origin.offset(cap)).getBlock() instanceof CapBlock capBlock) {
                effects = effects.with(capBlock.getCapType(), capBlock.getLevel());
            }
        }
        return effects;
    }
}
