package com.benbenlaw.miners.item.custom;

import com.benbenlaw.miners.multiblock.MultiBlockManagers;
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
                var minerResult = MultiBlockManagers.MINERS.findStructure(level, pos);
                var treeAbsorberResult = MultiBlockManagers.TREE_ABSORBERS.findStructure(level, pos);

                if (minerResult != null) {
                    player.sendSystemMessage(Component.literal("Found Structure : %s".formatted(minerResult.ID())));
                }
                if (treeAbsorberResult != null) {
                    player.sendSystemMessage(Component.literal("Found Structure : %s".formatted(treeAbsorberResult.ID())));
                }


                else {
                    player.sendSystemMessage(Component.literal("Found no structure!"));
                }
            }
        }
        return super.useOn(context);
    }
}
