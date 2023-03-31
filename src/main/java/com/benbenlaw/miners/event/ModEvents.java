package com.benbenlaw.miners.event;

import com.benbenlaw.miners.Miners;
import com.benbenlaw.miners.block.ModBlocks;
import com.benbenlaw.miners.item.ModItems;
import com.benbenlaw.miners.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod.EventBusSubscriber(modid = Miners.MOD_ID)

public class ModEvents {

    /*

    @SubscribeEvent
    public static void framesClimbable(TickEvent.@NotNull PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.getLevel();
        BlockPos blockPos = player.blockPosition().offset(player.dela);



        if(player.onClimbable() && level.getBlockState(player.blockPosition()).getBlock().defaultBlockState().is(ModBlocks.GOLD_SUPPORT_FRAME.get()))
            {
                Vec3 motion = player.getDeltaMovement();
                float maxMotion = 0.15F;
                motion = new Vec3(
                        Mth.clamp(motion.x, -maxMotion, maxMotion),
                        Math.max(motion.y, -maxMotion),
                        Mth.clamp(motion.z, -maxMotion, maxMotion)
                );

                player.fallDistance = 0.0F;

                if(motion.y < 0&& player instanceof Player &&player.isShiftKeyDown())
                    motion = new Vec3(motion.x, 0, motion.z);
                else if(player.horizontalCollision)
                    motion = new Vec3(motion.x, 0.2, motion.z);
                player.setDeltaMovement(motion);
            }
        }

     */

    }
