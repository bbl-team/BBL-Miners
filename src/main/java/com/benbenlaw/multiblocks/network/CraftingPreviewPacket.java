package com.benbenlaw.multiblocks.network;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.client.ClientCraftingPreviewCache;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.Optional;

/** One Crafting Controller recipe for the client's JEI category, with the pattern of the structure it needs (if any). */
public record CraftingPreviewPacket(int index, int total, Preview preview) implements CustomPacketPayload {

    public static final Type<CraftingPreviewPacket> TYPE = new Type<>(Multiblocks.identifier("crafting_preview"));

    public record Preview(Identifier recipeId, ControllerCraftingRecipe recipe, Optional<StructurePattern> pattern) {

        public static final StreamCodec<RegistryFriendlyByteBuf, Preview> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, Preview::recipeId,
                ControllerCraftingRecipe.STREAM_CODEC, Preview::recipe,
                ByteBufCodecs.optional(ByteBufCodecs.fromCodecWithRegistries(StructurePattern.CODEC)), Preview::pattern,
                Preview::new
        );
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingPreviewPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CraftingPreviewPacket::index,
            ByteBufCodecs.VAR_INT, CraftingPreviewPacket::total,
            Preview.STREAM_CODEC, CraftingPreviewPacket::preview,
            CraftingPreviewPacket::new
    );

    public static final IPayloadHandler<CraftingPreviewPacket> HANDLER = (packet, context) ->
            context.enqueueWork(() -> ClientCraftingPreviewCache.receive(packet));

    @Override
    public Type<CraftingPreviewPacket> type() {
        return TYPE;
    }
}
