package xen42.peacefulitems;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import xen42.peacefulitems.item.EffigyItem;

public class PeacefulModItems {
    public static final Item BAT_WING = register("bat_wing", Item::new, new Item.Settings());
    public static final Item GUANO = register("guano", Item::new, new Item.Settings());
    public static final Item SULPHUR = register("sulphur", (settings) -> new BlockItem(PeacefulModBlocks.SULPHUR_CLUSTER, settings), new Item.Settings());
    public static final Item FLAX = register("flax", (settings) -> 
        new BlockItem(PeacefulModBlocks.FLAX_CROP, settings), new Item.Settings().food(new FoodComponent(2, 1, false)));
    public static final Item GHASTLING_SPAWN_EGG = register("ghastling_spawn_egg", (settings) -> 
        new SpawnEggItem(PeacefulMod.GHASTLING_ENTITY, settings), new Item.Settings());
    public static final Item END_CLAM_SPAWN_EGG = register("end_clam_spawn_egg", (settings) -> 
        new SpawnEggItem(PeacefulMod.END_CLAM_ENTITY, settings), new Item.Settings());
    public static final Item WITHER_EFFIGY = register("wither_effigy", (settings) -> 
        new EffigyItem(settings, "wither_effigy", (ServerPlayerEntity user) -> {
            user.dropItem((ServerWorld)user.getWorld(), Items.NETHER_STAR);
            ExperienceOrbEntity.spawn((ServerWorld)user.getWorld(), user.getPos(), 50);
        }, SoundEvents.ENTITY_WITHER_DEATH),
        new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    public static final Item GUARDIAN_EFFIGY = register("guardian_effigy", (settings) -> 
        new EffigyItem(settings, "guardian_effigy", (ServerPlayerEntity user) -> { 
            var world = (ServerWorld)user.getWorld();
            user.dropItem(world, Blocks.SPONGE);
            for (int i = 0; i < user.getRandom().nextBetween(0, 2); i++) {
                user.dropItem(world, Items.PRISMARINE_SHARD);
            }
            if (user.getRandom().nextFloat() < 0.2) {
                user.dropItem(world, Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }
            var r = user.getRandom().nextFloat();
            if (r < 0.5) {
                user.dropItem(world, Items.COOKED_COD);
            }
            else if (r < 0.83) {
                user.dropItem(world, Items.PRISMARINE_CRYSTALS);
            }
            ExperienceOrbEntity.spawn((ServerWorld)user.getWorld(), user.getPos(), 10);
        }, SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH),
        new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));
    
    public static final Item DRAGON_EFFIGY = register("dragon_effigy", (settings) -> 
        new EffigyItem(settings, "dragon_effigy", (ServerPlayerEntity user) -> {
            
            var fight = ((ServerWorld)user.getWorld()).getEnderDragonFight();
            try {
                var generateNewEndGatewayMethod = fight.getClass().getDeclaredMethod("generateNewEndGateway");
                generateNewEndGatewayMethod.setAccessible(true);
                generateNewEndGatewayMethod.invoke(fight);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            ExperienceOrbEntity.spawn((ServerWorld)user.getWorld(), user.getPos(), 500);
        }, SoundEvents.ENTITY_ENDER_DRAGON_DEATH), new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    public static void initialize() {
        // Add custom items to groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> { 
            itemGroup.add(BAT_WING);
            itemGroup.add(GUANO);
            itemGroup.add(SULPHUR);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> { 
            itemGroup.add(PeacefulModBlocks.SULPHUR_BLOCK.asItem());
            itemGroup.add(PeacefulModBlocks.SULPHUR_ORE.asItem());
            itemGroup.add(PeacefulModBlocks.SULPHUR_CLUSTER.asItem());
            itemGroup.add(PeacefulModBlocks.FOSSIL_ORE.asItem());
            itemGroup.add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE.asItem());
            itemGroup.add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE.asItem());
            itemGroup.add(PeacefulModBlocks.BLAZE_PICKLE.asItem());
            itemGroup.add(PeacefulModBlocks.BREEZE_CORAL.asItem());
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> { 
            itemGroup.add(FLAX);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((itemGroup) -> {
            itemGroup.add(GHASTLING_SPAWN_EGG);
            itemGroup.add(END_CLAM_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> {
            itemGroup.add(WITHER_EFFIGY);
            itemGroup.add(DRAGON_EFFIGY);
            itemGroup.add(GUARDIAN_EFFIGY);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> {
            itemGroup.add(PeacefulModBlocks.EFFIGY_ALTAR.asItem());
        });

        CompostingChanceRegistry.INSTANCE.add(GUANO, 2f);
        CompostingChanceRegistry.INSTANCE.add(FLAX, 0.3f);
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
