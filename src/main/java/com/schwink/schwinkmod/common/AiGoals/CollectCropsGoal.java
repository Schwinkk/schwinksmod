package com.schwink.schwinkmod.common.AiGoals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollectCropsGoal extends Goal {

    protected final PathfinderMob mob;
    public final double speedModifier;
    private BlockPos cropToHarvest;
    private boolean reachedTarget;

    public static final List<BlockPos> allDirections = new ArrayList<>();

    static {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    allDirections.add(new BlockPos(x, y, z));
                }
            }
        }
    }

    public CollectCropsGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        if (!GoalUtils.hasGroundPathNavigation(mob)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }

    @Override
    public boolean canUse() {
        BlockPos mobPos = this.mob.blockPosition();

        return checkNearbyForCrops(mobPos);
    }

    @Override
    public void start(){
        if (this.cropToHarvest != null){
            moveToBlock(this.cropToHarvest);
        }
    }

    @Override
    public void tick(){
        if (!this.cropToHarvest.closerToCenterThan((Position) this.mob.blockPosition(),0.5)){
            if (cropToHarvest !=null){

            }
            else {
                if (checkNearbyForCrops(this.mob.blockPosition())){
                    
                }
            }
        }
    }

    private boolean checkNearbyForCrops(BlockPos pos){
        Level level = mob.level();

        for (BlockPos dir : allDirections) {
            BlockState state = level.getBlockState(pos.offset(dir));

            if (state.getBlock() instanceof CropBlock cropBlock){
                if (cropBlock.isMaxAge(state)){
                    this.cropToHarvest = pos.offset(dir);
                    System.out.println("IMBA");
                    return true;
                }
            }
        }

        return false;
    }

    private void moveToBlock(BlockPos endPoint){
        this.mob.getNavigation().moveTo(endPoint.getX(),endPoint.getY(),endPoint.getZ(),speedModifier);
    }

    private boolean isReachedTarget(){
        return this.reachedTarget;
    }
}
