package com.benbenlaw.miners.structure;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StructurePatternTest {

    private static final String COBBLE_MINER = """
            {
              "pattern": [
                ["   ", " @ ", "   "],
                ["CCC", "CCC", "CCC"],
                ["CCC", "CCC", "CCC"],
                ["CCC", "CCC", "CCC"]
              ],
              "keys": { "C": "minecraft:cobblestone" }
            }
            """;

    private static DataResult<StructurePattern> parse(String json) {
        JsonElement element = JsonParser.parseString(json);
        return StructurePattern.CODEC.parse(JsonOps.INSTANCE, element);
    }

    private static ExtraCodecs.TagOrElementLocation block(String id) {
        return new ExtraCodecs.TagOrElementLocation(Identifier.parse(id), false);
    }

    private static CompiledStructure cobbleMiner() {
        return parse(COBBLE_MINER).getOrThrow().compile();
    }

    @Test
    void cobbleMinerHas27EntriesBelowTheController() {
        CompiledStructure structure = cobbleMiner();

        assertEquals(27, structure.entries().size());
        for (CompiledStructure.Entry entry : structure.entries()) {
            assertTrue(entry.dy() >= -3 && entry.dy() <= -1, "dy " + entry.dy());
            assertTrue(entry.dx() >= -1 && entry.dx() <= 1, "dx " + entry.dx());
            assertTrue(entry.dz() >= -1 && entry.dz() <= 1, "dz " + entry.dz());
            assertEquals(block("minecraft:cobblestone"), entry.block());
        }
    }

    @Test
    void tagKeysAreParsedAsTags() {
        StructurePattern pattern = parse("""
                { "pattern": [["L@"]], "keys": { "L": "#minecraft:logs" } }
                """).getOrThrow();

        ExtraCodecs.TagOrElementLocation log = pattern.keys().get('L');
        assertTrue(log.tag());
        assertEquals(Identifier.parse("minecraft:logs"), log.id());
        assertEquals(-1, pattern.compile().entries().getFirst().dx());
    }

    @Test
    void matchesWhenEveryBlockIsCobblestone() {
        Map<String, String> world = new HashMap<>();
        for (int x = 9; x <= 11; x++) for (int y = 61; y <= 63; y++) for (int z = 19; z <= 21; z++) {
            world.put(x + "," + y + "," + z, "minecraft:cobblestone");
        }

        assertTrue(cobbleMiner().matches(10, 64, 20, (x, y, z, b) ->
                b.id().toString().equals(world.get(x + "," + y + "," + z))));
    }

    @Test
    void theFirstLayerIsTheTop() {
        StructurePattern pattern = parse("""
                { "pattern": [["@"], ["A"], ["B"]],
                  "keys": { "A": "minecraft:stone", "B": "minecraft:dirt" } }
                """).getOrThrow();

        CompiledStructure.Entry first = pattern.compile().entries().getFirst();
        assertEquals(-1, first.dy());
        assertEquals(block("minecraft:stone"), first.block());
        assertEquals(-2, pattern.compile().entries().get(1).dy());
        assertEquals(block("minecraft:dirt"), pattern.compile().entries().get(1).block());
    }

    @Test
    void reportsTheFirstMismatch() {
        Map<String, String> world = new HashMap<>();
        for (int x = 9; x <= 11; x++) for (int y = 61; y <= 63; y++) for (int z = 19; z <= 21; z++) {
            world.put(x + "," + y + "," + z, "minecraft:cobblestone");
        }
        world.put("10,62,20", "minecraft:stone");

        CompiledStructure.BlockAccess access = (x, y, z, b) -> b.id().toString().equals(world.get(x + "," + y + "," + z));
        CompiledStructure.Entry mismatch = cobbleMiner().firstMismatch(10, 64, 20, access).orElseThrow();

        assertEquals(0, mismatch.dx());
        assertEquals(-2, mismatch.dy());
        assertEquals(0, mismatch.dz());
    }

    @Test
    void rejectsMissingController() {
        assertTrue(parse("""
                { "pattern": [["CCC"]], "keys": { "C": "minecraft:stone" } }
                """).error().isPresent());
    }

    @Test
    void rejectsTwoControllers() {
        assertTrue(parse("""
                { "pattern": [["@@"]], "keys": {} }
                """).error().isPresent());
    }

    @Test
    void rejectsUndefinedCharacter() {
        assertTrue(parse("""
                { "pattern": [["X@"]], "keys": {} }
                """).error().isPresent());
    }

    @Test
    void rejectsUnusedKey() {
        assertTrue(parse("""
                { "pattern": [["@"]], "keys": { "C": "minecraft:stone" } }
                """).error().isPresent());
    }

    @Test
    void rejectsRaggedRows() {
        assertTrue(parse("""
                { "pattern": [["CC", "C@"], ["CC", "C"]], "keys": { "C": "minecraft:stone" } }
                """).error().isPresent());
        assertTrue(parse("""
                { "pattern": [["CC", "C@"], ["CC"]], "keys": { "C": "minecraft:stone" } }
                """).error().isPresent());
    }

    @Test
    void rejectsReservedKeys() {
        assertTrue(parse("""
                { "pattern": [["@"]], "keys": { "@": "minecraft:stone" } }
                """).error().isPresent());
        assertTrue(parse("""
                { "pattern": [["@"]], "keys": { "CC": "minecraft:stone" } }
                """).error().isPresent());
    }
}
