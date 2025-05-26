package xen42.peacefulitems.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.encoding.VarInts;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.PeacefulModItems;

public class EffigyAltarRecipe implements Recipe<EffigyAltarRecipeInput> {
	final RawRecipe raw;
	public final ItemStack result;
	final OptionalInt cost;
	final String group;

	public EffigyAltarRecipe(String group, RawRecipe raw, ItemStack result) {
		this(group, raw, result, OptionalInt.empty());
	}

	public EffigyAltarRecipe(String group, RawRecipe raw, ItemStack result, Optional<Integer> cost) {
		this(group, raw, result, cost.stream().mapToInt(i -> i).findFirst());
	}

	public EffigyAltarRecipe(String group, RawRecipe raw, ItemStack result, OptionalInt cost) {
		this.group = group;
		this.raw = raw;
		this.result = result;
		this.cost = cost;
	}

	@Override
	public RecipeType<EffigyAltarRecipe> getType() {
		return PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE;
	}

	@Override
	public RecipeSerializer<? extends EffigyAltarRecipe> getSerializer() {
		return PeacefulMod.EFFIGY_ALTAR_RECIPE_SERIALIZER;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return false;
	}

	@Override
	public boolean showNotification() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		DefaultedList<Ingredient> defaultedList = this.getIngredients();
		return defaultedList.isEmpty()
			|| defaultedList.stream().filter(ingredient -> !ingredient.isEmpty()).anyMatch(ingredient -> ingredient.getMatchingStacks().length == 0);
	}

	public DefaultedList<ItemStack> getRecipeRemainders(EffigyAltarRecipeInput input) {
		return collectRecipeRemainders(input);
	}

	public static DefaultedList<ItemStack> collectRecipeRemainders(EffigyAltarRecipeInput input) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < defaultedList.size(); i++) {
			defaultedList.set(i, input.getStackInSlot(i));
		}

		return defaultedList;
	}

	@Override
	@VisibleForTesting
	public DefaultedList<Ingredient> getIngredients() {
		return this.raw.getIngredients();
	}

	public boolean matches(EffigyAltarRecipeInput input, World world) {
		return this.raw.matches(input);
	}

	public ItemStack craft(EffigyAltarRecipeInput input, DynamicRegistryManager registryManager) {
		return this.result.copy();
	}

	public ItemStack result() {
		return this.result;
	}
	
	public OptionalInt getCost() {
		return cost;
	}
	
	public int getCostOrDefault() {
		return getCost().orElse(5);
	}
	
	public Optional<Integer> getBoxedCost() {
		return cost.stream().boxed().findFirst();
	}

	public static class Serializer implements RecipeSerializer<EffigyAltarRecipe> {
		private static final Codec<Item> ITEM_CODEC = Codecs.validate(
			Registries.ITEM.getCodec(), item -> item == Items.AIR ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(item)
		);
		public static final Codec<ItemStack> RECIPE_RESULT_CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ITEM_CODEC.fieldOf("item").forGetter(ItemStack::getItem),
					Codecs.createStrictOptionalFieldCodec(Codecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount)
				)
				.apply(instance, ItemStack::new)
			);
		public static final Codec<EffigyAltarRecipe> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
					RawRecipe.CODEC.forGetter(recipe -> recipe.raw),
					RECIPE_RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					Codec.INT.optionalFieldOf("cost").forGetter(EffigyAltarRecipe::getBoxedCost)
				)
				.apply(instance, EffigyAltarRecipe::new)
		);

		@Override
		public Codec<EffigyAltarRecipe> codec() {
			return CODEC;
		}

		public EffigyAltarRecipe read(PacketByteBuf buf) {
			String string = buf.readString();
			RawRecipe rawRecipe = RawRecipe.readFromBuf(buf);
			ItemStack result = buf.readItemStack();
			var value = buf.readVarInt();
			OptionalInt cost = value == 0 ? OptionalInt.empty() : OptionalInt.of(value - 1);
			return new EffigyAltarRecipe(string, rawRecipe, result, cost);
		}

		public void write(PacketByteBuf buf, EffigyAltarRecipe recipe) {
			buf.writeString(recipe.group);
			recipe.raw.writeToBuf(buf);
			buf.writeItemStack(recipe.result());
			buf.writeVarInt(recipe.cost.isPresent() ? recipe.cost.getAsInt() + 1 : 0);
		}
	}
	

	public static final class RawRecipe {
		private static final int MAX_WIDTH_AND_HEIGHT = 3;
		private static final int MAX_WIDTH_END = 1;
		public static final char SPACE = ' ';
		public static final MapCodec<RawRecipe> CODEC = RawRecipe.Data.CODEC
			.flatXmap(
				RawRecipe::fromData,
				recipe -> (DataResult<RawRecipe.Data>)recipe.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe"))
			);

		public void writeToBuf(PacketByteBuf buf) {
			for (Ingredient ingredient : this.ingredients) {
				ingredient.write(buf);
			}
		}

		public static RawRecipe readFromBuf(PacketByteBuf buf) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize((MAX_WIDTH_AND_HEIGHT * MAX_WIDTH_AND_HEIGHT) - MAX_WIDTH_END, Ingredient.EMPTY);
			defaultedList.replaceAll(ingredient -> Ingredient.fromPacket(buf));
			return new RawRecipe(defaultedList, Optional.empty());
		}
		
		private final DefaultedList<Ingredient> ingredients;
		private final Optional<RawRecipe.Data> data;
		private final int ingredientCount;
		private final Ingredient brimstone;
	
		public RawRecipe(DefaultedList<Ingredient> ingredients, Optional<RawRecipe.Data> data) {
			this.ingredients = ingredients;
			brimstone = getBrimstone();
			ingredients.set(MAX_WIDTH_END + ((MAX_WIDTH_AND_HEIGHT - MAX_WIDTH_END) * MAX_WIDTH_AND_HEIGHT), brimstone);
			this.data = data;
			this.ingredientCount = (int)ingredients.stream().count();
		}
	
		private static RawRecipe create(DefaultedList<Ingredient> ingredients) {
			return new RawRecipe(ingredients, Optional.empty());
		}
	
		public static RawRecipe create(Map<Character, Ingredient> key, String... pattern) {
			return create(key, List.of(pattern));
		}
	
		public static RawRecipe create(Map<Character, Ingredient> key, List<String> pattern) {
			RawRecipe.Data data = new RawRecipe.Data(key, pattern);
			return Util.getResult(fromData(data), IllegalArgumentException::new);
		}
	
		private static DataResult<RawRecipe> fromData(RawRecipe.Data data) {
			String[] strings = removePadding(data.pattern);
			int i = strings[0].length();
			int j = strings.length;
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j - MAX_WIDTH_END, Ingredient.EMPTY);
			CharSet charSet = new CharArraySet(data.key.keySet());

			for (int k = 0; k < strings.length; k++) {
				String string = strings[k];

				for (int l = 0; l < string.length(); l++) {
					char c = string.charAt(l);
					Ingredient ingredient = c == ' ' ? Ingredient.EMPTY : (Ingredient)data.key.get(c);
					if (ingredient == null) {
						return DataResult.error(() -> "Pattern references symbol '" + c + "' but it's not defined in the key");
					}

					charSet.remove(c);
					PeacefulMod.LOGGER.info("" + (l + i * k));
					defaultedList.set(l + i * k, ingredient);
				}
			}
	
			return !charSet.isEmpty()
				? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charSet)
				: DataResult.success(new RawRecipe(defaultedList, Optional.of(data)));
		}
	
		/**
		 * Removes empty space from around the recipe pattern.
		 * 
		 * <p>Turns patterns such as:
		 * <pre>
		 * {@code
		 * "   o"
		 * "   a"
		 * "	"
		 * }
		 * </pre>
		 * Into:
		 * <pre>
		 * {@code
		 * "o"
		 * "a"
		 * }
		 * </pre>
		 * 
		 * @return a new recipe pattern with all leading and trailing empty rows/columns removed
		 */
		@VisibleForTesting
		static String[] removePadding(List<String> pattern) {
			// Trim each line
			List<String> trimmedLines = pattern.stream()
				.map(String::trim)
				.toList();

			// Remove leading empty lines
			int start = 0;
			while (start < trimmedLines.size() && trimmedLines.get(start).isEmpty()) {
				start++;
			}

			// Remove trailing empty lines
			int end = trimmedLines.size();
			while (end > start && trimmedLines.get(end - 1).isEmpty()) {
				end--;
			}

			// Return the cleaned pattern
			return trimmedLines.subList(start, end).toArray(new String[0]);
		}
	
		public boolean matches(EffigyAltarRecipeInput input) {
			if (input.getStackCount() != this.ingredientCount) {
				return false;
			} else {
				for (int i = 0; i < MAX_WIDTH_AND_HEIGHT; i++) {
					final int fi = i + 1;
					if (fi == MAX_WIDTH_AND_HEIGHT) {
						Ingredient ingredient = this.ingredients.get(i * MAX_WIDTH_AND_HEIGHT);

						ItemStack itemStack = input.getStackInSlot(0, i);
						if (!ingredient.test(itemStack)) {
							return false;
						}

						if (!brimstone.test(input.getStackInSlot(1, i))) {
							return false;
						}
					}
					else {
						for (int j = 0; j < MAX_WIDTH_AND_HEIGHT; j++) {
							Ingredient ingredient = this.ingredients.get(j + i * MAX_WIDTH_AND_HEIGHT);

							ItemStack itemStack = input.getStackInSlot(j, i);
							if (!ingredient.test(itemStack)) {
								return false;
							}
						}
					}
				}
		
				return true;
			}
		}
	
		public DefaultedList<Ingredient> getIngredients() {
			return this.ingredients;
		}
	
		public record Data(Map<Character, Ingredient> key, List<String> pattern) {
			private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(pattern -> {
				if (pattern.size() > MAX_WIDTH_AND_HEIGHT) {
					return DataResult.error(() -> "Invalid pattern: too many rows, 3 is maximum");
				} else if (pattern.isEmpty()) {
					return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
				} else {
					for (int i = 0; i < pattern.size(); i++) {
						final int fi = i + 1;
						String string = pattern.get(i);
						int length = string.length();
						if (fi == MAX_WIDTH_AND_HEIGHT) { // is end
							if (length > MAX_WIDTH_END) {
								return DataResult.error(() -> "Invalid pattern: too many columns for row #" + fi + ", 1 is maximum and minimum");
							}
							else if (length < MAX_WIDTH_END) {
								return DataResult.error(() -> "Invalid pattern: too little columns for row #" + fi + ", 1 is maximum and minimum");
							}
						}
						else {
							if (length > MAX_WIDTH_AND_HEIGHT) {
								return DataResult.error(() -> "Invalid pattern: too many columns for row #" + fi + ", 3 is maximum and minimum");
							}
							else if (length < MAX_WIDTH_AND_HEIGHT) {
								return DataResult.error(() -> "Invalid pattern: too little columns for row #" + fi + ", 3 is maximum and minimum");
							}
						}
					}
	
					return DataResult.success(pattern);
				}
			}, Function.identity());
			private static final Codec<Character> KEY_ENTRY_CODEC = Codec.STRING.comapFlatMap(keyEntry -> {
				if (keyEntry.length() != 1) {
					return DataResult.error(() -> "Invalid key entry: '" + keyEntry + "' is an invalid symbol (must be 1 character only).");
				} else {
					return " ".equals(keyEntry) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(keyEntry.charAt(0));
				}
			}, String::valueOf);
			public static final MapCodec<RawRecipe.Data> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						Codecs.strictUnboundedMap(KEY_ENTRY_CODEC, Ingredient.DISALLOW_EMPTY_CODEC).fieldOf("key").forGetter(data -> data.key),
						PATTERN_CODEC.fieldOf("pattern").forGetter(data -> data.pattern)
					)
					.apply(instance, RawRecipe.Data::new)
			);
		}
	}

	public static Ingredient getBrimstone() {
		return Ingredient.ofItems(PeacefulModItems.SULPHUR);
	}
	
	public static Optional<Ingredient> getBrimstoneOptional() {
		return Optional.of(getBrimstone());
	}

	@Override
	public boolean fits(int width, int height) {
		if (width == RawRecipe.MAX_WIDTH_AND_HEIGHT) {
			return height <= RawRecipe.MAX_WIDTH_AND_HEIGHT - RawRecipe.MAX_WIDTH_END;
		}
		else {
			return width >= RawRecipe.MAX_WIDTH_AND_HEIGHT && height >= RawRecipe.MAX_WIDTH_AND_HEIGHT;
		}
	}

	@Override
	public ItemStack getResult(DynamicRegistryManager registryManager) {
		return result();
	}
}