package com.benbenlaw.multiblocks.screen;

import com.benbenlaw.multiblocks.Multiblocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MultiblocksMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Multiblocks.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MinerControllerMenu>> MINER_CONTROLLER_MENU =
            MENUS.register("miner_controller_menu", () -> IMenuTypeExtension.create(MinerControllerMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<CraftingControllerMenu>> CRAFTING_CONTROLLER_MENU =
            MENUS.register("crafting_controller_menu", () -> IMenuTypeExtension.create(CraftingControllerMenu::new));
}
