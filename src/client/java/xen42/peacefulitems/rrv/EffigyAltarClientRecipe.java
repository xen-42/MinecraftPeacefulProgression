package xen42.peacefulitems.rrv;


import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;

import java.util.ArrayList;
import java.util.List;

public class EffigyAltarClientRecipe implements ReliableClientRecipe {

    private final ArrayList<SlotContent> ingredients;
    private final SlotContent result;

    public EffigyAltarClientRecipe(EffigyAltarServerRecipe recipe) {
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
    public ReliableClientRecipeType getViewType() {
        return EffigyAltarClientRecipeType.INSTANCE;
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
}
