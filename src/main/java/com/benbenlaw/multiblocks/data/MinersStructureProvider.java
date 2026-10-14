package com.benbenlaw.multiblocks.data;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.structure.StructureManager;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.multiblocks.data.custom.StructurePatternBuilder.pattern;

public class MinersStructureProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public MinersStructureProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, StructureManager.DIRECTORY);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        Map<Identifier, StructurePattern> structures = new LinkedHashMap<>();
        addStructures(structures);
        return DataProvider.saveAll(cache, StructurePattern.CODEC, pathProvider, structures);
    }

    private void addStructures(Map<Identifier, StructurePattern> structures) {

        addSimpleMiner(structures, "cobblestone_miner", "minecraft:cobblestone", "bblmultiblocks:tier_1_frame");
        addSimpleMiner(structures, "stone_miner", "minecraft:stone", "bblmultiblocks:tier_2_frame");
        addSimpleMiner(structures, "granite_miner", "minecraft:granite", "bblmultiblocks:tier_2_frame");
        addSimpleMiner(structures, "diorite_miner", "minecraft:diorite", "bblmultiblocks:tier_2_frame");
        addSimpleMiner(structures, "andesite_miner", "minecraft:andesite", "bblmultiblocks:tier_2_frame");

        structures.put(Multiblocks.identifier("crafting_demo"), pattern()
                .layer("   ", " @ ", "   ")
                .layer("SSS", "SSS", "SSS")
                .key('S', "minecraft:stone_bricks")
                .cap(1, 0, 0)
                .cap(-1, 0, 0)
                .build());

        // Showcase: a frame of any tier 3 or better, mixed blocks and eight cap slots around the controller
        structures.put(Multiblocks.identifier("showcase_altar"), pattern()
                .layer("F   F", "     ", "  @  ", "     ", "F   F")
                .layer("FSSSF", "SGGGS", "SGGGS", "SGGGS", "FSSSF")
                .layer("F   F", " OOO ", " ODO ", " OOO ", "F   F")
                .layer("FFFFF", "FOOOF", "FOOOF", "FOOOF", "FFFFF")
                .keyTag('F', Multiblocks.MOD_ID + ":tier_3_frame")
                .key('S', "minecraft:polished_blackstone_bricks")
                .key('G', "minecraft:gold_block")
                .key('O', "minecraft:obsidian")
                .key('D', "minecraft:diamond_block")
                .cap(1, 0, 0)
                .cap(-1, 0, 0)
                .cap(0, 0, 1)
                .cap(0, 0, -1)
                .cap(2, 0, 0)
                .cap(-2, 0, 0)
                .cap(0, 0, 2)
                .cap(0, 0, -2)
                .build());


    }


    private void addSimpleMiner(Map<Identifier, StructurePattern> structures, String name, String block, String frameTag) {
        structures.put(Multiblocks.identifier(name), pattern()
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
        return Multiblocks.MOD_ID + " Structures";
    }
}
