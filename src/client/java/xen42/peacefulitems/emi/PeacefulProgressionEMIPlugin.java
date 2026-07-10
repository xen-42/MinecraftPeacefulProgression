package xen42.peacefulitems.emi;

import static dev.emi.emi.api.recipe.VanillaEmiRecipeCategories.CRAFTING;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.raid.Raid;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.PeacefulModFluids;
import xen42.peacefulitems.PeacefulModItems;
import xen42.peacefulitems.PeacefulModTags;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;

public class PeacefulProgressionEMIPlugin implements EmiPlugin {

	private static EmiRecipeCategory createCategory(Identifier id, EmiStack icon, Comparator<EmiRecipe> sorter) {
		return new EmiRecipeCategory(
			id,
			icon,
			new EmiTexture( // simplified icon
					Identifier.of(PeacefulMod.MOD_ID, "textures/gui/emi/icon/" + id.getPath() + ".png"),
					0, 0, 16, 16, 16, 16, 16, 16
				),
			sorter
		);
	}

	public static final EmiStack ALTAR = EmiStack.of(PeacefulModBlocks.EFFIGY_ALTAR);
	public static final EmiRecipeCategory EFFIGY_CATEGORY = createCategory(PeacefulMod.EFFIGY_ALTAR_ID, 
			ALTAR, EmiRecipeSorting.compareOutputThenInput());

	public static final int BUCKET = 81_000;
	public static final int BOTTLE = 27_000;

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(EFFIGY_CATEGORY);
		registry.addWorkstation(EFFIGY_CATEGORY, ALTAR);
		registry.addRecipeHandler(PeacefulMod.EFFIGY_ALTAR_SCREEN_HANDLER, new EffigyAltarRecipeHandler());

		RecipeManager manager = registry.getRecipeManager();

		for (EffigyAltarRecipe recipe : manager.listAllOfType(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE)) {
			addRecipeSafe(registry, () -> new EffigyAltarEmiRecipe(recipe), recipe);
		}

		addRecipeSafe(registry, () -> {
			Item fossilOre = PeacefulModBlocks.FOSSIL_ORE.asItem();
			EmiIngredient fossilOres = getPreferredTag(PeacefulModTags.ItemTags.FOSSIL_ORES, stack(fossilOre));
			EmiIngredient brush = damagedTool(stack(Items.BRUSH), 1);
			return worldRecipe(
				syntheticWorldId(fossilOre),
				fossilOres,
				brush,
				stack(Items.BONE)
			);
		});

		addDragonBreathCauldronRecipes(registry);

		addAnvilRepairRecipe(registry, PeacefulModItems.CAPE, PeacefulModItems.BAT_WING);

		addInformationRecipes(registry);
	}

	private static EmiIngredient getPreferredTag(TagKey<Item> candidate, EmiIngredient fallback) {
		EmiIngredient potential = EmiIngredient.of(candidate);
		if (!potential.isEmpty()) {
			return potential;
		}
		return fallback;
	}

	private static EmiIngredient getPreferredTag(List<TagKey<Item>> candidates, EmiIngredient fallback) {
		for (TagKey<Item> key : candidates) {
			EmiIngredient potential = EmiIngredient.of(key);
			if (!potential.isEmpty()) {
				return potential;
			}
		}
		return fallback;
	}

	private static String subId(Item item) {
		Identifier id = item.getRegistryEntry().registryKey().getValue();
		return id.getNamespace() + "/" + id.getPath();
	}

	private static Identifier synthetic(String type, String name) {
		String path = "/" + type + "/" + name;
		//PeacefulMod.LOGGER.info(path);
		return Identifier.of(PeacefulMod.MOD_ID, path);
	}

	private static Identifier syntheticWorldId(Item item) {
		return synthetic("world/unique", subId(item));
	}

	private static Identifier syntheticWorldId(Item item, String suffix) {
		return synthetic("world/unique", subId(item) + "_" + suffix);
	}

	private static EmiStack stack(Item item) {
		return EmiStack.of(item);
	}

	private static EmiStack stack(Fluid fluid, long amount) {
		return EmiStack.of(fluid, amount);
	}

	private static EmiStack withRemainder(EmiStack stack, EmiStack remainder) {
		return stack.copy().setRemainder(remainder);
	}

	private static EmiIngredient damagedTool(EmiIngredient tool, int damage) {
		for (EmiStack stack : tool.getEmiStacks()) {
			ItemStack is = stack.getItemStack().copy();
			is.setDamage(damage);
			stack.setRemainder(EmiStack.of(is));
		}
		return tool;
	}

	private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
		try {
			registry.addRecipe(supplier.get());
		} catch (Throwable e) {
			PeacefulMod.LOGGER.warn("Exception thrown when parsing recipe (no ID available)", e);
		}
	}

	private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, Recipe<?> recipe) {
		try {
			registry.addRecipe(supplier.get());
		} catch (Throwable e) {
			PeacefulMod.LOGGER.warn("Exception thrown when parsing recipe " + recipe.getId().toString(), e);
		}
	}

	private static void addAnvilRepairRecipe(EmiRegistry registry, Item tool, Item material) {
		addRecipeSafe(registry, () -> new EmiAnvilRecipe(
			EmiStack.of(tool),
			EmiStack.of(material),
			synthetic("anvil/repairing/material", subId(tool) + "/" + subId(material))
		));
	}

	private static EmiRecipe worldRecipe(
		Identifier id,
		EmiIngredient leftInput,
		EmiIngredient rightInput,
		EmiStack output
	) {
		return EmiWorldInteractionRecipe.builder()
			.id(id)
			.leftInput(leftInput)
			.rightInput(rightInput, true)
			.output(output)
			.build();
	}

	private static EmiRecipe worldRecipe(
		Identifier id,
		EmiIngredient leftInput,
		EmiIngredient rightInput,
		EmiIngredient extraRightInput,
		EmiStack output
	) {
		return EmiWorldInteractionRecipe.builder()
			.id(id)
			.leftInput(leftInput)
			.rightInput(rightInput, true)
			.rightInput(extraRightInput, false)
			.output(output)
			.build();
	}

	private static void addDragonBreathCauldronRecipes(EmiRegistry registry) {
		Fluid fluid = PeacefulModFluids.DRAGON_BREATH;
		Item effigy = PeacefulModItems.DRAGON_EFFIGY;
		Item fluidBottle = Items.DRAGON_BREATH;

		EmiStack cauldron = stack(Items.CAULDRON);
		EmiStack bottle = stack(Items.GLASS_BOTTLE);

		EmiStack fluidFull = stack(fluid, BUCKET);
		EmiStack fluidThird = stack(fluid, BOTTLE);

		EmiStack effigyStack = stack(effigy);
		EmiStack filledBottle = stack(fluidBottle);
		EmiStack filledBottleRemainder = withRemainder(filledBottle, bottle);

		addRecipeSafe(registry, () -> worldRecipe(
			syntheticWorldId(effigy, "empty"),
			effigyStack,
			cauldron,
			fluidFull
		));

		addRecipeSafe(registry, () -> worldRecipe(
			syntheticWorldId(fluidBottle, "fill"),
			bottle,
			cauldron,
			fluidThird,
			filledBottle
		));

		addRecipeSafe(registry, () -> worldRecipe(
			syntheticWorldId(fluidBottle, "empty"),
			filledBottleRemainder,
			cauldron,
			fluidThird
		));
	}

	private static void addInformationRecipes(EmiRegistry registry) {
		PeacefulMod.LOGGER.info("Registering EMI information");
		DynamicRegistryManager registryManager = MinecraftClient.getInstance().world.getRegistryManager();

		// Effigy Altar
		addInfo(registry, PeacefulModBlocks.EFFIGY_ALTAR.asItem(), "dungeon");

		// Effigy
		Text dragonEffigy = Text.translatable(Items.DRAGON_BREATH.getTranslationKey() + ".dragon_effigy_information");
		addInfo(registry, Items.DRAGON_BREATH, dragonEffigy);
		addInfo(registry, PeacefulModFluids.DRAGON_BREATH, dragonEffigy);
		addInfo(registry, Items.NETHER_STAR, "wither_effigy");

		// Elder Effigy
		Text guardianEffigy = Text.translatable(PeacefulModItems.GUARDIAN_EFFIGY.getTranslationKey() + ".drop_information");
		addInfo(registry, Items.COOKED_COD, guardianEffigy);
		addInfo(registry, Items.PRISMARINE_CRYSTALS, guardianEffigy);
		addInfo(registry, Items.PRISMARINE_SHARD, guardianEffigy);

		// Raid Effigy
		Text raidEffigy = Text.translatable(PeacefulModItems.RAID_EFFIGY.getTranslationKey() + ".drop_information");
		addInfo(registry, Raid.getOminousBanner().getItem(), raidEffigy);
		addInfo(registry, Items.CROSSBOW, raidEffigy);
		addInfo(registry, Items.SADDLE, raidEffigy);
		addInfo(registry, Items.IRON_AXE, raidEffigy);
		addInfo(registry, Items.EMERALD, raidEffigy);
		addInfo(registry, Blocks.REDSTONE_WIRE.asItem(), raidEffigy);
		addInfo(registry, Items.GUNPOWDER, raidEffigy);
		addInfo(registry, Items.STICK, raidEffigy);
		addInfo(registry, Items.GLOWSTONE_DUST, raidEffigy);
		addInfo(registry, Items.SUGAR, raidEffigy);
		addInfo(registry, Items.SPIDER_EYE, raidEffigy);
		addInfo(registry, Items.GLASS_BOTTLE, raidEffigy);

		// Enderclam
		Text clam = Text.translatable(PeacefulMod.END_CLAM_ENTITY.getTranslationKey() + ".drop_information");
		addInfo(registry, Items.ENDER_PEARL, clam);
		addInfo(registry, PeacefulModItems.SULPHUR, clam);
		addInfo(registry, Items.GOLD_NUGGET, clam);

		// Panda
		addInfo(registry, Items.SLIME_BALL, "sneeze");

		// Frog
		Text froglight = Text.translatable(Items.MAGMA_CREAM.getTranslationKey() + ".frog_information");
		addInfo(registry, Items.MAGMA_CREAM, froglight);
		addInfo(registry, Blocks.OCHRE_FROGLIGHT.asItem(), froglight);
		addInfo(registry, Blocks.PEARLESCENT_FROGLIGHT.asItem(), froglight);
		addInfo(registry, Blocks.VERDANT_FROGLIGHT.asItem(), froglight);

		// Wisp
		addInfo(registry, Items.GHAST_TEAR, "wisp");

		// Bat
		addInfo(registry, PeacefulModItems.GUANO, "bat");

		// Sniffer
		Text soulSand = Text.translatable(Blocks.SOUL_SAND.getTranslationKey() + ".sniffer_information");
		addInfo(registry, Blocks.SOUL_SAND.asItem(), soulSand);
		addInfo(registry, Blocks.SOUL_SOIL.asItem(), soulSand);
		addInfo(registry, Items.WITHER_SKELETON_SKULL, soulSand);
		addInfo(registry, Items.SKELETON_SKULL, soulSand);
		addInfo(registry, Items.WITHER_ROSE, soulSand);
		addInfo(registry, PeacefulModBlocks.BLAZE_PICKLE.asItem(), soulSand);
		addInfo(registry, Items.BONE, soulSand);

		Text sand = Text.translatable(Blocks.SAND.getTranslationKey() + ".sniffer_information");
		addInfo(registry, Blocks.SAND.asItem(), sand);
		addInfo(registry, Items.NAUTILUS_SHELL, sand);
		addInfo(registry, Items.PRISMARINE_SHARD, sand);
		addInfo(registry, Items.PRISMARINE_CRYSTALS, sand);

		Text gravel = Text.translatable(Blocks.GRAVEL.getTranslationKey() + ".sniffer_information");
		addInfo(registry, Blocks.GRAVEL.asItem(), gravel);
		addInfo(registry, Items.FLINT, gravel);

		// Fossil
		Text brush = Text.translatable(PeacefulModBlocks.FOSSIL_ORE.getTranslationKey() + ".brush_information");
		addInfo(registry, Items.BRUSH, brush);
		addInfo(registry, PeacefulModBlocks.FOSSIL_ORE.asItem(), brush);
		addInfo(registry, PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE.asItem(), brush);
		addInfo(registry, PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE.asItem(), brush);

		// Wandering Trader
		Text wanderingTraderKey = Text.translatable(EntityType.WANDERING_TRADER.getTranslationKey() + ".key_head_information");
		addInfo(registry, Items.ZOMBIE_HEAD, wanderingTraderKey);
		addInfo(registry, Items.CREEPER_HEAD, wanderingTraderKey);
		addInfo(registry, Items.PIGLIN_HEAD, wanderingTraderKey);

		// Dungeon Chest
		Text headKey = Text.translatable(Items.CREEPER_HEAD.getTranslationKey() + ".dungeon_chest_information");
		addInfo(registry, Items.CREEPER_HEAD, headKey);
		addInfo(registry, Items.PIGLIN_HEAD, headKey);
		addInfo(registry, Items.ZOMBIE_HEAD, headKey);
	}

	private static void addInfo(EmiRegistry registry, Item item, String prefix) {
		addInfo(
			registry,
			item,
			Text.translatable(item.getTranslationKey() + "." + prefix + "_information"),
			prefix
		);
	}

	private static void addInfo(EmiRegistry registry, Item item, Text information) {
		addInfo(registry, item, information, translationKey(information));
	}

	private static void addInfo(EmiRegistry registry, Item item, Text information, String suffix) {
		addRecipeSafe(registry, () -> new EmiInfoRecipe(
			List.of(stack(item)),
			List.of(information),
			synthetic("info", subId(item) + "/" + cleanInfoSuffix(item, suffix))
		));
	}

	private static void addInfo(EmiRegistry registry, Fluid fluid, Text information) {
		Identifier id = fluid.getRegistryEntry().registryKey().getValue();

		addRecipeSafe(registry, () -> new EmiInfoRecipe(
			List.of(stack(fluid, BUCKET)),
			List.of(information),
			synthetic("info", id.getNamespace() + "/" + id.getPath() + "/" + cleanIdPart(translationKey(information)))
		));
	}

	private static String translationKey(Text text) {
		if (text.getContent() instanceof net.minecraft.text.TranslatableTextContent translatable) {
			return translatable.getKey();
		}
		return "info";
	}

	private static String cleanInfoSuffix(Item item, String suffix) {
		String itemId = subId(item);
		String cleaned = cleanIdPart(suffix);

		if (cleaned.startsWith(itemId + "/")) {
			return cleaned.substring(itemId.length() + 1);
		}

		return cleaned;
	}

	private static String cleanIdPart(String value) {
		return value
			.replace(':', '/')
			.replace('.', '/')
			.replace("/item/", "/")
			.replace("/block/", "/")
			.replace("/entity/", "/")
			.replace("item/", "")
			.replace("block/", "")
			.replace("entity/", "");
	}
}