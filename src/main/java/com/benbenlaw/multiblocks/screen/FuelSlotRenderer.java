package com.benbenlaw.multiblocks.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Draws a fuel slot frame for the controllers, since the GUI textures no longer contain one. */
public final class FuelSlotRenderer {

    private static final int LIGHT = 0xFFFFFFFF;
    private static final int DARK = 0xFF373737;
    private static final int FILL = 0xFF8B8B8B;

    private FuelSlotRenderer() {
    }

    /** x and y are the screen position of the slot's item area (the same numbers the menu uses for the slot). */
    public static void draw(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, LIGHT);
        graphics.fill(x - 1, y - 1, x + 16, y + 16, DARK);
        graphics.fill(x, y, x + 16, y + 16, FILL);
    }
}
