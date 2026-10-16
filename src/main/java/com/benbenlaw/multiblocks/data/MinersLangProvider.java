package com.benbenlaw.multiblocks.data;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.MultiblocksBlocks;
import com.benbenlaw.multiblocks.block.custom.CapBlock;
import com.benbenlaw.multiblocks.item.MultiblocksItems;
import com.benbenlaw.multiblocks.structure.StructureNames;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MinersLangProvider extends LanguageProvider {

    public MinersLangProvider(PackOutput output) {
        super(output, Multiblocks.MOD_ID, "en_us");
    }

    private static final String[] ROMAN = {"", "I", "II", "III", "IV", "V"};

    private static String titleCase(String id) {
        StringBuilder name = new StringBuilder();
        for (String word : id.split("_")) {
            if (!name.isEmpty()) name.append(' ');
            name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return name.toString();
    }

    private static String capName(CapBlock cap) {
        StringBuilder name = new StringBuilder();
        for (String word : cap.getCapType().getId().split("_")) {
            name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(' ');
        }
        return name + "Cap " + ROMAN[cap.getLevel()];
    }

    @Override
    protected void addTranslations() {

        //Items
        addItem(MultiblocksItems.PATTERN_MAKER, "Pattern Maker");

        //Pattern Maker
        add("gui.bblmultiblocks.pattern.title", "Create Structure");
        add("gui.bblmultiblocks.pattern.name", "Structure name");
        add("gui.bblmultiblocks.pattern.size", "Selection: %s x %s x %s");
        add("gui.bblmultiblocks.pattern.too_large", "Too large (max %s per side)");
        add("gui.bblmultiblocks.pattern.frame_tags_on", "Frame blocks use tier tags: ON");
        add("gui.bblmultiblocks.pattern.frame_tags_off", "Frame blocks use tier tags: OFF");
        add("gui.bblmultiblocks.pattern.states_on", "Save block states (facing, shape): ON");
        add("gui.bblmultiblocks.pattern.states_off", "Save block states (facing, shape): OFF");
        add("gui.bblmultiblocks.pattern.create", "Create");
        add("gui.bblmultiblocks.pattern.cancel", "Cancel");
        add("gui.bblmultiblocks.pattern.hint", "Cap blocks in the box become cap slots");
        add("message.bblmultiblocks.pattern.creative_only", "The Pattern Maker only works in creative mode");
        add("message.bblmultiblocks.pattern.first_corner", "First corner set at %s. Right-click the opposite corner (a block or the air).");
        add("message.bblmultiblocks.pattern.cancelled", "Selection cancelled");
        add("message.bblmultiblocks.pattern.created", "Saved structure %s");
        add("message.bblmultiblocks.pattern.saved_to", "Saved to %s. Copy it into a datapack's bblmultiblocks/structures folder to ship it.");
        add("message.bblmultiblocks.pattern.error.no_controller", "The selection needs a Crafting Controller");
        add("message.bblmultiblocks.pattern.error.multiple_controllers", "The selection has more than one controller");
        add("message.bblmultiblocks.pattern.error.empty", "There are no blocks to record");
        add("message.bblmultiblocks.pattern.error.too_large", "The selection is too large (max %s blocks per side)");
        add("message.bblmultiblocks.pattern.error.too_many_block_types", "Too many different blocks in the selection (max 62)");
        add("message.bblmultiblocks.pattern.error.invalid_name", "Enter a name using letters or numbers");
        add("message.bblmultiblocks.pattern.error.not_loaded", "Part of the selection isn't loaded");
        add("message.bblmultiblocks.pattern.error.write_failed", "Couldn't write the file, see the log");
        add("message.bblmultiblocks.pattern.error.invalid", "The recorded structure isn't valid");

        //Creative tab
        add("itemGroup." + Multiblocks.MOD_ID, "BBL Multiblocks");

        //Blocks
        addBlock(MultiblocksBlocks.CRAFTING_CONTROLLER, "Crafting Controller");

        //Structure names
        MinersStructureProvider.definitions().keySet().forEach(id -> add(StructureNames.translationKey(id), StructureNames.prettyName(id)));

        //Caps
        MultiblocksBlocks.CAPS.forEach(cap -> addBlock(cap, capName(cap.get())));

        //Support Frames
        SupportFrames.ALL.forEach(frame -> addBlock(frame, titleCase(frame.getId().getPath())));

        //GUI
        add("gui.bblmultiblocks.structure_valid", "Structure built");
        add("gui.bblmultiblocks.structure_invalid", "Structure not built");
        add("gui.bblmultiblocks.extra_items", "Extra Items");
        add("gui.bblmultiblocks.lock_locked", "Slots locked");
        add("gui.bblmultiblocks.lock_locked_hint", "Automation only puts each slot's own item back into it. Click to unlock.");
        add("gui.bblmultiblocks.lock_unlocked", "Slots unlocked");
        add("gui.bblmultiblocks.lock_unlocked_hint", "Click to lock each slot to its item. Empty slots then stay closed to automation.");
        add("gui.bblmultiblocks.cap_effects", "Cap Effects");
        add("gui.bblmultiblocks.cap_none", "No caps installed");
        add("gui.bblmultiblocks.cap_efficiency", "Efficiency: %s levels (x%s speed)");
        add("gui.bblmultiblocks.cap_fortune", "Fortune: %s");
        add("gui.bblmultiblocks.cap_silk_touch", "Silk Touch");
        add("gui.bblmultiblocks.cap_hint", "Shift + right-click to toggle cap outlines");
        add("message.bblmultiblocks.build_structure_first", "Build the structure to see its cap positions");

        //JEI
        add("jei.bblmultiblocks.crafting_controller", "Crafting Controller");
        add("jei.bblmultiblocks.structure_blocks", "Required Blocks");
        add("jei.bblmultiblocks.structure_block_line", "%sx %s");
        add("jei.bblmultiblocks.structure_tag", "Any block in %s");
        add("jei.bblmultiblocks.structure_caps", "%s cap slots (any cap block)");
        add("jei.bblmultiblocks.crafting_summary", "%ss, %s FE/t");
        add("jei.bblmultiblocks.required_items", "Required Items");
        add("jei.bblmultiblocks.optional_items", "Optional Items");
        add("jei.bblmultiblocks.extra_outputs", "Possible Extra Outputs");
        add("jei.bblmultiblocks.optional_item", "Optional item");
        add("jei.bblmultiblocks.required_item", "Required item");
        add("jei.bblmultiblocks.consumed", "Consumed when crafting");
        add("jei.bblmultiblocks.not_consumed", "Not consumed");
        add("jei.bblmultiblocks.consume_chance", "%s chance to be consumed");
        add("jei.bblmultiblocks.effect.duration", "Craft time x%s");
        add("jei.bblmultiblocks.effect.energy", "Energy use x%s");
        add("jei.bblmultiblocks.effect.output_add", "%s extra output");
        add("jei.bblmultiblocks.effect.output_multiplier", "Output x%s");
        add("jei.bblmultiblocks.effect.output_both", "Output x%s, then %s extra");
        add("jei.bblmultiblocks.effect.bonus_output", "Bonus: %s (%s chance)");
        add("jei.bblmultiblocks.effect.replace_result", "Makes %s instead");
        add("jei.bblmultiblocks.summary", "%s rolls, %ss, %s FE/t");
        add("jei.bblmultiblocks.count", "Count: %s");
        add("jei.bblmultiblocks.chance", "Chance per roll: %s");
        add("jei.bblmultiblocks.note.enchant_level_range", "Level %s-%s");
        add("jei.bblmultiblocks.note.enchant_extra", "May have extra enchantments");
        add("jei.bblmultiblocks.note.modifier", "Added by %s");
        add("jei.bblmultiblocks.note.bonus_rolls", "Extra rolls with Luck");
        add("jei.bblmultiblocks.note.luck", "Weight affected by Luck");
        add("jei.bblmultiblocks.note.tag", "One random item from %s");
        add("jei.bblmultiblocks.note.fallback", "Only if the earlier options don't apply");
        add("jei.bblmultiblocks.note.if", "%s (if %s)");
        add("jei.bblmultiblocks.fn.enchant_random", "Random enchantment");
        add("jei.bblmultiblocks.fn.enchant_random_from", "Random enchantment from %s");
        add("jei.bblmultiblocks.fn.enchant_levels", "Enchanted at level %s");
        add("jei.bblmultiblocks.fn.enchant_set", "Enchantments: %s");
        add("jei.bblmultiblocks.fn.count", "Count: %s");
        add("jei.bblmultiblocks.fn.per_level", "+%s per %s level");
        add("jei.bblmultiblocks.fn.bonus", "Increased by %s");
        add("jei.bblmultiblocks.cond.killed_by_player", "Needs to be killed by a player");
        add("jei.bblmultiblocks.cond.random_chance", "%s%% chance");
        add("jei.bblmultiblocks.cond.random_chance_bonus", "%s%% chance (%s%% with %s, +%s%% per extra level)");
        add("jei.bblmultiblocks.cond.silk_touch", "Mined with Silk Touch");
        add("jei.bblmultiblocks.cond.shears", "Mined with Shears");
        add("jei.bblmultiblocks.cond.not", "Not: %s");
        add("jei.bblmultiblocks.cond.any_of", "Any of: %s");
        add("jei.bblmultiblocks.cond.all_of", "All of: %s");
        add("jei.bblmultiblocks.cond.table_bonus", "%s chances by level: %s%%");
        add("jei.bblmultiblocks.cond.block_state", "Block %s (%s)");
        add("jei.bblmultiblocks.cond.raining", "While raining");
        add("jei.bblmultiblocks.cond.not_raining", "While not raining");
        add("jei.bblmultiblocks.cond.thundering", "While thundering");
        add("jei.bblmultiblocks.cond.not_thundering", "While not thundering");
        add("jei.bblmultiblocks.cond.reference", "Predicate %s");

    }
}
