package com.benbenlaw.miners.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record StructurePattern(List<List<String>> layers, Map<Character, ExtraCodecs.TagOrElementLocation> keys, List<Vec3i> caps) {

    /** Upgrade slots. Each is a position relative to the controller where any cap block can go. */

    public static final char CONTROLLER = '@';
    public static final char ANY = ' ';

    private static final Codec<Character> KEY_CHAR = Codec.STRING.comapFlatMap(
            s -> s.length() == 1 && s.charAt(0) != CONTROLLER && s.charAt(0) != ANY
                    ? DataResult.success(s.charAt(0))
                    : DataResult.error(() -> "Key must be a single character other than '@' or a space: '" + s + "'"),
            String::valueOf);

    public static final MapCodec<StructurePattern> MAP_CODEC = RecordCodecBuilder.<StructurePattern>mapCodec(instance -> instance.group(
            Codec.STRING.listOf().listOf().fieldOf("pattern").forGetter(StructurePattern::layers),
            Codec.unboundedMap(KEY_CHAR, ExtraCodecs.TAG_OR_ELEMENT_ID).fieldOf("keys").forGetter(StructurePattern::keys),
            Vec3i.CODEC.listOf().optionalFieldOf("caps", List.of()).forGetter(StructurePattern::caps)
    ).apply(instance, StructurePattern::new)).validate(StructurePattern::validate);

    public static final Codec<StructurePattern> CODEC = MAP_CODEC.codec();

    private DataResult<StructurePattern> validate() {
        if (layers.isEmpty()) return error("pattern has no layers");

        int rowCount = layers.getFirst().size();
        int width = rowCount == 0 ? 0 : layers.getFirst().getFirst().length();
        if (rowCount == 0 || width == 0) return error("pattern layers must contain non-empty rows");

        int controllers = 0;
        int cx = 0, cy = 0, cz = 0;
        Set<Character> used = new HashSet<>();

        for (int y = 0; y < layers.size(); y++) {
            List<String> layer = layers.get(y);
            if (layer.size() != rowCount) {
                return error("layer " + y + " has " + layer.size() + " rows, expected " + rowCount);
            }
            for (int z = 0; z < layer.size(); z++) {
                String row = layer.get(z);
                if (row.length() != width) {
                    return error("layer " + y + " row " + z + " is " + row.length() + " wide, expected " + width);
                }
                for (char c : row.toCharArray()) {
                    if (c == CONTROLLER) {
                        controllers++;
                        cx = row.indexOf(CONTROLLER);
                        cy = y;
                        cz = z;
                    } else if (c != ANY) {
                        if (!keys.containsKey(c)) return error("pattern uses '" + c + "' but it is not defined in keys");
                        used.add(c);
                    }
                }
            }
        }

        if (controllers != 1) return error("pattern must contain exactly one '@', found " + controllers);

        for (Character key : keys.keySet()) {
            if (!used.contains(key)) return error("key '" + key + "' is never used in the pattern");
        }

        return validateCaps(cx, cy, cz);
    }

    private DataResult<StructurePattern> validateCaps(int cx, int cy, int cz) {
        Set<Vec3i> occupied = new HashSet<>();
        for (int y = 0; y < layers.size(); y++) {
            for (int z = 0; z < layers.get(y).size(); z++) {
                String row = layers.get(y).get(z);
                for (int x = 0; x < row.length(); x++) {
                    if (row.charAt(x) != ANY) occupied.add(new Vec3i(x - cx, y - cy, z - cz));
                }
            }
        }

        Set<Vec3i> seen = new HashSet<>();
        for (Vec3i cap : caps) {
            if (occupied.contains(cap)) {
                return error("cap at " + cap.toShortString() + " overlaps the pattern (use a space or leave it outside)");
            }
            if (!seen.add(cap)) return error("two caps share the position " + cap.toShortString());
        }

        return DataResult.success(this);
    }

    private static DataResult<StructurePattern> error(String message) {
        return DataResult.error(() -> "Invalid structure pattern: " + message);
    }

    public CompiledStructure compile() {
        int cx = 0, cy = 0, cz = 0;
        for (int y = 0; y < layers.size(); y++) {
            for (int z = 0; z < layers.get(y).size(); z++) {
                int x = layers.get(y).get(z).indexOf(CONTROLLER);
                if (x >= 0) {
                    cx = x;
                    cy = y;
                    cz = z;
                }
            }
        }

        List<CompiledStructure.Entry> entries = new ArrayList<>();
        for (int y = 0; y < layers.size(); y++) {
            for (int z = 0; z < layers.get(y).size(); z++) {
                String row = layers.get(y).get(z);
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    if (c == CONTROLLER || c == ANY) continue;
                    entries.add(new CompiledStructure.Entry(x - cx, y - cy, z - cz, keys.get(c)));
                }
            }
        }
        return new CompiledStructure(List.copyOf(entries), List.copyOf(caps));
    }
}
