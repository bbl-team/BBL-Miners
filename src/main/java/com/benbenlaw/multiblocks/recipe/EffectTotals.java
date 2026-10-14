package com.benbenlaw.multiblocks.recipe;

/** The numeric effects of every active optional item added together. */
public record EffectTotals(double durationMultiplier, double energyMultiplier, int outputAdd, double outputMultiplier) {

    public static final EffectTotals NONE = new EffectTotals(1.0, 1.0, 0, 1.0);

    /** This total with one more effect applied. Effects that aren't numeric (items) leave it unchanged. */
    public EffectTotals with(OptionalEffect effect) {
        return switch (effect) {
            case OptionalEffect.Duration duration -> new EffectTotals(durationMultiplier * duration.multiplier(), energyMultiplier, outputAdd, outputMultiplier);
            case OptionalEffect.Energy energy -> new EffectTotals(durationMultiplier, energyMultiplier * energy.multiplier(), outputAdd, outputMultiplier);
            case OptionalEffect.OutputCount count -> new EffectTotals(durationMultiplier, energyMultiplier, outputAdd + count.add(), outputMultiplier * count.multiplier());
            case OptionalEffect.BonusOutput ignored -> this;
            case OptionalEffect.ReplaceResult ignored -> this;
        };
    }

    public int duration(int baseDuration) {
        return Math.max(1, (int) Math.round(baseDuration * durationMultiplier));
    }

    public int energyPerTick(int baseEnergy) {
        return Math.max(0, (int) Math.round(baseEnergy * energyMultiplier));
    }

    public int outputCount(int baseCount) {
        return Math.max(1, (int) Math.floor(baseCount * outputMultiplier) + outputAdd);
    }
}
