package com.schwink.schwinkmod.server;

import com.schwink.schwinkmod.common.Config;
import com.schwink.schwinkmod.common.PacketTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.handling.ClientPayloadContext;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@Mod(Config.MODID)
@EventBusSubscriber
public class SchwinkModServer {

    //breaking all tree when breaking log with axe
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

    // Sending packet to client to show Hitmarker
    @SubscribeEvent
    private static void onLivingAttack(LivingDamageEvent.Post event){
        final DamageSource source = event.getEntity().getLastDamageSource();
        if (source!= null && source.getEntity() instanceof ServerPlayer player){
            PacketDistributor.sendToPlayer((ServerPlayer) source.getEntity(), new PacketTypes.HitData("hit"));
        }
    }

    @SubscribeEvent
    private static void registerPayload(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                PacketTypes.HitData.TYPE,
                PacketTypes.HitData.STREAM_CODEC,
                ServerPayloadHandler::handleDataOnMain
        );
    }

    @SubscribeEvent
    private static void commonSetup(FMLCommonSetupEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

    }
}
