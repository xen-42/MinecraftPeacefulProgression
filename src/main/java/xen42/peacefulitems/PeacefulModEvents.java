package xen42.peacefulitems;

import java.util.Map;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

// Events used for mod compatibility 
// Probably terrible, should work with reflection (just call RegisterListener)
public class PeacefulModEvents {
    public interface TotemEventCallback {
        public void onUse(ServerPlayerEntity user);
    }

    private static Event<TotemEventCallback> createTotemEvent() {
        return EventFactory.createArrayBacked(TotemEventCallback.class, listeners -> (user) -> {
            for (var listener : listeners) {
                listener.onUse(user);
            }
        });
    }

    public static void RegisterTotemEventListener(String eventName, Consumer<ServerPlayerEntity> listener) {
        var callback = new TotemEventCallback() {
            @Override
            public void onUse(ServerPlayerEntity user) {
                listener.accept(user);
            }
        };
        var event = map.get(eventName);
        if (event != null) {
            event.register(callback);
        }
        else {
            PeacefulMod.LOGGER.error("Could not find event named " + eventName + ". Possible version mismatch?");
        }
    }

    public static final Event<TotemEventCallback> DRAGON_TOTEM_USE_EVENT = createTotemEvent();
    public static final Event<TotemEventCallback> WITHER_TOTEM_USE_EVENT = createTotemEvent();
    public static final Event<TotemEventCallback> RAID_TOTEM_USE_EVENT = createTotemEvent();
    public static final Event<TotemEventCallback> GUARDIAN_TOTEM_USE_EVENT = createTotemEvent();

    private static Map<String, Event<TotemEventCallback>> map = Map.of(
        "DRAGON_TOTEM_USE_EVENT", DRAGON_TOTEM_USE_EVENT,
        "WITHER_TOTEM_USE_EVENT", WITHER_TOTEM_USE_EVENT,
        "RAID_TOTEM_USE_EVENT", RAID_TOTEM_USE_EVENT,
        "GUARDIAN_TOTEM_USE_EVENT", GUARDIAN_TOTEM_USE_EVENT
    );

    public static void onInitialize() { }
}
