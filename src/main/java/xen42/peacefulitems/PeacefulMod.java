package xen42.peacefulitems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeacefulMod implements ModInitializer {
	public static final String MOD_ID = "peaceful-items";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<PlacedFeature> FOSSIL_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"fossil_ore"));
	public static final RegistryKey<PlacedFeature> NETHER_FOSSIL_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"nether_fossil_ore"));
	public static final RegistryKey<PlacedFeature> SULPHUR_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"sulphur_ore"));

	public static final TrackedData<Integer> BAT_BREEDING_TICKS = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Boolean> BAT_IS_BABY = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Integer> BAT_BREEDING_COOLDOWN = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static int BatGrowUpTicks = 5 * 60 * 20; // Normal mobs its 20 minutes but I feel like bats can grow up fast maybe idk!
	public static int BatBreedingCooldown = 5 * 60 * 20;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Loading Peaceful Mod!");

		PeacefulModItems.initialize();
		PeacefulModBlocks.initialize();

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, FOSSIL_ORE_PLACED_KEY);
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, NETHER_FOSSIL_ORE_PLACED_KEY);
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_ORE_PLACED_KEY);

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
 
	}
}