package com.benbenlaw.multiblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** One possible drop of a loot table result, as the JEI category shows it. */
public record LootDrop(ItemStack stack, List<ItemStack> variants, int min, int max, float chance, List<Component> notes) {

    public static final StreamCodec<RegistryFriendlyByteBuf, LootDrop> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, LootDrop::stack,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), LootDrop::variants,
            ByteBufCodecs.VAR_INT, LootDrop::min,
            ByteBufCodecs.VAR_INT, LootDrop::max,
            ByteBufCodecs.FLOAT, LootDrop::chance,
            ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list()), LootDrop::notes,
            LootDrop::new
    );
}
