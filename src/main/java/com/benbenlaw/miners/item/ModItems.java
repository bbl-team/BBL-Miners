package com.benbenlaw.miners.item;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.item.custom.MinerStructurePlacerItem;
import com.benbenlaw.miners.item.custom.ModCreativeModTab;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Miners.MOD_ID);

    public static final RegistryObject<Item> MINER_PICKAXE = ITEMS.register("miner_pickaxe", () -> new Item(
            new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE).tab(ModCreativeModTab.MINERS)));

    public static final RegistryObject<Item> MINER_STRUCTURE_PLACER = ITEMS.register("miner_structure_placer", () -> new MinerStructurePlacerItem(
            new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE).tab(ModCreativeModTab.MINERS)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
