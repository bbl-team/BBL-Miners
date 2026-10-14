package com.benbenlaw.multiblocks.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStackTemplate;

/**
 * Something an optional item does to a craft while it sits in a research slot. An optional item can have any number of
 * these, and the effects of every active optional item add up.
 */
public sealed interface OptionalEffect {

    Type type();

    /** Multiplies how long the craft takes: 0.5 is twice as fast, 2 is twice as slow. */
    record Duration(double multiplier) implements OptionalEffect {
        public static final MapCodec<Duration> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.doubleRange(0.0001, 10000).fieldOf("multiplier").forGetter(Duration::multiplier)
        ).apply(instance, Duration::new));

        @Override
        public Type type() {
            return Type.DURATION;
        }
    }

    /** Multiplies the energy used per tick. */
    record Energy(double multiplier) implements OptionalEffect {
        public static final MapCodec<Energy> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.doubleRange(0, 10000).fieldOf("multiplier").forGetter(Energy::multiplier)
        ).apply(instance, Energy::new));

        @Override
        public Type type() {
            return Type.ENERGY;
        }
    }

    /** Changes how many of the result the craft makes: the count is multiplied, then "add" is added. */
    record OutputCount(int add, double multiplier) implements OptionalEffect {
        public static final MapCodec<OutputCount> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.optionalFieldOf("add", 0).forGetter(OutputCount::add),
                Codec.doubleRange(0, 10000).optionalFieldOf("multiplier", 1.0).forGetter(OutputCount::multiplier)
        ).apply(instance, OutputCount::new));

        @Override
        public Type type() {
            return Type.OUTPUT_COUNT;
        }
    }

    /** An extra item that is produced alongside the result, with a chance (0 to 1) each craft. */
    record BonusOutput(ItemStackTemplate item, double chance) implements OptionalEffect {
        public static final MapCodec<BonusOutput> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackTemplate.CODEC.fieldOf("item").forGetter(BonusOutput::item),
                Codec.doubleRange(0, 1).optionalFieldOf("chance", 1.0).forGetter(BonusOutput::chance)
        ).apply(instance, BonusOutput::new));

        @Override
        public Type type() {
            return Type.BONUS_OUTPUT;
        }
    }

    /** Makes the craft produce a different item. If several are active the last one wins. */
    record ReplaceResult(ItemStackTemplate item) implements OptionalEffect {
        public static final MapCodec<ReplaceResult> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackTemplate.CODEC.fieldOf("item").forGetter(ReplaceResult::item)
        ).apply(instance, ReplaceResult::new));

        @Override
        public Type type() {
            return Type.REPLACE_RESULT;
        }
    }

    enum Type implements StringRepresentable {
        DURATION("duration", Duration.CODEC),
        ENERGY("energy", Energy.CODEC),
        OUTPUT_COUNT("output_count", OutputCount.CODEC),
        BONUS_OUTPUT("bonus_output", BonusOutput.CODEC),
        REPLACE_RESULT("replace_result", ReplaceResult.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final String name;
        private final MapCodec<? extends OptionalEffect> codec;

        Type(String name, MapCodec<? extends OptionalEffect> codec) {
            this.name = name;
            this.codec = codec;
        }

        public MapCodec<? extends OptionalEffect> codec() {
            return codec;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    Codec<OptionalEffect> CODEC = Type.CODEC.dispatch("type", OptionalEffect::type, Type::codec);
}
