package com.benbenlaw.multiblocks.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record SizedShapedPattern(List<String> rows, Map<Character, SizedIngredient> key) {

    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final char EMPTY = ' ';

    private static final Codec<Character> KEY_CHAR = Codec.STRING.comapFlatMap(
            s -> s.length() == 1 && s.charAt(0) != EMPTY
                    ? DataResult.success(s.charAt(0))
                    : DataResult.error(() -> "Key must be a single character other than a space: '" + s + "'"),
            String::valueOf);

    public static final Codec<SizedIngredient> SIZED = Codec.withAlternative(
            SizedIngredient.NESTED_CODEC,
            net.minecraft.world.item.crafting.Ingredient.CODEC,
            ingredient -> new SizedIngredient(ingredient, 1));

    public static final MapCodec<SizedShapedPattern> MAP_CODEC = RecordCodecBuilder.<SizedShapedPattern>mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pattern").forGetter(SizedShapedPattern::rows),
            Codec.unboundedMap(KEY_CHAR, SIZED).fieldOf("key").forGetter(SizedShapedPattern::key)
    ).apply(instance, SizedShapedPattern::new)).validate(SizedShapedPattern::validate);

    public static final Codec<SizedShapedPattern> CODEC = MAP_CODEC.codec();

    public interface SlotCheck {
        boolean isEmpty(int slot);

        boolean accepts(char key, int slot);
    }

    private DataResult<SizedShapedPattern> validate() {
        if (rows.isEmpty() || rows.size() > GRID_HEIGHT) return error("pattern must have 1 to " + GRID_HEIGHT + " rows");

        int width = rows.getFirst().length();
        if (width == 0 || width > GRID_WIDTH) return error("pattern rows must be 1 to " + GRID_WIDTH + " wide");

        Set<Character> used = new HashSet<>();
        boolean anyIngredient = false;
        for (String row : rows) {
            if (row.length() != width) return error("every row must be " + width + " wide");
            for (char c : row.toCharArray()) {
                if (c == EMPTY) continue;
                if (!key.containsKey(c)) return error("pattern uses '" + c + "' but it is not defined in key");
                used.add(c);
                anyIngredient = true;
            }
        }

        if (!anyIngredient) return error("pattern has no ingredients");

        for (Character defined : key.keySet()) {
            if (!used.contains(defined)) return error("key '" + defined + "' is never used in the pattern");
        }

        for (Map.Entry<Character, SizedIngredient> entry : key.entrySet()) {
            if (entry.getValue().count() > 64) return error("key '" + entry.getKey() + "' asks for more than a stack (64)");
        }

        return DataResult.success(this);
    }

    private static DataResult<SizedShapedPattern> error(String message) {
        return DataResult.error(() -> "Invalid controller crafting pattern: " + message);
    }

    public static char @Nullable [] layout(List<String> rows, SlotCheck check) {
        char[] normal = tryLayout(rows, check);
        if (normal != null) return normal;

        List<String> mirrored = new ArrayList<>();
        for (String row : rows) mirrored.add(new StringBuilder(row).reverse().toString());
        return tryLayout(mirrored, check);
    }

    private static char @Nullable [] tryLayout(List<String> rows, SlotCheck check) {
        int height = rows.size();
        int width = rows.getFirst().length();

        for (int top = 0; top <= GRID_HEIGHT - height; top++) {
            for (int left = 0; left <= GRID_WIDTH - width; left++) {
                char[] expected = fits(rows, top, left, check);
                if (expected != null) return expected;
            }
        }
        return null;
    }

    private static char @Nullable [] fits(List<String> rows, int top, int left, SlotCheck check) {
        char[] expected = new char[GRID_WIDTH * GRID_HEIGHT];

        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                int slot = y * GRID_WIDTH + x;
                int row = y - top;
                int column = x - left;

                char key = EMPTY;
                if (row >= 0 && row < rows.size() && column >= 0 && column < rows.get(row).length()) {
                    key = rows.get(row).charAt(column);
                }

                if (key == EMPTY ? !check.isEmpty(slot) : !check.accepts(key, slot)) return null;
                expected[slot] = key;
            }
        }
        return expected;
    }

    public int @Nullable [] amounts(List<ItemStack> grid) {
        char[] layout = layout(rows, new SlotCheck() {
            @Override
            public boolean isEmpty(int slot) {
                return grid.get(slot).isEmpty();
            }

            @Override
            public boolean accepts(char keyChar, int slot) {
                return key.get(keyChar).test(grid.get(slot));
            }
        });
        if (layout == null) return null;

        int[] amounts = new int[layout.length];
        for (int slot = 0; slot < layout.length; slot++) {
            if (layout[slot] != EMPTY) amounts[slot] = key.get(layout[slot]).count();
        }
        return amounts;
    }
}
