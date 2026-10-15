package com.benbenlaw.multiblocks.data.custom;

import com.benbenlaw.multiblocks.structure.BlockMatcher;
import com.benbenlaw.multiblocks.structure.StructurePattern;
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
    private final Map<Character, BlockMatcher> keys = new LinkedHashMap<>();
    private final List<Vec3i> caps = new ArrayList<>();

    public static StructurePatternBuilder pattern() {
        return new StructurePatternBuilder();
    }

    public StructurePatternBuilder layer(String... rows) {
        layers.add(List.of(rows));
        return this;
    }

    public StructurePatternBuilder key(char key, String block) {
        keys.put(key, BlockMatcher.parse(block).getOrThrow());
        return this;
    }

    public StructurePatternBuilder keyTag(char key, String tag) {
        keys.put(key, BlockMatcher.tag(Identifier.parse(tag)));
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
