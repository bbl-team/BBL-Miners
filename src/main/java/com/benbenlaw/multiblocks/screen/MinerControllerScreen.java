package com.benbenlaw.multiblocks.screen;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.cap.CapEffects;
import com.benbenlaw.multiblocks.config.MultiblocksConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MinerControllerScreen extends AbstractContainerScreen<MinerControllerMenu> {

    private static final Identifier TEXTURE = Multiblocks.identifier("textures/gui/miner_gui.png");
    private static final Identifier ENERGY_BAR = Multiblocks.identifier("energy_bar");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    private static final Identifier STATUS_BUILT = Multiblocks.identifier("status_built");
    private static final Identifier STATUS_NOT_BUILT = Multiblocks.identifier("status_not_built");

private static final Identifier CAP_INFO = Multiblocks.identifier("cap_info");

private static final int CAP_X = 137;
private static final int CAP_Y = 5;

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

        if (MultiblocksConfig.MINER_FUEL_SLOT.get()) {
            FuelSlotRenderer.draw(guiGraphics, x + MinerControllerMenu.FUEL_X, y + MinerControllerMenu.FUEL_Y);
        }

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
                    ? "gui.bblmultiblocks.structure_valid"
                    : "gui.bblmultiblocks.structure_invalid"), mouseX, mouseY);
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, CAP_INFO, 10, 10, 0, 0, x + CAP_X, y + CAP_Y, 10, 10);

        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, CAP_X, CAP_Y, 10, 10)) {
            guiGraphics.setComponentTooltipForNextFrame(font, capTooltip(), mouseX, mouseY);
        }
    }

    private List<Component> capTooltip() {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("gui.bblmultiblocks.cap_effects").withStyle(ChatFormatting.WHITE));

        int efficiency = menu.getEfficiencyLevels();
        int fortune = menu.getFortuneLevels();
        boolean silkTouch = menu.hasSilkTouch();

        if (efficiency == 0 && fortune == 0 && !silkTouch) {
            lines.add(Component.translatable("gui.bblmultiblocks.cap_none").withStyle(ChatFormatting.GRAY));
        } else {
            double speed = 1.0 + CapEffects.SPEED_PER_EFFICIENCY_LEVEL * efficiency;
            if (efficiency > 0) {
                lines.add(Component.translatable("gui.bblmultiblocks.cap_efficiency", efficiency, String.format("%.2f", speed)).withStyle(ChatFormatting.GOLD));
            }
            if (fortune > 0) {
                lines.add(Component.translatable("gui.bblmultiblocks.cap_fortune", fortune).withStyle(ChatFormatting.GREEN));
            }
            if (silkTouch) {
                lines.add(Component.translatable("gui.bblmultiblocks.cap_silk_touch").withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }

        lines.add(Component.translatable("gui.bblmultiblocks.cap_hint").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        return lines;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (event.button() == 1 && event.hasShiftDown()
                && MouseUtil.isMouseAboveArea((int) event.x(), (int) event.y(), x, y, CAP_X, CAP_Y, 10, 10)) {
            assert minecraft.gameMode != null;
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, MinerControllerMenu.BUTTON_SHOW_CAPS);
            return true;
        }

        return super.mouseClicked(event, doubleClick);
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
