package com.benbenlaw.multiblocks.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MultiblocksConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue CRAFTING_FUEL_SLOT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("The built-in Crafting Controller's fuel slot (custom controllers choose theirs in KubeJS). With a fuel slot, burnable items are turned into energy. Turn a slot off to power that controller only with energy from outside (cables, generators and so on).")
                .push("fuel_slots");

        CRAFTING_FUEL_SLOT = builder
                .comment("Give the Crafting Controller a fuel slot")
                .define("crafting_controller", true);

        builder.pop();

        SPEC = builder.build();
    }
}
