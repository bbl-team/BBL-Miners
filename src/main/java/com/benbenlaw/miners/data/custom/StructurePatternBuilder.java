package com.benbenlaw.miners.data.custom;

import com.benbenlaw.miners.structure.StructurePattern;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a {@link StructurePattern} layer by layer, bottom to top. Each layer is a list of rows (north to south),
 * and each row is a string of keys (west to east). '@' is the controller and a space matches anything.
 */
public class StructurePatternBuilder {

    private final List<List<String>> layers = new ArrayList<>();
    private final Map<Character, ExtraCodecs.TagOrElementLocation> keys = new LinkedHashMap<>();

    public static StructurePatternBuilder pattern() {
        return new StructurePatternBuilder();
    }

    public StructurePatternBuilder layer(String... rows) {
        layers.add(List.of(rows));
        return this;
    }

    public StructurePatternBuilder key(char key, String block) {
        keys.put(key, new ExtraCodecs.TagOrElementLocation(Identifier.parse(block), false));
        return this;
    }

    public StructurePatternBuilder keyTag(char key, String tag) {
        keys.put(key, new ExtraCodecs.TagOrElementLocation(Identifier.parse(tag), true));
        return this;
    }

    public StructurePattern build() {
        StructurePattern built = new StructurePattern(List.copyOf(layers), Collections.unmodifiableMap(new LinkedHashMap<>(keys)));
        StructurePattern.CODEC.encodeStart(JsonOps.INSTANCE, built).getOrThrow();
        return built;
    }
}
