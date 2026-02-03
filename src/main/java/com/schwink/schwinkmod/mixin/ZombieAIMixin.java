package com.schwink.schwinkmod.mixin;

import com.schwink.schwinkmod.common.AiGoals.DropCropsGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Zombie.class)
public class ZombieAIMixin extends Monster {

    protected ZombieAIMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Overwrite()
    protected void registerGoals(){
        // move through vilage to find crops
        // collect all crops
        // move through village to drop collected items
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    @Overwrite
    protected void addBehaviourGoals(){
        this.goalSelector.addGoal(6, new DropCropsGoal(this, (double)1.0F, 1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
    }

    @Overwrite
    public boolean canBreakDoors(){
        return true;
    }

    @Overwrite
    public void setCanBreakDoors(boolean canBreakDoors){

    }

    @Overwrite
    protected boolean isSunSensitive(){
        return false;
    }
}
