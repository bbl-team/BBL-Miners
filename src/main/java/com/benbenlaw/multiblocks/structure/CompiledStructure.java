package com.benbenlaw.multiblocks.structure;

import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Optional;

public record CompiledStructure(List<Entry> entries, List<Vec3i> caps) {

    public record Entry(int dx, int dy, int dz, ExtraCodecs.TagOrElementLocation block) {
    }

    public interface BlockAccess {
        boolean is(int x, int y, int z, ExtraCodecs.TagOrElementLocation block);
    }

    public Optional<Entry> firstMismatch(int originX, int originY, int originZ, BlockAccess access) {
        for (Entry entry : entries) {
            if (!access.is(originX + entry.dx(), originY + entry.dy(), originZ + entry.dz(), entry.block())) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    public boolean matches(int originX, int originY, int originZ, BlockAccess access) {
        return firstMismatch(originX, originY, originZ, access).isEmpty();
    }
}
