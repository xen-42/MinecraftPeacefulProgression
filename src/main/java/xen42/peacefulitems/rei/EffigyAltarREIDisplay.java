package xen42.peacefulitems.rei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;

public class EffigyAltarREIDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
	public static final BasicDisplay.Serializer<EffigyAltarREIDisplay> SERIALIZER = BasicDisplay.Serializer.<EffigyAltarREIDisplay>of((input, output, location, tag) -> {
			int cost = tag.getInt("Cost");
			return EffigyAltarREIDisplay.simple(input, output, cost, location);
		}, (display, tag) -> {
			tag.putInt("Cost", display.getCost());
		});
	private final int cost;

	@SuppressWarnings("unchecked")
	public static EffigyAltarREIDisplay simple(List<EntryIngredient> input, List<EntryIngredient> output, int cost, Optional<Identifier> location) {
		Optional<RecipeEntry<?>> optionalRecipe = location.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation));
		if (optionalRecipe.isPresent()) {
			return new EffigyAltarREIDisplay((RecipeEntry<EffigyAltarRecipe>)optionalRecipe.get());
		}
		else {
			return new EffigyAltarREIDisplay(input, output, cost);
		}
	}

	static List<EntryIngredient> ingredientsFromRecipe(EffigyAltarRecipe recipe){
		List<EntryIngredient> ingredients = new ArrayList<EntryIngredient>();
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredients.add(EntryIngredients.ofIngredient(ingredient));
		}
		return ingredients;
	}
	
	public EffigyAltarREIDisplay(RecipeEntry<EffigyAltarRecipe> recipe) {
		this(recipe.value());
	}
	
	public EffigyAltarREIDisplay(EffigyAltarRecipe recipe) {
		this(ingredientsFromRecipe(recipe), List.of(EntryIngredients.of(recipe.result())), recipe.getCostOrDefault());
	}

	protected Optional<RecipeEntry<EffigyAltarRecipe>> recipe;
	
	public EffigyAltarREIDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int cost) {
		super(inputs, outputs);
		this.cost = cost;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputs;
	}
	
	public int getCost() {
		return cost;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY;
	}
	
	static int getSlotWithSize(int recipeWidth, int index, int craftingGridWidth) {
		int x = index % recipeWidth;
		int y = (index - x) / recipeWidth;
		return craftingGridWidth * y + x;
	}
	
	public int getSlotWithSize(int index, int craftingGridWidth) {
		return getSlotWithSize(getInputWidth(craftingGridWidth, 3), index, craftingGridWidth);
	}
	
	public List<EntryIngredient> getOrganisedInputEntries(int menuWidth, int menuHeight) {
		List<EntryIngredient> list = new ArrayList<EntryIngredient>(menuWidth * menuHeight);
		for (int i = 0; i < menuWidth * menuHeight; i++) {
			list.add(EntryIngredient.empty());
		}
		for (int i = 0; i < getInputEntries().size(); i++) {
			list.set(getSlotWithSize(i, menuWidth), getInputEntries().get(i));
		}
		return list;
	}
	
	@Override
	public List<InputIngredient<EntryStack<?>>> getInputIngredients(@Nullable ScreenHandler menu, @Nullable PlayerEntity player) {
		return getInputIngredients(3, 3);
	}
	
	public List<InputIngredient<EntryStack<?>>> getInputIngredients(int craftingWidth, int craftingHeight) {
		int inputWidth = getInputWidth(craftingWidth, craftingHeight);
		int inputHeight = getInputHeight(craftingWidth, craftingHeight);
		
		Map<IntIntPair, InputIngredient<EntryStack<?>>> grid = new HashMap<>();
		
		List<EntryIngredient> inputEntries = getInputEntries();
		for (int i = 0; i < inputEntries.size(); i++) {
			EntryIngredient stacks = inputEntries.get(i);
			if (stacks.isEmpty()) {
				continue;
			}
			int index = getSlotWithSize(inputWidth, i, craftingWidth);
			int x = i % inputWidth;
			int y = i / inputHeight;
			grid.put(new IntIntImmutablePair(x, y), InputIngredient.of(index, 3 * y + x, stacks));
		}
		
		List<InputIngredient<EntryStack<?>>> list = new ArrayList<>((craftingHeight * craftingWidth) - 1);
		for (int i = 0, n = (craftingWidth * craftingHeight) - 1; i < n; i++) {
			list.add(InputIngredient.empty(i));
		}

		for (int y = 0; y < craftingHeight; y++) {
			for (int x = 0; x < (y == 2 ? craftingWidth - 1 : craftingWidth); x++) {
				InputIngredient<EntryStack<?>> ingredient = grid.get(new IntIntImmutablePair(x, y));
				if (ingredient != null) {
					int index = craftingWidth * y + x;
					list.set(index, ingredient);
				}
			}
		}
		
		return list;
	}
	
	@Nullable
	public static EffigyAltarREIDisplay of(RecipeEntry<? extends EffigyAltarRecipe> holder) {
		EffigyAltarRecipe recipe = holder.value();
		return new EffigyAltarREIDisplay(recipe);
	}
}
