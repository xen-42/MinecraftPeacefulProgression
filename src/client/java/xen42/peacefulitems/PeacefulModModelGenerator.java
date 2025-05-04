package xen42.peacefulitems;

import java.util.Optional;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.VariantsBlockStateSupplier;
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
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SULPHUR_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SULPHUR_ORE);
        blockStateModelGenerator.registerAmethyst(PeacefulModBlocks.SULPHUR_CLUSTER);
        registerSeaPickle(blockStateModelGenerator, PeacefulModBlocks.BLAZE_PICKLE.asItem(), PeacefulModBlocks.BLAZE_PICKLE);
        registerSeaPickle(blockStateModelGenerator, PeacefulModBlocks.BREEZE_CORAL.asItem(), PeacefulModBlocks.BREEZE_CORAL);

        blockStateModelGenerator.registerCrop(PeacefulModBlocks.FLAX_CROP, Properties.AGE_7, 0, 1, 2, 3, 4, 4, 5, 6);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(PeacefulModItems.BAT_WING, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.GUANO, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.SULPHUR, Models.GENERATED);
        
        itemModelGenerator.register(PeacefulModItems.WITHER_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.GUARDIAN_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.DRAGON_EFFIGY, Models.GENERATED);

        itemModelGenerator.registerSpawnEgg(PeacefulModItems.GHASTLING_SPAWN_EGG, 0xFFFFFF, 0x7A7A7A);
        itemModelGenerator.registerSpawnEgg(PeacefulModItems.END_CLAM_SPAWN_EGG, 0x6F4B6F, 0x2B1E2B);
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
				VariantsBlockStateSupplier.create(block)
					.coordinate(
						BlockStateVariantMap.create(Properties.PICKLES)
							.register(1, BlockStateVariant.create().put(VariantSettings.MODEL, id1))
							.register(2, BlockStateVariant.create().put(VariantSettings.MODEL, id2))
							.register(3, BlockStateVariant.create().put(VariantSettings.MODEL, id3))
							.register(4, BlockStateVariant.create().put(VariantSettings.MODEL, id4))
                    )
			);
	}
}
