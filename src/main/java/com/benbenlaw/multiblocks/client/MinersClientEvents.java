package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.Multiblocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;

@EventBusSubscriber(modid = Multiblocks.MOD_ID, value = Dist.CLIENT)
public class MinersClientEvents {

    @SubscribeEvent
    public static void registerPipRenderers(RegisterPictureInPictureRenderersEvent event) {
        event.register(StructurePreviewRenderState.class, StructurePreviewRenderer::new);
    }
}
