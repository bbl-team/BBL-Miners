package com.benbenlaw.multiblocks.recipe;

import com.benbenlaw.multiblocks.structure.StructureManager;
import net.minecraft.resources.Identifier;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/** Checks the data the mod ships (datagen output) rather than anything built in code. */
class CobbleMinerRecipeTest {

    private static final Path DATA_ROOT = projectRoot().resolve("src").resolve("generated").resolve("resources").resolve("data");
    private static final Path DATA = DATA_ROOT.resolve("bblmultiblocks");

    private static Path projectRoot() {
        Path dir = Path.of("").toAbsolutePath();
        while (dir != null && !Files.exists(dir.resolve("build.gradle"))) dir = dir.getParent();
        assertNotNull(dir, "could not find the project root");
        return dir;
    }

    private static JsonElement read(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return JsonParser.parseReader(reader);
        }
    }

    private static List<Path> jsonFiles(Path directory) throws IOException {
        assertTrue(Files.isDirectory(directory), directory + " does not exist, run datagen first");
        try (Stream<Path> files = Files.list(directory)) {
            return files.filter(path -> path.toString().endsWith(".json")).sorted().toList();
        }
    }

    @Test
    void everyShippedStructureParses() throws IOException {
        List<Path> structures = jsonFiles(DATA.resolve(StructureManager.DIRECTORY));
        assertFalse(structures.isEmpty());

        for (Path file : structures) {
            StructurePattern pattern = StructurePattern.CODEC.parse(JsonOps.INSTANCE, read(file)).getOrThrow();
            assertFalse(pattern.compile().entries().isEmpty(), file + " has no blocks");
        }
    }

    @Test
    void everyMinerRecipeParsesAndEveryStructureReferenceExists() throws IOException {
        List<Path> recipes = jsonFiles(DATA.resolve("recipe"));
        assertFalse(recipes.isEmpty());

        int minerRecipes = 0;
        int craftingRecipes = 0;

        for (Path file : recipes) {
            JsonElement json = read(file);
            String type = json.getAsJsonObject().get("type").getAsString();
            Identifier structureId;

            if (type.equals("bblmultiblocks:miner_structure")) {
                MinerStructureRecipe recipe = MinerStructureRecipe.CODEC.codec().parse(JsonOps.INSTANCE, json).getOrThrow();
                assertFalse(recipe.lootTables().isEmpty(), file + " has no loot tables");
                structureId = recipe.structureId();
                minerRecipes++;
            } else if (type.equals("bblmultiblocks:controller_crafting")) {
                // Ingredients need a running registry to parse, so only the structure reference is checked here
                var structure = json.getAsJsonObject().get("structure_id");
                if (structure == null) {
                    craftingRecipes++;
                    continue;
                }
                structureId = Identifier.parse(structure.getAsString());
                craftingRecipes++;
            } else {
                continue;
            }

            // The structure loader turns data/<namespace>/bblmultiblocks/structures/<id>.json into <namespace>:<id>
            Path structure = DATA_ROOT.resolve(structureId.getNamespace())
                    .resolve(StructureManager.DIRECTORY)
                    .resolve(structureId.getPath() + ".json");
            assertTrue(Files.exists(structure),
                    file.getFileName() + " points at structure " + structureId + " but " + structure + " does not exist");
        }

        assertTrue(minerRecipes > 0);
        assertTrue(craftingRecipes > 0);
    }
}
