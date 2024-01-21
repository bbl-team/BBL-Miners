package com.benbenlaw.miners.screen;

import com.benbenlaw.miners.Miners;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Miners.MOD_ID);

    public static final RegistryObject<MenuType<MinerMenu>> MINER_MENU =
            registerMenuType(MinerMenu::new, "miner_menu");

    public static final RegistryObject<MenuType<TreeAbsorberMenu>> TREE_ABSORBER_MENU =
            registerMenuType(TreeAbsorberMenu::new, "tree_absorber_menu");

    public static final RegistryObject<MenuType<FluidAbsorberMenu>> FLUID_ABSORBER_MENU =
            registerMenuType(FluidAbsorberMenu::new, "fluid_absorber_menu");

    public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER_MENU =
            registerMenuType(CrusherMenu::new, "crusher_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}