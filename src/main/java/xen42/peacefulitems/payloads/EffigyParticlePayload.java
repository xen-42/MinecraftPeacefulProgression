package xen42.peacefulitems.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import xen42.peacefulitems.PeacefulMod;

public record EffigyParticlePayload(String particleID) implements CustomPayload {

    public static final CustomPayload.Id<EffigyParticlePayload> ID = new CustomPayload.Id<>(PeacefulMod.EFFIGY_PARTICLE_PAYLOAD);
    public static final PacketCodec<RegistryByteBuf, EffigyParticlePayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, EffigyParticlePayload::particleID,
        EffigyParticlePayload::new
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
