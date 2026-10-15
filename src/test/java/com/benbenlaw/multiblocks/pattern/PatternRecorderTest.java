package com.benbenlaw.multiblocks.pattern;

import com.benbenlaw.multiblocks.structure.BlockMatcher;
import com.benbenlaw.multiblocks.structure.CompiledStructure;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatternRecorderTest {

    private static BlockMatcher block(String id) {
        return BlockMatcher.block(Identifier.parse(id));
    }

    private static BlockMatcher tag(String id) {
        return BlockMatcher.tag(Identifier.parse(id));
    }

    /**
     * Builds the cells from drawings. Layers are listed bottom to top (like building it), rows run north to south.
     * '.' is air, '@' the controller, 'c' a cap block, 'a' cobblestone, 'b' stone, 'f' a frame tag.
     */
    private static PatternRecorder.Cells draw(String[][] layers) {
        return (x, y, z) -> switch (layers[y][z].charAt(x)) {
            case '@' -> PatternRecorder.Cell.CONTROLLER;
            case 'c' -> PatternRecorder.Cell.CAP;
            case 'a' -> PatternRecorder.Cell.block(block("minecraft:cobblestone"));
            case 'b' -> PatternRecorder.Cell.block(block("minecraft:stone"));
            case 'f' -> PatternRecorder.Cell.block(tag("bblmultiblocks:tier_1_frame"));
            default -> PatternRecorder.Cell.AIR;
        };
    }

    private static PatternRecorder.Outcome record(String[][] layers) {
        return PatternRecorder.record(layers[0][0].length(), layers.length, layers[0].length, draw(layers));
    }

    @Test
    void theTopLayerComesFirstAndTheControllerIsMarked() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {"aaa", "aaa", "aaa"},
                {"...", ".@.", "..."}
        });

        assertTrue(outcome.ok());
        assertEquals(List.of("   ", " @ ", "   "), outcome.pattern().layers().get(0));
        assertEquals(List.of("AAA", "AAA", "AAA"), outcome.pattern().layers().get(1));
    }

    @Test
    void theRecordedPatternMatchesTheBuild() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {"aaa", "aaa", "aaa"},
                {"...", ".@.", "..."}
        });

        CompiledStructure compiled = outcome.pattern().compile();
        assertEquals(9, compiled.entries().size());
        for (CompiledStructure.Entry entry : compiled.entries()) {
            assertEquals(-1, entry.dy());
            assertEquals(block("minecraft:cobblestone"), entry.block());
        }
        assertEquals(2, outcome.pattern().layers().size());
        assertEquals("@", outcome.pattern().layers().get(0).get(1).trim());
    }

    @Test
    void sameBlocksShareAKeyAndDifferentBlocksGetTheirOwn() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {"aba", "bab", "aba"},
                {"...", ".@.", "..."}
        });

        assertEquals(2, outcome.pattern().keys().size());
        assertTrue(outcome.pattern().keys().containsValue(block("minecraft:cobblestone")));
        assertTrue(outcome.pattern().keys().containsValue(block("minecraft:stone")));
    }

    @Test
    void tagsSurviveAsTagKeys() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {"fff", "faf", "fff"},
                {"...", ".@.", "..."}
        });

        assertTrue(outcome.pattern().keys().containsValue(tag("bblmultiblocks:tier_1_frame")));
    }

    @Test
    void capBlocksBecomeCapSlotsRelativeToTheController() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {"aaa", "aaa", "aaa"},
                {"c.c", ".@.", "..."}
        });

        assertTrue(outcome.ok());
        List<Vec3i> caps = outcome.pattern().caps();
        assertEquals(2, caps.size());
        assertTrue(caps.contains(new Vec3i(-1, 0, -1)));
        assertTrue(caps.contains(new Vec3i(1, 0, -1)));
        // a cap is never written into the pattern as a block
        assertEquals(1, outcome.pattern().keys().size());
    }

    @Test
    void aCapOutsideTheBuiltBlocksStillCounts() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {"aaa", "aaa", "aaa"},
                {"...", ".@.", "..c"}
        });

        assertEquals(List.of(new Vec3i(1, 0, 1)), outcome.pattern().caps());
    }

    @Test
    void emptySpaceAroundTheBuildIsTrimmedAway() {
        PatternRecorder.Outcome outcome = record(new String[][]{
                {".....", ".....", "..a..", ".....", "....."},
                {".....", ".....", "..@..", ".....", "....."}
        });

        assertEquals(List.of("@"), outcome.pattern().layers().get(0));
        assertEquals(List.of("A"), outcome.pattern().layers().get(1));
    }

    @Test
    void aSelectionWithoutAControllerIsRejected() {
        assertEquals(PatternRecorder.Error.NO_CONTROLLER, record(new String[][]{{"aaa"}}).error());
    }

    @Test
    void twoControllersAreRejected() {
        assertEquals(PatternRecorder.Error.MULTIPLE_CONTROLLERS, record(new String[][]{{"a@@"}}).error());
    }

    @Test
    void aControllerAloneIsRejected() {
        assertEquals(PatternRecorder.Error.EMPTY, record(new String[][]{{".@."}}).error());
    }

    @Test
    void theRecordedPatternSurvivesTheStructureCodec() {
        StructurePattern pattern = record(new String[][]{
                {"aaa", "aba", "aaa"},
                {"c.c", ".@.", "c.c"}
        }).pattern();

        assertTrue(StructurePattern.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, pattern).result().isPresent());
    }

    @Test
    void theSameBlockInDifferentStatesGetsDifferentKeys() {
        BlockMatcher north = BlockMatcher.parse("minecraft:oak_stairs[facing=north,half=bottom]").getOrThrow();
        BlockMatcher south = BlockMatcher.parse("minecraft:oak_stairs[facing=south,half=bottom]").getOrThrow();

        PatternRecorder.Outcome outcome = PatternRecorder.record(4, 1, 1, (x, y, z) -> switch (x) {
            case 0, 1 -> PatternRecorder.Cell.block(north);
            case 2 -> PatternRecorder.Cell.block(south);
            default -> PatternRecorder.Cell.CONTROLLER;
        });

        assertTrue(outcome.ok());
        assertEquals(2, outcome.pattern().keys().size());
        assertTrue(outcome.pattern().keys().containsValue(north));
        assertTrue(outcome.pattern().keys().containsValue(south));
        assertEquals(List.of("AABV".replace('V', '@')), outcome.pattern().layers().get(0));
    }
}
