package com.benbenlaw.multiblocks.structure;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BlockMatcherTest {

    private static BlockMatcher parse(String text) {
        return BlockMatcher.parse(text).getOrThrow();
    }

    @Test
    void aPlainBlockHasNoProperties() {
        BlockMatcher matcher = parse("minecraft:cobblestone");

        assertEquals(Identifier.parse("minecraft:cobblestone"), matcher.id());
        assertFalse(matcher.tag());
        assertTrue(matcher.properties().isEmpty());
    }

    @Test
    void aTagStartsWithAHash() {
        BlockMatcher matcher = parse("#bblmultiblocks:tier_1_frame");

        assertTrue(matcher.tag());
        assertEquals(Identifier.parse("bblmultiblocks:tier_1_frame"), matcher.id());
    }

    @Test
    void propertiesAreReadInBrackets() {
        BlockMatcher matcher = parse("minecraft:oak_stairs[half=bottom,facing=north]");

        assertEquals(Map.of("facing", "north", "half", "bottom"), matcher.properties());
    }

    @Test
    void spacesAroundPropertiesAreIgnored() {
        assertEquals(parse("minecraft:oak_stairs[facing=north,half=bottom]"), parse("minecraft:oak_stairs[ facing = north , half = bottom ]"));
    }

    @Test
    void emptyBracketsMeanNoProperties() {
        assertEquals(parse("minecraft:stone"), parse("minecraft:stone[]"));
    }

    @Test
    void theStringFormIsSortedAndRoundTrips() {
        String text = parse("minecraft:oak_stairs[half=bottom,facing=north]").toString();

        assertEquals("minecraft:oak_stairs[facing=north,half=bottom]", text);
        assertEquals(parse(text), parse("minecraft:oak_stairs[facing=north,half=bottom]"));
        assertEquals("#bblmultiblocks:tier_1_frame", parse("#bblmultiblocks:tier_1_frame").toString());
        assertEquals("minecraft:stone", parse("minecraft:stone").toString());
    }

    @Test
    void propertiesCanBeDroppedForGrouping() {
        assertEquals(parse("minecraft:oak_stairs"), parse("minecraft:oak_stairs[facing=north]").withoutProperties());
    }

    @Test
    void badTextIsRejected() {
        assertTrue(BlockMatcher.parse("minecraft:oak_stairs[facing=north").error().isPresent());
        assertTrue(BlockMatcher.parse("minecraft:oak_stairs[facing]").error().isPresent());
        assertTrue(BlockMatcher.parse("minecraft:oak_stairs[facing=]").error().isPresent());
        assertTrue(BlockMatcher.parse("minecraft:oak_stairs[facing=north,facing=south]").error().isPresent());
        assertTrue(BlockMatcher.parse("#minecraft:logs[axis=y").error().isPresent());
        assertTrue(BlockMatcher.parse("Not A Block").error().isPresent());
    }

    @Test
    void aTagCanHaveProperties() {
        BlockMatcher matcher = parse("#minecraft:wooden_stairs[facing=east,half=bottom,shape=straight]");

        assertTrue(matcher.tag());
        assertEquals(Identifier.parse("minecraft:wooden_stairs"), matcher.id());
        assertEquals(Map.of("facing", "east", "half", "bottom", "shape", "straight"), matcher.properties());
        assertEquals("#minecraft:wooden_stairs[facing=east,half=bottom,shape=straight]", matcher.toString());
        assertEquals(parse("#minecraft:wooden_stairs"), matcher.withoutProperties());
    }
}
