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
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.ScreenHandler;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;
import xen42.peacefulitems.recipe.EffigyAltarRecipeDisplay;

public class EffigyAltarREIDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
	public static final DisplaySerializer<EffigyAltarREIDisplay> SERIALIZER = DisplaySerializer.of(
			RecordCodecBuilder.mapCodec(instance -> instance.group(
					EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(EffigyAltarREIDisplay::getInputEntries),
					EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(EffigyAltarREIDisplay::getOutputEntries),
					Codec.INT.fieldOf("cost").forGetter(EffigyAltarREIDisplay::getCost)
			).apply(instance, EffigyAltarREIDisplay::new)),
			PacketCodec.tuple(
					EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
					EffigyAltarREIDisplay::getInputEntries,
					EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
					EffigyAltarREIDisplay::getOutputEntries,
					PacketCodecs.INTEGER,
					EffigyAltarREIDisplay::getCost,
					EffigyAltarREIDisplay::new
			), false);
	
	private final int cost;
	
	public EffigyAltarREIDisplay(EffigyAltarRecipeDisplay recipe) {
		this(EntryIngredients.ofSlotDisplays(recipe.ingredients()),
				List.of(EntryIngredients.ofSlotDisplay(recipe.result())),
				recipe.cost());
	}
	
	static List<EntryIngredient> ingredientsFromRecipe(EffigyAltarRecipe recipe){
		List<EntryIngredient> ingredients = new ArrayList<EntryIngredient>();
		for (Optional<Ingredient> optionalIngredient : recipe.getIngredients()) {
			if (optionalIngredient.isPresent()) {
				ingredients.add(EntryIngredients.ofIngredient(optionalIngredient.get()));
			}
		}
		return ingredients;
	}
	
	public EffigyAltarREIDisplay(RecipeEntry<EffigyAltarRecipe> recipe) {
		this(recipe.value());
	}
	
	public EffigyAltarREIDisplay(EffigyAltarRecipe recipe) {
		this(ingredientsFromRecipe(recipe), List.of(EntryIngredients.of(recipe.result())), recipe.getCostOrDefault());
	}
	
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
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
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
		if (recipe instanceof EffigyAltarRecipe) {
			return new EffigyAltarREIDisplay(recipe);
		} else if (!recipe.isIgnoredInRecipeBook()) {
			for (RecipeDisplay d : recipe.getDisplays()) {
				if (d instanceof EffigyAltarRecipeDisplay display) {
					return new EffigyAltarREIDisplay(display);
				}
			}
		}
		
		return null;
	}
}
