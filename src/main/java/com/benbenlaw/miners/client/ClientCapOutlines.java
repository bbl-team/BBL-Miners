package com.benbenlaw.miners.client;

import com.benbenlaw.miners.network.CapOutlinesPacket;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ClientCapOutlines {

    private static @Nullable BlockPos controller;
    private static List<BlockPos> positions = List.of();

    public static void toggle(CapOutlinesPacket packet) {
        if (packet.controller().equals(controller)) {
            clear();
            return;
        }

        controller = packet.controller();
        positions = packet.positions();
    }

    public static void clear() {
        controller = null;
        positions = List.of();
    }

    public static @Nullable BlockPos controller() {
        return controller;
    }

    public static List<BlockPos> positions() {
        return positions;
    }
}
