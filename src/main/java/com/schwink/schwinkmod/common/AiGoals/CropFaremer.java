package com.schwink.schwinkmod.common.AiGoals;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public interface CropFaremer {
    ArrayList<Integer> harvestedCrops = null;
    void decreaseCrops();
    void increaseCrops(Integer itemId);
}
