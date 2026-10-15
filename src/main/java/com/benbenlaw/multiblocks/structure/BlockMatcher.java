package com.benbenlaw.multiblocks.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * What a structure key accepts. It is written as a string: {@code minecraft:stone} for a block, {@code #ns:tag} for any block
 * in a tag. Either can end with properties, like {@code minecraft:oak_stairs[facing=north,half=bottom]} or
 * {@code #minecraft:wooden_stairs[facing=north]}, which the block must also have (any property that isn't listed can be anything).
 */
public record BlockMatcher(Identifier id, boolean tag, Map<String, String> properties) {

    /** The properties the Pattern Maker records, the ones that decide how a block is placed and looks. */
    private static final Set<String> SHAPE_PROPERTIES = Set.of(
            "facing", "half", "shape", "type", "axis", "rotation", "face", "hinge", "part", "orientation", "attachment");

    public static final Codec<BlockMatcher> CODEC = Codec.STRING.comapFlatMap(BlockMatcher::parse, BlockMatcher::toString);

    public BlockMatcher {
        properties = Collections.unmodifiableMap(new TreeMap<>(properties));
    }

    public static BlockMatcher block(Identifier id) {
        return new BlockMatcher(id, false, Map.of());
    }

    public static BlockMatcher tag(Identifier id) {
        return new BlockMatcher(id, true, Map.of());
    }

    public static DataResult<BlockMatcher> parse(String text) {
        String value = text.trim();
        boolean tag = value.startsWith("#");
        String body = tag ? value.substring(1) : value;

        int open = body.indexOf('[');
        DataResult<Identifier> id = Identifier.read(open < 0 ? body : body.substring(0, open));
        if (id.error().isPresent()) return DataResult.error(() -> id.error().get().message());
        if (open < 0) return DataResult.success(new BlockMatcher(id.result().get(), tag, Map.of()));

        if (!body.endsWith("]")) return DataResult.error(() -> "Missing ']' in '" + text + "'");

        Map<String, String> properties = new TreeMap<>();
        String inside = body.substring(open + 1, body.length() - 1).trim();
        if (!inside.isEmpty()) {
            for (String part : inside.split(",")) {
                int equals = part.indexOf('=');
                String name = equals < 0 ? "" : part.substring(0, equals).trim();
                String propertyValue = equals < 0 ? "" : part.substring(equals + 1).trim();

                if (name.isEmpty() || propertyValue.isEmpty()) return DataResult.error(() -> "Bad property '" + part + "' in '" + text + "'");
                if (properties.put(name, propertyValue) != null) return DataResult.error(() -> "Property '" + name + "' is listed twice in '" + text + "'");
            }
        }
        return DataResult.success(new BlockMatcher(id.result().get(), tag, properties));
    }

    /** The block (with the properties that decide its shape and facing, when asked for) as a matcher. */
    public static BlockMatcher of(BlockState state, boolean saveProperties) {
        Map<String, String> properties = new TreeMap<>();

        if (saveProperties) {
            for (Property<?> property : state.getProperties()) {
                if (SHAPE_PROPERTIES.contains(property.getName())) properties.put(property.getName(), valueName(state, property));
            }
        }
        return new BlockMatcher(BuiltInRegistries.BLOCK.getKey(state.getBlock()), false, properties);
    }

    public BlockMatcher withoutProperties() {
        return new BlockMatcher(id, tag, Map.of());
    }

    public boolean matches(BlockState state) {
        boolean blockMatches = tag
                ? state.is(TagKey.create(Registries.BLOCK, id))
                : state.is(BuiltInRegistries.BLOCK.getValue(id));
        if (!blockMatches) return false;

        for (Map.Entry<String, String> required : properties.entrySet()) {
            Property<?> property = state.getBlock().getStateDefinition().getProperty(required.getKey());
            if (property == null || !valueName(state, property).equals(required.getValue())) return false;
        }
        return true;
    }

    /** The block with these properties set, for showing it. Unknown properties are ignored. */
    public BlockState toState() {
        return applyProperties(BuiltInRegistries.BLOCK.getValue(id).defaultBlockState());
    }

    /** Sets these properties on a block state, for showing a block picked out of a tag. Properties the block doesn't have are ignored. */
    public BlockState applyProperties(BlockState state) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Property<?> property = state.getBlock().getStateDefinition().getProperty(entry.getKey());
            if (property != null) state = withValue(state, property, entry.getValue());
        }
        return state;
    }

    @Override
    public String toString() {
        String text = (tag ? "#" : "") + id;
        if (properties.isEmpty()) return text;

        List<String> parts = new ArrayList<>();
        properties.forEach((name, value) -> parts.add(name + "=" + value));
        return text + "[" + String.join(",", parts) + "]";
    }

    private static <T extends Comparable<T>> String valueName(BlockState state, Property<T> property) {
        return property.getName(state.getValue(property));
    }

    private static <T extends Comparable<T>> BlockState withValue(BlockState state, Property<T> property, String value) {
        return property.getValue(value).map(parsed -> state.setValue(property, parsed)).orElse(state);
    }
}
