package xen42.peacefulitems;


import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffers.SellMapFactory;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

public class PeacefulModVillagers {
    public static final RegistryKey<PointOfInterestType> JUKEBOX_KEY = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(PeacefulMod.MOD_ID, "jukebox_poi"));
    public static final PointOfInterestType JUKEBOX_POI = registerNewPOI("jukebox_poi", Blocks.JUKEBOX);
    public static final RegistryKey<VillagerProfession> DJ_VILLAGER_KEY = RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, Identifier.of(PeacefulMod.MOD_ID, "dj"));
    public static final VillagerProfession DJ_VILLAGER = registerNewProfession(DJ_VILLAGER_KEY, JUKEBOX_KEY, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);

    public static VillagerProfession registerNewProfession(RegistryKey<VillagerProfession> key, RegistryKey<PointOfInterestType> poi, SoundEvent sound) {
        var profession = new VillagerProfession(
                Text.translatable("entity." + key.getValue().getNamespace() + ".villager." + key.getValue().getPath()),
                entry -> entry.matchesKey(poi),
                entry -> entry.matchesKey(poi),
                ImmutableSet.of(),
                ImmutableSet.of(),
                sound
            );
        return Registry.register(Registries.VILLAGER_PROFESSION, key.getValue(), profession);
    }

    public static PointOfInterestType registerNewPOI(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(PeacefulMod.MOD_ID, name), 1, 1, block);
    }

    
    public static void initialize() {
		RegistryEntry<PointOfInterestType> poiTypeEntry = Registries.POINT_OF_INTEREST_TYPE.getOrThrow(PointOfInterestTypes.LEATHERWORKER);
		for (BlockState state : PeacefulModBlocks.DRAGON_BREATH_CAULDRON.getStateManager().getStates())
			PointOfInterestTypes.POI_STATES_TO_TYPE.put(state, poiTypeEntry);
		
		// 0.05 is a "low" price modifier. High is 0.2
		// I think level 1 is Novice, level 5 is Master
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 1),
				new ItemStack(Items.SPIDER_EYE, 3), 12, 1, 0.05f));
		});

		// Identical trade to wheat
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(PeacefulModItems.FLAX, 20),
				new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
		});

		// Identical trade to rabbit hide
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.LEATHERWORKER, 3, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(PeacefulModItems.BAT_WING, 9),
				new ItemStack(Items.EMERALD, 1), 12, 20, 0.05f));
		});

        // CUSTOM VILLAGER
        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER_KEY, 1, factories -> {
			// Same as librarian
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.PAPER, 24),
				new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
            factories.add((entity, random) -> new TradeOffer(
                new TradedItem(Items.STRING, 12),
                new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER_KEY, 2, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 2),
				new ItemStack(Blocks.NOTE_BLOCK, 1), 12, 5, 0.05f));
            factories.add((entity, random) -> RandomHorn(entity, random));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER_KEY, 3, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 6),
				new ItemStack(Blocks.JUKEBOX, 1), 12, 20, 0.05f));
            factories.add((entity, random) -> RandomDisc(entity, random));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER_KEY, 4, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 12),
				new ItemStack(Blocks.BELL, 1), 12, 20, 0.05f));
            factories.add((entity, random) -> RandomDisc(entity, random));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER_KEY, 5, factories -> {
            factories.add((entity, random) -> RandomHorn(entity, random));
            factories.add((entity, random) -> RandomDisc(entity, random));
		});

		TradeOfferHelper.registerWanderingTraderOffers(builder -> {
			builder.addOffersToPool(
				TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL, 
				new TradeOfferFactory(new TradeOffer(
					new TradedItem(Items.EMERALD, 5),
					new ItemStack(Items.TRIAL_KEY, 1), 12, 20, 0.05f)
				),
				new TradeOfferFactory(new TradeOffer(
					new TradedItem(Items.EMERALD, 10),
					new ItemStack(Items.OMINOUS_TRIAL_KEY, 1), 12, 20, 0.05f)
				),
				new TradeOfferFactory(new TradeOffer(
					new TradedItem(Items.EMERALD, 5),
					new ItemStack(Items.PIGLIN_HEAD, 1), 12, 20, 0.05f)
				),
				new TradeOfferFactory(new TradeOffer(
					new TradedItem(Items.EMERALD, 5),
					new ItemStack(Items.CREEPER_HEAD, 1), 12, 20, 0.05f)
				),
				new TradeOfferFactory(new TradeOffer(
					new TradedItem(Items.EMERALD, 5),
					new ItemStack(Items.ZOMBIE_HEAD, 1), 12, 20, 0.05f)
				)
			);
		});
		
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3, factories -> {
			// Map to altar
			factories.add((entity, random) -> SellMap(entity, random, 10,
				PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, MapDecorationTypes.TARGET_X, 4, 15));
			
			// Map to trail ruin
			factories.add((entity, random) -> SellMap(entity, random, 10,
				PeacefulModTags.StructureTags.TRAIL_RUINS, MapDecorationTypes.TARGET_X, 12, 10));
			
			// Map to ocean ruin
			factories.add((entity, random) -> SellMap(entity, random, 10,
				StructureTags.OCEAN_RUIN, MapDecorationTypes.TARGET_X, 12, 10));
		});
    }
    
    private static TradeOffer SellMap(Entity entity, Random random, int price, TagKey<Structure> structure, RegistryEntry<MapDecorationType> decoration, int maxUses, int experience) {
    	return new SellMapFactory(price, structure, 
    		"filled_map." + structure.id().getNamespace() + "." + structure.id().getPath(),
    		decoration, maxUses, experience).create(entity, random);
    }

    private static TradeOffer RandomDisc(Entity entity, Random random) {
        var item = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ITEM).getRandomEntry(ItemTags.CREEPER_DROP_MUSIC_DISCS, random).get();
        return new TradeOffer(
            new TradedItem(Items.EMERALD, 24),
            new ItemStack(item, 1), 1, 30, 0.05f);
    }

	private static TradeOffer RandomHorn(Entity entity, Random random) {
		var tag = random.nextBoolean() ? InstrumentTags.SCREAMING_GOAT_HORNS : InstrumentTags.REGULAR_GOAT_HORNS;
		var horn = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.INSTRUMENT).getRandomEntry(tag, random)
			.map(registryEntry -> GoatHornItem.getStackForInstrument(Items.GOAT_HORN, registryEntry))
			.orElseGet(() -> new ItemStack(Items.GOAT_HORN));
		return new TradeOffer(
			new TradedItem(Items.EMERALD, 6),
			horn, 2, 5, 0.05f);
	}

	private static class TradeOfferFactory implements Factory {
		private TradeOffer _tradeOffer;

		public TradeOfferFactory(TradeOffer tradeOffer) {
			_tradeOffer = tradeOffer;
		}

		@Override
		public TradeOffer create(Entity arg0, Random arg1) {
			return _tradeOffer.copy();
		}
	}
}
