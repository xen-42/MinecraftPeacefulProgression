package xen42.peacefulitems.payloads;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

public record EffigyParticlePayload(String particleID) implements FabricPacket {

    public static final Identifier ID = PeacefulMod.EFFIGY_PARTICLE_PAYLOAD;
	public static final PacketType<EffigyParticlePayload> PACKET_TYPE = PacketType.create(ID, EffigyParticlePayload::new);

    public Identifier id() {
        return ID;
    }

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
    }

    public EffigyParticlePayload(String particleID)
    {
        this.particleID = particleID;
    }

    public EffigyParticlePayload(PacketByteBuf buf)
    {
        this(buf.readString());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(particleID);
    }
}
