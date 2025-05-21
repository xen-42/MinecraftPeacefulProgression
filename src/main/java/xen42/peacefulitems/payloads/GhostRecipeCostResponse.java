package xen42.peacefulitems.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

public record GhostRecipeCostResponse(int cost) implements CustomPayload {
    public static final Identifier ID = Identifier.of(PeacefulMod.MOD_ID, "ghost_recipe_cost_response");
    public static final CustomPayload.Id<GhostRecipeCostResponse> PAYLOAD_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, GhostRecipeCostResponse> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, GhostRecipeCostResponse::cost,
            GhostRecipeCostResponse::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

}
