package com.benbenlaw.multiblocks.pattern;

import com.benbenlaw.multiblocks.structure.BlockMatcher;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Turns a box of blocks that was built in the world into a structure pattern. The controller becomes '@', cap blocks
 * become cap slots (and a space in the pattern), air becomes a space, and every other block gets a key letter.
 */
public final class PatternRecorder {

    private static final String KEY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public enum Kind {
        AIR, CONTROLLER, CAP, BLOCK
    }

    public record Cell(Kind kind, @Nullable BlockMatcher block) {
        public static final Cell AIR = new Cell(Kind.AIR, null);
        public static final Cell CONTROLLER = new Cell(Kind.CONTROLLER, null);
        public static final Cell CAP = new Cell(Kind.CAP, null);

        public static Cell block(BlockMatcher location) {
            return new Cell(Kind.BLOCK, location);
        }
    }

    public enum Error {
        NO_CONTROLLER, MULTIPLE_CONTROLLERS, EMPTY, TOO_MANY_BLOCK_TYPES, INVALID
    }

    public record Outcome(@Nullable StructurePattern pattern, @Nullable Error error) {
        public boolean ok() {
            return pattern != null;
        }
    }

    /** The cell at a position, counted from the lowest corner of the box. */
    public interface Cells {
        Cell get(int x, int y, int z);
    }

    private PatternRecorder() {
    }

    public static Outcome record(int sizeX, int sizeY, int sizeZ, Cells cells) {
        int controllerX = -1, controllerY = -1, controllerZ = -1;
        int controllers = 0;

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        boolean anyBlock = false;

        for (int y = 0; y < sizeY; y++) {
            for (int z = 0; z < sizeZ; z++) {
                for (int x = 0; x < sizeX; x++) {
                    Cell cell = cells.get(x, y, z);

                    if (cell.kind() == Kind.CONTROLLER) {
                        controllers++;
                        controllerX = x;
                        controllerY = y;
                        controllerZ = z;
                    }

                    if (cell.kind() == Kind.BLOCK) anyBlock = true;

                    if (cell.kind() == Kind.BLOCK || cell.kind() == Kind.CONTROLLER) {
                        minX = Math.min(minX, x);
                        minY = Math.min(minY, y);
                        minZ = Math.min(minZ, z);
                        maxX = Math.max(maxX, x);
                        maxY = Math.max(maxY, y);
                        maxZ = Math.max(maxZ, z);
                    }
                }
            }
        }

        if (controllers == 0) return new Outcome(null, Error.NO_CONTROLLER);
        if (controllers > 1) return new Outcome(null, Error.MULTIPLE_CONTROLLERS);
        if (!anyBlock) return new Outcome(null, Error.EMPTY);

        Map<BlockMatcher, Character> keys = new LinkedHashMap<>();
        List<List<String>> layers = new ArrayList<>();
        List<Vec3i> caps = new ArrayList<>();

        // the first layer is the top one, rows run north to south and columns west to east
        for (int y = maxY; y >= minY; y--) {
            List<String> rows = new ArrayList<>();
            for (int z = minZ; z <= maxZ; z++) {
                StringBuilder row = new StringBuilder();
                for (int x = minX; x <= maxX; x++) {
                    Cell cell = cells.get(x, y, z);
                    switch (cell.kind()) {
                        case CONTROLLER -> row.append(StructurePattern.CONTROLLER);
                        case BLOCK -> {
                            Character key = keys.get(cell.block());
                            if (key == null) {
                                if (keys.size() >= KEY_CHARS.length()) return new Outcome(null, Error.TOO_MANY_BLOCK_TYPES);
                                key = KEY_CHARS.charAt(keys.size());
                                keys.put(cell.block(), key);
                            }
                            row.append(key.charValue());
                        }
                        default -> row.append(StructurePattern.ANY);
                    }
                }
                rows.add(row.toString());
            }
            layers.add(List.copyOf(rows));
        }

        // cap blocks can sit anywhere in the selection, even outside the trimmed pattern
        for (int y = sizeY - 1; y >= 0; y--) {
            for (int z = 0; z < sizeZ; z++) {
                for (int x = 0; x < sizeX; x++) {
                    if (cells.get(x, y, z).kind() == Kind.CAP) {
                        caps.add(new Vec3i(x - controllerX, y - controllerY, z - controllerZ));
                    }
                }
            }
        }

        Map<Character, BlockMatcher> keyMap = new LinkedHashMap<>();
        keys.forEach((block, key) -> keyMap.put(key, block));

        StructurePattern pattern = new StructurePattern(List.copyOf(layers), keyMap, List.copyOf(caps));
        if (StructurePattern.CODEC.encodeStart(JsonOps.INSTANCE, pattern).error().isPresent()) {
            return new Outcome(null, Error.INVALID);
        }
        return new Outcome(pattern, null);
    }
}
