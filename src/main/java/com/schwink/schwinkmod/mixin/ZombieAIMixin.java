package com.schwink.schwinkmod.mixin;

import com.google.common.primitives.Ints;
import com.schwink.schwinkmod.common.AiGoals.FindCropsGoal;
import com.schwink.schwinkmod.common.AiGoals.ICropFarmer;
import com.schwink.schwinkmod.common.AiGoals.DropCropsGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Zombie.class)
public class ZombieAIMixin extends Monster implements ICropFarmer {

    protected ZombieAIMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    protected ArrayList<Integer> harvestedCrops = new ArrayList<>();

    @Unique
    public void decreaseCrops() {
        harvestedCrops.removeFirst();
    }

    @Unique
    public void increaseCrops(Integer itemId) {
        harvestedCrops.addLast(itemId);
    }

    @Overwrite()
    protected void registerGoals(){
        this.goalSelector.addGoal(1, new FindCropsGoal(this, 1, 8,4));
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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void saveHarvestData(ValueOutput output, CallbackInfo ci){

        int[] data = new int[harvestedCrops.size()];
        for (int i = 0; i < data.length; i++){
            data[i] = harvestedCrops.get(i);
        }

        output.putIntArray("harvestedCrops", data);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readHarvestData(ValueInput input, CallbackInfo ci){

        this.harvestedCrops = input.getIntArray("harvestedCrops")
                .map(array -> new ArrayList<>(Ints.asList(array))).orElse(new ArrayList<>());
    }

}
