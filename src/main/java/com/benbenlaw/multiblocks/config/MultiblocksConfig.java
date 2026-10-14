package com.benbenlaw.multiblocks.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MultiblocksConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue MINER_FUEL_SLOT;
    public static final ModConfigSpec.BooleanValue CRAFTING_FUEL_SLOT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Controller fuel slots. With a fuel slot, burnable items are turned into energy. Turn a slot off to power that controller only with energy from outside (cables, generators and so on).")
                .push("fuel_slots");

        MINER_FUEL_SLOT = builder
                .comment("Give the Miner Controller a fuel slot")
                .define("miner_controller", true);

        CRAFTING_FUEL_SLOT = builder
                .comment("Give the Crafting Controller a fuel slot")
                .define("crafting_controller", true);

        builder.pop();

        SPEC = builder.build();
    }
}
