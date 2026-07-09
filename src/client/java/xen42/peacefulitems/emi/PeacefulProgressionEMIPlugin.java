package xen42.peacefulitems.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;

public class PeacefulProgressionEMIPlugin implements EmiPlugin {

	public static final Identifier SPRITESHEET = Identifier.of(PeacefulMod.MOD_ID, "textures/gui/emi_simplified_textures.png");
	public static final EmiStack ALTAR = EmiStack.of(PeacefulModBlocks.EFFIGY_ALTAR);
	public static final EmiRecipeCategory EFFIGY_CATEGORY = new EmiRecipeCategory(
			PeacefulMod.EFFIGY_ALTAR_ID, ALTAR, new EmiTexture(SPRITESHEET, 0, 0, 16, 16));

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(EFFIGY_CATEGORY);
		registry.addWorkstation(EFFIGY_CATEGORY, ALTAR);

		RecipeManager manager = registry.getRecipeManager();

		for (RecipeEntry<EffigyAltarRecipe> recipe : manager.listAllOfType(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE)) {
			registry.addRecipe(new EffigyAltarEmiRecipe(recipe));
		}
	}
}