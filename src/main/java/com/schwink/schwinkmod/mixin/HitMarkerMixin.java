package com.schwink.schwinkmod.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class HitMarkerMixin {

    static {
        System.out.println("MIXIN LOADED");
    }

    private static final int HITMARKER_TIME = 10;
    public static int hitmarkerTicks = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    public void renderHitMarker(CallbackInfo ci){
        System.out.println("GAVNO");
    }

    public static void HitMarkerInitializer(){
       hitmarkerTicks = HITMARKER_TIME;
    }

}
