package xen42.peacefulitems;

import java.util.Optional;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.data.Model;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class PeacefulModModelGenerator extends FabricModelProvider {

    public PeacefulModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerCubeAllModelTexturePool(PeacefulModBlocks.SULPHUR_BLOCK).family(PeacefulModBlocks.SULPHUR);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SULPHUR_ORE);
        blockStateModelGenerator.registerAmethyst(PeacefulModBlocks.SULPHUR_CLUSTER);
        registerSeaPickle(blockStateModelGenerator, PeacefulModBlocks.BLAZE_PICKLE.asItem(), PeacefulModBlocks.BLAZE_PICKLE);
        registerSeaPickle(blockStateModelGenerator, PeacefulModBlocks.BREEZE_CORAL.asItem(), PeacefulModBlocks.BREEZE_CORAL);

        blockStateModelGenerator.registerCrop(PeacefulModBlocks.FLAX_CROP, Properties.AGE_7, 0, 1, 2, 3, 4, 4, 5, 6);
        
        registerCauldron(blockStateModelGenerator, PeacefulModBlocks.DRAGON_BREATH_CAULDRON, Identifier.of(PeacefulMod.MOD_ID, "dragon_breath").withPrefixedPath("block/"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(PeacefulModItems.BAT_WING, Models.GENERATED);
        itemModelGenerator.registerWithBrokenCondition(PeacefulModItems.CAPE);
        itemModelGenerator.register(PeacefulModItems.GUANO, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.ECTOPLASM, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.SULPHUR, Models.GENERATED);
        
        itemModelGenerator.register(PeacefulModItems.WITHER_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.GUARDIAN_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.DRAGON_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.RAID_EFFIGY, Models.GENERATED);

        itemModelGenerator.register(PeacefulModItems.GHASTLING_SPAWN_EGG, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.END_CLAM_SPAWN_EGG, Models.GENERATED);

        itemModelGenerator.register(PeacefulModItems.CLAM, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.COOKED_CLAM, Models.GENERATED);
    }

    @Override
    public String getName() {
        return "PeacefulModModelGenerator";
    }

    private Model GetModel(String parent) {
        return new Model(Optional.of(Identifier.ofVanilla("block/" + parent)), Optional.empty(), TextureKey.ALL, TextureKey.PARTICLE);
    }

    public void registerSeaPickle(BlockStateModelGenerator blockStateModelGenerator, Item item, Block block) {
		blockStateModelGenerator.registerItemModel(item);
        var textureMap = TextureMap.all(block);

        var id1 = GetModel("dead_sea_pickle").upload(block, "_one", textureMap, blockStateModelGenerator.modelCollector);
        var id2 = GetModel("two_dead_sea_pickles").upload(block, "_two", textureMap, blockStateModelGenerator.modelCollector);
        var id3 = GetModel("three_dead_sea_pickles").upload(block, "_three", textureMap, blockStateModelGenerator.modelCollector);
        var id4 = GetModel("four_dead_sea_pickles").upload(block, "_four", textureMap, blockStateModelGenerator.modelCollector);

		blockStateModelGenerator.blockStateCollector
			.accept(
                VariantsBlockModelDefinitionCreator.of(block)
                    .with(BlockStateVariantMap.models(Properties.PICKLES)
							.register(1, BlockStateModelGenerator.createWeightedVariant(new ModelVariant(id1)))
							.register(2, BlockStateModelGenerator.createWeightedVariant(new ModelVariant(id2)))
							.register(3, BlockStateModelGenerator.createWeightedVariant(new ModelVariant(id3)))
							.register(4, BlockStateModelGenerator.createWeightedVariant(new ModelVariant(id4)))
                    )
			);
	}
    
    public void registerCauldron(BlockStateModelGenerator blockStateModelGenerator, Block cauldronBlock, Identifier fluidTexture) {
    	TextureMap cauldronTextureMap = TextureMap.cauldron(fluidTexture);
        blockStateModelGenerator.blockStateCollector
			.accept(
				VariantsBlockModelDefinitionCreator.of(cauldronBlock)
					.with(
						BlockStateVariantMap.models(LeveledCauldronBlock.LEVEL)
							.register(
								1,
								BlockStateModelGenerator.createWeightedVariant(
									Models.TEMPLATE_CAULDRON_LEVEL1
										.upload(cauldronBlock, "_level1", cauldronTextureMap, blockStateModelGenerator.modelCollector)
								)
							)
							.register(
								2,
								BlockStateModelGenerator.createWeightedVariant(
									Models.TEMPLATE_CAULDRON_LEVEL2
										.upload(cauldronBlock, "_level2", cauldronTextureMap, blockStateModelGenerator.modelCollector)
								)
							)
							.register(
								3,
								BlockStateModelGenerator.createWeightedVariant(
									Models.TEMPLATE_CAULDRON_FULL
										.upload(cauldronBlock, "_full", cauldronTextureMap, blockStateModelGenerator.modelCollector)
								)
							)
					)
			);
    }
}
