package com.benbenlaw.miners.cap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapEffectsTest {

    @Test
    void noCapsLeavesTheDurationAlone() {
        assertEquals(100, CapEffects.NONE.duration(100));
        assertFalse(CapEffects.NONE.silkTouch());
    }

    @Test
    void efficiencyLevelsStackAndShortenTheDuration() {
        CapEffects effects = CapEffects.NONE
                .with(CapType.EFFICIENCY, 1)
                .with(CapType.EFFICIENCY, 3);

        assertEquals(4, effects.efficiency());
        assertEquals(2.0, effects.speedMultiplier(), 1e-9);
        assertEquals(50, effects.duration(100));
    }

    @Test
    void fortuneStacksAndSilkTouchIsAFlag() {
        CapEffects effects = CapEffects.NONE
                .with(CapType.FORTUNE, 2)
                .with(CapType.FORTUNE, 1)
                .with(CapType.SILK_TOUCH, 1)
                .with(CapType.SILK_TOUCH, 1);

        assertEquals(3, effects.fortune());
        assertTrue(effects.silkTouch());
        assertEquals(0, effects.efficiency());
    }

    @Test
    void durationNeverDropsBelowOneTick() {
        assertEquals(1, new CapEffects(1000, 0, false).duration(10));
    }

    @Test
    void capTypeIdsRoundTrip() {
        for (CapType type : CapType.values()) {
            assertEquals(type, CapType.byId(type.getId()));
        }
        assertEquals("silk_touch", CapType.SILK_TOUCH.getId());
        assertNull(CapType.byId("speed"));
    }
}
