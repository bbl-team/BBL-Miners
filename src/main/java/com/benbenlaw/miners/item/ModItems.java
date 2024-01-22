package com.benbenlaw.miners.item;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.item.custom.DetectorItem;
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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
