package com.benbenlaw.miners.items;

import com.benbenlaw.miners.core.MultiblockManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class DetectorItem extends Item {
    public DetectorItem() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            var level = context.getLevel();
            var pos = context.getClickedPos();
            var player = context.getPlayer();
            if (player != null) {
                var result = MultiblockManager.findUnformedStructure(level, pos);
                if (result != null) {
                    player.sendSystemMessage(Component.literal("Found Structure : %s".formatted(result)));
                } else {
                    player.sendSystemMessage(Component.literal("Found no structure!"));
                }
            }
        }
        return super.useOn(context);
    }
}
