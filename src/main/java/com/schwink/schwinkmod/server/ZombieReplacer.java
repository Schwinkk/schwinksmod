package com.schwink.schwinkmod.server;

import com.schwink.schwinkmod.common.Config;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod(Config.MODID)
@EventBusSubscriber
public class ZombieReplacer {
    @SubscribeEvent
    public void onEntitySpawnEvent(EntityJoinLevelEvent event){
        if (event.getLevel().isClientSide){
            return;
        }

        if (event.getEntity().getType() == EntityType.ZOMBIE){
            event.setCanceled(true);


        }
    }
}
