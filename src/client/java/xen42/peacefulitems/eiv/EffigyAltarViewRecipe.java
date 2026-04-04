package xen42.peacefulitems.eiv;

import de.crafty.eiv.common.api.recipe.IEivRecipeViewType;
import de.crafty.eiv.common.api.recipe.IEivViewRecipe;
import de.crafty.eiv.common.recipe.inventory.RecipeViewMenu;
import de.crafty.eiv.common.recipe.inventory.SlotContent;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import xen42.peacefulitems.screen.EffigyAltarHandledScreen;

public class EffigyAltarViewRecipe implements IEivViewRecipe {

    private final ArrayList<SlotContent> ingredients;
    private final SlotContent result;

    public EffigyAltarViewRecipe(EffigyAltarServerRecipe recipe) {
        this.ingredients = new ArrayList<>();
        recipe.getIngredients().forEach(ingredient -> {
            if (ingredient.isPresent()) {
                this.ingredients.add(SlotContent.of(ingredient.get()));
            } else {
                this.ingredients.add(SlotContent.of());
            }
        });

        this.result = SlotContent.of(recipe.getResult());
    }

    @Override
    public IEivRecipeViewType getViewType() {
        return EffigyAltarViewType.INSTANCE;
    }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext) {
        for (int i = 0; i < ingredients.size() && i < 8; i++) {
            slotFillContext.bindSlot(i, ingredients.get(i));
        }

        slotFillContext.bindSlot(8, this.result);
    }

    @Override
    public List<SlotContent> getIngredients() {
        return ingredients;
    }

    @Override
    public List<SlotContent> getResults() {
        return List.of(result);
    }

    @Override
    public boolean supportsItemTransfer() {
        return true;
    }

    @Override
    public List<Class<? extends HandledScreen<?>>> getTransferClasses() {
        return List.of(EffigyAltarHandledScreen.class);
    }

    @Override
    public void mapRecipeItems(RecipeTransferMap map, HandledScreen<?> screen) {
        map.linkSlots(0, 1);
        map.linkSlots(1, 2);
        map.linkSlots(2, 3);
        map.linkSlots(3, 4);
        map.linkSlots(4, 5);
        map.linkSlots(5, 6);
        map.linkSlots(6, 7);
        map.linkSlots(7, 8);
    }
}
