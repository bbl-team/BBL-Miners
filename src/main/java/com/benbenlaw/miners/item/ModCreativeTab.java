package com.benbenlaw.miners.item;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Miners.MOD_ID);

    public static final RegistryObject<CreativeModeTab> INFINITY_TAB = CREATIVE_MODE_TABS.register("miners", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModBlocks.MINER.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.miners"))
            .displayItems((parameters, output) -> {

                output.accept(ModItems.DETECTOR.get());
                output.accept(ModBlocks.MINER.get());
                output.accept(ModBlocks.TREE_ABSORBER.get());
                output.accept(ModBlocks.FLUID_ABSORBER.get());
                output.accept(ModBlocks.WOODEN_SUPPORT_FRAME.get());
                output.accept(ModBlocks.STONE_SUPPORT_FRAME.get());
                output.accept(ModBlocks.LAPIS_SUPPORT_FRAME.get());
                output.accept(ModBlocks.COPPER_SUPPORT_FRAME.get());
                output.accept(ModBlocks.IRON_SUPPORT_FRAME.get());
                output.accept(ModBlocks.GOLD_SUPPORT_FRAME.get());
                output.accept(ModBlocks.DIAMOND_SUPPORT_FRAME.get());
                output.accept(ModBlocks.EMERALD_SUPPORT_FRAME.get());
                output.accept(ModBlocks.NETHERITE_SUPPORT_FRAME.get());
                output.accept(ModBlocks.ULTIMATE_SUPPORT_FRAME.get());



            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }


}
