package com.schwink.schwinkmod.client;

import com.schwink.schwinkmod.common.Config;
import com.schwink.schwinkmod.server.SchwinkModServer;
import com.schwink.schwinkmod.server.TreeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Config.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Config.MODID, value = Dist.CLIENT)
public class TreeCapitatorClient {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        Config.LOGGER.info("HELLO FROM CLIENT SETUP");
        Config.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public static void OnBreakSpeed(PlayerEvent.BreakSpeed event) {

        var state = event.getState();

        if (!state.is(Config.LOGS_TAG)) {
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
}
