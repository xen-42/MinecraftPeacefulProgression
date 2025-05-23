package xen42.peacefulitems.recipe;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.input.RecipeInput;

public class EffigyAltarRecipeInput implements RecipeInput {
	private static final int MAX_WIDTH_AND_HEIGHT = 3;
	private final List<ItemStack> stacks;
	private final RecipeMatcher matcher = new RecipeMatcher();
	private final int stackCount;

	private EffigyAltarRecipeInput(List<ItemStack> stacks) {
		this.stacks = stacks;
		int i = 0;

		for (ItemStack itemStack : stacks) {
			if (!itemStack.isEmpty()) {
				i++;
				this.matcher.addInput(itemStack, 1);
			}
		}

		this.stackCount = i;
	}

	public static EffigyAltarRecipeInput create(List<ItemStack> stacks) {
		return new EffigyAltarRecipeInput(stacks);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return (ItemStack)this.stacks.get(slot);
	}

	public ItemStack getStackInSlot(int x, int y) {
		return (ItemStack)this.stacks.get(x + y * MAX_WIDTH_AND_HEIGHT);
	}

	public int size() {
		return this.stacks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.stackCount == 0;
	}

	public RecipeMatcher getRecipeMatcher() {
		return this.matcher;
	}

	public List<ItemStack> getStacks() {
		return this.stacks;
	}

	public int getStackCount() {
		return this.stackCount;
	}
	
	public static boolean stacksEqual(List<ItemStack> left, List<ItemStack> right) {
		if (left.size() != right.size()) {
			return false;
		} else {
			for (int i = 0; i < left.size(); i++) {
				if (!ItemStack.areEqual((ItemStack)left.get(i), (ItemStack)right.get(i))) {
					return false;
				}
			}

			return true;
		}
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else {
			return !(o instanceof EffigyAltarRecipeInput effigyAltarRecipeInput)
				? false
				: this.stackCount == effigyAltarRecipeInput.stackCount
					&& stacksEqual(this.stacks, effigyAltarRecipeInput.stacks);
		}
	}

	public int hashCode() {
		int i = 0;
		
		for (ItemStack itemStack : this.stacks) {
			i = i * 31 + ItemStack.hashCode(itemStack);
		}

		return i;
	}

	@Override
	public int getSize() {
		return size();
	}
}
