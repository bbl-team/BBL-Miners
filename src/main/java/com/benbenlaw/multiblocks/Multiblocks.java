package com.benbenlaw.multiblocks;

import com.benbenlaw.multiblocks.block.MultiblocksBlockEntities;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.item.MultiblocksCreativeTab;
import com.benbenlaw.multiblocks.item.MultiblocksItems;
import com.benbenlaw.multiblocks.recipe.MultiblocksRecipeTypes;
import com.benbenlaw.multiblocks.screen.CraftingControllerScreen;
import com.benbenlaw.multiblocks.screen.MultiblocksMenuTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import com.benbenlaw.multiblocks.config.MultiblocksConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Multiblocks.MOD_ID)
public class Multiblocks {
    public static final String MOD_ID = "bblmultiblocks";
    public static final Logger LOGGER = LogManager.getLogger();

    public Multiblocks(final IEventBus eventBus, final ModContainer modContainer) {

        MultiblocksBlocks.BLOCKS.register(eventBus);
        MultiblocksItems.ITEMS.register(eventBus);
        MultiblocksBlockEntities.BLOCK_ENTITIES.register(eventBus);
        MultiblocksMenuTypes.MENUS.register(eventBus);
        MultiblocksRecipeTypes.SERIALIZER.register(eventBus);
        MultiblocksRecipeTypes.TYPES.register(eventBus);
        MultiblocksCreativeTab.CREATIVE_MODE_TABS.register(eventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, MultiblocksConfig.SPEC, "bbl/multiblocks/server.toml");

    }

    @EventBusSubscriber(modid = Multiblocks.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(MultiblocksMenuTypes.CRAFTING_CONTROLLER_MENU.get(), CraftingControllerScreen::new);

        }
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
