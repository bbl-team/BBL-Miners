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

        addSimpleMiner(structures, "cobblestone_miner", "minecraft:cobblestone", "bblminers:tier_1_frame");
        addSimpleMiner(structures, "stone_miner", "minecraft:stone", "bblminers:tier_2_frame");
        addSimpleMiner(structures, "granite_miner", "minecraft:granite", "bblminers:tier_2_frame");
        addSimpleMiner(structures, "diorite_miner", "minecraft:diorite", "bblminers:tier_2_frame");
        addSimpleMiner(structures, "andesite_miner", "minecraft:andesite", "bblminers:tier_2_frame");


    }


    private void addSimpleMiner(Map<Identifier, StructurePattern> structures, String name, String block, String frameTag) {
        structures.put(Miners.identifier(name), pattern()
                .layer("F   F", "     ", "  @  ", "     ", "F   F")
                .layer("FFFFF", "FCCCF", "FCCCF", "FCCCF", "FFFFF")
                .layer("F   F", " CCC ", " CCC ", " CCC ", "F   F")
                .layer("F   F", " CCC ", " CCC ", " CCC ", "F   F")
                .layer("F   F", " CCC ", " CCC ", " CCC ", "F   F")

                .key('C', block)
                .keyTag('F', frameTag)

                .cap(2, 1, 2)
                .cap(2, 1, -2)
                .cap(-2, 1, 2)
                .cap(-2, 1, -2)
                .build());
    }


    @Override
    public @NotNull String getName() {
        return Miners.MOD_ID + " Structures";
    }
}
