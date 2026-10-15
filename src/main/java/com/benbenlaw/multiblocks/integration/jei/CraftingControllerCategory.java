package com.benbenlaw.multiblocks.integration.jei;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock;
import com.benbenlaw.multiblocks.client.StructurePreviewBuilder;
import com.benbenlaw.multiblocks.client.StructurePreviewRenderState;
import com.benbenlaw.multiblocks.client.StructurePreviewRenderer;
import com.benbenlaw.multiblocks.network.CraftingPreviewPacket;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import com.benbenlaw.multiblocks.recipe.ItemEffect;
import com.benbenlaw.multiblocks.recipe.ExtraItem;
import com.benbenlaw.multiblocks.recipe.SizedShapedPattern;
import com.benbenlaw.multiblocks.structure.StructureNames;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingControllerCategory implements IRecipeCategory<CraftingControllerCategory.Entry> {

    private static final Map<Identifier, IRecipeType<Entry>> TYPES = new HashMap<>();

    /** The built-in Crafting Controller keeps bblmultiblocks:crafting_controller, any other controller uses its own block id. */
    public static IRecipeType<Entry> typeFor(Identifier controller) {
        return TYPES.computeIfAbsent(controller, id -> IRecipeType.create(id, Entry.class));
    }

    private static final int WIDTH = 176;
    private static final int HEIGHT = 152;

    private static final int RESULT_X = 86;
    private static final int RESULT_Y = 18;
    private static final int ARROW_X = 60;
    private static final int ARROW_Y = 19;

    private static final int VIEW_X = 112;
    private static final int VIEW_WIDTH = 64;
    private static final int VIEW_HEIGHT = 54;

    private static final int REQUIRED_LABEL_Y = 58;
    private static final int REQUIRED_Y = 68;
    private static final int OPTIONAL_LABEL_Y = 88;
    private static final int OPTIONAL_Y = 98;
    private static final int OUTPUTS_LABEL_Y = 118;
    private static final int OUTPUTS_Y = 128;
    private static final int FOOTER_Y = 144;

    public record Entry(CraftingPreviewPacket.Preview preview) {
    }

    private final Identifier controller;
    private final CraftingControllerBlock block;
    private final IDrawable icon;
    private final Map<Identifier, StructurePreviewRenderer> renderers = new HashMap<>();

    public CraftingControllerCategory(IGuiHelper guiHelper, Identifier controller, CraftingControllerBlock block) {
        this.controller = controller;
        this.block = block;
        this.icon = guiHelper.createDrawableItemLike(block.jeiIcon() != null ? block.jeiIcon() : block);
    }

    public Identifier controller() {
        return controller;
    }

    public CraftingControllerBlock block() {
        return block;
    }

    @Override
    public IRecipeType<Entry> getRecipeType() {
        return typeFor(controller);
    }

    @Override
    public Component getTitle() {
        if (block.jeiTitle() != null) return block.jeiTitle();
        return controller.equals(ControllerCraftingRecipe.DEFAULT_CONTROLLER)
                ? Component.translatable("jei.bblmultiblocks.crafting_controller")
                : block.getName();
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
        ControllerCraftingRecipe recipe = entry.preview().recipe();
        SizedShapedPattern pattern = recipe.pattern();

        // the 3x3 grid, empty cells included so the grid always looks like a grid
        for (int row = 0; row < SizedShapedPattern.GRID_HEIGHT; row++) {
            for (int column = 0; column < SizedShapedPattern.GRID_WIDTH; column++) {
                IRecipeSlotBuilder slot = builder.addInputSlot(column * 18, row * 18).setStandardSlotBackground();

                if (row < pattern.rows().size() && column < pattern.rows().get(row).length()) {
                    char key = pattern.rows().get(row).charAt(column);
                    SizedIngredient sized = key == SizedShapedPattern.EMPTY ? null : pattern.key().get(key);
                    if (sized != null) addStacks(slot, sized.ingredient(), sized.count());
                }
            }
        }

        // the result
        builder.addOutputSlot(RESULT_X, RESULT_Y).setStandardSlotBackground().add(recipe.result().create());

        // extra items are inputs whether or not they get used up; the tooltip says which
        addExtraRow(builder, recipe, true, REQUIRED_Y);
        addExtraRow(builder, recipe, false, OPTIONAL_Y);

        // items an extra item can add to the output, so they can be looked up too
        int outputIndex = 0;
        for (ExtraItem extra : recipe.extraItems()) {
            for (ItemEffect effect : extra.effects()) {
                ItemStack extraOutput = switch (effect) {
                    case ItemEffect.BonusOutput bonus -> bonus.item().create();
                    case ItemEffect.ReplaceResult replace -> replace.item().create();
                    default -> null;
                };
                if (extraOutput == null) continue;

                IRecipeSlotBuilder slot = builder.addOutputSlot(outputIndex++ * 18, OUTPUTS_Y).setStandardSlotBackground();
                slot.add(extraOutput);
                slot.addRichTooltipCallback((view, tooltip) -> tooltip.add(describeEffect(effect).withStyle(ChatFormatting.DARK_AQUA)));
            }
        }
    }

    private static void addExtraRow(IRecipeLayoutBuilder builder, ControllerCraftingRecipe recipe, boolean required, int y) {
        int column = 0;
        for (ExtraItem extra : recipe.extraItems()) {
            if (extra.required() != required) continue;

            IRecipeSlotBuilder slot = builder.addInputSlot(column++ * 18, y).setStandardSlotBackground();
            addStacks(slot, extra.ingredient(), extra.count());
            slot.addRichTooltipCallback((view, tooltip) -> describe(extra).forEach(tooltip::add));
        }
    }

    private static boolean hasExtras(ControllerCraftingRecipe recipe, boolean required) {
        for (ExtraItem extra : recipe.extraItems()) {
            if (extra.required() == required) return true;
        }
        return false;
    }

    private static void addStacks(IRecipeSlotBuilder slot, Ingredient ingredient, int count) {
        ingredient.items().forEach((Holder<Item> item) -> slot.add(new ItemStack(item, count)));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Entry entry, IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(Math.max(20, entry.preview().recipe().baseDuration()))
                .setPosition(ARROW_X, ARROW_Y);
    }

    @Override
    public void draw(Entry entry, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        CraftingPreviewPacket.Preview preview = entry.preview();
        ControllerCraftingRecipe recipe = preview.recipe();
        Minecraft mc = Minecraft.getInstance();
        var font = mc.font;

        if (hasExtras(recipe, true)) {
            guiGraphics.text(font, Component.translatable("jei.bblmultiblocks.required_items"), 0, REQUIRED_LABEL_Y, 0xFF404040, false);
        }
        if (hasExtras(recipe, false)) {
            guiGraphics.text(font, Component.translatable("jei.bblmultiblocks.optional_items"), 0, OPTIONAL_LABEL_Y, 0xFF404040, false);
        }
        if (hasExtraOutputs(recipe)) {
            guiGraphics.text(font, Component.translatable("jei.bblmultiblocks.extra_outputs"), 0, OUTPUTS_LABEL_Y, 0xFF404040, false);
        }

        Component summary = Component.translatable("jei.bblmultiblocks.crafting_summary", recipe.baseDuration() / 20.0, recipe.energyPerTick());
        guiGraphics.text(font, summary, (WIDTH - font.width(summary)) / 2, FOOTER_Y, 0xFF404040, false);

        if (recipe.structureId().isPresent()) {
            Component name = StructureNames.of(recipe.structureId().get());
            String text = font.plainSubstrByWidth(name.getString(), VIEW_WIDTH);
            guiGraphics.text(font, Component.literal(text), VIEW_X + (VIEW_WIDTH - font.width(text)) / 2, VIEW_HEIGHT + 3, 0xFF404040, false);
        }

        if (preview.pattern().isEmpty()) return;

        guiGraphics.fill(VIEW_X, 0, VIEW_X + VIEW_WIDTH, VIEW_HEIGHT, 0x33000000);

        Vector2f origin = guiGraphics.pose().transformPosition(0f, 0f, new Vector2f());
        int x0 = Math.round(origin.x) + VIEW_X;
        int y0 = Math.round(origin.y);

        long now = System.currentTimeMillis();
        StructurePreviewBuilder.Result result = StructurePreviewBuilder.build(preview.pattern().get(), block, now);

        StructurePreviewRenderState state = new StructurePreviewRenderState(
                result.blocks(), result.size(), (now % 36000L) / 20f,
                x0, y0, x0 + VIEW_WIDTH, y0 + VIEW_HEIGHT, 1.0f, null, 44.0f);

        StructurePreviewRenderer renderer = renderers.computeIfAbsent(preview.recipeId(),
                key -> new StructurePreviewRenderer(mc.renderBuffers().bufferSource()));
        renderer.prepare(state, guiGraphics.guiRenderState, (int) mc.getWindow().getGuiScale());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Entry entry, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (entry.preview().pattern().isEmpty()) return;
        if (mouseX < VIEW_X || mouseX > VIEW_X + VIEW_WIDTH || mouseY < 0 || mouseY > VIEW_HEIGHT) return;

        tooltip.addAll(StructureTooltip.lines(entry.preview().pattern().get(), block.getName()));
    }

    private static boolean hasExtraOutputs(ControllerCraftingRecipe recipe) {
        for (ExtraItem extra : recipe.extraItems()) {
            for (ItemEffect effect : extra.effects()) {
                if (effect instanceof ItemEffect.BonusOutput || effect instanceof ItemEffect.ReplaceResult) return true;
            }
        }
        return false;
    }

    public void closeRenderers() {
        renderers.values().forEach(StructurePreviewRenderer::close);
        renderers.clear();
    }

    private static List<Component> describe(ExtraItem optional) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(optional.required() ? "jei.bblmultiblocks.required_item" : "jei.bblmultiblocks.optional_item").withStyle(ChatFormatting.GRAY));

        if (!optional.consume()) {
            lines.add(Component.translatable("jei.bblmultiblocks.not_consumed").withStyle(ChatFormatting.GREEN));
        } else if (optional.consumeChance() < 1.0) {
            lines.add(Component.translatable("jei.bblmultiblocks.consume_chance", percent(optional.consumeChance())).withStyle(ChatFormatting.YELLOW));
        } else {
            lines.add(Component.translatable("jei.bblmultiblocks.consumed").withStyle(ChatFormatting.YELLOW));
        }

        for (ItemEffect effect : optional.effects()) {
            lines.add(describeEffect(effect).withStyle(ChatFormatting.DARK_AQUA));
        }
        return lines;
    }

    private static net.minecraft.network.chat.MutableComponent describeEffect(ItemEffect effect) {
        return switch (effect) {
            case ItemEffect.Duration duration -> Component.translatable("jei.bblmultiblocks.effect.duration", number(duration.multiplier()));
            case ItemEffect.Energy energy -> Component.translatable("jei.bblmultiblocks.effect.energy", number(energy.multiplier()));
            case ItemEffect.OutputCount count -> {
                if (count.add() != 0 && count.multiplier() != 1.0) {
                    yield Component.translatable("jei.bblmultiblocks.effect.output_both", number(count.multiplier()), count.add());
                }
                yield count.add() != 0
                        ? Component.translatable("jei.bblmultiblocks.effect.output_add", count.add())
                        : Component.translatable("jei.bblmultiblocks.effect.output_multiplier", number(count.multiplier()));
            }
            case ItemEffect.BonusOutput bonus -> Component.translatable("jei.bblmultiblocks.effect.bonus_output",
                    bonus.item().create().getHoverName(), percent(bonus.chance()));
            case ItemEffect.ReplaceResult replace -> Component.translatable("jei.bblmultiblocks.effect.replace_result",
                    replace.item().create().getHoverName());
        };
    }

    private static String number(double value) {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.format("%.2f", value);
    }

    private static String percent(double chance) {
        return String.format("%.0f%%", chance * 100);
    }
}
