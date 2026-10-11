package com.benbenlaw.miners.screen;

import com.benbenlaw.miners.Miners;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MinersMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Miners.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MinerControllerMenu>> MINER_CONTROLLER_MENU =
            MENUS.register("miner_controller_menu", () -> IMenuTypeExtension.create(MinerControllerMenu::new));
}
