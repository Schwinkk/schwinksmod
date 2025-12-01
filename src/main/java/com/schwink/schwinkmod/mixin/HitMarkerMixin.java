package com.schwink.schwinkmod.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.schwink.schwinkmod.HitMarkerMixinData.alphaDecreaseSpeed;
import static com.schwink.schwinkmod.HitMarkerMixinData.a;
import static com.schwink.schwinkmod.HitMarkerMixinData.color;

@Mixin(Gui.class)
public class HitMarkerMixin {

    private static final ResourceLocation HIT_ICON = ResourceLocation.fromNamespaceAndPath("schwinkmod", "textures/gui/hit_marker.png");

    @Inject(method = "render", at = @At("TAIL"))
    public void renderHitMarker(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){

        if (a == 0){return;}

        if (a - alphaDecreaseSpeed <= 0){
            a = 0;
        }
        else {
            a = a - alphaDecreaseSpeed;
        }

        int color1 = (a << 24) | (color & 0x00FFFFFF);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,HIT_ICON,guiGraphics.guiWidth()/2 - 12, guiGraphics.guiHeight()/2 - 13,0,0, 25, 25, 25, 25,color1);
    }
}
