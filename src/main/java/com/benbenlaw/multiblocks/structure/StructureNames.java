package com.benbenlaw.multiblocks.structure;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Display names for structures. A structure called {@code ns:path/name} is named by the lang key
 * {@code structure.ns.path.name}, and falls back to a tidied up version of its id, so a pack can name its structures
 * with a lang file but doesn't have to.
 */
public final class StructureNames {

    private StructureNames() {
    }

    public static String translationKey(Identifier id) {
        return "structure." + id.getNamespace() + "." + id.getPath().replace('/', '.');
    }

    public static Component of(Identifier id) {
        return Component.translatableWithFallback(translationKey(id), prettyName(id));
    }

    /** "cobblestone_miner" becomes "Cobblestone Miner". */
    public static String prettyName(Identifier id) {
        StringBuilder out = new StringBuilder();
        for (String word : id.getPath().split("[_/]")) {
            if (word.isEmpty()) continue;
            if (!out.isEmpty()) out.append(' ');
            out.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return out.toString();
    }
}
