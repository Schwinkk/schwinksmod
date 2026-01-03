package com.schwink.schwinkmod.client;

import com.schwink.schwinkmod.common.PacketTypes;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleDataOnMain (final PacketTypes.HitData data, final IPayloadContext context) {
        if (!data.something().equals("hit")) return;
        HitMarkerMixinData.showHitMarker();
    }
}
