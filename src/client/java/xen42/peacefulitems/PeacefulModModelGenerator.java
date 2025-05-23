package xen42.peacefulitems;

import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.item.Item;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class PeacefulModModelGenerator extends FabricModelProvider {

    public PeacefulModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    	blockStateModelGenerator.registerParentedItemModel(PeacefulModBlocks.EFFIGY_ALTAR, ModelIds.getBlockModelId(PeacefulModBlocks.EFFIGY_ALTAR));
        registerSpawnEgg(blockStateModelGenerator, PeacefulModItems.GHASTLING_SPAWN_EGG);
        registerSpawnEgg(blockStateModelGenerator, PeacefulModItems.END_CLAM_SPAWN_EGG);

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
        registerWithBrokenCondition(itemModelGenerator, PeacefulModItems.CAPE);
        itemModelGenerator.register(PeacefulModItems.GUANO, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.ECTOPLASM, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.SULPHUR, Models.GENERATED);
        
        itemModelGenerator.register(PeacefulModItems.WITHER_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.GUARDIAN_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.DRAGON_EFFIGY, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.RAID_EFFIGY, Models.GENERATED);

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
    
    public void registerWithBrokenCondition(ItemModelGenerator itemModelGenerator, Item item) {
    	Identifier model = ModelIds.getItemModelId(item);
    	Identifier brokenModel = model.withSuffixedPath("_broken");
    	Identifier texture = TextureMap.getId(item);
    	Identifier brokenTexture = texture.withSuffixedPath("_broken");
		Models.GENERATED.upload(model, TextureMap.layer0(texture), itemModelGenerator.writer, (identifier, textures) -> createBrokenConditionJson(model, brokenModel, textures));
		Models.GENERATED.upload(brokenModel, TextureMap.layer0(brokenTexture), itemModelGenerator.writer);
    }

	public final JsonObject createBrokenConditionJson(Identifier id,
			Identifier brokenId, Map<TextureKey, Identifier> textures) {
		JsonObject model = Models.GENERATED.createJson(id, textures);
		JsonArray overrides = new JsonArray();

		JsonObject override = new JsonObject();
		JsonObject predicate = new JsonObject();
		predicate.addProperty("broken", 1);
		override.add("predicate", predicate);
		override.addProperty("model", brokenId.toString());
		overrides.add(override);

		model.add("overrides", overrides);
		return model;
	}

    public void registerSpawnEgg(BlockStateModelGenerator blockStateModelGenerator, Item item) {
    	blockStateModelGenerator.registerParentedItemModel(item, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
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
    
    public void registerCauldron(BlockStateModelGenerator blockStateModelGenerator, Block cauldronBlock, Identifier fluidTexture) {
    	TextureMap cauldronTextureMap = TextureMap.cauldron(fluidTexture);
        blockStateModelGenerator.blockStateCollector
			.accept(
				VariantsBlockStateSupplier.create(cauldronBlock)
					.coordinate(
						BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL)
							.register(
								1,
								BlockStateVariant.create().put(
									VariantSettings.MODEL,
									Models.TEMPLATE_CAULDRON_LEVEL1
										.upload(cauldronBlock, "_level1", cauldronTextureMap, blockStateModelGenerator.modelCollector)
								)
							)
							.register(
								2,
								BlockStateVariant.create().put(
									VariantSettings.MODEL,
									Models.TEMPLATE_CAULDRON_LEVEL2
										.upload(cauldronBlock, "_level2", cauldronTextureMap, blockStateModelGenerator.modelCollector)
								)
							)
							.register(
								3,
								BlockStateVariant.create().put(
									VariantSettings.MODEL,
									Models.TEMPLATE_CAULDRON_FULL
										.upload(cauldronBlock, "_full", cauldronTextureMap, blockStateModelGenerator.modelCollector)
								)
							)
					)
			);
    }
}
