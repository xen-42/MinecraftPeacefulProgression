package xen42.peacefulitems.emi;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;

public class EffigyAltarEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EffigyAltarEmiRecipe(RecipeEntry<EffigyAltarRecipe> recipe) {
        this.id = recipe.id();
        this.input = List.of(
                EmiIngredient.of(recipe.value().getIngredients().get(0)),
                EmiIngredient.of(recipe.value().getIngredients().get(1)),
                EmiIngredient.of(recipe.value().getIngredients().get(2)),
                EmiIngredient.of(recipe.value().getIngredients().get(3)),
                EmiIngredient.of(recipe.value().getIngredients().get(4)),
                EmiIngredient.of(recipe.value().getIngredients().get(5)),
                EmiIngredient.of(recipe.value().getIngredients().get(6)),
                EmiIngredient.of(recipe.value().getIngredients().get(7)));
        this.output = List.of(EmiStack.of(recipe.value().result()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PeacefulProgressionEMIPlugin.EFFIGY_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 118;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 4);
        widgets.addSlot(input.get(0), 0, 0);
        widgets.addSlot(input.get(1), 18, 0);
        widgets.addSlot(input.get(2), 36, 0);
        widgets.addSlot(input.get(3), 0, 18);
        widgets.addSlot(input.get(4), 18, 18);
        widgets.addSlot(input.get(5), 36, 18);
        widgets.addSlot(input.get(6), 18, 36);
        widgets.addSlot(input.get(7), 62, 28);
        widgets.addSlot(output.get(0), 92, 0).large(true).recipeContext(this);
    }

}
