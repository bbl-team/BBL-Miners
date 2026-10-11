package com.benbenlaw.miners.integration.jei;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.client.StructurePreviewBuilder;
import com.benbenlaw.miners.client.StructurePreviewRenderState;
import com.benbenlaw.miners.client.StructurePreviewRenderer;
import com.benbenlaw.miners.network.MinerPreviewPacket;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IScrollGridWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerStructureCategory implements IRecipeCategory<MinerStructureCategory.Entry> {

    public static final IRecipeType<Entry> TYPE = IRecipeType.create(Miners.MOD_ID, "miner_structure", Entry.class);

    private static final int WIDTH = 180;
    private static final int HEIGHT = 108;
    private static final int FOOTER_HEIGHT = 12;
    private static final int VIEW_WIDTH = 112;
    private static final int VIEW_HEIGHT = 80;
    private static final int NAME_Y = 85;
    private static final int GRID_X = WIDTH - 52;
    private static final int GRID_Y = 22;
    private static final int COLUMNS = 2;
    private static final int VISIBLE_ROWS = 4;

    public record Entry(MinerPreviewPacket.Preview preview) {
    }

    private final IDrawable icon;
    private final Map<Identifier, StructurePreviewRenderer> renderers = new HashMap<>();

    public MinerStructureCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(MinersBlocks.MINER_CONTROLLER.get());
    }

    @Override
    public IRecipeType<Entry> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.bblminers.miner_structure");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Entry entry, IFocusGroup focuses) {
        builder.addInputSlot()
                .add(new ItemStack(MinersBlocks.MINER_CONTROLLER.get()))
                .setStandardSlotBackground();

        for (MinerPreviewPacket.Drop drop : entry.preview().drops()) {
            IRecipeSlotBuilder slot = builder.addOutputSlot();
            if (drop.variants().isEmpty()) slot.add(drop.stack());
            else drop.variants().forEach(slot::add);

            slot.addRichTooltipCallback((view, tooltip) -> {
                String count = drop.min() == drop.max() ? String.valueOf(drop.min()) : drop.min() + "-" + drop.max();
                tooltip.add(Component.translatable("jei.bblminers.count", count).withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("jei.bblminers.chance", formatChance(drop.chance())).withStyle(ChatFormatting.GRAY));
                for (Component note : drop.notes()) {
                    tooltip.add(Component.literal("- ").append(note).withStyle(ChatFormatting.DARK_AQUA));
                }
            });
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Entry entry, IFocusGroup focuses) {
        List<IRecipeSlotDrawable> outputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.OUTPUT);
        IScrollGridWidget grid = builder.addScrollGridWidget(outputSlots, COLUMNS, VISIBLE_ROWS);
        grid.setPosition(GRID_X, GRID_Y, WIDTH - GRID_X, HEIGHT - FOOTER_HEIGHT - GRID_Y, HorizontalAlignment.CENTER, VerticalAlignment.TOP);

        builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT).getFirst()
                .setPosition(GRID_X + (COLUMNS * 18 - 18) / 2 + 1, 1);
    }

    @Override
    public void draw(Entry entry, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        MinerPreviewPacket.Preview preview = entry.preview();
        Minecraft mc = Minecraft.getInstance();
        var font = mc.font;

        Component name = Component.literal(prettyName(preview.structureId()));
        guiGraphics.text(font, name, (VIEW_WIDTH - font.width(name)) / 2, NAME_Y, 0xFF404040, false);

        Component summary = Component.translatable("jei.bblminers.summary",
                preview.baseRolls(), preview.baseDuration() / 20.0, preview.energyPerTick());
        guiGraphics.text(font, summary, (WIDTH - font.width(summary)) / 2, HEIGHT - FOOTER_HEIGHT + 3, 0xFF404040, false);

        guiGraphics.fill(0, 0, VIEW_WIDTH, VIEW_HEIGHT, 0x33000000);

        Vector2f origin = guiGraphics.pose().transformPosition(0f, 0f, new Vector2f());
        int x0 = Math.round(origin.x);
        int y0 = Math.round(origin.y);

        long now = System.currentTimeMillis();
        StructurePreviewBuilder.Result result = StructurePreviewBuilder.build(preview.pattern(), now);

        StructurePreviewRenderState state = new StructurePreviewRenderState(
                result.blocks(), result.size(), (now % 36000L) / 20f,
                x0, y0, x0 + VIEW_WIDTH, y0 + VIEW_HEIGHT, 1.0f, null, 56.0f);

        StructurePreviewRenderer renderer = renderers.computeIfAbsent(preview.recipeId(),
                key -> new StructurePreviewRenderer(mc.renderBuffers().bufferSource()));
        renderer.prepare(state, guiGraphics.guiRenderState, (int) mc.getWindow().getGuiScale());
    }

    public void closeRenderers() {
        renderers.values().forEach(StructurePreviewRenderer::close);
        renderers.clear();
    }

private static String prettyName(Identifier id) {
    StringBuilder out = new StringBuilder();
    for (String word : id.getPath().split("[_/]")) {
        if (word.isEmpty()) continue;
        if (!out.isEmpty()) out.append(' ');
        out.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
    }
    return out.toString();
}

private static String formatChance(float chance) {
        float percent = chance * 100f;
        if (percent >= 10f) return String.format("%.0f%%", percent);
        if (percent >= 1f) return String.format("%.1f%%", percent);
        return String.format("%.2f%%", Math.max(percent, 0.01f));
    }
}
