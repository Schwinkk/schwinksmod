package com.schwink.schwinkmod.common.AiGoals;

import com.google.common.collect.Lists;
import com.schwink.schwinkmod.mixin.ZombieAIMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
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
        if (!GoalUtils.hasGroundPathNavigation(mob)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
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
            System.out.println("NASHELLLL");
            return serverLevel.isVillage(blockPos);
        }

        return true;
    }


}
