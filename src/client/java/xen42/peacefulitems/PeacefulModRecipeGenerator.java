package xen42.peacefulitems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Identifier;

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
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

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
                
                createShaped(RecipeCategory.MISC, Items.TOTEM_OF_UNDYING)
                        .pattern("ebe")
                        .pattern("ggg")
                        .pattern(" g ")
                        .input('b', Blocks.GOLD_BLOCK)
                        .input('e', Items.EMERALD)
                        .input('g', Items.GOLD_INGOT)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .criterion(hasItem(Items.GOLD_BLOCK), conditionsFromItem(Items.GOLD_BLOCK))
                        .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, PeacefulModItems.DRAGON_EFFIGY)
                        .pattern("ebe")
                        .pattern("ggg")
                        .pattern(" g ")
                        .input('b', Blocks.CRYING_OBSIDIAN)
                        .input('e', Items.ENDER_EYE)
                        .input('g', Blocks.OBSIDIAN)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Blocks.CRYING_OBSIDIAN), conditionsFromItem(Blocks.CRYING_OBSIDIAN))
                        .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                        .criterion(hasItem(Blocks.OBSIDIAN), conditionsFromItem(Blocks.OBSIDIAN))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, PeacefulModItems.GUARDIAN_EFFIGY)
                        .pattern("beb")
                        .pattern("bbb")
                        .pattern(" b ")
                        .input('b', Items.TROPICAL_FISH)
                        .input('e', Items.PUFFERFISH)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.TROPICAL_FISH), conditionsFromItem(Items.TROPICAL_FISH))
                        .criterion(hasItem(Items.PUFFERFISH), conditionsFromItem(Items.PUFFERFISH))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, PeacefulModItems.WITHER_EFFIGY)
                        .pattern("bbb")
                        .pattern("eee")
                        .pattern(" e ")
                        .input('b', Items.WITHER_SKELETON_SKULL)
                        .input('e', Blocks.SOUL_SAND)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.WITHER_SKELETON_SKULL), conditionsFromItem(Items.WITHER_SKELETON_SKULL))
                        .criterion(hasItem(Blocks.SOUL_SAND), conditionsFromItem(Blocks.SOUL_SAND))
                        .offerTo(exporter);
            }
        };
    }
}
