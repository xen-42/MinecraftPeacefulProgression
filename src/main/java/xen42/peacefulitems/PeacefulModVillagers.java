package xen42.peacefulitems;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffers.SellMapFactory;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.poi.PointOfInterestType;

public class PeacefulModVillagers {
    public static final RegistryKey<PointOfInterestType> JUKEBOX_KEY = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(PeacefulMod.MOD_ID, "jukebox_poi"));
    public static final PointOfInterestType JUKEBOX_POI = registerNewPOI("jukebox_poi", Blocks.JUKEBOX);
    public static final VillagerProfession DJ_VILLAGER = registerNewProfession("dj", JUKEBOX_KEY, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);

    public static VillagerProfession registerNewProfession(String name, RegistryKey<PointOfInterestType> poi, SoundEvent sound) {
        var profession = new VillagerProfession(name, entry -> entry.matchesKey(poi), entry -> entry.matchesKey(poi), ImmutableSet.of(), ImmutableSet.of(), sound);
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(PeacefulMod.MOD_ID, name), profession);
    }

    public static PointOfInterestType registerNewPOI(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(PeacefulMod.MOD_ID, name), 1, 1, block);
    }

    
    public static void initialize() {
		// 0.05 is a "low" price modifier. High is 0.2
		// I think level 1 is Novice, level 5 is Master
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 1),
				new ItemStack(Items.SPIDER_EYE, 3), 12, 1, 0.05f));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 3, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 5),
				new ItemStack(Blocks.ZOMBIE_HEAD, 1), 12, 30, 0.05f));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 4, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 5),
				new ItemStack(Blocks.CREEPER_HEAD, 1), 12, 30, 0.05f));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, factories -> {
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 5),
				new ItemStack(Blocks.PIGLIN_HEAD, 1), 12, 30, 0.05f));
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
        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER, 1, factories -> {
			// Same as librarian
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.PAPER, 24),
				new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
            factories.add((entity, random) -> new TradeOffer(
                new TradedItem(Items.STRING, 12),
                new ItemStack(Items.EMERALD, 1), 16, 2, 0.05f));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER, 2, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 2),
				new ItemStack(Blocks.NOTE_BLOCK, 1), 12, 5, 0.05f));
            factories.add((entity, random) -> RandomHorn(entity, random));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER, 3, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 6),
				new ItemStack(Blocks.JUKEBOX, 1), 12, 20, 0.05f));
            factories.add((entity, random) -> RandomDisc(entity, random));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER, 4, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 12),
				new ItemStack(Blocks.BELL, 1), 12, 20, 0.05f));
            factories.add((entity, random) -> RandomDisc(entity, random));
		});

        TradeOfferHelper.registerVillagerOffers(DJ_VILLAGER, 5, factories -> {
            factories.add((entity, random) -> RandomHorn(entity, random));
            factories.add((entity, random) -> RandomDisc(entity, random));
		});

		TradeOfferHelper.registerWanderingTraderOffers(0, factories -> {
            factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 5),
				new ItemStack(Items.TRIAL_KEY, 1), 12, 20, 0.05f));
			factories.add((entity, random) -> new TradeOffer(
				new TradedItem(Items.EMERALD, 10),
				new ItemStack(Items.OMINOUS_TRIAL_KEY, 1), 12, 20, 0.05f));
		});
		
		// Map to altar
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3, factories -> {
			factories.add((entity, random) -> new SellMapFactory(10, PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, 
			"filled_map.peaceful-items:effigy_altar_dungeon", MapDecorationTypes.TARGET_X, 4, 15).create(entity, random));
		});
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
}
