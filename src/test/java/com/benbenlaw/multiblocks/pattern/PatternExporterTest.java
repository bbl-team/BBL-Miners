package com.benbenlaw.multiblocks.pattern;

import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternExporterTest {

    @Test
    void namesBecomeFileSafeIds() {
        assertEquals("my_cool_miner", PatternExporter.sanitize("My Cool Miner!"));
        assertEquals("deep_miner_2", PatternExporter.sanitize("  Deep-Miner  2 "));
        assertEquals("a_b", PatternExporter.sanitize("a___b"));
    }

    @Test
    void unusableNamesBecomeEmpty() {
        assertEquals("", PatternExporter.sanitize("   "));
        assertEquals("", PatternExporter.sanitize("!!!"));
        assertEquals("", PatternExporter.sanitize("___"));
    }

    @Test
    void longNamesAreCut() {
        assertEquals(PatternExporter.MAX_NAME_LENGTH, PatternExporter.sanitize("a".repeat(100)).length());
    }

    @Test
    void theSelectionSizeIsLimited() {
        assertFalse(PatternExporter.tooLarge(new BlockPos(0, 0, 0), new BlockPos(31, 31, 15)));
        assertTrue(PatternExporter.tooLarge(new BlockPos(0, 0, 0), new BlockPos(32, 0, 0)));
        assertTrue(PatternExporter.tooLarge(new BlockPos(0, 0, 0), new BlockPos(31, 31, 31)));
    }

    @Test
    void cornersCanBeGivenInAnyOrder() {
        assertFalse(PatternExporter.tooLarge(new BlockPos(10, 10, 10), new BlockPos(0, 0, 0)));
        assertTrue(PatternExporter.tooLarge(new BlockPos(40, 0, 0), new BlockPos(0, 0, 0)));
    }
}
