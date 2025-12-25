package com.schwink.schwinkmod.server;

import com.schwink.schwinkmod.common.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.Optional;


@Mod(Config.MODID)
public class SchwinkModServer {

    @EventBusSubscriber
    public static class TreeCapitatorStarter{

        @SubscribeEvent
        public static void OnBlockBreak(BlockEvent.BreakEvent event) {

            var state = event.getState();
            if (!state.is(Config.LOGS_TAG)) {
                return;
            }

            ServerPlayer player = (ServerPlayer) event.getPlayer();
            Level level = (Level) event.getLevel();
            BlockPos pos = event.getPos();

            if (player.getMainHandItem().getItem() instanceof AxeItem){
                TreeManager.destroyAndDrop(level, pos, player);
            }
        }

        @SubscribeEvent
        private void onLivingAttack(LivingDamageEvent event){
            final DamageSource source = event.getEntity().getLastDamageSource();
            if (source.getEntity().)
        }
    }


    @SubscribeEvent
    private void commonSetup(FMLCommonSetupEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
