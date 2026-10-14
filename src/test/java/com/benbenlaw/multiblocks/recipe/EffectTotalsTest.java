package com.benbenlaw.multiblocks.recipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectTotalsTest {

    @Test
    void noEffectsChangeNothing() {
        assertEquals(200, EffectTotals.NONE.duration(200));
        assertEquals(40, EffectTotals.NONE.energyPerTick(40));
        assertEquals(3, EffectTotals.NONE.outputCount(3));
    }

    @Test
    void durationMultipliersStack() {
        EffectTotals totals = EffectTotals.NONE
                .with(new OptionalEffect.Duration(0.5))
                .with(new OptionalEffect.Duration(0.5));

        assertEquals(0.25, totals.durationMultiplier(), 1e-9);
        assertEquals(50, totals.duration(200));
    }

    @Test
    void anOptionalItemCanBeFasterAndCostMoreEnergy() {
        EffectTotals totals = EffectTotals.NONE
                .with(new OptionalEffect.Duration(0.5))
                .with(new OptionalEffect.Energy(1.5));

        assertEquals(100, totals.duration(200));
        assertEquals(60, totals.energyPerTick(40));
    }

    @Test
    void outputCountAppliesTheMultiplierThenAddsTheBonus() {
        EffectTotals totals = EffectTotals.NONE
                .with(new OptionalEffect.OutputCount(0, 2.0))
                .with(new OptionalEffect.OutputCount(1, 1.0));

        // 3 * 2 = 6, plus 1
        assertEquals(7, totals.outputCount(3));
    }

    @Test
    void theOutputCountNeverDropsBelowOne() {
        EffectTotals totals = EffectTotals.NONE.with(new OptionalEffect.OutputCount(-10, 1.0));

        assertEquals(1, totals.outputCount(2));
    }

    @Test
    void durationNeverDropsBelowOneTick() {
        assertEquals(1, EffectTotals.NONE.with(new OptionalEffect.Duration(0.0001)).duration(10));
    }

    @Test
    void energyCanBeReducedToNothing() {
        assertEquals(0, EffectTotals.NONE.with(new OptionalEffect.Energy(0)).energyPerTick(40));
    }

    @Test
    void effectTypesHaveStableSerializedNames() {
        assertEquals("duration", OptionalEffect.Type.DURATION.getSerializedName());
        assertEquals("energy", OptionalEffect.Type.ENERGY.getSerializedName());
        assertEquals("output_count", OptionalEffect.Type.OUTPUT_COUNT.getSerializedName());
        assertEquals("bonus_output", OptionalEffect.Type.BONUS_OUTPUT.getSerializedName());
        assertEquals("replace_result", OptionalEffect.Type.REPLACE_RESULT.getSerializedName());
    }
}
