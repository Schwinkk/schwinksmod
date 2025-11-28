package com.schwink.schwinkmod.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class TestMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void renderHitMarker(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){

    }
}
