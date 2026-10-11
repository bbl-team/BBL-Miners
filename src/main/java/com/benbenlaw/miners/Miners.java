package com.benbenlaw.miners;

import com.benbenlaw.miners.block.MinersBlockEntities;
import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.item.MinersCreativeTab;
import com.benbenlaw.miners.item.MinersItems;
import com.benbenlaw.miners.recipe.MinersRecipeTypes;
import com.benbenlaw.miners.screen.MinerControllerScreen;
import com.benbenlaw.miners.screen.MinersMenuTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Miners.MOD_ID)
public class Miners {
    public static final String MOD_ID = "bblminers";
    public static final Logger LOGGER = LogManager.getLogger();

    public Miners(final IEventBus eventBus, final ModContainer modContainer) {

        MinersBlocks.BLOCKS.register(eventBus);
        MinersItems.ITEMS.register(eventBus);
        MinersBlockEntities.BLOCK_ENTITIES.register(eventBus);
        MinersMenuTypes.MENUS.register(eventBus);
        MinersRecipeTypes.SERIALIZER.register(eventBus);
        MinersRecipeTypes.TYPES.register(eventBus);
        MinersCreativeTab.CREATIVE_MODE_TABS.register(eventBus);

    }

    @EventBusSubscriber(modid = Miners.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MinersMenuTypes.MINER_CONTROLLER_MENU.get(), MinerControllerScreen::new);

        }
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
