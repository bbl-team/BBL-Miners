package com.benbenlaw.multiblocks.recipe;

import org.jetbrains.annotations.Nullable;

public final class ExtraAssignment {

    public interface Fits {
        boolean test(int needed, int slot);
    }

    private ExtraAssignment() {
    }

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
