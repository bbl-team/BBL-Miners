package com.benbenlaw.multiblocks.recipe;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SizedShapedPatternTest {

    /** A grid written as 9 characters, row by row: '.' is an empty slot, anything else is an item named by that letter. */
    private static SizedShapedPattern.SlotCheck grid(String cells) {
        return new SizedShapedPattern.SlotCheck() {
            @Override
            public boolean isEmpty(int slot) {
                return cells.charAt(slot) == '.';
            }

            @Override
            public boolean accepts(char key, int slot) {
                return cells.charAt(slot) == key;
            }
        };
    }

    private static String layout(List<String> rows, String cells) {
        char[] layout = SizedShapedPattern.layout(rows, grid(cells));
        return layout == null ? null : new String(layout).replace(' ', '.');
    }

    @Test
    void aSingleKeyFindsItsItemInAnySlot() {
        assertEquals("....F....", layout(List.of("F"), "....F...."));
        assertEquals("F........", layout(List.of("F"), "F........"));
        assertEquals("........F", layout(List.of("F"), "........F"));
    }

    @Test
    void everyOtherSlotMustBeEmpty() {
        assertNull(layout(List.of("F"), "F...F...."));
        assertNull(layout(List.of("F"), "F..X....."));
    }

    @Test
    void anEmptyGridMatchesNothing() {
        assertNull(layout(List.of("F"), "........."));
    }

    @Test
    void theWrongItemDoesNotMatch() {
        assertNull(layout(List.of("F"), "....X...."));
    }

    @Test
    void aSmallPatternCanSitAnywhereInTheGrid() {
        List<String> rows = List.of("AB", "CD");

        assertEquals("AB.CD....", layout(rows, "AB.CD...."));
        assertEquals(".AB.CD...", layout(rows, ".AB.CD..."));
        assertEquals("...AB.CD.", layout(rows, "...AB.CD."));
        assertEquals("....AB.CD", layout(rows, "....AB.CD"));
    }

    @Test
    void gapsInThePatternMustBeEmptySlots() {
        List<String> rows = List.of("A A");

        assertEquals("A.A......", layout(rows, "A.A......"));
        assertNull(layout(rows, "AXA......"));
    }

    @Test
    void thePatternCanBeMirrored() {
        // "AB" is placed as "BA" in the grid
        assertEquals("BA.......", layout(List.of("AB"), "BA......."));
    }

    @Test
    void aFullGridPatternNeedsEverySlot() {
        List<String> rows = List.of("AAA", "ABA", "AAA");

        assertEquals("AAAABAAAA", layout(rows, "AAAABAAAA"));
        assertNull(layout(rows, "AAAA.AAAA"));
    }
}
