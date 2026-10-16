package com.benbenlaw.multiblocks.recipe;

import com.benbenlaw.core.recipe.ChanceResult;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;

/** What a Controller Crafting recipe produces: items that each have a chance, or drops rolled from loot tables (simulated mining). */
public sealed interface CraftingResult {

    /**
     * {@code [{"item": {"id": "minecraft:diamond", "count": 2}, "chance": 0.5}]}. A single object, or a plain item
     * like {@code {"id": "minecraft:diamond"}}, is read as a list of one result that always drops.
     */
    record Items(List<ChanceResult> results) implements CraftingResult {

        private static final Codec<ChanceResult> PLAIN_ITEM = ItemStackTemplate.CODEC.xmap(
                template -> new ChanceResult(template, 1.0f), ChanceResult::template);

        private static final Codec<ChanceResult> SINGLE = Codec.withAlternative(ChanceResult.CODEC, PLAIN_ITEM);

        public static final Codec<List<ChanceResult>> LIST_CODEC = Codec.withAlternative(
                ChanceResult.CODEC.listOf(1, 64), SINGLE, List::of);
    }

    /** {@code {"loot_tables": ["minecraft:blocks/stone"], "rolls": 1}}. Each roll picks one of the tables at random. */
    record Loot(List<Identifier> tables, int rolls) implements CraftingResult {
        public static final Codec<Loot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.listOf(1, 64).fieldOf("loot_tables").forGetter(Loot::tables),
                Codec.intRange(1, 64).optionalFieldOf("rolls", 1).forGetter(Loot::rolls)
        ).apply(instance, Loot::new));
    }

    Codec<CraftingResult> CODEC = Codec.either(Items.LIST_CODEC, Loot.CODEC).xmap(
            either -> either.map(results -> (CraftingResult) new Items(results), loot -> (CraftingResult) loot),
            result -> switch (result) {
                case Items items -> Either.left(items.results());
                case Loot loot -> Either.right(loot);
            });

    StreamCodec<RegistryFriendlyByteBuf, CraftingResult> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    static CraftingResult of(ItemStackTemplate stack) {
        return new Items(List.of(new ChanceResult(stack, 1.0f)));
    }

    default boolean isLoot() {
        return this instanceof Loot;
    }
}
