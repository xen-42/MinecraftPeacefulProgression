package xen42.peacefulitems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.structure.Structure;
import xen42.peacefulitems.entities.EndClamEntity;
import xen42.peacefulitems.entities.GhastlingEntity;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;
import xen42.peacefulitems.recipe.EffigyAltarRecipeDisplay;
import xen42.peacefulitems.screen.EffigyAltarScreenHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeacefulMod implements ModInitializer {
	public static final String MOD_ID = "peaceful-items";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final RegistryKey<Structure> EFFIGY_ALTAR_DUNGEON_KEY = RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(MOD_ID, "effigy_altar_dungeon"));

	public static final RegistryKey<RecipeType<?>> EFFIGY_ALTAR_RECIPE_TYPE_KEY = RegistryKey.of(RegistryKeys.RECIPE_TYPE, Identifier.of(MOD_ID, "effigy_altar"));
	public static final RecipeType<EffigyAltarRecipe> EFFIGY_ALTAR_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, Identifier.of(MOD_ID, "effigy_altar"), new RecipeType<EffigyAltarRecipe>() {
		public String toString() {
			return "effigy_altar";
		}
	});
	public static final RecipeSerializer<EffigyAltarRecipe> EFFIGY_ALTAR_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(MOD_ID, "effigy_altar"), new EffigyAltarRecipe.Serializer());
	public static final RecipeDisplay.Serializer<EffigyAltarRecipeDisplay> EFFIGY_ALTAR_RECIPE_DISPLAY = Registry.register(Registries.RECIPE_DISPLAY, Identifier.of(MOD_ID, "effigy_altar"), EffigyAltarRecipeDisplay.SERIALIZER);
	public static final RecipeBookCategory EFFIGY_ALTAR_RECIPE_BOOK_CATEGORY = Registry.register(Registries.RECIPE_BOOK_CATEGORY, Identifier.of(MOD_ID, "effigy_altar"), new RecipeBookCategory() {
		public String toString() {
			return "EFFIGY_ALTAR";
		}
	});
	public static final RegistryKey<PlacedFeature> FOSSIL_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"fossil_ore"));
	public static final RegistryKey<PlacedFeature> NETHER_FOSSIL_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"nether_fossil_ore"));
	public static final RegistryKey<PlacedFeature> SULPHUR_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"sulphur_ore"));
	public static final RegistryKey<PlacedFeature> SULPHUR_CLUSTER_FLOOR_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"sulphur_cluster_patch_floor"));
	public static final RegistryKey<PlacedFeature> SULPHUR_CLUSTER_CEILING_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"sulphur_cluster_patch_ceiling"));
	public static final RegistryKey<PlacedFeature> FLAX_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID,"flax"));

	public static final TrackedData<Integer> BAT_BREEDING_TICKS = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Boolean> BAT_IS_BABY = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Integer> BAT_BREEDING_COOLDOWN = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static int BatGrowUpTicks = 5 * 60 * 20; // Normal mobs its 20 minutes but I feel like bats can grow up fast maybe idk!
	public static int BatBreedingCooldown = 5 * 60 * 20;

	public static final GameRules.Key<BooleanRule> DISABLE_HUNGER_PEACEFUL =
		GameRuleRegistry.register("disableHungerPeaceful", Category.PLAYER, GameRuleFactory.createBooleanRule(false));

	public static final RegistryKey<EntityType<?>> GHASTLING_ENTITY_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID,"ghastling"));
	public static final EntityType<GhastlingEntity> GHASTLING_ENTITY = Registry.register(
		Registries.ENTITY_TYPE, 
		Identifier.of(MOD_ID, "ghastling"), 
		EntityType.Builder.create(GhastlingEntity::new, SpawnGroup.AMBIENT).dimensions(0.5f, 1.5f).build(GHASTLING_ENTITY_KEY));

	public static final RegistryKey<EntityType<?>> END_CLAM_ENTITY_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID,"end_clam"));
	public static final EntityType<EndClamEntity> END_CLAM_ENTITY = Registry.register(
		Registries.ENTITY_TYPE, 
		Identifier.of(MOD_ID, "end_clam"), 
		EntityType.Builder.create(EndClamEntity::new, SpawnGroup.AMBIENT).dimensions(0.5f, 0.3f).build(END_CLAM_ENTITY_KEY));

	public static final Identifier EFFIGY_PARTICLE_PAYLOAD = Identifier.of(MOD_ID, "effigy_particle_payload");

	public static final ScreenHandlerType<EffigyAltarScreenHandler> EFFIGY_ALTAR_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,
		"effigy_altar", new ScreenHandlerType<EffigyAltarScreenHandler>(EffigyAltarScreenHandler::new, null));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Loading Peaceful Mod!");

		PeacefulModItems.initialize();
		PeacefulModBlocks.initialize();
		PeacefulModVillagers.initialize();

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, FOSSIL_ORE_PLACED_KEY);
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, NETHER_FOSSIL_ORE_PLACED_KEY);
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_ORE_PLACED_KEY); 
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_CLUSTER_CEILING_PLACED_KEY); 
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, SULPHUR_CLUSTER_FLOOR_PLACED_KEY); 
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.MEADOW, BiomeKeys.WINDSWEPT_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, FLAX_PLACED_KEY);

		FabricDefaultAttributeRegistry.register(GHASTLING_ENTITY, GhastlingEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(END_CLAM_ENTITY, EndClamEntity.createMobAttributes());
		
		// I don't know why but the SpawnGroup.CREATURES group had them never spawning
		// Ghastling further lowers its spawn area to fortresses in its own class
		var ghastlingBiomes = BiomeSelectors.foundInTheNether();
		var clamBiomes = BiomeSelectors.includeByKey(BiomeKeys.WARPED_FOREST).or(BiomeSelectors.foundInTheEnd());

		BiomeModifications.addSpawn(ghastlingBiomes, SpawnGroup.AMBIENT, GHASTLING_ENTITY, 100, 2, 3);
		SpawnRestriction.register(GHASTLING_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhastlingEntity::isValidSpawn);

		BiomeModifications.addSpawn(clamBiomes, SpawnGroup.AMBIENT, END_CLAM_ENTITY, 100, 1, 1);
		SpawnRestriction.register(END_CLAM_ENTITY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndClamEntity::isValidSpawn);

		PayloadTypeRegistry.playS2C().register(EffigyParticlePayload.ID, EffigyParticlePayload.CODEC);
	}
}