package xen42.peacefulitems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

// Events used for mod compatibility 
// Probably terrible, should work with reflection (just call RegisterListener)
public class PeacefulModEvents {
    @FunctionalInterface
    public interface TotemEventCallback {
        public void onUse(ServerPlayerEntity user);
    }

    private static final Map<String, Event<TotemEventCallback>> EVENTS = new HashMap<>();

    public static final Event<TotemEventCallback> DRAGON_TOTEM_USE_EVENT =
        createTotemEvent("DRAGON_TOTEM_USE_EVENT");

    public static final Event<TotemEventCallback> WITHER_TOTEM_USE_EVENT =
        createTotemEvent("WITHER_TOTEM_USE_EVENT");

    public static final Event<TotemEventCallback> RAID_TOTEM_USE_EVENT =
        createTotemEvent("RAID_TOTEM_USE_EVENT");

    public static final Event<TotemEventCallback> GUARDIAN_TOTEM_USE_EVENT =
        createTotemEvent("GUARDIAN_TOTEM_USE_EVENT");

    private static Event<TotemEventCallback> createTotemEvent(String id) {
        var event = EventFactory.createArrayBacked(TotemEventCallback.class, listeners -> user -> {
            PeacefulMod.LOGGER.info("Firing totem event '{}' for {}", id, user.getName().getString());

            for (var listener : listeners) {
                listener.onUse(user);
            }
        });

        EVENTS.put(id, event);
        return event;
    }

    public static void RegisterTotemEventListener(String eventName, Consumer<ServerPlayerEntity> listener) {
        var event = EVENTS.get(eventName);

        if (event == null) {
            PeacefulMod.LOGGER.error(
                "Unknown totem event '{}'. Possible version mismatch? Valid events: {}",
                eventName,
                EVENTS.keySet()
            );
            return;
        }

        event.register(listener::accept);

        PeacefulMod.LOGGER.info("Registered listener for '{}'", eventName);
    }

    public static void onInitialize() { }
}
