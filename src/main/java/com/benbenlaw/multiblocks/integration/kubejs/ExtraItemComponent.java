package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.recipe.ExtraItem;
import com.benbenlaw.multiblocks.recipe.ItemEffect;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.SizedIngredientComponent;
import dev.latvian.mods.kubejs.util.JsonUtils;
import dev.latvian.mods.rhino.NativeArray;
import dev.latvian.mods.rhino.NativeObject;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * An extra item for a Controller Crafting recipe. In a script it can be written as
 * <pre>
 * '4x #minecraft:coals'                                       the item, required and used up
 * ['4x #minecraft:coals', 0.5]                                ...with a 50% chance to be used up
 * ['minecraft:echo_shard', 1, {required: false, effects: []}] ...with options (and effects) in the last place
 * {ingredient: '4x #minecraft:coals', consumeChance: 0.5, consume: true, required: true, effects: [...]}
 * </pre>
 */
public record ExtraItemComponent(ResourceKey<RecipeComponentType<?>> type, Codec<ExtraItem> codec) implements RecipeComponent<ExtraItem> {

    public static final ExtraItemComponent EXTRA_ITEM = new ExtraItemComponent(RecipeComponentType.key(Multiblocks.identifier("extra_item")), ExtraItem.CODEC);

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.OBJECT;
    }

    @Override
    public ExtraItem wrap(RecipeScriptContext cx, Object from) {
        if (from instanceof ExtraItem extra) return extra;

        if (from instanceof NativeArray array) {
            if (array.getLength() < 1) throw new IllegalArgumentException("An extra item needs at least an ingredient");

            Object options = null;
            Object chance = null;
            for (int i = 1; i < array.getLength(); i++) {
                Object part = array.get(i);
                if (part instanceof NativeObject) options = part;
                else chance = part;
            }

            ExtraItemFields fields = new ExtraItemFields();
            if (options instanceof NativeObject object) fields.read(cx, object);
            if (chance != null) fields.consumeChance = number(chance, "consume chance");
            return fields.build(cx, array.get(0));
        }

        if (from instanceof NativeObject object) {
            ExtraItemFields fields = new ExtraItemFields();
            fields.read(cx, object);

            Object ingredient = object.get("ingredient");
            if (ingredient == null) ingredient = object.get("item");
            if (ingredient == null) throw new IllegalArgumentException("An extra item needs an 'ingredient'");
            return fields.build(cx, ingredient);
        }

        return new ExtraItemFields().build(cx, from);
    }

    private static double number(Object value, String what) {
        if (value instanceof Number number) return number.doubleValue();
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The " + what + " of an extra item must be a number, not " + value);
        }
    }

    private static class ExtraItemFields {
        private int count = -1;
        private boolean required = true;
        private boolean consume = true;
        private double consumeChance = 1.0;
        private final List<ItemEffect> effects = new ArrayList<>();

        void read(RecipeScriptContext cx, NativeObject object) {
            if (object.get("count") != null) count = (int) number(object.get("count"), "count");
            if (object.get("required") != null) required = Boolean.parseBoolean(String.valueOf(object.get("required")));
            if (object.get("consume") != null) consume = Boolean.parseBoolean(String.valueOf(object.get("consume")));

            for (String name : new String[]{"consumeChance", "consume_chance", "chance"}) {
                if (object.get(name) != null) consumeChance = number(object.get(name), "consume chance");
            }

            Object effectList = object.get("effects");
            if (effectList instanceof NativeArray array) {
                for (int i = 0; i < array.getLength(); i++) {
                    JsonElement json = JsonUtils.of(cx.cx(), array.get(i));
                    effects.add(ItemEffect.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(message -> new IllegalArgumentException("Bad effect: " + message)));
                }
            }
        }

        ExtraItem build(RecipeScriptContext cx, Object ingredient) {
            SizedIngredient sized = SizedIngredientComponent.SIZED_INGREDIENT.wrap(cx, ingredient);
            int amount = count > 0 ? count : sized.count();
            return new ExtraItem(sized.ingredient(), amount, required, consume, consumeChance, List.copyOf(effects));
        }
    }
}
