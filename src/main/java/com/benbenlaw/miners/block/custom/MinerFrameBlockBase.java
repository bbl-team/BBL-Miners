package com.benbenlaw.miners.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class MinerFrameBlockBase extends Block {
    public MinerFrameBlockBase(Properties p_49795_) {
        super(p_49795_);
    }

    private static final VoxelShape COLLISION_SHAPE = box(1, 0, 1, 15, 16, 15);

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, CollisionContext context){
        return COLLISION_SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn)
    {
        super.entityInside(state, worldIn, pos, entityIn);
        if(entityIn instanceof LivingEntity&&isLadder(state, worldIn, pos, (LivingEntity)entityIn))
            applyLadderLogic(entityIn);
    }

    //From Immersive Engineering found here: https://github.com/BluSunrize/ImmersiveEngineering/blob/9dda4f57cf1c93e5cdcdee15ed7fc9b8d4372f85/src/main/java/blusunrize/immersiveengineering/common/blocks/IEBaseBlock.java#L347
    //Thanks BluSunrize for this applyLadderLogic code found above ^

    public static void applyLadderLogic(Entity entityIn) {
        if(entityIn instanceof LivingEntity&&!((LivingEntity)entityIn).onClimbable())
        {
            Vec3 motion = entityIn.getDeltaMovement();
            float maxMotion = 0.15F;
            motion = new Vec3(
                    Mth.clamp(motion.x, -maxMotion, maxMotion),
                    Math.max(motion.y, -maxMotion),
                    Mth.clamp(motion.z, -maxMotion, maxMotion)
            );

            entityIn.fallDistance = 0.0F;

            if(motion.y < 0&&entityIn instanceof Player&&entityIn.isShiftKeyDown())
                motion = new Vec3(motion.x, 0, motion.z);
            else if(entityIn.horizontalCollision)
                motion = new Vec3(motion.x, 0.2, motion.z);
            entityIn.setDeltaMovement(motion);
        }
    }


}
