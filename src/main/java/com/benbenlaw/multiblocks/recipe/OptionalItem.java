package com.benbenlaw.multiblocks.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * An item that may sit in a research slot of the Crafting Controller. It is never required. While it is there its effects
 * apply to the craft, and it can be used up ("consume") or kept, optionally with only a chance of being used up.
 */
public record OptionalItem(Ingredient ingredient, int count, boolean consume, double consumeChance, List<OptionalEffect> effects) {

    public static final Codec<OptionalItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(OptionalItem::ingredient),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(OptionalItem::count),
            Codec.BOOL.optionalFieldOf("consume", true).forGetter(OptionalItem::consume),
            Codec.doubleRange(0, 1).optionalFieldOf("consume_chance", 1.0).forGetter(OptionalItem::consumeChance),
            OptionalEffect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(OptionalItem::effects)
    ).apply(instance, OptionalItem::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, OptionalItem> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, List<OptionalItem>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    /** True when the stack is the right item and there are enough of them. */
    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }
}
