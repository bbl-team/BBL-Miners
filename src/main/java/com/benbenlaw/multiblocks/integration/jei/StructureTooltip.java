package com.benbenlaw.multiblocks.integration.jei;

import com.benbenlaw.multiblocks.structure.BlockMatcher;
import com.benbenlaw.multiblocks.structure.CompiledStructure;
import com.benbenlaw.multiblocks.structure.StructureMatching;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** The "Required Blocks" tooltip shown over a structure in the JEI categories. */
final class StructureTooltip {

    private StructureTooltip() {
    }

    static List<Component> lines(StructurePattern pattern, Component controllerName) {
        CompiledStructure compiled = StructureMatching.compile(pattern);

        Map<BlockMatcher, Integer> counts = new LinkedHashMap<>();
        for (CompiledStructure.Entry entry : compiled.entries()) {
            counts.merge(entry.block().withoutProperties(), 1, Integer::sum);
        }

        List<Map.Entry<BlockMatcher, Integer>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("jei.bblmultiblocks.structure_blocks").withStyle(ChatFormatting.WHITE));

        lines.add(Component.translatable("jei.bblmultiblocks.structure_block_line", 1, controllerName).withStyle(ChatFormatting.GRAY));

        for (Map.Entry<BlockMatcher, Integer> entry : sorted) {
            BlockMatcher block = entry.getKey();

            Component name = block.tag()
                    ? Component.translatable("jei.bblmultiblocks.structure_tag", "#" + block.id()).withStyle(ChatFormatting.AQUA)
                    : BuiltInRegistries.BLOCK.getValue(block.id()).getName().copy().withStyle(ChatFormatting.GRAY);

            lines.add(Component.translatable("jei.bblmultiblocks.structure_block_line", entry.getValue(), name).withStyle(ChatFormatting.GRAY));
        }

        if (!compiled.caps().isEmpty()) {
            lines.add(Component.translatable("jei.bblmultiblocks.structure_caps", compiled.caps().size()).withStyle(ChatFormatting.GOLD));
        }
        return lines;
    }
}
