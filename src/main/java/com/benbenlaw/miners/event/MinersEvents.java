package com.benbenlaw.miners.event;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlockEntities;
import com.benbenlaw.miners.loot.LootPreviewBuilder;
import com.benbenlaw.miners.network.CapOutlinesPacket;
import com.benbenlaw.miners.network.MinerPreviewPacket;
import com.benbenlaw.miners.recipe.MinerStructureRecipe;
import com.benbenlaw.miners.structure.StructureManager;
import com.benbenlaw.miners.structure.StructurePattern;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Miners.MOD_ID)
public class MinersEvents {

    private static Object cachedRecipes;
    private static Object cachedStructures;
    private static List<MinerPreviewPacket.Preview> cachedPreviews = List.of();

    @SubscribeEvent
    public static void addReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(StructureManager.ID, StructureManager.INSTANCE);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(Capabilities.Item.BLOCK, MinersBlockEntities.MINER_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemHandler());

        event.registerBlockEntity(Capabilities.Energy.BLOCK, MinersBlockEntities.MINER_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getEnergyHandler());

    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {

        //Server to Client
        event.registrar(Miners.MOD_ID).playToClient(MinerPreviewPacket.TYPE, MinerPreviewPacket.STREAM_CODEC, MinerPreviewPacket.HANDLER);
        event.registrar(Miners.MOD_ID).playToClient(CapOutlinesPacket.TYPE, CapOutlinesPacket.STREAM_CODEC, CapOutlinesPacket.HANDLER);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        MinecraftServer server = event.getPlayerList().getServer();

        Object recipes = server.getRecipeManager();
        Object structures = StructureManager.INSTANCE.all();
        if (cachedRecipes != recipes || cachedStructures != structures) {
            cachedRecipes = recipes;
            cachedStructures = structures;
            cachedPreviews = buildPreviews(server);
        }

        List<MinerPreviewPacket> packets = new ArrayList<>();
        for (int i = 0; i < cachedPreviews.size(); i++) {
            packets.add(new MinerPreviewPacket(i, cachedPreviews.size(), cachedPreviews.get(i)));
        }
        event.getRelevantPlayers().forEach(player -> packets.forEach(packet -> PacketDistributor.sendToPlayer(player, packet)));
    }

    private static List<MinerPreviewPacket.Preview> buildPreviews(MinecraftServer server) {
        Map<Identifier, List<MinerPreviewPacket.Drop>> drops = LootPreviewBuilder.build(server);
        List<MinerPreviewPacket.Preview> previews = new ArrayList<>();

        for (RecipeHolder<MinerStructureRecipe> holder : server.getRecipeManager().recipeMap().byType(MinerStructureRecipe.TYPE)) {
            MinerStructureRecipe recipe = holder.value();
            StructurePattern pattern = StructureManager.INSTANCE.get(recipe.structureId());
            if (pattern == null) continue;

            Identifier recipeId = holder.id().identifier();
            previews.add(new MinerPreviewPacket.Preview(recipeId, recipe.structureId(), pattern,
                    recipe.baseDuration(), recipe.baseRolls(), recipe.energyPerTick(),
                    drops.getOrDefault(recipeId, List.of())));
        }
        return previews;
    }
}
