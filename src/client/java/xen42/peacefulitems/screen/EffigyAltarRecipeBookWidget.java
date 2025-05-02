package xen42.peacefulitems.screen;

import java.util.List;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.recipe.RecipeGridAligner.Filler;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SlotDisplay.CompositeSlotDisplay;
import net.minecraft.recipe.display.SlotDisplay.ItemSlotDisplay;
import net.minecraft.recipe.display.SlotDisplay.StackSlotDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.EffigyAltarRecipeDisplay;
import xen42.peacefulitems.EffigyAltarRecipeDisplay.BrimstoneSlotDisplay;

public class EffigyAltarRecipeBookWidget extends RecipeBookWidget<EffigyAltarScreenHandler> {
	private static final ButtonTextures TEXTURES = new ButtonTextures(
		Identifier.ofVanilla("recipe_book/filter_enabled"),
		Identifier.ofVanilla("recipe_book/filter_disabled"),
		Identifier.ofVanilla("recipe_book/filter_enabled_highlighted"),
		Identifier.ofVanilla("recipe_book/filter_disabled_highlighted")
	);
	private static final Text TOGGLE_CRAFTABLE_TEXT = Text.translatable("gui.recipebook.toggleRecipes.craftable");
	private static final List<RecipeBookWidget.Tab> TABS = List.of(
		new RecipeBookWidget.Tab(Items.TOTEM_OF_UNDYING, PeacefulMod.EFFIGY_ALTAR_RECIPE_BOOK_CATEGORY)
	);
	private static final int MAX_WIDTH_AND_HEIGHT = 3;
	private static final int MAX_WIDTH_END = 1;

	public EffigyAltarRecipeBookWidget(EffigyAltarScreenHandler screenHandler) {
		super(screenHandler, TABS);
	}

	@Override
	protected boolean isValid(Slot slot) {
		return this.craftingScreenHandler.getOutputSlot() == slot || this.craftingScreenHandler.getInputSlots().contains(slot) || this.craftingScreenHandler.getBrimstoneSlot() == slot;
	}

	private boolean canDisplay(RecipeDisplay display) {
		return true;
	}

	@Override
	protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
		EffigyAltarRecipeDisplay effigyAltarDisplay = (EffigyAltarRecipeDisplay)display;
		ghostRecipe.addResults(this.craftingScreenHandler.getOutputSlot(), context, effigyAltarDisplay.result());
		List<Slot> inputSlots = this.craftingScreenHandler.getInputSlots();
		List<SlotDisplay> ingredients = effigyAltarDisplay.ingredients(); 
		for (int i = 0; i < inputSlots.size(); i++) {
			ghostRecipe.addInputs(inputSlots.get(i), context, ingredients.get(i));
		}
		ghostRecipe.addInputs(this.craftingScreenHandler.getBrimstoneSlot(), context, effigyAltarDisplay.brimstone());
	}

	@Override
	protected void setBookButtonTexture() {
		this.toggleCraftableButton.setTextures(TEXTURES);
	}

	@Override
	protected Text getToggleCraftableButtonText() {
		return TOGGLE_CRAFTABLE_TEXT;
	}

	@Override
	protected void populateRecipes(RecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
		recipeResultCollection.populateRecipes(recipeFinder, this::canDisplay);
	}
}
