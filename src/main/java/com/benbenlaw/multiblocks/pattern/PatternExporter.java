package com.benbenlaw.multiblocks.pattern;

import com.benbenlaw.multiblocks.structure.BlockMatcher;
import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.block.custom.CapBlock;
import com.benbenlaw.multiblocks.block.custom.CraftingControllerBlock;
import com.benbenlaw.multiblocks.block.custom.MinerControllerBlock;
import com.benbenlaw.multiblocks.structure.StructureManager;
import com.benbenlaw.multiblocks.util.MinersTags;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Scans a box of blocks in the world, turns it into a structure and saves it as a structure json. */
public final class PatternExporter {

    /** Relative to the game directory. Structure files in here are loaded alongside the datapack ones. */
    public static final String EXPORT_DIRECTORY = "bblmultiblocks/exported_structures";

    public static final int MAX_SIDE = 32;
    public static final int MAX_VOLUME = 16384;
    public static final int MAX_NAME_LENGTH = 48;

    /** Highest tier first, so a block gets the highest tier tag it is part of. */
    private static final List<TagKey<Block>> FRAME_TIERS = List.of(
            MinersTags.Blocks.TIER_7_FRAME, MinersTags.Blocks.TIER_6_FRAME, MinersTags.Blocks.TIER_5_FRAME,
            MinersTags.Blocks.TIER_4_FRAME, MinersTags.Blocks.TIER_3_FRAME, MinersTags.Blocks.TIER_2_FRAME,
            MinersTags.Blocks.TIER_1_FRAME);

    public enum Result {
        OK, TOO_LARGE, NOT_LOADED, INVALID_NAME, NO_CONTROLLER, MULTIPLE_CONTROLLERS, EMPTY, TOO_MANY_BLOCK_TYPES, INVALID, WRITE_FAILED
    }

    public record Outcome(Result result, @Nullable Identifier id, @Nullable Path file) {
        static Outcome failed(Result result) {
            return new Outcome(result, null, null);
        }
    }

    private PatternExporter() {
    }

    public static Path directory() {
        return FMLPaths.GAMEDIR.get().resolve(EXPORT_DIRECTORY);
    }

    /** "My Cool Miner!" becomes "my_cool_miner". Returns an empty string when nothing usable is left. */
    public static String sanitize(String raw) {
        String name = raw.trim().toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[\\s\\-]+", "_")
                .replaceAll("[^a-z0-9_]", "")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
        return name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH).replaceAll("_$", "") : name;
    }

    public static boolean tooLarge(BlockPos a, BlockPos b) {
        int sizeX = Math.abs(a.getX() - b.getX()) + 1;
        int sizeY = Math.abs(a.getY() - b.getY()) + 1;
        int sizeZ = Math.abs(a.getZ() - b.getZ()) + 1;
        return sizeX > MAX_SIDE || sizeY > MAX_SIDE || sizeZ > MAX_SIDE || (long) sizeX * sizeY * sizeZ > MAX_VOLUME;
    }

    public static Outcome export(ServerLevel level, BlockPos a, BlockPos b, String rawName, boolean frameTags, boolean saveStates) {
        String name = sanitize(rawName);
        if (name.isEmpty()) return Outcome.failed(Result.INVALID_NAME);
        if (tooLarge(a, b)) return Outcome.failed(Result.TOO_LARGE);

        BlockPos min = new BlockPos(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
        BlockPos max = new BlockPos(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
        if (!level.hasChunksAt(min, max)) return Outcome.failed(Result.NOT_LOADED);

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        PatternRecorder.Outcome recorded = PatternRecorder.record(
                max.getX() - min.getX() + 1, max.getY() - min.getY() + 1, max.getZ() - min.getZ() + 1,
                (x, y, z) -> cell(level.getBlockState(cursor.set(min.getX() + x, min.getY() + y, min.getZ() + z)), frameTags, saveStates));

        if (!recorded.ok()) {
            return Outcome.failed(switch (recorded.error()) {
                case NO_CONTROLLER -> Result.NO_CONTROLLER;
                case MULTIPLE_CONTROLLERS -> Result.MULTIPLE_CONTROLLERS;
                case EMPTY -> Result.EMPTY;
                case TOO_MANY_BLOCK_TYPES -> Result.TOO_MANY_BLOCK_TYPES;
                case INVALID -> Result.INVALID;
            });
        }

        JsonElement json = com.benbenlaw.multiblocks.structure.StructurePattern.CODEC
                .encodeStart(JsonOps.INSTANCE, recorded.pattern()).result().orElse(null);
        if (json == null) return Outcome.failed(Result.INVALID);

        Path file = directory().resolve(name + ".json");
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(json), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Multiblocks.LOGGER.error("Could not save structure {}", file, e);
            return Outcome.failed(Result.WRITE_FAILED);
        }

        Identifier id = Multiblocks.identifier(name);
        StructureManager.INSTANCE.register(id, recorded.pattern());
        return new Outcome(Result.OK, id, file);
    }

    private static PatternRecorder.Cell cell(BlockState state, boolean frameTags, boolean saveStates) {
        Block block = state.getBlock();

        if (state.isAir() || block instanceof LiquidBlock) return PatternRecorder.Cell.AIR;
        if (block instanceof MinerControllerBlock || block instanceof CraftingControllerBlock) return PatternRecorder.Cell.CONTROLLER;
        if (block instanceof CapBlock) return PatternRecorder.Cell.CAP;

        if (frameTags) {
            for (TagKey<Block> tier : FRAME_TIERS) {
                if (state.is(tier)) {
                    return PatternRecorder.Cell.block(BlockMatcher.tag(tier.location()));
                }
            }
        }

        return PatternRecorder.Cell.block(BlockMatcher.of(state, saveStates));
    }
}
