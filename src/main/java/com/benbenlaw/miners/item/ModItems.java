package com.benbenlaw.miners.item;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.item.custom.DetectorItem;
import com.benbenlaw.miners.item.custom.upgrades.improved.ImprovedOutputUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.improved.ImprovedRFUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.improved.ImprovedSpeedUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.reinforced.ReinforcedOutputUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.reinforced.ReinforcedRFUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.reinforced.ReinforcedSpeedUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.sturdy.SturdyOutputUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.sturdy.SturdyRFUpgrade;
import com.benbenlaw.miners.item.custom.upgrades.sturdy.SturdySpeedUpgrade;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Miners.MOD_ID);

    //New Items


    public static final RegistryObject<Item> DETECTOR = ITEMS.register("detector", DetectorItem::new);

    // Upgrades

    // Improved Upgrades
    public static final RegistryObject<Item> IMPROVED_SPEED_UPGRADE = ITEMS.register("improved_speed_upgrade",
            () -> new ImprovedSpeedUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> IMPROVED_OUTPUT_UPGRADE = ITEMS.register("improved_output_upgrade",
            () -> new ImprovedOutputUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> IMPROVED_RF_UPGRADE = ITEMS.register("improved_rf_upgrade",
            () -> new ImprovedRFUpgrade(new Item.Properties().stacksTo(1)));

    // Sturdy Upgrades
    public static final RegistryObject<Item> STURDY_SPEED_UPGRADE = ITEMS.register("sturdy_speed_upgrade",
            () -> new SturdySpeedUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> STURDY_OUTPUT_UPGRADE = ITEMS.register("sturdy_output_upgrade",
            () -> new SturdyOutputUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> STURDY_RF_UPGRADE = ITEMS.register("sturdy_rf_upgrade",
            () -> new SturdyRFUpgrade(new Item.Properties().stacksTo(1)));

    // Reinforced Upgrades
    public static final RegistryObject<Item> REINFORCED_SPEED_UPGRADE = ITEMS.register("reinforced_speed_upgrade",
            () -> new ReinforcedSpeedUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> REINFORCED_OUTPUT_UPGRADE = ITEMS.register("reinforced_output_upgrade",
            () -> new ReinforcedOutputUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> REINFORCED_RF_UPGRADE = ITEMS.register("reinforced_rf_upgrade",
            () -> new ReinforcedRFUpgrade(new Item.Properties().stacksTo(1)));

    // Everlasting Upgrades
    public static final RegistryObject<Item> EVERLASTING_SPEED_UPGRADE = ITEMS.register("everlasting_speed_upgrade",
            () -> new ReinforcedSpeedUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EVERLASTING_OUTPUT_UPGRADE = ITEMS.register("everlasting_output_upgrade",
            () -> new ReinforcedOutputUpgrade(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EVERLASTING_RF_UPGRADE = ITEMS.register("everlasting_rf_upgrade",
            () -> new ReinforcedRFUpgrade(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
