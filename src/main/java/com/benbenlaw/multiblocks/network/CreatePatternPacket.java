package com.benbenlaw.multiblocks.network;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.event.MinersEvents;
import com.benbenlaw.multiblocks.pattern.PatternExporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

/** Asks the server to turn the selected box into a structure json. */
public record CreatePatternPacket(BlockPos first, BlockPos second, String name, boolean frameTags, boolean saveStates) implements CustomPacketPayload {

    public static final Type<CreatePatternPacket> TYPE = new Type<>(Multiblocks.identifier("create_pattern"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CreatePatternPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, CreatePatternPacket::first,
            BlockPos.STREAM_CODEC, CreatePatternPacket::second,
            ByteBufCodecs.STRING_UTF8, CreatePatternPacket::name,
            ByteBufCodecs.BOOL, CreatePatternPacket::frameTags,
            ByteBufCodecs.BOOL, CreatePatternPacket::saveStates,
            CreatePatternPacket::new
    );

    public static final IPayloadHandler<CreatePatternPacket> HANDLER = (packet, context) -> {
        if (!(context.player() instanceof ServerPlayer player)) return;

        if (!player.isCreative()) {
            player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.pattern.creative_only").withStyle(ChatFormatting.RED));
            return;
        }

        PatternExporter.Outcome outcome = PatternExporter.export((ServerLevel) player.level(),
                packet.first(), packet.second(), packet.name(), packet.frameTags(), packet.saveStates());

        if (outcome.result() == PatternExporter.Result.OK) {
            player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.pattern.created", outcome.id().toString()).withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.translatable("message.bblmultiblocks.pattern.saved_to", outcome.file().toString()).withStyle(ChatFormatting.GRAY));

            // let every client's JEI see the new structure straight away
            MinersEvents.syncPreviews(player.level().getServer(), player.level().getServer().getPlayerList().getPlayers());
        } else {
            player.sendOverlayMessage(Component.translatable("message.bblmultiblocks.pattern.error." + outcome.result().name().toLowerCase(java.util.Locale.ROOT),
                    PatternExporter.MAX_SIDE).withStyle(ChatFormatting.RED));
        }
    };

    @Override
    public Type<CreatePatternPacket> type() {
        return TYPE;
    }
}
