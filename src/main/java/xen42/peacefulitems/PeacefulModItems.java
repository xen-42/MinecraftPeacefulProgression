package xen42.peacefulitems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.FireworkStarRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import xen42.peacefulitems.item.DispensibleSpawnEggItem;
import xen42.peacefulitems.item.EffigyItem;
import xen42.peacefulitems.blocks.DragonBreathCauldronBlock;
import xen42.peacefulitems.item.BrushDispenserBehavior;
import xen42.peacefulitems.item.CapeItem;
import xen42.peacefulitems.mixin.EnderDragonFight_Invoker;
import net.minecraft.village.raid.Raid;
import net.minecraft.advancement.criterion.Criteria;

public class PeacefulModItems {
    public static final Item BAT_WING = register("bat_wing", Item::new, new Item.Settings());
    
    public static final Identifier CAPE_EQUIPMENT_ASSET = Identifier.of(PeacefulMod.MOD_ID, "cape");
    public static final Item CAPE = register("cape", CapeItem::new,
        new Item.Settings()
            .maxDamage(108)
            .rarity(Rarity.COMMON));
    
    public static final Item ECTOPLASM = register("ectoplasm", Item::new, new Item.Settings());
    public static final Item GUANO = register("guano", Item::new, new Item.Settings());
    public static final Item SULPHUR = register("sulphur", (settings) -> new BlockItem(PeacefulModBlocks.SULPHUR_CLUSTER, settings), new Item.Settings());
    public static final Item FLAX = register("flax", (settings) -> 
        new BlockItem(PeacefulModBlocks.FLAX_CROP, settings) {
            @Override
            public ActionResult place(ItemPlacementContext context) {
            	ActionResult result = super.place(context);
                if (result == ActionResult.SUCCESS)
                {
                    // Grant the player the "A Seedy Place" advancement
                    if (context.getPlayer() instanceof ServerPlayerEntity player && player.getServer() != null)
                    {
                    	AdvancementEntry seedyPlace = player.getServer().getAdvancementLoader().get(Identifier.ofVanilla("husbandry/plant_seed"));
                        if (seedyPlace != null)
                        {
                        	String first = seedyPlace.value().criteria().keySet().iterator().next();
                        	player.getAdvancementTracker().grantCriterion(seedyPlace, first);
                        }
                    }
                }
                return result;
            }
        }, new Item.Settings().food(new FoodComponent.Builder().nutrition(2).saturationModifier(1F).build()));
    public static final Item GHASTLING_SPAWN_EGG = register("ghastling_spawn_egg", (settings) -> 
        new DispensibleSpawnEggItem(PeacefulMod.GHASTLING_ENTITY, 0xFFFFFF, 0x7A7A7A, settings), new Item.Settings());
    public static final Item END_CLAM_SPAWN_EGG = register("end_clam_spawn_egg", (settings) -> 
        new DispensibleSpawnEggItem(PeacefulMod.END_CLAM_ENTITY, 0x6F4B6F, 0x2B1E2B, settings), new Item.Settings());
    public static final Item WITHER_EFFIGY = register("wither_effigy", (settings) -> 
        new EffigyItem(settings, "wither_effigy", (ServerPlayerEntity user) -> {
            user.dropItem(Items.NETHER_STAR);
        }, SoundEvents.ENTITY_WITHER_DEATH),
        new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    public static final Item GUARDIAN_EFFIGY = register("guardian_effigy", (settings) -> 
        new EffigyItem(settings, "guardian_effigy", (ServerPlayerEntity user) -> { 
            var world = user.getServerWorld();

            user.dropItem(Blocks.SPONGE);
            if (user.getRandom().nextFloat() < 0.2) {
                user.dropItem(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
            }

            // Will drop more as if you had killed multiple guardians (which you would normally)
            user.dropStack(new ItemStack(Items.COOKED_COD, user.getRandom().nextBetween(1, 6)));
            user.dropStack(new ItemStack(Items.PRISMARINE_CRYSTALS, user.getRandom().nextBetween(2, 12)));
            user.dropStack(new ItemStack(Items.PRISMARINE_SHARD, user.getRandom().nextBetween(2, 8)));

        }, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE),
        new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));
    
    public static final Item DRAGON_EFFIGY = register("dragon_effigy", (settings) -> 
        new EffigyItem(settings, "dragon_effigy", (ServerPlayerEntity user) -> {
            ServerWorld end = user.getServer().getWorld(World.END);
            var fight = end.getEnderDragonFight();
            ((EnderDragonFight_Invoker)fight).invokeGenerateNewEndGateway();
        }, SoundEvents.ENTITY_ENDER_DRAGON_DEATH), new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    public static final Item RAID_EFFIGY = register("raid_effigy", (settings) -> 
        new EffigyItem(settings, "raid_effigy", (ServerPlayerEntity user) -> {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 48000, 1, false, false, true));
            Criteria.HERO_OF_THE_VILLAGE.trigger(user);
            var r = user.getRandom().nextFloat();
            var world = user.getServerWorld();
            // Bunch of different raid drops excluding Totem of Undying
            if (r < 0.5) {
                user.dropItem(Items.OMINOUS_BOTTLE);
            }
            else {
                user.dropStack(Raid.getOminousBanner(world.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN)));
            }
            if (r < 0.33) {
                user.dropItem(Items.CROSSBOW);
            }
            if (r < 0.33) {
                user.dropItem(Items.SADDLE);
            }
            if (r < 0.33) {
                user.dropItem(Items.IRON_AXE);
            }

            user.dropStack(new ItemStack(Items.EMERALD, user.getRandom().nextBetween(1, 20)));

            // Witch drops
            user.dropStack(new ItemStack(Blocks.REDSTONE_WIRE, user.getRandom().nextBetween(1, 12)));
            user.dropStack(new ItemStack(Items.GUNPOWDER, user.getRandom().nextBetween(1, 12)));
            user.dropStack(new ItemStack(Items.STICK, user.getRandom().nextBetween(1, 12)));
            user.dropStack(new ItemStack(Items.GLOWSTONE_DUST, user.getRandom().nextBetween(1, 12)));
            user.dropStack(new ItemStack(Items.SUGAR, user.getRandom().nextBetween(1, 12)));
            user.dropStack(new ItemStack(Items.SPIDER_EYE, user.getRandom().nextBetween(1, 12)));
            user.dropStack(new ItemStack(Items.GLASS_BOTTLE, user.getRandom().nextBetween(1, 12)));
        }, SoundEvents.EVENT_RAID_HORN), new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    public static final Item CLAM = register("clam_meat", Item::new, 
        new Item.Settings().food(new FoodComponent.Builder().nutrition(5).saturationModifier(0.6F).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F).build()));
    
    public static final Item COOKED_CLAM = register("cooked_clam_meat", Item::new, 
        new Item.Settings().food(new FoodComponent.Builder().nutrition(5).saturationModifier(0.6F).build()));

    public static void initialize() {
        // Add custom items to groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> {
            itemGroup.add(PeacefulModBlocks.SULPHUR_BLOCK.asItem());
            itemGroup.add(PeacefulModBlocks.SULPHUR_STAIRS.asItem());
            itemGroup.add(PeacefulModBlocks.SULPHUR_SLAB.asItem());
            itemGroup.add(PeacefulModBlocks.SULPHUR_WALL.asItem());
            itemGroup.add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK.asItem());
        });
        
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> { 
            itemGroup.add(BAT_WING);
            itemGroup.add(ECTOPLASM);
            itemGroup.add(GUANO);
            itemGroup.add(SULPHUR);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> {
            itemGroup.add(FLAX);
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
            itemGroup.add(CLAM);
            itemGroup.add(COOKED_CLAM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((itemGroup) -> {
            itemGroup.add(GHASTLING_SPAWN_EGG);
            itemGroup.add(END_CLAM_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> {
            itemGroup.add(CAPE);
            itemGroup.add(WITHER_EFFIGY);
            itemGroup.add(DRAGON_EFFIGY);
            itemGroup.add(GUARDIAN_EFFIGY);
            itemGroup.add(RAID_EFFIGY);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> {
            itemGroup.add(PeacefulModBlocks.EFFIGY_ALTAR.asItem());
            itemGroup.add(Blocks.WATER_CAULDRON.asItem());
            itemGroup.add(Blocks.LAVA_CAULDRON.asItem());
            itemGroup.add(Blocks.POWDER_SNOW_CAULDRON.asItem());
            itemGroup.add(PeacefulModBlocks.DRAGON_BREATH_CAULDRON.asItem());
        });

        CompostingChanceRegistry.INSTANCE.add(GUANO, 2f);
        CompostingChanceRegistry.INSTANCE.add(FLAX, 0.3f);

        int sulphurTime = 200 * 4; // More useful than wood, but not as good as coal
        FuelRegistry.INSTANCE.add(PeacefulModItems.SULPHUR, sulphurTime);
        int sulphurBlockTime = sulphurTime * 10; // Coal blocks are 10 times so we do that too
        FuelRegistry.INSTANCE.add(PeacefulModBlocks.SULPHUR_BLOCK, sulphurBlockTime);
        FuelRegistry.INSTANCE.add(PeacefulModBlocks.SULPHUR_WALL, sulphurBlockTime);
        FuelRegistry.INSTANCE.add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK, sulphurBlockTime);
        FuelRegistry.INSTANCE.add(PeacefulModBlocks.SULPHUR_SLAB, sulphurBlockTime / 2); // 2/4
        FuelRegistry.INSTANCE.add(PeacefulModBlocks.SULPHUR_STAIRS, sulphurBlockTime / (4 / 3)); // 3/4

        DispenserBlock.registerBehavior(Items.BRUSH.asItem(), new BrushDispenserBehavior());

        CauldronFluidContent.registerCauldron(PeacefulModBlocks.DRAGON_BREATH_CAULDRON, PeacefulModFluids.DRAGON_BREATH, FluidConstants.BOTTLE, LeveledCauldronBlock.LEVEL);
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(DRAGON_EFFIGY, new DragonBreathCauldronBlock.FillFromEffigyBehavior());
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(Items.DRAGON_BREATH, new DragonBreathCauldronBlock.FillFromBottleBehavior());
        DragonBreathCauldronBlock.DRAGON_BREATH_CAULDRON_BEHAVIOR.map().put(Items.GLASS_BOTTLE, new DragonBreathCauldronBlock.DecrementFluidLevelBehavior());
        DragonBreathCauldronBlock.DRAGON_BREATH_CAULDRON_BEHAVIOR.map().put(Items.DRAGON_BREATH, new DragonBreathCauldronBlock.IncrementFluidLevelBehavior());
        
        // Replace the original immutable map with a mutable one and add custom items
        Map<Item, FireworkExplosionComponent.Type> fireworkExplosionTypeModifierMap = new HashMap<>(FireworkStarRecipe.TYPE_MODIFIER_MAP);
        fireworkExplosionTypeModifierMap.put(PeacefulModBlocks.BLAZE_PICKLE.asItem(), FireworkExplosionComponent.Type.LARGE_BALL);
        fireworkExplosionTypeModifierMap.put(PeacefulModBlocks.BREEZE_CORAL.asItem(), FireworkExplosionComponent.Type.BURST);
        FireworkStarRecipe.TYPE_MODIFIER_MAP = fireworkExplosionTypeModifierMap;
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PeacefulMod.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings);

		// Register the item.
		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}
}
