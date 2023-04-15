package com.benbenlaw.miners.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class MinersConfigFile {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> leavesDropChanceFromTreeAbsorber;
    public static final ForgeConfigSpec.ConfigValue<Boolean> showParticlesOnMachines;




    static {
        BUILDER.push("Miners config file");

        leavesDropChanceFromTreeAbsorber = BUILDER.comment("Chance that leaves generate from tree absorbers, 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.25")
                .define("Leaves drop chance", 0.50);

        showParticlesOnMachines = BUILDER.comment("Show the particles of the machines when the multiblock and/or the cap blocks are there.")
                .define("Show particles?", true);




        BUILDER.pop();
        SPEC = BUILDER.build();
    }



}
