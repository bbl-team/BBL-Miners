package com.benbenlaw.miners.item;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
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
                output.accept(ModBlocks.CRUSHER.get());
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

                TagKey<Block> oreTin = BlockTags.create(new ResourceLocation("forge","ores/tin"));
                TagKey<Block> oreRuby = BlockTags.create(new ResourceLocation("forge","ores/ruby"));

                /*if (oreTin != null){
                    output.accept(ModItems.RAW_TIN.get());
                }
                if (oreRuby != null){
                    output.accept(ModItems.RAW_RUBY.get());
                }*/

                // Upgrades
                output.accept(ModItems.IMPROVED_OUTPUT_UPGRADE.get());
                output.accept(ModItems.IMPROVED_SPEED_UPGRADE.get());
                output.accept(ModItems.IMPROVED_RF_UPGRADE.get());

                output.accept(ModItems.STURDY_OUTPUT_UPGRADE.get());
                output.accept(ModItems.STURDY_SPEED_UPGRADE.get());
                output.accept(ModItems.STURDY_RF_UPGRADE.get());

                output.accept(ModItems.REINFORCED_OUTPUT_UPGRADE.get());
                output.accept(ModItems.REINFORCED_SPEED_UPGRADE.get());
                output.accept(ModItems.REINFORCED_RF_UPGRADE.get());

                output.accept(ModItems.EVERLASTING_OUTPUT_UPGRADE.get());
                output.accept(ModItems.EVERLASTING_SPEED_UPGRADE.get());
                output.accept(ModItems.EVERLASTING_RF_UPGRADE.get());

                output.accept(ModItems.SPECIALIZED_OUTPUT_UPGRADE.get());
                output.accept(ModItems.SPECIALIZED_SPEED_UPGRADE.get());
                output.accept(ModItems.SPECIALIZED_RF_UPGRADE.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }


}
