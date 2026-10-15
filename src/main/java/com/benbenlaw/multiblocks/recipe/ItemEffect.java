package com.benbenlaw.multiblocks.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStackTemplate;

public sealed interface ItemEffect {

    Type type();

    record Duration(double multiplier) implements ItemEffect {
        public static final MapCodec<Duration> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.doubleRange(0.0001, 10000).fieldOf("multiplier").forGetter(Duration::multiplier)
        ).apply(instance, Duration::new));

        @Override
        public Type type() {
            return Type.DURATION;
        }
    }

    record Energy(double multiplier) implements ItemEffect {
        public static final MapCodec<Energy> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.doubleRange(0, 10000).fieldOf("multiplier").forGetter(Energy::multiplier)
        ).apply(instance, Energy::new));

        @Override
        public Type type() {
            return Type.ENERGY;
        }
    }

    record OutputCount(int add, double multiplier) implements ItemEffect {
        public static final MapCodec<OutputCount> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.optionalFieldOf("add", 0).forGetter(OutputCount::add),
                Codec.doubleRange(0, 10000).optionalFieldOf("multiplier", 1.0).forGetter(OutputCount::multiplier)
        ).apply(instance, OutputCount::new));

        @Override
        public Type type() {
            return Type.OUTPUT_COUNT;
        }
    }

    record BonusOutput(ItemStackTemplate item, double chance) implements ItemEffect {
        public static final MapCodec<BonusOutput> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackTemplate.CODEC.fieldOf("item").forGetter(BonusOutput::item),
                Codec.doubleRange(0, 1).optionalFieldOf("chance", 1.0).forGetter(BonusOutput::chance)
        ).apply(instance, BonusOutput::new));

        @Override
        public Type type() {
            return Type.BONUS_OUTPUT;
        }
    }

    record ReplaceResult(ItemStackTemplate item) implements ItemEffect {
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
        private final MapCodec<? extends ItemEffect> codec;

        Type(String name, MapCodec<? extends ItemEffect> codec) {
            this.name = name;
            this.codec = codec;
        }

        public MapCodec<? extends ItemEffect> codec() {
            return codec;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    Codec<ItemEffect> CODEC = Type.CODEC.dispatch("type", ItemEffect::type, Type::codec);
}
