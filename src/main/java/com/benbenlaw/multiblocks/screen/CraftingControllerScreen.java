package com.benbenlaw.multiblocks.screen;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.entity.CraftingControllerBlockEntity;
import com.benbenlaw.multiblocks.config.MultiblocksConfig;
import com.benbenlaw.multiblocks.structure.StructureNames;
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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingControllerScreen extends AbstractContainerScreen<CraftingControllerMenu> {

    private static final Identifier TEXTURE = Multiblocks.identifier("textures/gui/crafter_gui.png");
    private static final Identifier ENERGY_BAR = Multiblocks.identifier("energy_bar");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");
    private static final Identifier STATUS_BUILT = Multiblocks.identifier("status_built");
    private static final Identifier STATUS_NOT_BUILT = Multiblocks.identifier("status_not_built");

    private static final int ARROW_X = 87;
    private static final int ARROW_Y = 31;
    private static final Identifier LOCK_LOCKED = Multiblocks.identifier("lock_locked");
    private static final Identifier LOCK_UNLOCKED = Multiblocks.identifier("lock_unlocked");

    /** The title stops before the icons in the top right. */
    private static final int TITLE_MAX_WIDTH = 125;

    private static final int LOCK_X = 137;
    private static final int LOCK_Y = 5;

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

        if (menu.blockEntity.hasFuelSlot()) {
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

    /** The block name, or the name of the structure the controller is built as. */
    private void drawTitles(GuiGraphicsExtractor graphics) {
        graphics.text(font, Component.literal(font.plainSubstrByWidth(title.getString(), TITLE_MAX_WIDTH)), titleLabelX, titleLabelY, 0xFF404040, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xFF404040, false);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        drawTitles(graphics);
        graphics.text(font, Component.translatable("gui.bblmultiblocks.extra_items"), CraftingControllerMenu.RESEARCH_X, CraftingControllerMenu.RESEARCH_Y - 10, 0xFF404040, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int x = leftPos;
        int y = topPos;

        DurationTooltip.renderDurationTooltip(graphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));

        renderLockGhosts(graphics, x, y);
        renderLockButton(graphics, mouseX, mouseY, x, y);

        int structureState = menu.getStructureState();
        if (structureState != CraftingControllerBlockEntity.STRUCTURE_NOT_REQUIRED) {
            boolean built = structureState == CraftingControllerBlockEntity.STRUCTURE_BUILT;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, built ? STATUS_BUILT : STATUS_NOT_BUILT, 10, 10, 0, 0,
                    x + STATUS_X, y + STATUS_Y, 10, 10);

            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, STATUS_X, STATUS_Y, 10, 10)) {
                List<Component> lines = new ArrayList<>();
                lines.add(Component.translatable(built ? "gui.bblmultiblocks.structure_valid" : "gui.bblmultiblocks.structure_invalid"));

                Identifier structure = menu.blockEntity.getActiveStructureId();
                if (built && structure != null) lines.add(StructureNames.of(structure).copy().withStyle(ChatFormatting.GRAY));
                graphics.setComponentTooltipForNextFrame(font, lines, mouseX, mouseY);
            }
        }
    }

    /** Empty locked slots show the item they will accept, faded; locked slots that accept nothing are darkened. */
    private void renderLockGhosts(GuiGraphicsExtractor graphics, int x, int y) {
        if (!menu.isLocked()) return;

        for (int index = 0; index < menu.lockableSlots.length; index++) {
            Slot slot = menu.lockableSlots[index];
            if (slot == null || slot.hasItem()) continue;

            ItemStack filter = menu.blockEntity.getLockFilter(index);
            int slotX = x + slot.x;
            int slotY = y + slot.y;

            if (filter.isEmpty()) {
                graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0x66000000);
            } else {
                graphics.item(filter, slotX, slotY, slotX + slotY * imageWidth);
                graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0x908B8B8B);
            }
        }
    }

    private void renderLockButton(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int x, int y) {
        boolean locked = menu.isLocked();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, locked ? LOCK_LOCKED : LOCK_UNLOCKED, 10, 10, 0, 0, x + LOCK_X, y + LOCK_Y, 10, 10);

        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, LOCK_X, LOCK_Y, 10, 10)) {
            graphics.setComponentTooltipForNextFrame(font, List.of(
                    Component.translatable(locked ? "gui.bblmultiblocks.lock_locked" : "gui.bblmultiblocks.lock_unlocked"),
                    Component.translatable(locked ? "gui.bblmultiblocks.lock_locked_hint" : "gui.bblmultiblocks.lock_unlocked_hint").withStyle(ChatFormatting.GRAY)
            ), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && MouseUtil.isMouseAboveArea((int) event.x(), (int) event.y(), leftPos, topPos, LOCK_X, LOCK_Y, 10, 10)) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, CraftingControllerMenu.BUTTON_TOGGLE_LOCK);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
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
