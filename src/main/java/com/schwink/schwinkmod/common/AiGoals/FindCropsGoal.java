package com.schwink.schwinkmod.common.AiGoals;

import com.google.common.collect.Lists;
import com.schwink.schwinkmod.mixin.ZombieAIMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;

public class FindCropsGoal extends MoveToBlockGoal {

    protected final PathfinderMob mob;
    private final double speedModifier;
    @Nullable
    private Path path;
    private BlockPos poiPos;
    private final List<BlockPos> visited = Lists.newArrayList();
    private final int distanceToPoi;

    public FindCropsGoal(PathfinderMob mob, double speedModifier, int searchRange, int distanceToPoi) {
        super(mob, speedModifier, searchRange);
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.distanceToPoi = distanceToPoi;
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        BlockState state = levelReader.getBlockState(blockPos);

        if (!state.is(BlockTags.CROPS)){
            return false;
        }

        if (state.getBlock() instanceof CropBlock crop){
            if (!crop.isMaxAge(state)){
                return false;
            }
        }

        if (levelReader instanceof ServerLevel serverLevel){
            if (!serverLevel.isVillage(blockPos)){
                return false;
            }
        }

        return true;
    }


}
