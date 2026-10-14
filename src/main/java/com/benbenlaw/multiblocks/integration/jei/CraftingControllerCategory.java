package com.benbenlaw.multiblocks.integration.jei;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.client.StructurePreviewBuilder;
import com.benbenlaw.multiblocks.client.StructurePreviewRenderState;
import com.benbenlaw.multiblocks.client.StructurePreviewRenderer;
import com.benbenlaw.multiblocks.network.CraftingPreviewPacket;
import com.benbenlaw.multiblocks.recipe.ControllerCraftingRecipe;
import com.benbenlaw.multiblocks.recipe.OptionalEffect;
import com.benbenlaw.multiblocks.recipe.OptionalItem;
import com.benbenlaw.multiblocks.recipe.SizedShapedPattern;
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

    public static final IRecipeType<Entry> TYPE = IRecipeType.create(Multiblocks.MOD_ID, "crafting_controller", Entry.class);

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

    private final IDrawable icon;
    private final Map<Identifier, StructurePreviewRenderer> renderers = new HashMap<>();

    public CraftingControllerCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemLike(MultiblocksBlocks.CRAFTING_CONTROLLER.get());
    }

    @Override
    public IRecipeType<Entry> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.bblmultiblocks.crafting_controller");
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

        // required research items
        List<SizedIngredient> extras = recipe.extras();
        for (int i = 0; i < extras.size(); i++) {
            IRecipeSlotBuilder slot = builder.addInputSlot(i * 18, REQUIRED_Y).setStandardSlotBackground();
            addStacks(slot, extras.get(i).ingredient(), extras.get(i).count());
        }

        // optional items are inputs whether or not they get used up; the tooltip says which
        List<OptionalItem> optionalItems = recipe.optionalItems();
        for (int i = 0; i < optionalItems.size(); i++) {
            OptionalItem optional = optionalItems.get(i);
            IRecipeSlotBuilder slot = builder.addInputSlot(i * 18, OPTIONAL_Y).setStandardSlotBackground();
            addStacks(slot, optional.ingredient(), optional.count());
            slot.addRichTooltipCallback((view, tooltip) -> describe(optional).forEach(tooltip::add));
        }

        // items an optional item can add to the output, so they can be looked up too
        int outputIndex = 0;
        for (OptionalItem optional : optionalItems) {
            for (OptionalEffect effect : optional.effects()) {
                ItemStack extraOutput = switch (effect) {
                    case OptionalEffect.BonusOutput bonus -> bonus.item().create();
                    case OptionalEffect.ReplaceResult replace -> replace.item().create();
                    default -> null;
                };
                if (extraOutput == null) continue;

                IRecipeSlotBuilder slot = builder.addOutputSlot(outputIndex++ * 18, OUTPUTS_Y).setStandardSlotBackground();
                slot.add(extraOutput);
                slot.addRichTooltipCallback((view, tooltip) -> tooltip.add(describeEffect(effect).withStyle(ChatFormatting.DARK_AQUA)));
            }
        }
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

        if (!recipe.extras().isEmpty()) {
            guiGraphics.text(font, Component.translatable("jei.bblmultiblocks.required_items"), 0, REQUIRED_LABEL_Y, 0xFF404040, false);
        }
        if (!recipe.optionalItems().isEmpty()) {
            guiGraphics.text(font, Component.translatable("jei.bblmultiblocks.optional_items"), 0, OPTIONAL_LABEL_Y, 0xFF404040, false);
        }
        if (hasExtraOutputs(recipe)) {
            guiGraphics.text(font, Component.translatable("jei.bblmultiblocks.extra_outputs"), 0, OUTPUTS_LABEL_Y, 0xFF404040, false);
        }

        Component summary = Component.translatable("jei.bblmultiblocks.crafting_summary", recipe.baseDuration() / 20.0, recipe.energyPerTick());
        guiGraphics.text(font, summary, (WIDTH - font.width(summary)) / 2, FOOTER_Y, 0xFF404040, false);

        if (preview.pattern().isEmpty()) return;

        guiGraphics.fill(VIEW_X, 0, VIEW_X + VIEW_WIDTH, VIEW_HEIGHT, 0x33000000);

        Vector2f origin = guiGraphics.pose().transformPosition(0f, 0f, new Vector2f());
        int x0 = Math.round(origin.x) + VIEW_X;
        int y0 = Math.round(origin.y);

        long now = System.currentTimeMillis();
        StructurePreviewBuilder.Result result = StructurePreviewBuilder.build(preview.pattern().get(), now);

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

        tooltip.addAll(StructureTooltip.lines(entry.preview().pattern().get(), MultiblocksBlocks.CRAFTING_CONTROLLER.get().getName()));
    }

    private static boolean hasExtraOutputs(ControllerCraftingRecipe recipe) {
        for (OptionalItem optional : recipe.optionalItems()) {
            for (OptionalEffect effect : optional.effects()) {
                if (effect instanceof OptionalEffect.BonusOutput || effect instanceof OptionalEffect.ReplaceResult) return true;
            }
        }
        return false;
    }

    public void closeRenderers() {
        renderers.values().forEach(StructurePreviewRenderer::close);
        renderers.clear();
    }

    private static List<Component> describe(OptionalItem optional) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("jei.bblmultiblocks.optional_item").withStyle(ChatFormatting.GRAY));

        if (!optional.consume()) {
            lines.add(Component.translatable("jei.bblmultiblocks.not_consumed").withStyle(ChatFormatting.GREEN));
        } else if (optional.consumeChance() < 1.0) {
            lines.add(Component.translatable("jei.bblmultiblocks.consume_chance", percent(optional.consumeChance())).withStyle(ChatFormatting.YELLOW));
        } else {
            lines.add(Component.translatable("jei.bblmultiblocks.consumed").withStyle(ChatFormatting.YELLOW));
        }

        for (OptionalEffect effect : optional.effects()) {
            lines.add(describeEffect(effect).withStyle(ChatFormatting.DARK_AQUA));
        }
        return lines;
    }

    private static net.minecraft.network.chat.MutableComponent describeEffect(OptionalEffect effect) {
        return switch (effect) {
            case OptionalEffect.Duration duration -> Component.translatable("jei.bblmultiblocks.effect.duration", number(duration.multiplier()));
            case OptionalEffect.Energy energy -> Component.translatable("jei.bblmultiblocks.effect.energy", number(energy.multiplier()));
            case OptionalEffect.OutputCount count -> {
                if (count.add() != 0 && count.multiplier() != 1.0) {
                    yield Component.translatable("jei.bblmultiblocks.effect.output_both", number(count.multiplier()), count.add());
                }
                yield count.add() != 0
                        ? Component.translatable("jei.bblmultiblocks.effect.output_add", count.add())
                        : Component.translatable("jei.bblmultiblocks.effect.output_multiplier", number(count.multiplier()));
            }
            case OptionalEffect.BonusOutput bonus -> Component.translatable("jei.bblmultiblocks.effect.bonus_output",
                    bonus.item().create().getHoverName(), percent(bonus.chance()));
            case OptionalEffect.ReplaceResult replace -> Component.translatable("jei.bblmultiblocks.effect.replace_result",
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
