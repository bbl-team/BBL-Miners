package com.benbenlaw.multiblocks.item;

import com.benbenlaw.multiblocks.Multiblocks;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MultiblocksItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Multiblocks.MOD_ID);

    public static final DeferredItem<Item> PATTERN_MAKER = ITEMS.registerItem("pattern_maker",
            PatternMakerItem::new, properties -> properties.stacksTo(1));
}
