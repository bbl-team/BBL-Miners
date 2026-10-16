package com.benbenlaw.multiblocks.event;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlockEntities;
import com.benbenlaw.multiblocks.loot.LootPreviewBuilder;
import com.benbenlaw.multiblocks.network.CapOutlinesPacket;
import com.benbenlaw.multiblocks.network.CraftingPreviewPacket;
import com.benbenlaw.multiblocks.network.CreatePatternPacket;
import com.benbenlaw.multiblocks.network.LootDrop;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import com.benbenlaw.multiblocks.structure.StructureManager;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
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
    private static List<CraftingPreviewPacket.Preview> cachedCraftingPreviews = List.of();

    @SubscribeEvent
    public static void addReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(StructureManager.ID, StructureManager.INSTANCE);
    }

    /** Any other crafting controller block (a custom one from KubeJS, say) uses the same block entity. */
    @SubscribeEvent
    public static void addControllerBlocks(BlockEntityTypeAddBlocksEvent event) {
        var extra = net.minecraft.core.registries.BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock)
                .filter(block -> block != com.benbenlaw.multiblocks.block.MultiblocksBlocks.CRAFTING_CONTROLLER.get())
                .toArray(net.minecraft.world.level.block.Block[]::new);
        event.modify(MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(), extra);

    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(Capabilities.Item.BLOCK, MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemHandler());

        event.registerBlockEntity(Capabilities.Energy.BLOCK, MultiblocksBlockEntities.CRAFTING_CONTROLLER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getEnergyHandler());

    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {

        //Server to Client
        event.registrar(Multiblocks.MOD_ID).playToClient(CapOutlinesPacket.TYPE, CapOutlinesPacket.STREAM_CODEC, CapOutlinesPacket.HANDLER);
        event.registrar(Multiblocks.MOD_ID).playToClient(CraftingPreviewPacket.TYPE, CraftingPreviewPacket.STREAM_CODEC, CraftingPreviewPacket.HANDLER);

        //Client to Server
        event.registrar(Multiblocks.MOD_ID).playToServer(CreatePatternPacket.TYPE, CreatePatternPacket.STREAM_CODEC, CreatePatternPacket.HANDLER);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        syncPreviews(event.getPlayerList().getServer(), event.getRelevantPlayers().toList());
    }

    /** Sends the miner and crafting recipe previews (with their structures) that the JEI categories show. */
    public static void syncPreviews(MinecraftServer server, List<ServerPlayer> players) {
        Object recipes = server.getRecipeManager().recipeMap();
        Object structures = StructureManager.INSTANCE.all();
        if (cachedRecipes != recipes || cachedStructures != structures) {
            cachedRecipes = recipes;
            cachedStructures = structures;
            cachedCraftingPreviews = buildCraftingPreviews(server);
        }

        List<CraftingPreviewPacket> craftingPackets = new ArrayList<>();
        for (int i = 0; i < cachedCraftingPreviews.size(); i++) {
            craftingPackets.add(new CraftingPreviewPacket(i, cachedCraftingPreviews.size(), cachedCraftingPreviews.get(i)));
        }
        players.forEach(player -> craftingPackets.forEach(packet -> PacketDistributor.sendToPlayer(player, packet)));
    }

    private static List<CraftingPreviewPacket.Preview> buildCraftingPreviews(MinecraftServer server) {
        List<CraftingPreviewPacket.Preview> previews = new ArrayList<>();
        Map<Identifier, List<LootDrop>> drops = LootPreviewBuilder.build(server);

        for (RecipeHolder<ControllerCraftingRecipe> holder : server.getRecipeManager().recipeMap().byType(ControllerCraftingRecipe.TYPE)) {
            ControllerCraftingRecipe recipe = holder.value();
            Optional<StructurePattern> pattern = recipe.structureId().map(StructureManager.INSTANCE::get);
            previews.add(new CraftingPreviewPacket.Preview(holder.id().identifier(), recipe, pattern,
                    drops.getOrDefault(holder.id().identifier(), List.of())));
        }
        return previews;
    }
}
