package com.schwink.schwinkmod.client;

import com.schwink.schwinkmod.common.Config;
import com.schwink.schwinkmod.server.SchwinkModServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Config.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
public class HitMarkerMixinData {

    public static float alphaDecreaseSpeed = 3f;
    public static int color = 0x00FFFFFF;
    public static float alpha = 0f;
}
