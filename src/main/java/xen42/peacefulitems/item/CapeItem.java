package xen42.peacefulitems.item;

import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import xen42.peacefulitems.PeacefulModItems;

public class CapeItem extends ElytraItem implements FabricElytraItem {
	public CapeItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.isOf(PeacefulModItems.BAT_WING);
	}
}
