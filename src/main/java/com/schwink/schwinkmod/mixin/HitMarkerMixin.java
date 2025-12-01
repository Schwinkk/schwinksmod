package com.schwink.schwinkmod.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.schwink.schwinkmod.TreeCapitator;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public class HitMarkerMixin {

    private static final ResourceLocation HIT_ICON = ResourceLocation.fromNamespaceAndPath(TreeCapitator.MODID,"textures/gui/hit_marker");

    public float iconOpacatity = 0f;

    @Inject(method = "render", at = @At("TAIL"))
    public void renderHitMarker(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,HIT_ICON,);
    }

    public void showHitMarker(){

    }
}
