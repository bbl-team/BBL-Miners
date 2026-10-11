package com.benbenlaw.miners.structure;

import com.benbenlaw.miners.Miners;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class StructureManager extends SimpleJsonResourceReloadListener<StructurePattern> {

    public static final Identifier ID = Miners.identifier("structures");
    public static final StructureManager INSTANCE = new StructureManager();

    private volatile Map<Identifier, StructurePattern> structures = Map.of();

    private StructureManager() {
        super(StructurePattern.CODEC, FileToIdConverter.json("bblminers/structure"));
    }

    @Override
    protected void apply(Map<Identifier, StructurePattern> loaded, ResourceManager manager, ProfilerFiller profiler) {
        structures = Map.copyOf(loaded);
        Miners.LOGGER.info("Loaded {} miner structures", loaded.size());
    }

    public @Nullable StructurePattern get(Identifier id) {
        return structures.get(id);
    }

    public Map<Identifier, StructurePattern> all() {
        return structures;
    }
}
