package xen42.peacefulitems;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class PeacefulModSounds {

	public static void initialize() { }

	private static final Map<SoundEvent, RegistryEntry.Reference<SoundEvent>> REFERENCES = new HashMap<>();

	public static final SoundEvent ITEM_BOTTLE_EMPTY_DRAGONBREATH = registerSound("item.bottle.empty_dragonbreath");

	public static SoundEvent registerSound(String name) {
		var id = Identifier.of(PeacefulMod.MOD_ID, name);
		var event = SoundEvent.of(id);
		var reference = Registry.registerReference(Registries.SOUND_EVENT, id, event);
		REFERENCES.put(event, reference);
		return event;
	}

	public static RegistryEntry.Reference<SoundEvent> getReference(SoundEvent event) {
		return REFERENCES.get(event);
	}
}
