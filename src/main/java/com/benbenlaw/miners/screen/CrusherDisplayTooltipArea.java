package com.benbenlaw.miners.screen;

import com.benbenlaw.miners.block.entity.CrusherBlockEntity;
import com.benbenlaw.miners.block.entity.FluidAbsorberBlockEntity;
import net.minecraft.network.chat.Component;

import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/LICENSE
 *
 *  Slightly Modified Version by: Kaupenjoe
 */
public class CrusherDisplayTooltipArea {

    private final CrusherBlockEntity entity;

    public CrusherDisplayTooltipArea(int xMin, int yMin, CrusherBlockEntity entity)  {
        this(entity);
    }

    public CrusherDisplayTooltipArea(CrusherBlockEntity entity)  {
        this.entity = entity;
    }

    public List<Component> getTooltips() {

        Component patternComponent = Component.literal("Pattern: No Pattern Found!");
        Component RFPerTickComponent = Component.literal("RF Per Tick: " + entity.getRFPerTick());
        Component durationComponent = Component.literal("Ticks Per Crush: " + entity.getMaxProgress());
        Component progressComponent = Component.literal("Progress: No Crusher Recipe Found!");

        if (entity.getPattern() != null) {
            patternComponent = Component.literal("Pattern: " + entity.getPattern());
        }
        if (entity.getMaxProgress() != 0 && entity.getProgress() != 0) {
            progressComponent = Component.literal("Progress: " + ((entity.getProgress() * 100) / entity.getMaxProgress()) + " %");
        }

        return List.of(
                patternComponent,
                RFPerTickComponent,
                durationComponent,
                progressComponent
        );
    }

}