package xen42.peacefulitems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class PeacefulModRecipeGenerator extends FabricRecipeProvider {
	public PeacefulModRecipeGenerator(FabricDataOutput generator, CompletableFuture<WrapperLookup> registriesFuture) {
        super(generator, registriesFuture);
    }

    @Override
    public String getName() {
        return "PeacefulItemsRecipeGenerator";
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.MISC, Items.LEATHER)
                        .pattern("XX")
                        .pattern("XX")
                        .input('X', PeacefulModItems.BAT_WING)
                        // Advancement that gives the recipe
                        .criterion(hasItem(PeacefulModItems.BAT_WING), conditionsFromItem(PeacefulModItems.BAT_WING))
                        .offerTo(exporter);
                
                createShapeless(RecipeCategory.MISC, Items.GUNPOWDER, 3)
                        .input(Items.CHARCOAL) 
                        .input(PeacefulModItems.GUANO) 
                        .input(PeacefulModItems.SULPHUR) 
                        .criterion(hasItem(Items.CHARCOAL), conditionsFromItem(Items.CHARCOAL))
                        .criterion(hasItem(PeacefulModItems.GUANO), conditionsFromItem(PeacefulModItems.GUANO))
                        .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, PeacefulModBlocks.SULPHUR_BLOCK)
                        .pattern("XXX")
                        .pattern("XXX")
                        .pattern("XXX")
                        .input('X', PeacefulModItems.SULPHUR)
                        // Advancement that gives the recipe
                        .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, Items.SADDLE)
                        .pattern("XXX")
                        .pattern("ZYZ")
                        .input('X', Items.LEATHER)
                        .input('Y', Items.STRING)
                        .input('Z', Items.IRON_INGOT)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                        .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, PeacefulModItems.SULPHUR, 9)
                        .input(PeacefulModBlocks.SULPHUR_BLOCK) 
                        .criterion(hasItem(PeacefulModBlocks.SULPHUR_BLOCK), conditionsFromItem(PeacefulModBlocks.SULPHUR_BLOCK))
                        .offerTo(exporter);

                offerSmelting(List.of(PeacefulModBlocks.BLAZE_PICKLE), RecipeCategory.MISC, Items.BLAZE_ROD, 0.45f, 200, PeacefulModBlocks.BLAZE_PICKLE.getName().toString());
                offerSmelting(List.of(PeacefulModBlocks.BREEZE_CORAL), RecipeCategory.MISC, Items.BREEZE_ROD, 0.45f, 200, PeacefulModBlocks.BREEZE_CORAL.getName().toString());

                createShapeless(RecipeCategory.MISC, Items.YELLOW_DYE, 1)
                        .input(PeacefulModItems.SULPHUR)
                        .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, Items.ORANGE_DYE, 1)
                        .input(PeacefulModBlocks.BLAZE_PICKLE)
                        .criterion(hasItem(PeacefulModBlocks.BLAZE_PICKLE), conditionsFromItem(PeacefulModBlocks.BLAZE_PICKLE))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, Items.GRAY_DYE, 1)
                        .input(PeacefulModBlocks.BREEZE_CORAL)
                        .criterion(hasItem(PeacefulModBlocks.BREEZE_CORAL), conditionsFromItem(PeacefulModBlocks.BREEZE_CORAL))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, Items.PHANTOM_MEMBRANE, 2)
                        .input(PeacefulModItems.BAT_WING, 3) 
                        .input(Items.GHAST_TEAR)
                        .criterion(hasItem(PeacefulModItems.BAT_WING), conditionsFromItem(PeacefulModItems.BAT_WING))
                        .criterion(hasItem(Items.GHAST_TEAR), conditionsFromItem(Items.GHAST_TEAR))
                        .offerTo(exporter);
            }
        };
    }
}
