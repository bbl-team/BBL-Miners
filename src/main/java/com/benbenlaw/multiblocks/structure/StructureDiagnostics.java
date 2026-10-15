package com.benbenlaw.multiblocks.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/** Finds out why a structure doesn't match the blocks around a controller. */
public final class StructureDiagnostics {

    public record Mismatch(int dx, int dy, int dz, BlockPos pos, String expected, String found, boolean emptyTag) {
    }

    public record Report(int total, int wrong, List<Mismatch> mismatches) {
        public boolean matches() {
            return wrong == 0;
        }
    }

    private StructureDiagnostics() {
    }

    /** Checks every block of the structure and lists up to {@code limit} of the wrong ones. */
    public static Report check(Level level, BlockPos origin, StructurePattern pattern, int limit) {
        CompiledStructure compiled = StructureMatching.compile(pattern);

        List<Mismatch> mismatches = new ArrayList<>();
        int wrong = 0;

        for (CompiledStructure.Entry entry : compiled.entries()) {
            BlockPos pos = origin.offset(entry.dx(), entry.dy(), entry.dz());
            BlockState state = level.getBlockState(pos);
            if (entry.block().matches(state)) continue;

            wrong++;
            if (mismatches.size() < limit) {
                boolean emptyTag = entry.block().tag() && !BuiltInRegistries.BLOCK
                        .getTagOrEmpty(TagKey.create(Registries.BLOCK, entry.block().id())).iterator().hasNext();
                mismatches.add(new Mismatch(entry.dx(), entry.dy(), entry.dz(), pos,
                        entry.block().toString(), BlockMatcher.of(state, true).toString(), emptyTag));
            }
        }
        return new Report(compiled.entries().size(), wrong, mismatches);
    }
}
