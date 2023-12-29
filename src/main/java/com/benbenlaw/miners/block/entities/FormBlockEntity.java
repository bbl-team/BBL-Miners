package com.benbenlaw.miners.block.entities;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.core.MultiblockManager;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGenerator2BlockEntity;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraftforge.registries.ForgeRegistries;

import javax.swing.text.html.parser.Entity;
import java.util.Objects;

public class FormBlockEntity extends BlockEntity {
    private int counter = 0;
    private BlockPattern lastCheck;
    public FormBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(Miners.FORM_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }



    public void tick() {

        FormBlockEntity entity = this;
        entity.counter++;
        int tickRate = 220;

        if (!level.isClientSide()) {

            if (entity.counter % tickRate == 0) {

                var result = MultiblockManager.findUnformedStructure(level, this.worldPosition);
                if (result != null) {

                    ItemEntity itemAsEntity = new ItemEntity(level,
                            ((level.random.nextFloat() * 0.1) + 0.5) + this.worldPosition.getX(),
                            ((level.random.nextFloat() * 0.1) + 0.5) + this.worldPosition.getY(),
                            ((level.random.nextFloat() * 0.1) + 0.5) + this.worldPosition.getZ(),
                            new ItemStack(Items.ACACIA_FENCE)
                    );

                    itemAsEntity.setDefaultPickUpDelay();
                    itemAsEntity.setDeltaMovement((level.random.nextFloat() * 0.1 - 0.05), (level.random.nextFloat() * 0.1 - 0.03), (level.random.nextFloat() * 0.1 - 0.05));
                    level.addFreshEntity(itemAsEntity);

                } else {

                    //   player.sendSystemMessage(Component.literal("Found no structure!"));
                }
            }

        }

        if (getLevel() == null) return;
    }
}

