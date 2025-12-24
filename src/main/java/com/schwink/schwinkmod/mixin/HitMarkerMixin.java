package com.schwink.schwinkmod.mixin;

import com.schwink.schwinkmod.common.Config;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.schwink.schwinkmod.client.HitMarkerMixinData.*;

@Mixin(Gui.class)
public class HitMarkerMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void renderHitMarker(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){

        if (alpha == 0){return;}

        if (alpha - alphaDecreaseSpeed <= 0){
            alpha = 0;
        }
        else {
            alpha = alpha - alphaDecreaseSpeed;
        }

        int color1 = ((int) Math.abs(alpha - 24f)) | (color & 0x00FFFFFF);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Config.HIT_ICON,guiGraphics.guiWidth()/2 - 12, guiGraphics.guiHeight()/2 - 13,0,0, 25, 25, 25, 25,color1);
    }
}
