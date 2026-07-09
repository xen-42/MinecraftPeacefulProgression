package xen42.peacefulitems.emi;

import static dev.emi.emi.api.recipe.VanillaEmiRecipeCategories.CRAFTING;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
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
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
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

		for (RecipeEntry<EffigyAltarRecipe> recipe : manager.listAllOfType(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE)) {
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
		return Identifier.of("emi", "/" + type + "/" + name);
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

	private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, RecipeEntry<?> recipe) {
		try {
			registry.addRecipe(supplier.get());
		} catch (Throwable e) {
			PeacefulMod.LOGGER.warn("Exception thrown when parsing recipe " + recipe.id().toString(), e);
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
}