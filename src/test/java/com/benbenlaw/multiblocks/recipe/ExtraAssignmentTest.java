package com.benbenlaw.multiblocks.recipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtraAssignmentTest {

    @Test
    void noExtrasAlwaysSucceeds() {
        assertArrayEquals(new int[0], ExtraAssignment.assign(0, 9, (needed, slot) -> false));
    }

    @Test
    void failsWhenAnIngredientHasNoSlot() {
        assertNull(ExtraAssignment.assign(1, 9, (needed, slot) -> false));
    }

    @Test
    void eachIngredientGetsADifferentSlot() {
        int[] slots = ExtraAssignment.assign(3, 9, (needed, slot) -> true);

        assertNotNull(slots);
        assertEquals(3, slots.length);
        assertEquals(3, java.util.Arrays.stream(slots).distinct().count());
    }

    @Test
    void twoIngredientsCannotShareOneSlot() {
        // both ingredients only fit slot 4
        assertNull(ExtraAssignment.assign(2, 9, (needed, slot) -> slot == 4));
    }

    @Test
    void overlappingIngredientsAreResolvedByBacktracking() {
        // ingredient 0 fits slots 0 and 1, ingredient 1 only fits slot 0: a greedy pick of slot 0 for ingredient 0 would fail
        int[] slots = ExtraAssignment.assign(2, 2, (needed, slot) -> needed == 0 || slot == 0);

        assertArrayEquals(new int[]{1, 0}, slots);
    }
}
