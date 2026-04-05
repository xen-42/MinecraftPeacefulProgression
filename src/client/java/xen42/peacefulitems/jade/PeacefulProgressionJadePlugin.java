package xen42.peacefulitems.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

import xen42.peacefulitems.entities.EndClamEntity;
import xen42.peacefulitems.PeacefulMod;

@WailaPlugin(PeacefulMod.MOD_ID)
public class PeacefulProgressionJadePlugin implements IWailaPlugin {
	public PeacefulProgressionJadePlugin() {
		PeacefulMod.LOGGER.info("Creating Jade plugin");
	}
	
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		PeacefulMod.LOGGER.info("Registering client components");
		registration.registerEntityComponent(EndClamProvider.INSTANCE, EndClamEntity.class);
	}
}