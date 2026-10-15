package com.benbenlaw.multiblocks.integration.kubejs;

import com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

/** A controller made in a KubeJS startup script. It has the Crafting Controller's GUI and uses recipes that name it as their controller. */
public class CraftingControllerBuilder extends BlockBuilder {

    private @Nullable Component jeiTitle;
    private @Nullable Identifier jeiIcon;

    public CraftingControllerBuilder(Identifier id) {
        super(id);
    }

    @Info("The name of this controller's JEI category, defaults to the display name of the block")
    public CraftingControllerBuilder jeiTitle(Component title) {
        this.jeiTitle = title;
        return this;
    }

    @Info("The item shown as the icon of this controller's JEI category, eg minecraft:furnace, defaults to the controller itself")
    public CraftingControllerBuilder jeiIcon(Identifier item) {
        this.jeiIcon = item;
        return this;
    }

    @Override
    public Block createObject() {
        return new KubeCraftingControllerBlock(createProperties(), jeiTitle, jeiIcon);
    }

    private static class KubeCraftingControllerBlock extends CraftingControllerBlock {

        private final @Nullable Component jeiTitle;
        private final @Nullable Identifier jeiIcon;

        KubeCraftingControllerBlock(Properties properties, @Nullable Component jeiTitle, @Nullable Identifier jeiIcon) {
            super(properties);
            this.jeiTitle = jeiTitle;
            this.jeiIcon = jeiIcon;
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
