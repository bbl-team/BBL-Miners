package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import com.benbenlaw.miners.block.custom.CapBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MinersLangProvider extends LanguageProvider {

    public MinersLangProvider(PackOutput output) {
        super(output, Miners.MOD_ID, "en_us");
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

        //Blocks
        addBlock(MinersBlocks.MINER_CONTROLLER, "Miner Controller");

        //Caps
        MinersBlocks.CAPS.forEach(cap -> addBlock(cap, capName(cap.get())));

        //Support Frames
        SupportFrames.ALL.forEach(frame -> addBlock(frame, titleCase(frame.getId().getPath())));

        //GUI
        add("gui.bblminers.structure_valid", "Structure built");
        add("gui.bblminers.structure_invalid", "Structure not built");
        add("gui.bblminers.cap_effects", "Cap Effects");
        add("gui.bblminers.cap_none", "No caps installed");
        add("gui.bblminers.cap_efficiency", "Efficiency: %s levels (x%s speed)");
        add("gui.bblminers.cap_fortune", "Fortune: %s");
        add("gui.bblminers.cap_silk_touch", "Silk Touch");
        add("gui.bblminers.cap_hint", "Shift + right-click to toggle cap outlines");
        add("message.bblminers.build_structure_first", "Build the structure to see its cap positions");

        //JEI
        add("jei.bblminers.miner_structure", "Miner Structure");
        add("jei.bblminers.summary", "%s rolls, %ss, %s FE/t");
        add("jei.bblminers.count", "Count: %s");
        add("jei.bblminers.chance", "Chance per roll: %s");
        add("jei.bblminers.note.enchant_level_range", "Level %s-%s");
        add("jei.bblminers.note.enchant_extra", "May have extra enchantments");
        add("jei.bblminers.note.modifier", "Added by %s");
        add("jei.bblminers.note.bonus_rolls", "Extra rolls with Luck");
        add("jei.bblminers.note.luck", "Weight affected by Luck");
        add("jei.bblminers.note.tag", "One random item from %s");
        add("jei.bblminers.note.fallback", "Only if the earlier options don't apply");
        add("jei.bblminers.note.if", "%s (if %s)");
        add("jei.bblminers.fn.enchant_random", "Random enchantment");
        add("jei.bblminers.fn.enchant_random_from", "Random enchantment from %s");
        add("jei.bblminers.fn.enchant_levels", "Enchanted at level %s");
        add("jei.bblminers.fn.enchant_set", "Enchantments: %s");
        add("jei.bblminers.fn.count", "Count: %s");
        add("jei.bblminers.fn.per_level", "+%s per %s level");
        add("jei.bblminers.fn.bonus", "Increased by %s");
        add("jei.bblminers.cond.killed_by_player", "Needs to be killed by a player");
        add("jei.bblminers.cond.random_chance", "%s%% chance");
        add("jei.bblminers.cond.random_chance_bonus", "%s%% chance (%s%% with %s, +%s%% per extra level)");
        add("jei.bblminers.cond.silk_touch", "Mined with Silk Touch");
        add("jei.bblminers.cond.shears", "Mined with Shears");
        add("jei.bblminers.cond.not", "Not: %s");
        add("jei.bblminers.cond.any_of", "Any of: %s");
        add("jei.bblminers.cond.all_of", "All of: %s");
        add("jei.bblminers.cond.table_bonus", "%s chances by level: %s%%");
        add("jei.bblminers.cond.block_state", "Block %s (%s)");
        add("jei.bblminers.cond.raining", "While raining");
        add("jei.bblminers.cond.not_raining", "While not raining");
        add("jei.bblminers.cond.thundering", "While thundering");
        add("jei.bblminers.cond.not_thundering", "While not thundering");
        add("jei.bblminers.cond.reference", "Predicate %s");

    }
}
