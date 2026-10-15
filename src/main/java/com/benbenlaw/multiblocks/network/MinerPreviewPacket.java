package com.benbenlaw.multiblocks.network;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.client.ClientPreviewCache;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.List;

public record MinerPreviewPacket(int index, int total, Preview preview) implements CustomPacketPayload {

    public static final Type<MinerPreviewPacket> TYPE = new Type<>(Multiblocks.identifier("miner_preview"));

    public record Drop(ItemStack stack, List<ItemStack> variants, int min, int max, float chance, List<Component> notes) {

        public static final StreamCodec<RegistryFriendlyByteBuf, Drop> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, Drop::stack,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), Drop::variants,
                ByteBufCodecs.VAR_INT, Drop::min,
                ByteBufCodecs.VAR_INT, Drop::max,
                ByteBufCodecs.FLOAT, Drop::chance,
                ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list()), Drop::notes,
                Drop::new
        );
    }

    public record Preview(Identifier recipeId, Identifier controller, Identifier structureId, StructurePattern pattern,
                          int baseDuration, int baseRolls, int energyPerTick, List<Drop> drops) {

        public static final StreamCodec<RegistryFriendlyByteBuf, Preview> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, Preview::recipeId,
                Identifier.STREAM_CODEC, Preview::controller,
                Identifier.STREAM_CODEC, Preview::structureId,
                ByteBufCodecs.fromCodecWithRegistries(StructurePattern.CODEC), Preview::pattern,
                ByteBufCodecs.VAR_INT, Preview::baseDuration,
                ByteBufCodecs.VAR_INT, Preview::baseRolls,
                ByteBufCodecs.VAR_INT, Preview::energyPerTick,
                Drop.STREAM_CODEC.apply(ByteBufCodecs.list()), Preview::drops,
                Preview::new
        );
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, MinerPreviewPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MinerPreviewPacket::index,
            ByteBufCodecs.VAR_INT, MinerPreviewPacket::total,
            Preview.STREAM_CODEC, MinerPreviewPacket::preview,
            MinerPreviewPacket::new
    );

    public static final IPayloadHandler<MinerPreviewPacket> HANDLER = (packet, context) ->
            context.enqueueWork(() -> ClientPreviewCache.receive(packet));

    @Override
    public Type<MinerPreviewPacket> type() {
        return TYPE;
    }
}
