package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.MinersBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MinersLangProvider extends LanguageProvider {

    public MinersLangProvider(PackOutput output) {
        super(output, Miners.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Blocks
        addBlock(MinersBlocks.MINER_CONTROLLER, "Miner Controller");

        //GUI
        add("gui.bblminers.structure_valid", "Structure built");
        add("gui.bblminers.structure_invalid", "Structure not built");

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
