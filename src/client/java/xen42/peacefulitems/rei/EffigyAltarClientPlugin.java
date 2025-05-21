package xen42.peacefulitems.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.recipe.EffigyAltarRecipeDisplay;
import xen42.peacefulitems.screen.EffigyAltarHandledScreen;

public class EffigyAltarClientPlugin implements REIClientPlugin {
	public EffigyAltarClientPlugin() {
		PeacefulMod.LOGGER.info("Creating REI client plugin");
	}
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		PeacefulMod.LOGGER.info("Registering categories");
		
		registry.add(new EffigyAltarCategory());

		PeacefulMod.LOGGER.info("Registering workstations");
		
		registry.addWorkstations(EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY, EntryStacks.of(PeacefulModBlocks.EFFIGY_ALTAR));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		PeacefulMod.LOGGER.info("Registering displays");
		
		registry.beginRecipeFiller(EffigyAltarRecipeDisplay.class)
			.filterType(EffigyAltarRecipeDisplay.SERIALIZER)
			.fill(ClientsidedEffigyAltarREIDisplay::new);
	}
	
	@Override
	public void registerScreens(ScreenRegistry registry) {
		PeacefulMod.LOGGER.info("Registering screens");
		
		registry.registerContainerClickArea(new Rectangle(88, 24 - 8, 28, 23), EffigyAltarHandledScreen.class, EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY);
	}
	
	@Override
	public void registerTransferHandlers(TransferHandlerRegistry registry) {
		PeacefulMod.LOGGER.info("Registering transfer handlers");
		
		registry.register(new EffigyAltarTransferHandler());
	}
}