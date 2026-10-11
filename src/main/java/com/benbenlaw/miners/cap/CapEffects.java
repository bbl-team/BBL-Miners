package com.benbenlaw.miners.cap;

public record CapEffects(int efficiency, int fortune, boolean silkTouch) {

    public static final CapEffects NONE = new CapEffects(0, 0, false);

    public static final double SPEED_PER_EFFICIENCY_LEVEL = 0.25;

    public CapEffects with(CapType type, int level) {
        return switch (type) {
            case EFFICIENCY -> new CapEffects(efficiency + level, fortune, silkTouch);
            case FORTUNE -> new CapEffects(efficiency, fortune + level, silkTouch);
            case SILK_TOUCH -> new CapEffects(efficiency, fortune, true);
        };
    }

    public double speedMultiplier() {
        return 1.0 + SPEED_PER_EFFICIENCY_LEVEL * efficiency;
    }

    public int duration(int baseDuration) {
        return Math.max(1, (int) Math.round(baseDuration / speedMultiplier()));
    }
}
