package com.benbenlaw.multiblocks.event;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlockEntities;
import com.benbenlaw.multiblocks.loot.LootPreviewBuilder;
import com.benbenlaw.multiblocks.network.CapOutlinesPacket;
import com.benbenlaw.multiblocks.network.CraftingPreviewPacket;
import com.benbenlaw.multiblocks.network.MinerPreviewPacket;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import com.benbenlaw.multiblocks.recipe.MinerStructureRecipe;
import com.benbenlaw.multiblocks.structure.StructureManager;
import com.benbenlaw.multiblocks.structure.StructurePattern;
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
import java.util.Optional;

@EventBusSubscriber(modid = Multiblocks.MOD_ID)
public class MinersEvents {

    private static Object cachedRecipes;
    private static Object cachedStructures;
    private static List<MinerPreviewPacket.Preview> cachedPreviews = List.of();
    private static List<CraftingPreviewPacket.Preview> cachedCraftingPreviews = List.of();

    @SubscribeEvent
    public static void addReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(StructureManager.ID, StructureManager.INSTANCE);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(Capabilities.Item.BLOCK, MultiblocksBlockEntities.MINER_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemHandler());

        event.registerBlockEntity(Capabilities.Energy.BLOCK, MultiblocksBlockEntities.MINER_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getEnergyHandler());

        event.registerBlockEntity(Capabilities.Item.BLOCK, MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemHandler());

        event.registerBlockEntity(Capabilities.Energy.BLOCK, MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getEnergyHandler());

    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {

        //Server to Client
        event.registrar(Multiblocks.MOD_ID).playToClient(MinerPreviewPacket.TYPE, MinerPreviewPacket.STREAM_CODEC, MinerPreviewPacket.HANDLER);
        event.registrar(Multiblocks.MOD_ID).playToClient(CapOutlinesPacket.TYPE, CapOutlinesPacket.STREAM_CODEC, CapOutlinesPacket.HANDLER);
        event.registrar(Multiblocks.MOD_ID).playToClient(CraftingPreviewPacket.TYPE, CraftingPreviewPacket.STREAM_CODEC, CraftingPreviewPacket.HANDLER);
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
            cachedCraftingPreviews = buildCraftingPreviews(server);
        }

        List<MinerPreviewPacket> packets = new ArrayList<>();
        for (int i = 0; i < cachedPreviews.size(); i++) {
            packets.add(new MinerPreviewPacket(i, cachedPreviews.size(), cachedPreviews.get(i)));
        }
        event.getRelevantPlayers().forEach(player -> packets.forEach(packet -> PacketDistributor.sendToPlayer(player, packet)));

        List<CraftingPreviewPacket> craftingPackets = new ArrayList<>();
        for (int i = 0; i < cachedCraftingPreviews.size(); i++) {
            craftingPackets.add(new CraftingPreviewPacket(i, cachedCraftingPreviews.size(), cachedCraftingPreviews.get(i)));
        }
        event.getRelevantPlayers().forEach(player -> craftingPackets.forEach(packet -> PacketDistributor.sendToPlayer(player, packet)));
    }

    private static List<CraftingPreviewPacket.Preview> buildCraftingPreviews(MinecraftServer server) {
        List<CraftingPreviewPacket.Preview> previews = new ArrayList<>();

        for (RecipeHolder<ControllerCraftingRecipe> holder : server.getRecipeManager().recipeMap().byType(ControllerCraftingRecipe.TYPE)) {
            ControllerCraftingRecipe recipe = holder.value();
            Optional<StructurePattern> pattern = recipe.structureId().map(StructureManager.INSTANCE::get);
            previews.add(new CraftingPreviewPacket.Preview(holder.id().identifier(), recipe, pattern));
        }
        return previews;
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
