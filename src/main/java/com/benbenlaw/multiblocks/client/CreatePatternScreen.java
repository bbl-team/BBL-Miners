package com.benbenlaw.multiblocks.client;

import com.benbenlaw.multiblocks.network.CreatePatternPacket;
import com.benbenlaw.multiblocks.pattern.PatternExporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/** Names the structure a Pattern Maker selection will be turned into. */
public class CreatePatternScreen extends Screen {

    private static final int WIDTH = 220;
    private static final int HEIGHT = 162;

    private final BlockPos first;
    private final BlockPos second;
    private final boolean tooLarge;

    private EditBox nameBox;
    private Button frameTagsButton;
    private Button createButton;
    private boolean frameTags = true;
    private boolean saveStates = true;
    private int panelX;
    private int panelY;

    public CreatePatternScreen(BlockPos first, BlockPos second) {
        super(Component.translatable("gui.bblmultiblocks.pattern.title"));
        this.first = first;
        this.second = second;
        this.tooLarge = PatternExporter.tooLarge(first, second);
    }

    @Override
    protected void init() {
        panelX = (width - WIDTH) / 2;
        panelY = (height - HEIGHT) / 2;

        String previous = nameBox != null ? nameBox.getValue() : "";

        nameBox = new EditBox(Minecraft.getInstance().font, panelX + 8, panelY + 46, WIDTH - 16, 18, Component.translatable("gui.bblmultiblocks.pattern.name"));
        nameBox.setMaxLength(PatternExporter.MAX_NAME_LENGTH);
        nameBox.setValue(previous);
        addRenderableWidget(nameBox);
        setFocused(nameBox);

        frameTagsButton = addRenderableWidget(Button.builder(frameTagsLabel(), button -> {
            frameTags = !frameTags;
            button.setMessage(frameTagsLabel());
        }).bounds(panelX + 8, panelY + 70, WIDTH - 16, 18).build());

        addRenderableWidget(Button.builder(saveStatesLabel(), button -> {
            saveStates = !saveStates;
            button.setMessage(saveStatesLabel());
        }).bounds(panelX + 8, panelY + 92, WIDTH - 16, 18).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.bblmultiblocks.pattern.cancel"), button -> onClose())
                .bounds(panelX + 8, panelY + HEIGHT - 28, 96, 20).build());

        createButton = addRenderableWidget(Button.builder(Component.translatable("gui.bblmultiblocks.pattern.create"), button -> confirm())
                .bounds(panelX + WIDTH - 104, panelY + HEIGHT - 28, 96, 20).build());
        createButton.active = !tooLarge;
    }

    private Component saveStatesLabel() {
        return Component.translatable(saveStates ? "gui.bblmultiblocks.pattern.states_on" : "gui.bblmultiblocks.pattern.states_off");
    }

    private Component frameTagsLabel() {
        return Component.translatable(frameTags ? "gui.bblmultiblocks.pattern.frame_tags_on" : "gui.bblmultiblocks.pattern.frame_tags_off");
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);

        graphics.fill(panelX - 2, panelY - 2, panelX + WIDTH + 2, panelY + HEIGHT + 2, 0xFF1E1E1E);
        graphics.fill(panelX, panelY, panelX + WIDTH, panelY + HEIGHT, 0xFFC6C6C6);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        var font = Minecraft.getInstance().font;

        graphics.text(font, title, panelX + 8, panelY + 8, 0xFF404040, false);

        int sizeX = Math.abs(first.getX() - second.getX()) + 1;
        int sizeY = Math.abs(first.getY() - second.getY()) + 1;
        int sizeZ = Math.abs(first.getZ() - second.getZ()) + 1;
        graphics.text(font, Component.translatable("gui.bblmultiblocks.pattern.size", sizeX, sizeY, sizeZ),
                panelX + 8, panelY + 20, tooLarge ? 0xFFB71C1C : 0xFF404040, false);

        graphics.text(font, tooLarge
                        ? Component.translatable("gui.bblmultiblocks.pattern.too_large", PatternExporter.MAX_SIDE)
                        : Component.translatable("gui.bblmultiblocks.pattern.name"),
                panelX + 8, panelY + 34, tooLarge ? 0xFFB71C1C : 0xFF404040, false);

        graphics.text(font, Component.translatable("gui.bblmultiblocks.pattern.hint"), panelX + 8, panelY + 116, 0xFF606060, false);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if ((event.key() == org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER || event.key() == org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER) && !tooLarge) {
            confirm();
            return true;
        }
        return super.keyPressed(event);
    }

    private void confirm() {
        ClientPacketDistributor.sendToServer(new CreatePatternPacket(first, second, nameBox.getValue(), frameTags, saveStates));
        onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
