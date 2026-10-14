package com.benbenlaw.multiblocks.structure;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StructureNamesTest {

    @Test
    void theTranslationKeyIsBuiltFromTheNamespaceAndPath() {
        assertEquals("structure.bblmultiblocks.cobblestone_miner", StructureNames.translationKey(Identifier.parse("bblmultiblocks:cobblestone_miner")));
    }

    @Test
    void slashesInThePathBecomeDots() {
        assertEquals("structure.mypack.miners.deep_miner", StructureNames.translationKey(Identifier.parse("mypack:miners/deep_miner")));
    }

    @Test
    void theFallbackNameIsTidiedUp() {
        assertEquals("Cobblestone Miner", StructureNames.prettyName(Identifier.parse("bblmultiblocks:cobblestone_miner")));
        assertEquals("Miners Deep Miner", StructureNames.prettyName(Identifier.parse("mypack:miners/deep_miner")));
    }
}
