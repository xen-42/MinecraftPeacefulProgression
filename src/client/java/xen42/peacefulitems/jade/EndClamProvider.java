package xen42.peacefulitems.jade;

import net.minecraft.util.Identifier;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.JadeUI;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.entities.EndClamEntity;

public enum EndClamProvider implements IEntityComponentProvider {
	INSTANCE;

	@Override
	public Identifier getUid() {
		return Identifier.of(PeacefulMod.MOD_ID, "end_clam");
	}

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		var entity = accessor.getEntity();
		if (entity instanceof EndClamEntity endClam) {
			var containedItem = endClam.getContainedItem();
			if (!containedItem.isEmpty()) {
				tooltip.add(JadeUI.smallItem(containedItem));
				tooltip.append(IDisplayHelper.get().stripColor(containedItem.getName()));
			}
		}
	}
}
