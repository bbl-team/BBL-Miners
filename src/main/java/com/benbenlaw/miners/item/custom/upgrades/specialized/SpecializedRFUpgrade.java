package com.benbenlaw.miners.item.custom.upgrades.specialized;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SpecializedRFUpgrade extends Item {
    public SpecializedRFUpgrade(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.miners.specialized_rf_upgrade.shift"));
        } else {
            components.add(Component.translatable("tooltips.miners.upgrade"));
        }

        super.appendHoverText(stack, level, components, tooltipFlag);
    }
}