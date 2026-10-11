package com.benbenlaw.miners.cap;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum CapType {
    SILK_TOUCH,
    FORTUNE,
    EFFICIENCY;

    /** The id a structure file uses for a cap slot, e.g. "silk_touch". */
    public String getId() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static @Nullable CapType byId(String id) {
        for (CapType type : values()) {
            if (type.getId().equals(id)) return type;
        }
        return null;
    }
}
