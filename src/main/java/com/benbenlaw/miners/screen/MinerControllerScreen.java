package com.benbenlaw.miners.screen;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.miners.Miners;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class MinerControllerScreen extends AbstractContainerScreen<MinerControllerMenu> {

    private static final Identifier TEXTURE = Miners.identifier("textures/gui/miner_gui.png");
    private static final Identifier ENERGY_BAR = Miners.identifier("energy_bar");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    private static final Identifier STATUS_BUILT = Miners.identifier("status_built");
    private static final Identifier STATUS_NOT_BUILT = Miners.identifier("status_not_built");

    private static final int STATUS_X = 149;
    private static final int STATUS_Y = 5;

    private static final int ARROW_X = 76;
    private static final int ARROW_Y = 31;

    public MinerControllerScreen(MinerControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.getScaledProgress(24) > 0) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, 24, 16, 0, 0,
                    x + ARROW_X, y + ARROW_Y, menu.getScaledProgress(24) + 1, 16);
        }

        int energyHeight = menu.getScaledEnergy(52);
        if (energyHeight > 0) {
            int topOffset = 52 - energyHeight;
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_BAR, 16, 52, 0, topOffset,
                    x + 8, y + topOffset + 16, 16, energyHeight);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        DurationTooltip.renderDurationTooltip(guiGraphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));

        boolean built = menu.isStructureValid();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, built ? STATUS_BUILT : STATUS_NOT_BUILT, 10, 10, 0, 0,
                x + STATUS_X, y + STATUS_Y, 10, 10);

        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, STATUS_X, STATUS_Y, 10, 10)) {
            guiGraphics.setTooltipForNextFrame(font, Component.translatable(built
                    ? "gui.bblminers.structure_valid"
                    : "gui.bblminers.structure_invalid"), mouseX, mouseY);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (mouseX >= x + 8 && mouseX <= x + 24 && mouseY >= y + 16 && mouseY <= y + 68) {
            Component text = Component.literal("Energy: " + menu.data.get(2) + " / " + menu.data.get(3) + " FE");
            List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
            graphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }
}
