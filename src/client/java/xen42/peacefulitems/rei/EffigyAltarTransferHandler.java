package xen42.peacefulitems.rei;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import xen42.peacefulitems.screen.EffigyAltarScreenHandler;

public class EffigyAltarTransferHandler implements SimpleTransferHandler {
	
	public EffigyAltarScreenHandler getEffigyAltarMenu(Context context) {
		if (context.getMenu() instanceof EffigyAltarScreenHandler altarMenu) {
			return altarMenu;
		}
		else {
			return null;
		}
	}
	
	@Override
	public ApplicabilityResult checkApplicable(Context context) {
		if (context.getMenu() instanceof EffigyAltarScreenHandler
				&& context.getDisplay().getCategoryIdentifier() == EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY
				&& context.getContainerScreen() != null) {
			return ApplicabilityResult.createApplicable();
		}
		else {
			return ApplicabilityResult.createNotApplicable();
		}
	}
	
	@Override
	public Iterable<SlotAccessor> getInputSlots(Context context) {
		EffigyAltarScreenHandler altarMenu = getEffigyAltarMenu(context);
		List<Slot> slots = altarMenu.getInputSlots();
		List<SlotAccessor> accessors = new ArrayList<SlotAccessor>();
		for (Slot slot : slots) {
			accessors.add(SlotAccessor.fromSlot(slot));
		}
		accessors.add(SlotAccessor.fromSlot(altarMenu.getBrimstoneSlot()));
		return accessors;
	}
	
	@Override
	public Iterable<SlotAccessor> getInventorySlots(Context context) {
		ClientPlayerEntity player = context.getMinecraft().player;
		PlayerInventory inventory = player.getInventory();
		return IntStream.range(0, inventory.getMainStacks().size())
				.mapToObj(index -> SlotAccessor.fromPlayerInventory(player, index))
				.collect(Collectors.toList());
	}
}