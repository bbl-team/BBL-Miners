package com.benbenlaw.miners;

import com.benbenlaw.miners.block.entities.FormBlockEntity;
import com.benbenlaw.miners.block.FormBlock;
import com.benbenlaw.miners.item.DetectorItem;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Miners.MODID)
public class Miners {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "miners";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<DetectorItem> DETECTOR = ITEMS.register("detector", DetectorItem::new);

    public static final RegistryObject<FormBlock> FORM_BLOCK = BLOCKS.register("form", FormBlock::new);
    public static final RegistryObject<BlockItem> FORM_BLOCK_ITEM = ITEMS.register("form", () -> new BlockItem(FORM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockEntityType<FormBlockEntity>> FORM_BLOCK_ENTITY = BLOCK_ENTITYS.register(
            "form",
            () -> BlockEntityType.Builder.of(
                    FormBlockEntity::new,
                    FORM_BLOCK.get()
            ).build(null)
    );

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.BEDROCK::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(Items.BEDROCK);
                output.accept(DETECTOR.get());
                output.accept(FORM_BLOCK_ITEM.get());
            }).build());

    public Miners() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITYS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

}
