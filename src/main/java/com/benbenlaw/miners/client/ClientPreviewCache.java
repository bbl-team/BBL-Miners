package com.benbenlaw.miners.client;

import com.benbenlaw.miners.network.MinerPreviewPacket;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientPreviewCache {

    private static Map<Identifier, MinerPreviewPacket.Preview> previews = new LinkedHashMap<>();
    private static Map<Identifier, MinerPreviewPacket.Preview> pending = new LinkedHashMap<>();
    private static Runnable listener = () -> {};

    public static void receive(MinerPreviewPacket packet) {
        if (packet.index() == 0) pending = new LinkedHashMap<>();
        pending.put(packet.preview().recipeId(), packet.preview());

        if (packet.index() >= packet.total() - 1) {
            previews = pending;
            pending = new LinkedHashMap<>();
            listener.run();
        }
    }

    public static Collection<MinerPreviewPacket.Preview> all() {
        return previews.values();
    }

    public static void setListener(Runnable newListener) {
        listener = newListener;
    }
}
