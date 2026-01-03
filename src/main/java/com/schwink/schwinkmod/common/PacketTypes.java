package com.schwink.schwinkmod.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketTypes {

    public static record HitData(String something) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<HitData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("scwinkmod", "s2c_hitdata"));

        public static final StreamCodec<ByteBuf, HitData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                HitData::something,
                HitData::new
        );

        @Override
        public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type(){
            return TYPE;
        }
    }
}
