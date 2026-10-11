package com.benbenlaw.miners.recipe;

import com.benbenlaw.miners.structure.StructurePattern;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class CobbleMinerRecipeTest {

    private JsonElement resource(String path) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            assertNotNull(in, path + " is missing from resources");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
    }

    @Test
    void shippedStructureJsonParses() throws Exception {
        StructurePattern pattern = StructurePattern.CODEC
                .parse(JsonOps.INSTANCE, resource("/data/bblminers/bblminers/structure/cobble_miner.json")).getOrThrow();

        assertEquals(36, pattern.compile().entries().size());
    }

    @Test
    void shippedRecipeJsonParsesAndPointsAtTheStructure() throws Exception {
        MinerStructureRecipe recipe = MinerStructureRecipe.CODEC.codec()
                .parse(JsonOps.INSTANCE, resource("/data/bblminers/recipe/cobble_miner.json")).getOrThrow();

        assertEquals("bblminers:cobble_miner", recipe.structureId().toString());
        assertEquals(1, recipe.lootTables().size());
        assertEquals("minecraft:blocks/cobblestone", recipe.lootTables().getFirst().toString());
        assertEquals(100, recipe.baseDuration());
        assertEquals(20, recipe.energyPerTick());
        assertNotNull(getClass().getResource("/data/bblminers/bblminers/structure/" + recipe.structureId().getPath() + ".json"));
    }
}
