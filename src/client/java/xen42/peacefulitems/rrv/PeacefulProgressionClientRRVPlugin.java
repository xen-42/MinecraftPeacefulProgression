package xen42.peacefulitems.rrv;

import cc.cassian.rrv.api.ReliableRecipeViewerPlugin;
import cc.cassian.rrv.api.recipe.ItemView;

import java.util.Collections;

public class PeacefulProgressionClientRRVPlugin implements ReliableRecipeViewerPlugin {

    @Override
    public void onIntegrationInitialize() {

        // register all the client recipes
        ItemView.addClientRecipeWrapper(EffigyAltarServerRecipe.TYPE, modRecipe -> {
            return Collections.singletonList(new EffigyAltarClientRecipe(modRecipe));
        });
    }
}
