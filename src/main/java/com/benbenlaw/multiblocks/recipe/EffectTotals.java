package com.benbenlaw.multiblocks.recipe;

public record EffectTotals(double durationMultiplier, double energyMultiplier, int outputAdd, double outputMultiplier) {

    public static final EffectTotals NONE = new EffectTotals(1.0, 1.0, 0, 1.0);

    public EffectTotals with(ItemEffect effect) {
        return switch (effect) {
            case ItemEffect.Duration duration -> new EffectTotals(durationMultiplier * duration.multiplier(), energyMultiplier, outputAdd, outputMultiplier);
            case ItemEffect.Energy energy -> new EffectTotals(durationMultiplier, energyMultiplier * energy.multiplier(), outputAdd, outputMultiplier);
            case ItemEffect.OutputCount count -> new EffectTotals(durationMultiplier, energyMultiplier, outputAdd + count.add(), outputMultiplier * count.multiplier());
            case ItemEffect.BonusOutput ignored -> this;
            case ItemEffect.ReplaceResult ignored -> this;
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
