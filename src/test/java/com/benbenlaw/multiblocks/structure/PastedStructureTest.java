package com.benbenlaw.multiblocks.structure;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Vec3i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** A structure written by hand that mixes tags, block states and a cap, to check the whole thing loads. */
class PastedStructureTest {

    private static final String STRUCTURE = """
            {
              "pattern": [
                ["     ", "  A  ", " A A ", "  A  ", "     "],
                ["BCDEB", "F   F", "D   D", "G   G", "BCDEB"],
                ["HI JH", "K   K", "     ", "L   L", "HI JH"],
                ["M   M", "     ", "  @  ", "     ", "M   M"],
                ["MFFFM", "CNNNE", "CN NE", "CNNNE", "MGGGM"]
              ],
              "keys": {
                "A": "#minecraft:wooden_slabs[type=bottom]",
                "B": "#techopolis:eye_plant",
                "C": "#minecraft:wooden_stairs[facing=east,half=bottom,shape=straight]",
                "D": "#minecraft:wooden_slabs[type=top]",
                "E": "#minecraft:wooden_stairs[facing=west,half=bottom,shape=straight]",
                "F": "#minecraft:wooden_stairs[facing=south,half=bottom,shape=straight]",
                "G": "#minecraft:wooden_stairs[facing=north,half=bottom,shape=straight]",
                "H": "minecraft:pale_moss_block",
                "I": "#minecraft:wooden_stairs[facing=west,half=top,shape=straight]",
                "J": "#minecraft:wooden_stairs[facing=east,half=top,shape=straight]",
                "K": "#minecraft:wooden_stairs[facing=north,half=top,shape=straight]",
                "L": "#minecraft:wooden_stairs[facing=south,half=top,shape=straight]",
                "M": "#bblmultiblocks:tier_1_frame",
                "N": "strainers:mulch"
              },
              "caps": [[0, 3, 0]]
            }
            """;

    @Test
    void aStructureWithTagsAndStatesLoads() {
        StructurePattern pattern = StructurePattern.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(STRUCTURE)).getOrThrow();

        assertEquals(14, pattern.keys().size());

        BlockMatcher slab = pattern.keys().get('A');
        assertTrue(slab.tag());
        assertEquals("bottom", slab.properties().get("type"));

        BlockMatcher stairs = pattern.keys().get('C');
        assertTrue(stairs.tag());
        assertEquals("east", stairs.properties().get("facing"));

        assertEquals(java.util.List.of(new Vec3i(0, 3, 0)), pattern.caps());
        assertFalse(pattern.compile().entries().isEmpty());
    }
}
