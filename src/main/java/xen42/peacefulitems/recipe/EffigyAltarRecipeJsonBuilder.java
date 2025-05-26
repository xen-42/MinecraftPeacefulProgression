package xen42.peacefulitems.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.Map.Entry;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.ComplexRecipeJsonBuilder;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

import org.jetbrains.annotations.Nullable;

public class EffigyAltarRecipeJsonBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {
	private static final int MAX_WIDTH_AND_HEIGHT = 3;
	private static final int MAX_WIDTH_END = 1;
	public static final String SPACE = " ";
	Identifier ROOT = Identifier.of("minecraft","recipes/root");
	private final Item output;
	private final int count;
	private final List<String> pattern = Lists.<String>newArrayList();
	private final Map<Character, Ingredient> inputs = Maps.<Character, Ingredient>newLinkedHashMap();
	private final Map<String, CriterionConditions> criteria = new LinkedHashMap<String, CriterionConditions>();
	private OptionalInt cost = OptionalInt.empty();
	@Nullable
	private String group;

	private final RegistryEntryLookup<Item> registryLookup;

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
		return this.input(c, Ingredient.fromTag(tag));
	}

	public EffigyAltarRecipeJsonBuilder input(Character c, ItemConvertible item) {
		return this.input(c, Ingredient.ofItems(item));
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

	@Override
	public EffigyAltarRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
		this.criteria.put(name, conditions);
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

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		EffigyAltarRecipe.RawRecipe rawRecipe = this.validate(recipeId);
		Advancement.Builder builder = Advancement.Builder.createUntelemetered()
			.parent(ROOT)
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.criteriaMerger(CriterionMerger.OR);
		this.criteria.forEach(builder::criterion);
		EffigyAltarRecipe recipe = new EffigyAltarRecipe(
			recipeId,
			(String)Objects.requireNonNullElse(this.group, ""),
			rawRecipe,
			new ItemStack(this.output, this.count),
			this.cost
		);
		exporter.accept(new JsonProvider(
				recipeId,
				this.output,
				this.count,
				this.group == null ? "" : this.group,
				this.pattern,
				this.inputs,
				this.cost,
				recipeId.withPrefixedPath("recipes/effigy_altar/"),
				builder));
	}

	public void offerTo(Consumer<RecipeJsonProvider> exporter, RegistryKey<Recipe<?>> recipeKey) {
		offerTo(exporter, recipeKey.getValue());
	}

	private EffigyAltarRecipe.RawRecipe validate(Identifier recipeId) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		} else {
			return EffigyAltarRecipe.RawRecipe.create(this.inputs, this.pattern);
		}
	}
	
	public void offerTo(Consumer<RecipeJsonProvider> exporter) {
		this.offerTo(exporter, getItemId(this.getOutputItem()));
	}

	public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
		Identifier identifier = getItemId(this.getOutputItem());
		Identifier identifier2 = Identifier.of(PeacefulMod.MOD_ID, recipePath);
		if (identifier2.equals(identifier)) {
			throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
		} else {
			this.offerTo(exporter, identifier2);
		}
	}

	public static Identifier getItemId(ItemConvertible item) {
		return Identifier.of(PeacefulMod.MOD_ID, Registries.ITEM.getId(item.asItem()).getPath());
	}

	private static class JsonProvider implements RecipeJsonProvider {
		private final Identifier id;
		private final Item output;
		private final int resultCount;
		private final String group;
		private final List<String> pattern;
		private final Map<Character, Ingredient> inputs;
		private final OptionalInt cost;
		private final Identifier advancementId;
		private final Advancement.Builder advancementBuilder;

		protected JsonProvider(
				Identifier id,
				Item output,
				int resultCount,
				String group,
				List<String> pattern,
				Map<Character, Ingredient> inputs,
				OptionalInt cost,
				Identifier advancementId,
				Advancement.Builder advancementBuilder) {
			this.id = id;
			this.output = output;
			this.resultCount = resultCount;
			this.group = group;
			this.pattern = pattern;
			this.inputs = inputs;
			this.cost = cost;
			this.advancementId = advancementId;
			this.advancementBuilder = advancementBuilder;
		}

		@Override
		public void serialize(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}
			if (!this.cost.isEmpty()) {
				json.addProperty("cost", this.cost.getAsInt());
			}

			JsonArray pattern = new JsonArray();
			for (String string : this.pattern) {
				pattern.add(string);
			}
			json.add("pattern", pattern);

			JsonObject key = new JsonObject();
			for (Entry<Character, Ingredient> entry : this.inputs.entrySet()) {
				key.add(String.valueOf(entry.getKey()), ((Ingredient)entry.getValue()).toJson());
			}
			json.add("key", key);

			JsonObject result = new JsonObject();
			result.addProperty("item", Registries.ITEM.getId(this.output).toString());
			if (this.resultCount > 1) {
				result.addProperty("count", this.resultCount);
			}
			json.add("result", result);
		}

		@Override
		public Identifier getRecipeId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getSerializer() {
			return PeacefulMod.EFFIGY_ALTAR_RECIPE_SERIALIZER;
		}

		@Override
		public Identifier getAdvancementId() {
			return advancementId;
		}

		@Override
		public JsonObject toAdvancementJson() {
			return advancementBuilder.toJson();
		}
	}
}
