package xen42.peacefulitems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.recipe.EffigyAltarRecipeJsonBuilder;

public class PeacefulModRecipeGenerator extends FabricRecipeProvider {
    public PeacefulModRecipeGenerator(FabricDataOutput generator, CompletableFuture<WrapperLookup> registriesFuture) {
        super(generator, registriesFuture);
    }

    @Override
    public String getName() {
        return "PeacefulModRecipeGenerator";
    }

    @Override
    public Identifier getRecipeIdentifier(Identifier identifier) {
        return Identifier.of(PeacefulMod.MOD_ID, identifier.getPath());
    }
    
    public static void offerTo(CraftingRecipeJsonBuilder builder, RecipeExporter exporter) {
        builder.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, EffigyAltarRecipeJsonBuilder.getItemId(builder.getOutputItem())));
    }

    public static void offerTo(CraftingRecipeJsonBuilder builder, RecipeExporter exporter, String recipePath) {
        Identifier defaultIdentifier = EffigyAltarRecipeJsonBuilder.getItemId(builder.getOutputItem());
        Identifier identifier = Identifier.of(PeacefulMod.MOD_ID, recipePath);
        if (identifier.equals(defaultIdentifier)) {
            throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
        } else {
            builder.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, identifier));
        }
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void offerSmelting(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
                this.fixedOfferMultipleOptions(RecipeSerializer.SMELTING, SmeltingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_smelting");
            }

            @Override
            public void offerBlasting(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
                this.fixedOfferMultipleOptions(RecipeSerializer.BLASTING, BlastingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_blasting");
            }
            
            public final <T extends AbstractCookingRecipe> void fixedOfferMultipleOptions(
                    RecipeSerializer<T> serializer,
                    AbstractCookingRecipe.RecipeFactory<T> recipeFactory,
                    List<ItemConvertible> inputs,
                    RecipeCategory category,
                    ItemConvertible output,
                    float experience,
                    int cookingTime,
                    String group,
                    String suffix
                ) {
                    for (ItemConvertible itemConvertible : inputs) {
                        offerTo(CookingRecipeJsonBuilder.create(Ingredient.ofItem(itemConvertible), category, output, experience, cookingTime, serializer, recipeFactory)
                            .group(group)
                            .criterion(hasItem(itemConvertible), this.conditionsFromItem(itemConvertible))
                            , exporter, getItemPath(output) + suffix + "_" + getItemPath(itemConvertible));
                    }
            }
            
            @Override
            public void generate() {
                offerTo(createShaped(RecipeCategory.MISC, Items.LEATHER)
                        .pattern("XX")
                        .pattern("XX")
                        .input('X', PeacefulModItems.BAT_WING)
                        // Advancement that gives the recipe
                        .criterion(hasItem(PeacefulModItems.BAT_WING), conditionsFromItem(PeacefulModItems.BAT_WING))
                        , exporter);
                
                offerTo(createShapeless(RecipeCategory.MISC, Items.GUNPOWDER, 3)
                        .input(Items.CHARCOAL) 
                        .input(PeacefulModItems.GUANO) 
                        .input(PeacefulModItems.SULPHUR) 
                        .criterion(hasItem(Items.CHARCOAL), conditionsFromItem(Items.CHARCOAL))
                        .criterion(hasItem(PeacefulModItems.GUANO), conditionsFromItem(PeacefulModItems.GUANO))
                        .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                        , exporter);

                offerTo(createShaped(RecipeCategory.MISC, PeacefulModBlocks.SULPHUR_BLOCK)
                        .pattern("XXX")
                        .pattern("XXX")
                        .pattern("XXX")
                        .input('X', PeacefulModItems.SULPHUR)
                        // Advancement that gives the recipe
                        .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                        , exporter);

                offerTo(createShaped(RecipeCategory.MISC, Items.SADDLE)
                        .pattern("XXX")
                        .pattern("ZYZ")
                        .input('X', Items.LEATHER)
                        .input('Y', Items.STRING)
                        .input('Z', Items.IRON_INGOT)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                        .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        , exporter);

                offerTo(createShapeless(RecipeCategory.MISC, Items.ROTTEN_FLESH)
                    .input(PeacefulModItems.SULPHUR)
                    .input(ItemTags.MEAT)
                    .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                    , exporter);

                offerTo(createShapeless(RecipeCategory.MISC, PeacefulModItems.SULPHUR, 9)
                        .input(PeacefulModBlocks.SULPHUR_BLOCK) 
                        .criterion(hasItem(PeacefulModBlocks.SULPHUR_BLOCK), conditionsFromItem(PeacefulModBlocks.SULPHUR_BLOCK))
                        , exporter);

                offerSmelting(List.of(PeacefulModBlocks.BLAZE_PICKLE), RecipeCategory.MISC, Items.BLAZE_ROD, 0.45f, 200, PeacefulModBlocks.BLAZE_PICKLE.getName().toString());
                offerSmelting(List.of(PeacefulModBlocks.BREEZE_CORAL), RecipeCategory.MISC, Items.BREEZE_ROD, 0.45f, 200, PeacefulModBlocks.BREEZE_CORAL.getName().toString());

                offerTo(createShapeless(RecipeCategory.MISC, Items.YELLOW_DYE, 1)
                        .input(PeacefulModItems.SULPHUR)
                        .criterion(hasItem(PeacefulModItems.SULPHUR), conditionsFromItem(PeacefulModItems.SULPHUR))
                        , exporter);

                offerTo(createShapeless(RecipeCategory.MISC, Items.ORANGE_DYE, 1)
                        .input(PeacefulModBlocks.BLAZE_PICKLE)
                        .criterion(hasItem(PeacefulModBlocks.BLAZE_PICKLE), conditionsFromItem(PeacefulModBlocks.BLAZE_PICKLE))
                        , exporter);

                offerTo(createShapeless(RecipeCategory.MISC, Items.GRAY_DYE, 1)
                        .input(PeacefulModBlocks.BREEZE_CORAL)
                        .criterion(hasItem(PeacefulModBlocks.BREEZE_CORAL), conditionsFromItem(PeacefulModBlocks.BREEZE_CORAL))
                        , exporter);

                offerTo(createShapeless(RecipeCategory.MISC, Items.PHANTOM_MEMBRANE, 2)
                        .input(PeacefulModItems.BAT_WING, 3) 
                        .input(Items.GHAST_TEAR)
                        .criterion(hasItem(PeacefulModItems.BAT_WING), conditionsFromItem(PeacefulModItems.BAT_WING))
                        .criterion(hasItem(Items.GHAST_TEAR), conditionsFromItem(Items.GHAST_TEAR))
                        , exporter);
                
                offerTo(createEffigyAltar(Items.TOTEM_OF_UNDYING)
                        .pattern("ebe",
                                 "ggg",
                                  "g")
                        .input('b', Blocks.GOLD_BLOCK)
                        .input('e', Items.EMERALD)
                        .input('g', Items.GOLD_INGOT)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Blocks.GOLD_BLOCK), conditionsFromItem(Blocks.GOLD_BLOCK))
                        .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        , exporter);

                createEffigyAltar(PeacefulModItems.DRAGON_EFFIGY)
                        .pattern("ebe",
                                 "wgw",
                                  "g")
                        .input('b', Blocks.CRYING_OBSIDIAN)
                        .input('e', Items.ENDER_EYE)
                        .input('g', Blocks.OBSIDIAN)
                        .input('w', PeacefulModItems.BAT_WING)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Blocks.CRYING_OBSIDIAN), conditionsFromItem(Blocks.CRYING_OBSIDIAN))
                        .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE))
                        .criterion(hasItem(Blocks.OBSIDIAN), conditionsFromItem(Blocks.OBSIDIAN))
                        .criterion(hasItem(PeacefulModItems.BAT_WING), conditionsFromItem(PeacefulModItems.BAT_WING))
                        .offerTo(exporter);

                createEffigyAltar(PeacefulModItems.GUARDIAN_EFFIGY)
                        .pattern("beb",
                                 "bbb",
                                  "b")
                        .input('b', Items.TROPICAL_FISH)
                        .input('e', Items.PUFFERFISH)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.TROPICAL_FISH), conditionsFromItem(Items.TROPICAL_FISH))
                        .criterion(hasItem(Items.PUFFERFISH), conditionsFromItem(Items.PUFFERFISH))
                        .offerTo(exporter);

                createEffigyAltar(PeacefulModItems.WITHER_EFFIGY)
                        .pattern("bbb",
                                 "eee",
                                  "e")
                        .input('b', Items.WITHER_SKELETON_SKULL)
                        .input('e', Blocks.SOUL_SAND)
                        // Advancement that gives the recipe
                        .criterion(hasItem(Items.WITHER_SKELETON_SKULL), conditionsFromItem(Items.WITHER_SKELETON_SKULL))
                        .criterion(hasItem(Blocks.SOUL_SAND), conditionsFromItem(Blocks.SOUL_SAND))
                        .offerTo(exporter);
            }
            
            public EffigyAltarRecipeJsonBuilder createEffigyAltar(ItemConvertible output) {
                return EffigyAltarRecipeJsonBuilder.create(registryLookup.getOrThrow(RegistryKeys.ITEM), output);
            }
        };
    }
}
