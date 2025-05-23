package xen42.peacefulitems.payloads;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

public record GhostRecipeCostRequest(List<ItemStack> ghostInputs) implements CustomPayload {
    public static final Identifier ID = Identifier.of(PeacefulMod.MOD_ID, "ghost_recipe_cost_request");
    public static final CustomPayload.Id<GhostRecipeCostRequest> PAYLOAD_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, GhostRecipeCostRequest> CODEC = PacketCodec.of(
    		GhostRecipeCostRequest::write, GhostRecipeCostRequest::read
		);

    public GhostRecipeCostRequest(List<ItemStack> ghostInputs) {
		this.ghostInputs = ghostInputs;
	}

	private void write(RegistryByteBuf buf) {
        buf.writeVarInt(ghostInputs.size());
        for (ItemStack stack : ghostInputs) {
        	ItemStack.PACKET_CODEC.encode(buf, stack);
        }
	}
	
	private static GhostRecipeCostRequest read(RegistryByteBuf buf) {
        int size = buf.readVarInt();
        List<ItemStack> ghostInputs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ghostInputs.add(ItemStack.PACKET_CODEC.decode(buf));
        }
		return new GhostRecipeCostRequest(ghostInputs);
	}

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

}
