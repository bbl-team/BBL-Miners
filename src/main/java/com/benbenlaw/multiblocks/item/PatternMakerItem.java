package com.benbenlaw.multiblocks.item;

import net.minecraft.world.item.Item;

/**
 * Right-click two corners of a structure built in the world (a block or a point in the air), then name it. The box becomes
 * a structure json: the controller is the '@', cap blocks are the cap slots. Sneak and right-click, or left-click, to
 * cancel a selection. The clicks are handled on the client, see PatternSelectionEvents.
 */
public class PatternMakerItem extends Item {

    public PatternMakerItem(Properties properties) {
        super(properties);
    }
}
