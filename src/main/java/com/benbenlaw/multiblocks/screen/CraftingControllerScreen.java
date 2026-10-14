package com.benbenlaw.multiblocks.screen;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.entity.CraftingControllerBlockEntity;
import com.benbenlaw.multiblocks.config.MultiblocksConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class CraftingControllerScreen extends AbstractContainerScreen<CraftingControllerMenu> {

    private static final Identifier TEXTURE = Multiblocks.identifier("textures/gui/crafter_gui.png");
    private static final Identifier ENERGY_BAR = Multiblocks.identifier("energy_bar");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");
    private static final Identifier STATUS_BUILT = Multiblocks.identifier("status_built");
    private static final Identifier STATUS_NOT_BUILT = Multiblocks.identifier("status_not_built");

    private static final int ARROW_X = 87;
    private static final int ARROW_Y = 31;
    private static final int STATUS_X = 149;
    private static final int STATUS_Y = 5;

    public CraftingControllerScreen(CraftingControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, CraftingControllerMenu.IMAGE_WIDTH, CraftingControllerMenu.IMAGE_HEIGHT);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int x = leftPos;
        int y = topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (MultiblocksConfig.CRAFTING_FUEL_SLOT.get()) {
            FuelSlotRenderer.draw(graphics, x + CraftingControllerMenu.FUEL_X, y + CraftingControllerMenu.FUEL_Y);
        }

        int energyHeight = menu.getScaledEnergy(52);
        if (energyHeight > 0) {
            int topOffset = 52 - energyHeight;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_BAR, 16, 52, 0, topOffset,
                    x + 8, y + 16 + topOffset, 16, energyHeight);
        }

        if (menu.getScaledProgress(24) > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, 24, 16, 0, 0,
                    x + ARROW_X, y + ARROW_Y, menu.getScaledProgress(24) + 1, 16);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        graphics.text(font, Component.translatable("gui.bblmultiblocks.optional_items"), CraftingControllerMenu.RESEARCH_X, CraftingControllerMenu.RESEARCH_Y - 10, 0xFF404040, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int x = leftPos;
        int y = topPos;

        DurationTooltip.renderDurationTooltip(graphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));

        int structureState = menu.getStructureState();
        if (structureState != CraftingControllerBlockEntity.STRUCTURE_NOT_REQUIRED) {
            boolean built = structureState == CraftingControllerBlockEntity.STRUCTURE_BUILT;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, built ? STATUS_BUILT : STATUS_NOT_BUILT, 10, 10, 0, 0,
                    x + STATUS_X, y + STATUS_Y, 10, 10);

            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, STATUS_X, STATUS_Y, 10, 10)) {
                graphics.setTooltipForNextFrame(font, Component.translatable(built
                        ? "gui.bblmultiblocks.structure_valid"
                        : "gui.bblmultiblocks.structure_invalid"), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        if (mouseX >= leftPos + 8 && mouseX <= leftPos + 24 && mouseY >= topPos + 16 && mouseY <= topPos + 68) {
            Component text = Component.literal("Energy: " + menu.data.get(2) + " / " + menu.data.get(3) + " FE");
            List<ClientTooltipComponent> components = List.of(ClientTooltipComponent.create(text.getVisualOrderText()));
            graphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }
}
