package xen42.peacefulitems.rrv;

import cc.cassian.rrv.api.ReliableRecipeViewerPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.common.recipe.ServerRecipeManager;
import xen42.peacefulitems.PeacefulMod;

public class PeacefulProgressionServerRRVPlugin implements ReliableRecipeViewerPlugin {
    @Override
    public void onIntegrationInitialize() {
        // register the server recipes
        ItemView.addServerRecipeProvider(recipeList -> {
            ServerRecipeManager.INSTANCE.getRecipesForType(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE).forEach(recipe -> {
                recipeList.add(new EffigyAltarServerRecipe(recipe.getIngredients(), recipe.result(), recipe.getCostOrDefault()));
            });
        });
    }
}
