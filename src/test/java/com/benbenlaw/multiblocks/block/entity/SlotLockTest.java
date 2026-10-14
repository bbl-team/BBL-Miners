package com.benbenlaw.multiblocks.block.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlotLockTest {

    private static SlotLock<String> lock(int size) {
        return new SlotLock<>(size, "", String::equals, String::isEmpty);
    }

    @Test
    void anUnlockedSlotAcceptsAnything() {
        SlotLock<String> lock = lock(3);

        assertFalse(lock.isLocked());
        assertTrue(lock.allows(0, "furnace"));
        assertTrue(lock.allows(2, "anything"));
    }

    @Test
    void aLockedSlotOnlyAcceptsWhatItHeld() {
        SlotLock<String> lock = lock(3);
        List<String> contents = List.of("furnace", "", "redstone");

        lock.lock(contents::get);

        assertTrue(lock.isLocked());
        assertTrue(lock.allows(0, "furnace"));
        assertFalse(lock.allows(0, "redstone"));
        assertTrue(lock.allows(2, "redstone"));
        assertFalse(lock.allows(2, "furnace"));
    }

    @Test
    void anEmptySlotStaysClosedToAutomationWhileLocked() {
        SlotLock<String> lock = lock(3);

        lock.lock(slot -> slot == 0 ? "furnace" : "");

        assertFalse(lock.allows(1, "furnace"));
        assertFalse(lock.allows(2, "anything"));
    }

    @Test
    void unlockingOpensEverySlotAgainAndForgetsTheFilters() {
        SlotLock<String> lock = lock(2);
        lock.lock(slot -> "furnace");

        lock.unlock();

        assertFalse(lock.isLocked());
        assertTrue(lock.allows(0, "redstone"));
        assertEquals("", lock.filter(0));
    }

    @Test
    void lockingAgainTakesTheNewContents() {
        SlotLock<String> lock = lock(1);
        lock.lock(slot -> "furnace");
        lock.unlock();

        lock.lock(slot -> "redstone");

        assertTrue(lock.allows(0, "redstone"));
        assertFalse(lock.allows(0, "furnace"));
    }

    @Test
    void aSavedStateCanBeRestored() {
        SlotLock<String> lock = lock(2);

        lock.load(true, slot -> slot == 0 ? "furnace" : "");

        assertTrue(lock.isLocked());
        assertEquals("furnace", lock.filter(0));
        assertTrue(lock.allows(0, "furnace"));
        assertFalse(lock.allows(1, "furnace"));

        lock.load(false, slot -> "ignored");
        assertFalse(lock.isLocked());
        assertEquals("", lock.filter(0));
    }
}
