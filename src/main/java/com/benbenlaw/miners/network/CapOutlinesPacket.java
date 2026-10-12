package com.benbenlaw.miners.network;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.client.ClientCapOutlines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.List;

public record CapOutlinesPacket(BlockPos controller, List<BlockPos> positions) implements CustomPacketPayload {

    public static final Type<CapOutlinesPacket> TYPE = new Type<>(Miners.identifier("cap_outlines"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CapOutlinesPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, CapOutlinesPacket::controller,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), CapOutlinesPacket::positions,
            CapOutlinesPacket::new
    );

    public static final IPayloadHandler<CapOutlinesPacket> HANDLER = (packet, context) ->
            context.enqueueWork(() -> ClientCapOutlines.toggle(packet));

    @Override
    public Type<CapOutlinesPacket> type() {
        return TYPE;
    }
}
