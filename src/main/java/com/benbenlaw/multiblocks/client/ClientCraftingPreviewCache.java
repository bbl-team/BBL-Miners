package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.network.CraftingPreviewPacket;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientCraftingPreviewCache {

    private static Map<Identifier, CraftingPreviewPacket.Preview> previews = new LinkedHashMap<>();
    private static Map<Identifier, CraftingPreviewPacket.Preview> pending = new LinkedHashMap<>();
    private static Runnable listener = () -> {};

    public static void receive(CraftingPreviewPacket packet) {
        if (packet.index() == 0) pending = new LinkedHashMap<>();
        pending.put(packet.preview().recipeId(), packet.preview());

        if (packet.index() >= packet.total() - 1) {
            previews = pending;
            pending = new LinkedHashMap<>();
            listener.run();
        }
    }

    public static Collection<CraftingPreviewPacket.Preview> all() {
        return previews.values();
    }

    public static void setListener(Runnable newListener) {
        listener = newListener;
    }
}
