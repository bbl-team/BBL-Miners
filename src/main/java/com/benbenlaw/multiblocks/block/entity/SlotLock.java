package com.benbenlaw.multiblocks.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * Remembers what each input slot held when it was locked. While locked, automation may only put that same item into that
 * slot, and slots that were empty at the time stay closed to automation. Players can always use the slots by hand.
 */
public final class SlotLock<T> {

    private final T empty;
    private final BiPredicate<T, T> same;
    private final Predicate<T> isEmpty;
    private final List<T> filters;
    private boolean locked;

    public SlotLock(int size, T empty, BiPredicate<T, T> same, Predicate<T> isEmpty) {
        this.empty = empty;
        this.same = same;
        this.isEmpty = isEmpty;
        this.filters = new ArrayList<>(size);
        for (int i = 0; i < size; i++) filters.add(empty);
    }

    public boolean isLocked() {
        return locked;
    }

    public int size() {
        return filters.size();
    }

    /** What the slot held when it was locked, or the empty value. */
    public T filter(int slot) {
        return filters.get(slot);
    }

    /** True when automation may put this into the slot. */
    public boolean allows(int slot, T candidate) {
        if (!locked) return true;

        T filter = filters.get(slot);
        return !isEmpty.test(filter) && same.test(filter, candidate);
    }

    /** Locks every slot to whatever it holds right now. */
    public void lock(IntFunction<T> currentContents) {
        for (int slot = 0; slot < filters.size(); slot++) filters.set(slot, currentContents.apply(slot));
        locked = true;
    }

    public void unlock() {
        for (int slot = 0; slot < filters.size(); slot++) filters.set(slot, empty);
        locked = false;
    }

    /** Restores a saved state. */
    public void load(boolean locked, IntFunction<T> savedFilters) {
        for (int slot = 0; slot < filters.size(); slot++) filters.set(slot, locked ? savedFilters.apply(slot) : empty);
        this.locked = locked;
    }
}
