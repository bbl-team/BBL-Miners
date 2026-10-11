package com.benbenlaw.miners.client;

import com.benbenlaw.miners.Miners;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;

@EventBusSubscriber(modid = Miners.MOD_ID, value = Dist.CLIENT)
public class MinersClientEvents {

    @SubscribeEvent
    public static void registerPipRenderers(RegisterPictureInPictureRenderersEvent event) {
        event.register(StructurePreviewRenderState.class, StructurePreviewRenderer::new);
    }
}
