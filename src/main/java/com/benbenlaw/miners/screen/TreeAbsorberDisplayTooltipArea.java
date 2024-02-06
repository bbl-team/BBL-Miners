package com.benbenlaw.miners.screen;

import com.benbenlaw.miners.block.entity.MinerBlockEntity;
import com.benbenlaw.miners.block.entity.TreeAbsorberBlockEntity;
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
public class TreeAbsorberDisplayTooltipArea {

    private final TreeAbsorberBlockEntity entity;

    public TreeAbsorberDisplayTooltipArea(int xMin, int yMin, TreeAbsorberBlockEntity entity)  {
        this(entity);
    }

    public TreeAbsorberDisplayTooltipArea(TreeAbsorberBlockEntity entity)  {
        this.entity = entity;
    }

    public List<Component> getTooltips() {

        //Improved GUI

        Component patternComponent = Component.literal("Pattern: No Pattern Found!");
        Component RFPerTickComponent = Component.literal("RF Per Tick: " + entity.getRFPerTick());
        Component durationComponent = Component.literal("Ticks Per Tree: " + entity.getMaxProgress());
        Component progressComponent = Component.literal("Progress: No Tree Found!");

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

/*
        System.out.println("getTooltips method called. RFPerTick: " + entity.getRFPerTick());
        System.out.println("getTooltips method called. power: " + entity.getMaxProgress());


        if (entity.getMaxProgress() != 0 && entity.getProgress() != 0) {
            return List.of(
                    Component.literal("Progress: " + ((entity.getProgress() * 100) / entity.getMaxProgress()) + " %"),
                    Component.literal("RF Per Tick: " + entity.getRFPerTick()),
                    Component.literal(entity.getMaxProgress() + " Ticks Per Resource")
            );
        }

        return List.of(
                Component.literal("No resource being generated")
        );
    }

 */

    }
}