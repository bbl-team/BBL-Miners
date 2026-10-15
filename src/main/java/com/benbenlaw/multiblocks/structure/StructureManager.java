package com.benbenlaw.multiblocks.structure;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.pattern.PatternExporter;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StructureManager extends SimpleJsonResourceReloadListener<StructurePattern> {

    public static final String DIRECTORY = "bblmultiblocks/structures";

    public static final Identifier ID = Multiblocks.identifier("structures");
    public static final StructureManager INSTANCE = new StructureManager();

    private volatile Map<Identifier, StructurePattern> structures = Map.of();

    private StructureManager() {
        super(StructurePattern.CODEC, FileToIdConverter.json(DIRECTORY));
    }

    @Override
    protected void apply(Map<Identifier, StructurePattern> loaded, ResourceManager manager, ProfilerFiller profiler) {
        // structures saved with the Pattern Maker are loaded too, a datapack structure with the same id wins
        Map<Identifier, StructurePattern> merged = new HashMap<>(loadExported());
        merged.putAll(loaded);

        structures = Map.copyOf(merged);
        Multiblocks.LOGGER.info("Loaded {} miner structures", merged.size());
    }

    /** Adds or replaces a structure until the next reload. */
    public void register(Identifier id, StructurePattern pattern) {
        Map<Identifier, StructurePattern> updated = new HashMap<>(structures);
        updated.put(id, pattern);
        structures = Map.copyOf(updated);
    }

    public @Nullable StructurePattern get(Identifier id) {
        return structures.get(id);
    }

    public Map<Identifier, StructurePattern> all() {
        return structures;
    }

    private static Map<Identifier, StructurePattern> loadExported() {
        Map<Identifier, StructurePattern> exported = new HashMap<>();
        Path directory = PatternExporter.directory();
        if (!Files.isDirectory(directory)) return exported;

        try (Stream<Path> files = Files.list(directory)) {
            files.filter(file -> file.getFileName().toString().endsWith(".json")).forEach(file -> {
                String fileName = file.getFileName().toString();
                String name = fileName.substring(0, fileName.length() - ".json".length());

                try (Reader reader = Files.newBufferedReader(file)) {
                    StructurePattern.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader))
                            .ifSuccess(pattern -> exported.put(Multiblocks.identifier(name), pattern))
                            .ifError(error -> Multiblocks.LOGGER.error("Couldn't parse exported structure {}: {}", file, error.message()));
                } catch (IOException | RuntimeException e) {
                    Multiblocks.LOGGER.error("Couldn't read exported structure {}", file, e);
                }
            });
        } catch (IOException e) {
            Multiblocks.LOGGER.error("Couldn't list exported structures in {}", directory, e);
        }
        return exported;
    }
}
