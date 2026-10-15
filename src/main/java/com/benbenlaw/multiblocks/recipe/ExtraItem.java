package com.benbenlaw.multiblocks.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record ExtraItem(Ingredient ingredient, int count, boolean required, boolean consume, double consumeChance, List<ItemEffect> effects) {

    private static final Codec<ExtraItem> OBJECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ExtraItem::ingredient),
            Codec.intRange(1, 64).optionalFieldOf("count", 1).forGetter(ExtraItem::count),
            Codec.BOOL.optionalFieldOf("required", true).forGetter(ExtraItem::required),
            Codec.BOOL.optionalFieldOf("consume", true).forGetter(ExtraItem::consume),
            Codec.doubleRange(0, 1).optionalFieldOf("consume_chance", 1.0).forGetter(ExtraItem::consumeChance),
            ItemEffect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(ExtraItem::effects)
    ).apply(instance, ExtraItem::new));

    public static final Codec<ExtraItem> CODEC = Codec.withAlternative(OBJECT_CODEC, Ingredient.CODEC, ExtraItem::required);

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtraItem> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(OBJECT_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, List<ExtraItem>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static ExtraItem required(Ingredient ingredient) {
        return new ExtraItem(ingredient, 1, true, true, 1.0, List.of());
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }
}
