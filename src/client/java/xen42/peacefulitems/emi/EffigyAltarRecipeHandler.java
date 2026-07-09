package xen42.peacefulitems.emi;

import java.util.List;

import com.google.common.collect.Lists;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;
import xen42.peacefulitems.screen.EffigyAltarScreenHandler;

public class EffigyAltarRecipeHandler implements StandardRecipeHandler<EffigyAltarScreenHandler> {

	@Override
	public List<Slot> getInputSources(EffigyAltarScreenHandler handler) {
		List<Slot> list = Lists.newArrayList();
		for (int i = EffigyAltarScreenHandler.INPUT_SLOTS_START; i < EffigyAltarScreenHandler.INVENTORY_SLOTS_START; i++) { 
			list.add(handler.getSlot(i));
		}
		int invStart = EffigyAltarScreenHandler.INVENTORY_SLOTS_START;
		for (int i = invStart; i < invStart + 36; i++) { 
			list.add(handler.getSlot(i));
		}
		return list;
	}
	
	@Override
	public List<Slot> getCraftingSlots(EffigyAltarScreenHandler handler) {
		List<Slot> list = Lists.newArrayList();
		for (int i = EffigyAltarScreenHandler.INPUT_SLOTS_START; i < EffigyAltarScreenHandler.INVENTORY_SLOTS_START; i++) { 
			list.add(handler.getSlot(i));
		}
		return list;
	}

	@Override
	public @Nullable Slot getOutputSlot(EffigyAltarScreenHandler handler) {
		return handler.slots.get(EffigyAltarScreenHandler.OUTPUT_SLOT);
	}

	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe.getCategory() == PeacefulProgressionEMIPlugin.EFFIGY_CATEGORY && recipe.supportsRecipeTree();
	}

}
