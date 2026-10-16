package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.core.recipe.ChanceResult;
import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.recipe.CraftingResult;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.NativeArray;
import dev.latvian.mods.rhino.NativeObject;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of a Controller Crafting recipe. In a script it can be written as
 * <pre>
 * 'minecraft:diamond 2'                                         one item that always drops
 * ['minecraft:diamond 2', 0.5]                                  one item with a 50% chance
 * [['minecraft:diamond', 0.5], 'minecraft:coal', ['minecraft:emerald', 0.1]]   a list of results
 * {item: 'minecraft:diamond', chance: 0.5}                      the object form of one result
 * 'loot:minecraft:blocks/stone'                                 a loot table, rolled once
 * {loot: 'minecraft:blocks/stone', rolls: 2}                    a loot table rolled twice
 * {loot: ['minecraft:blocks/stone', 'minecraft:blocks/granite'], rolls: 3}   each roll picks one of the tables
 * </pre>
 */
public record CraftingResultComponent(ResourceKey<RecipeComponentType<?>> type, Codec<CraftingResult> codec) implements RecipeComponent<CraftingResult> {

    public static final CraftingResultComponent RESULT = new CraftingResultComponent(RecipeComponentType.key(Multiblocks.identifier("crafting_result")), CraftingResult.CODEC);

    private static final String LOOT_PREFIX = "loot:";

    @Override
    public TypeInfo typeInfo() {
        return ItemWrapper.TYPE_INFO;
    }

    @Override
    public boolean isEmpty(CraftingResult value) {
        return value instanceof CraftingResult.Items items && items.results().isEmpty();
    }

    @Override
    public CraftingResult wrap(RecipeScriptContext cx, Object from) {
        if (from instanceof CraftingResult result) return result;

        if (from instanceof CharSequence text && text.toString().startsWith(LOOT_PREFIX)) {
            return new CraftingResult.Loot(List.of(table(text)), 1);
        }

        if (from instanceof NativeObject object) {
            Object loot = first(object, "loot", "lootTable", "lootTables", "loot_tables");
            if (loot != null) {
                int rolls = object.get("rolls") == null ? 1 : (int) Double.parseDouble(String.valueOf(object.get("rolls")));
                return new CraftingResult.Loot(tables(loot), rolls);
            }
        }

        if (from instanceof NativeArray array && !isChancePair(array)) {
            List<ChanceResult> results = new ArrayList<>();
            for (int i = 0; i < array.getLength(); i++) results.add(chanceResult(cx, array.get(i)));
            return new CraftingResult.Items(results);
        }

        return new CraftingResult.Items(List.of(chanceResult(cx, from)));
    }

    /** [item, chance] is one result, anything else in an array is a list of results. */
    private static boolean isChancePair(NativeArray array) {
        return array.getLength() == 2 && array.get(1) instanceof Number;
    }

    private static ChanceResult chanceResult(RecipeScriptContext cx, Object from) {
        if (from instanceof ChanceResult result) return result;

        float chance = 1.0f;
        Object item = from;

        if (from instanceof NativeArray array && array.getLength() >= 1) {
            item = array.get(0);
            if (array.getLength() > 1) chance = Float.parseFloat(String.valueOf(array.get(1)));
        } else if (from instanceof NativeObject object && object.get("item") != null) {
            item = object.get("item");
            if (object.get("chance") != null) chance = Float.parseFloat(String.valueOf(object.get("chance")));
        }

        ItemStack stack = ItemStackComponent.ITEM_STACK.wrap(cx, item);
        return new ChanceResult(ItemStackTemplate.fromNonEmptyStack(stack), chance);
    }

    private static Object first(NativeObject object, String... names) {
        for (String name : names) {
            if (object.get(name) != null) return object.get(name);
        }
        return null;
    }

    private static List<Identifier> tables(Object loot) {
        List<Identifier> tables = new ArrayList<>();
        if (loot instanceof NativeArray array) {
            for (int i = 0; i < array.getLength(); i++) tables.add(table(array.get(i)));
        } else {
            tables.add(table(loot));
        }
        return tables;
    }

    private static Identifier table(Object value) {
        String text = String.valueOf(value);
        return Identifier.parse(text.startsWith(LOOT_PREFIX) ? text.substring(LOOT_PREFIX.length()).trim() : text.trim());
    }
}
