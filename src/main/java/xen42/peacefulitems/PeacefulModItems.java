package xen42.peacefulitems;

import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class PeacefulModItems {
    public static final Item BAT_WING = register("bat_wing", Item::new, new Item.Settings());
    public static final Item GUANO = register("guano", Item::new, new Item.Settings());
    public static final Item SULPHUR = register("sulphur", Item::new, new Item.Settings());

    public static void initialize() {
        // Add custom items to groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> { 
            itemGroup.add(BAT_WING);
            itemGroup.add(GUANO);
            itemGroup.add(SULPHUR);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> { 
            itemGroup.add(PeacefulModBlocks.SULPHUR_BLOCK.asItem());
            itemGroup.add(PeacefulModBlocks.FOSSIL_ORE.asItem());
            itemGroup.add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE.asItem());
            itemGroup.add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE.asItem());
        });

        CompostingChanceRegistry.INSTANCE.add(GUANO, 1f);
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PeacefulMod.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.registryKey(itemKey));

		// Register the item.
		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}
}
