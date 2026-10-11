package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.structure.StructurePattern;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.miners.data.custom.StructurePatternBuilder.pattern;

public class MinersStructureProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public MinersStructureProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "bblminers/structure");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        Map<Identifier, StructurePattern> structures = new LinkedHashMap<>();
        addStructures(structures);
        return DataProvider.saveAll(cache, StructurePattern.CODEC, pathProvider, structures);
    }

    private void addStructures(Map<Identifier, StructurePattern> structures) {

        structures.put(Miners.identifier("cobble_miner"), pattern()
                .layer("CCC", "CCC", "CCC")
                .layer("CCC", "CCC", "CCC")
                .layer("CCC", "CCC", "CCC")
                .layer("DDD", "DDD", "DDD")
                .layer("   ", " @ ", "   ")
                .key('C', "minecraft:cobblestone")
                .key('D', "minecraft:stone")
                .cap(1, 0, 0)
                .cap(-1, 0, 0)
                .cap(0, 0, 1)
                .cap(0, 0, -1)
                .build());
    }

    @Override
    public @NotNull String getName() {
        return Miners.MOD_ID + " Structures";
    }
}
