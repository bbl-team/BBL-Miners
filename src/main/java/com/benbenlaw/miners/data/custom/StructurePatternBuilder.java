package com.benbenlaw.miners.data.custom;

import com.benbenlaw.miners.structure.StructurePattern;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StructurePatternBuilder {

    private final List<List<String>> layers = new ArrayList<>();
    private final Map<Character, ExtraCodecs.TagOrElementLocation> keys = new LinkedHashMap<>();
    private final List<Vec3i> caps = new ArrayList<>();

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

public StructurePatternBuilder cap(int x, int y, int z) {
        caps.add(new Vec3i(x, y, z));
        return this;
    }

    public StructurePattern build() {
        StructurePattern built = new StructurePattern(List.copyOf(layers), Collections.unmodifiableMap(new LinkedHashMap<>(keys)), List.copyOf(caps));
        StructurePattern.CODEC.encodeStart(JsonOps.INSTANCE, built).getOrThrow();
        return built;
    }
}
