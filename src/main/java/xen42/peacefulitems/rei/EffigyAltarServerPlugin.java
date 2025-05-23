package xen42.peacefulitems.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

public class EffigyAltarServerPlugin implements REIServerPlugin {
	public static final Identifier EFFIGY_ALTAR = Identifier.of(PeacefulMod.MOD_ID, "plugins/effigy_altar");
	public static final CategoryIdentifier<EffigyAltarREIDisplay> EFFIGY_ALTAR_CATEGORY = CategoryIdentifier.of(PeacefulMod.MOD_ID, "plugins/effigy_altar");

	public EffigyAltarServerPlugin() {
		PeacefulMod.LOGGER.info("Creating REI server plugin");
	}
	
	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		PeacefulMod.LOGGER.info("Registering display serializers");

		registry.register(EFFIGY_ALTAR_CATEGORY, EffigyAltarREIDisplay.SERIALIZER);
	}
}