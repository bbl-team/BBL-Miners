package com.benbenlaw.miners.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class MinersConfigFile {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> leavesDropChanceFromTreeAbsorber;
    public static final ForgeConfigSpec.ConfigValue<Double> sticksDropChanceFromTreeAbsorber;
    public static final ForgeConfigSpec.ConfigValue<Double> applesDropChanceFromTreeAbsorberOakTrees;
    public static final ForgeConfigSpec.ConfigValue<Double> cocoaDropChanceFromTreeAbsorberJungleTrees;




    static {
        BUILDER.push("Miners config file");

        leavesDropChanceFromTreeAbsorber = BUILDER.comment("Chance that leaves generate from tree absorbers, 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.25")
                .define("Leaves drop chance", 0.50);

        sticksDropChanceFromTreeAbsorber = BUILDER.comment("Chance that stick generate from tree absorbers, 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.25")
                .define("Sticks drop chance", 0.90);

        applesDropChanceFromTreeAbsorberOakTrees = BUILDER.comment("Chance that apples generate from tree absorbers (Oak Trees Only), 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.25")
                .define("Apples drop chance", 0.90);

        cocoaDropChanceFromTreeAbsorberJungleTrees = BUILDER.comment("Chance that stick generate from tree absorbers (Jungle Trees Only), 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.25")
                .define("Apples drop chance", 0.90);




        BUILDER.pop();
        SPEC = BUILDER.build();
    }



}
