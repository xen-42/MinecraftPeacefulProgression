package xen42.peacefulitems.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

import org.jetbrains.annotations.Nullable;

public class EffigyAltarRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
	private static final int MAX_WIDTH_AND_HEIGHT = 3;
	private static final int MAX_WIDTH_END = 1;
	public static final String SPACE = " ";
	Identifier ROOT = Identifier.ofVanilla("recipes/root");
	private final RegistryEntryLookup<Item> registryLookup;
	private final Item output;
	private final int count;
	private final List<String> pattern = Lists.<String>newArrayList();
	private final Map<Character, Ingredient> inputs = Maps.<Character, Ingredient>newLinkedHashMap();
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<String, AdvancementCriterion<?>>();
	private OptionalInt cost = OptionalInt.empty();
	@Nullable
	private String group;

	private EffigyAltarRecipeJsonBuilder(RegistryEntryLookup<Item> registryLookup, ItemConvertible output, int count) {
		this.registryLookup = registryLookup;
		this.output = output.asItem();
		this.count = count;
	}

	public static EffigyAltarRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, ItemConvertible output) {
		return create(registryLookup, output, 1);
	}

	public static EffigyAltarRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, ItemConvertible output, int count) {
		return new EffigyAltarRecipeJsonBuilder(registryLookup, output, count);
	}

	public EffigyAltarRecipeJsonBuilder input(Character c, TagKey<Item> tag) {
		return this.input(c, Ingredient.fromTag(this.registryLookup.getOrThrow(tag)));
	}

	public EffigyAltarRecipeJsonBuilder input(Character c, ItemConvertible item) {
		return this.input(c, Ingredient.ofItem(item));
	}

	public EffigyAltarRecipeJsonBuilder input(Character c, Ingredient ingredient) {
		if (this.inputs.containsKey(c)) {
			throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
		} else if (c == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.inputs.put(c, ingredient);
			return this;
		}
	}

	public EffigyAltarRecipeJsonBuilder pattern(String patternStr1, String patternStr2, String patternStr3) {
		if (!this.pattern.isEmpty()) {
			throw new IllegalStateException("Already added pattern!");
		}
		else if (patternStr1.isEmpty() || patternStr2.isEmpty() || patternStr3.isEmpty()) {
			throw new IllegalArgumentException("Pattern must not be empty!");
		}
		else if (patternStr1.length() != MAX_WIDTH_AND_HEIGHT) {
			throw new IllegalArgumentException("Pattern #1 must have a width of 3!");
		}
		else if (patternStr2.length() != MAX_WIDTH_AND_HEIGHT) {
			throw new IllegalArgumentException("Pattern #2 must have a width of 3!");
		}
		else if (patternStr3.length() != MAX_WIDTH_END) {
			throw new IllegalArgumentException("Pattern #3 must have a width of 1!");
		}
		else if (patternStr1.contains(SPACE) || patternStr2.contains(SPACE) || patternStr3.contains(SPACE)) {
			throw new IllegalArgumentException("Pattern must not have empty spaces!");
		}
		else {
			this.pattern.add(patternStr1);
			this.pattern.add(patternStr2);
			this.pattern.add(patternStr3);
			return this;
		}
	}

	public EffigyAltarRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
		this.criteria.put(string, advancementCriterion);
		return this;
	}

	public EffigyAltarRecipeJsonBuilder cost(int cost) {
		if (cost > 0) {
			this.cost = OptionalInt.of(cost);
		} else {
			this.cost = OptionalInt.empty();
		}
		return this;
	}

	public EffigyAltarRecipeJsonBuilder group(@Nullable String string) {
		this.group = string;
		return this;
	}

	public Item getOutputItem() {
		return this.output;
	}

	public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
		EffigyAltarRecipe.RawRecipe rawRecipe = this.validate(recipeKey);
		Advancement.Builder builder = exporter.getAdvancementBuilder()
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey))
			.rewards(AdvancementRewards.Builder.recipe(recipeKey))
			.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
		this.criteria.forEach(builder::criterion);
		EffigyAltarRecipe recipe = new EffigyAltarRecipe(
			(String)Objects.requireNonNullElse(this.group, ""),
			rawRecipe,
			new ItemStack(this.output, this.count),
			this.cost
		);
		exporter.accept(recipeKey, recipe, builder.build(recipeKey.getValue().withPrefixedPath("recipes/effigy_altar/")));
	}

	private EffigyAltarRecipe.RawRecipe validate(RegistryKey<Recipe<?>> recipeKey) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeKey.getValue());
		} else {
			return EffigyAltarRecipe.RawRecipe.create(this.inputs, this.pattern);
		}
	}
	
	public void offerTo(RecipeExporter exporter) {
		this.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, getItemId(this.getOutputItem())));
	}

	public void offerTo(RecipeExporter exporter, String recipePath) {
		Identifier identifier = getItemId(this.getOutputItem());
		Identifier identifier2 = Identifier.of(PeacefulMod.MOD_ID, recipePath);
		if (identifier2.equals(identifier)) {
			throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
		} else {
			this.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, identifier2));
		}
	}

	public static Identifier getItemId(ItemConvertible item) {
		return Identifier.of(PeacefulMod.MOD_ID, Registries.ITEM.getId(item.asItem()).getPath());
	}
}
