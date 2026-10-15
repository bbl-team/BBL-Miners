package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.multiblocks.block.custom.MinerControllerBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

/** A miner controller made in a KubeJS startup script. It has the Miner Controller's GUI and uses the miner recipes that name it as their controller. */
public class MinerControllerBuilder extends BlockBuilder {

    private @Nullable Component jeiTitle;
    private @Nullable Identifier jeiIcon;
    private boolean fuelSlot = true;

    public MinerControllerBuilder(Identifier id) {
        super(id);
    }

    @Info("The name of this controller's JEI category, defaults to the display name of the block")
    public MinerControllerBuilder jeiTitle(Component title) {
        this.jeiTitle = title;
        return this;
    }

    @Info("The item shown as the icon of this controller's JEI category, eg minecraft:furnace, defaults to the controller itself")
    public MinerControllerBuilder jeiIcon(Identifier item) {
        this.jeiIcon = item;
        return this;
    }

    @Info("Whether the GUI has a fuel slot that turns burnable items into energy, default true")
    public MinerControllerBuilder fuelSlot(boolean fuelSlot) {
        this.fuelSlot = fuelSlot;
        return this;
    }

    @Override
    public Block createObject() {
        return new KubeMinerControllerBlock(createProperties(), jeiTitle, jeiIcon, fuelSlot);
    }

    private static class KubeMinerControllerBlock extends MinerControllerBlock {

        private final @Nullable Component jeiTitle;
        private final @Nullable Identifier jeiIcon;
        private final boolean fuelSlot;

        KubeMinerControllerBlock(Properties properties, @Nullable Component jeiTitle, @Nullable Identifier jeiIcon, boolean fuelSlot) {
            super(properties);
            this.fuelSlot = fuelSlot;
            this.jeiTitle = jeiTitle;
            this.jeiIcon = jeiIcon;
        }

        @Override
        public boolean hasFuelSlot() {
            return fuelSlot;
        }

        @Override
        public @Nullable Component jeiTitle() {
            return jeiTitle;
        }

        @Override
        public @Nullable ItemLike jeiIcon() {
            if (jeiIcon == null) return null;
            Item item = BuiltInRegistries.ITEM.getValue(jeiIcon);
            return item == net.minecraft.world.item.Items.AIR ? null : item;
        }
    }
}
