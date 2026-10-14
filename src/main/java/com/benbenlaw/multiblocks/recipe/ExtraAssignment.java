package com.benbenlaw.multiblocks.recipe;

import org.jetbrains.annotations.Nullable;

/** Works out which research slot satisfies which extra ingredient, trying every combination so overlapping ingredients still work. */
public final class ExtraAssignment {

    public interface Fits {
        boolean test(int needed, int slot);
    }

    private ExtraAssignment() {
    }

    /** One slot index per needed ingredient (in order), or null when they can't all be satisfied by different slots. */
    public static int @Nullable [] assign(int needed, int slots, Fits fits) {
        int[] result = new int[needed];
        return place(0, needed, slots, fits, new boolean[slots], result) ? result : null;
    }

    private static boolean place(int index, int needed, int slots, Fits fits, boolean[] used, int[] result) {
        if (index == needed) return true;

        for (int slot = 0; slot < slots; slot++) {
            if (used[slot] || !fits.test(index, slot)) continue;

            used[slot] = true;
            result[index] = slot;
            if (place(index + 1, needed, slots, fits, used, result)) return true;
            used[slot] = false;
        }
        return false;
    }
}
