package com.benbenlaw.miners.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraftforge.fluids.FluidStack;

//THANKS KAUPENJOE https://github.com/Kaupenjoe/Resource-Slimes/blob/1.19/src/main/java/net/kaupenjoe/resourceslimes/util/FluidJSONUtil.java

public class FluidJSONUtil {
    public static FluidStack readFluid(JsonObject json) {
        return FluidStack.CODEC.decode(JsonOps.INSTANCE, json).result().orElseThrow().getFirst();
    }

    public static JsonElement toJson(FluidStack stack) {
        return FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().orElseThrow();
    }
}
