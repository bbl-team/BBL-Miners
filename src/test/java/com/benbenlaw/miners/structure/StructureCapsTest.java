package com.benbenlaw.miners.structure;

import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Vec3i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureCapsTest {

    private static DataResult<StructurePattern> parse(String json) {
        return StructurePattern.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json));
    }

    @Test
    void capsAreOptionalAndDefaultToEmpty() {
        StructurePattern pattern = parse("""
                { "pattern": [["C@"]], "keys": { "C": "minecraft:stone" } }
                """).getOrThrow();

        assertTrue(pattern.caps().isEmpty());
        assertTrue(pattern.compile().caps().isEmpty());
    }

    @Test
    void capsArePlainPositionsRelativeToTheController() {
        StructurePattern pattern = parse("""
                {
                  "pattern": [["CCC", "C@C", "CCC"], ["   ", "   ", "   "]],
                  "keys": { "C": "minecraft:stone" },
                  "caps": [ [0, 1, 0], [1, 1, 0] ]
                }
                """).getOrThrow();

        assertEquals(2, pattern.caps().size());
        assertEquals(new Vec3i(0, 1, 0), pattern.compile().caps().getFirst());
        assertEquals(new Vec3i(1, 1, 0), pattern.compile().caps().get(1));
    }

    @Test
    void rejectsACapOnTopOfAPatternBlock() {
        assertTrue(parse("""
                { "pattern": [["C@"]], "keys": { "C": "minecraft:stone" }, "caps": [ [-1, 0, 0] ] }
                """).error().isPresent());
    }

    @Test
    void rejectsACapOnTheController() {
        assertTrue(parse("""
                { "pattern": [["C@"]], "keys": { "C": "minecraft:stone" }, "caps": [ [0, 0, 0] ] }
                """).error().isPresent());
    }

    @Test
    void rejectsDuplicateCapPositions() {
        assertTrue(parse("""
                { "pattern": [["C@"]], "keys": { "C": "minecraft:stone" }, "caps": [ [0, 1, 0], [0, 1, 0] ] }
                """).error().isPresent());
    }

    @Test
    void acceptsACapInsideTheBoundsWhenThatCellIsASpace() {
        assertTrue(parse("""
                { "pattern": [["C @"]], "keys": { "C": "minecraft:stone" }, "caps": [ [-1, 0, 0] ] }
                """).result().isPresent());
    }
}
