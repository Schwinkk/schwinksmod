package com.schwink.schwinkmod;

import com.schwink.schwinkmod.mixin.HitMarkerMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;


@Mod(TreeCapitator.MODID)
public class TreeCapitator {

    public static final String MODID = "schwinkmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final TagKey<Block> LOGS_TAG = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("minecraft", "logs"));


    @EventBusSubscriber
    public static class TreeCapitatorStarter{

        @SubscribeEvent
        public static void OnBreakSpeed(PlayerEvent.BreakSpeed event) {

            var state = event.getState();

            if (!isBlockLog(state)) {
                return;
            }

            Level level = event.getEntity().level();
            Optional<BlockPos> optionalPos = event.getPosition();
            BlockPos pos;
            Player player = event.getEntity();


            if (optionalPos.isPresent()){
                pos = optionalPos.get();
            }
            else{
                return;
            }

            int x = TreeManager.getTreeSize(pos, level);

            if (player.getMainHandItem().getItem() instanceof AxeItem & x > 1){
                float breakSpeedModifier = (float) Math.sqrt((double) 1 / (x * 2));
                event.setNewSpeed(event.getOriginalSpeed() * breakSpeedModifier);
            }

        }

        @SubscribeEvent
        public static void OnBlockBreak(BlockEvent.BreakEvent event) {

            HitMarkerMixin.HitMarkerInitializer();

            var state = event.getState();
            if (!isBlockLog(state)) {
                return;
            }

            ServerPlayer player = (ServerPlayer) event.getPlayer();
            Level level = (Level) event.getLevel(); // надеюсь приведение ничего не сломает))))
            BlockPos pos = event.getPos();

            if (player.getMainHandItem().getItem() instanceof AxeItem){
                TreeManager.destroyAndDrop(level, pos, player);
            }
        }

        private static boolean isBlockLog(BlockState state) {
            return state.is(LOGS_TAG);
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
