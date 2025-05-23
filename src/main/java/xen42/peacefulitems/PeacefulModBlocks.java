package xen42.peacefulitems;

import java.util.Set;
import java.util.function.Function;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.blocks.BlazePickleBlock;
import xen42.peacefulitems.blocks.BreezeCoralBlock;
import xen42.peacefulitems.blocks.DragonBreathCauldronBlock;
import xen42.peacefulitems.blocks.EffigyAltarBlock;
import xen42.peacefulitems.blocks.FlaxCropBlock;
import xen42.peacefulitems.blocks.FossilOreBlock;
import xen42.peacefulitems.blocks.FossilOreBlockEntity;
import xen42.peacefulitems.blocks.SulphurClusterBlock;

public class PeacefulModBlocks {

	public static void initialize() { }

	public static final Block SULPHUR_BLOCK = register(
		"sulphur_block",
		Block::new,
		AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).strength(3.0f, 3.0f).requiresTool().instrument(NoteBlockInstrument.BASEDRUM),
		true
	);

	public static final Block SULPHUR_STAIRS = register(
		"sulphur_stairs",
		settings -> new StairsBlock(SULPHUR_BLOCK.getDefaultState(), settings),
		AbstractBlock.Settings.copy(SULPHUR_BLOCK),
		true
	);

	public static final Block SULPHUR_SLAB = register(
		"sulphur_slab",
		SlabBlock::new,
		AbstractBlock.Settings.copy(SULPHUR_BLOCK),
		true
	);

	public static final Block SULPHUR_WALL = register(
		"sulphur_wall",
		WallBlock::new,
		AbstractBlock.Settings.copy(SULPHUR_BLOCK),
		true
	);

	public static final Block CHISELED_SULPHUR_BLOCK = register(
		"chiseled_sulphur_block",
		Block::new,
		AbstractBlock.Settings.copy(SULPHUR_BLOCK),
		true
	);
	
	public static final BlockFamily SULPHUR = BlockFamilies.register(SULPHUR_BLOCK)
			.wall(SULPHUR_WALL)
			.stairs(SULPHUR_STAIRS)
			.slab(SULPHUR_SLAB)
			.chiseled(CHISELED_SULPHUR_BLOCK)
			.group("sulphur")
			.noGenerateRecipes()
			.build();

	public static final Block SULPHUR_CLUSTER = register(
		"sulphur_cluster",
		setting -> new SulphurClusterBlock(7, 3, setting),
		AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).solid().nonOpaque().pistonBehavior(PistonBehavior.DESTROY)
			.sounds(BlockSoundGroup.NETHERRACK).breakInstantly(),
		false
	);

	public static final Block SULPHUR_ORE = register(
		"sulphur_ore",
		Block::new,
		AbstractBlock.Settings.copy(Blocks.SOUL_SOIL),
		true
	);

	public static final Block FOSSIL_ORE = register(
		"fossil_ore",
		(settings) -> new FossilOreBlock(Blocks.STONE, SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, SoundEvents.BLOCK_STONE_BREAK, settings),
		AbstractBlock.Settings.copy(Blocks.STONE),
		true
	);

	public static final Block DEEPSLATE_FOSSIL_ORE = register(
		"deepslate_fossil_ore",
		(settings) -> new FossilOreBlock(Blocks.DEEPSLATE, SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, SoundEvents.BLOCK_DEEPSLATE_BREAK, settings),
		AbstractBlock.Settings.copy(Blocks.DEEPSLATE),
		true
	);

	public static final Block SOUL_SOIL_FOSSIL_ORE = register(
		"soul_soil_fossil_ore",
		(settings) -> new FossilOreBlock(Blocks.SOUL_SOIL, SoundEvents.ITEM_BRUSH_BRUSHING_SAND, SoundEvents.ITEM_BRUSH_BRUSHING_SAND_COMPLETE, settings),
		AbstractBlock.Settings.copy(Blocks.SOUL_SOIL),
		true
	);

	public static <T extends BlockEntityType<?>> T registerBlockEntityType(String path, T blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(PeacefulMod.MOD_ID, path), blockEntityType);
	}
	
	public static final BlockEntityType<FossilOreBlockEntity> FOSSIL_ORE_ENTITY = registerBlockEntityType(
		"fossil_ore_entity",
		FabricBlockEntityTypeBuilder.create(FossilOreBlockEntity::new, 
			new Block[] { FOSSIL_ORE, DEEPSLATE_FOSSIL_ORE, SOUL_SOIL_FOSSIL_ORE}).build()
	);

	public static final FlaxCropBlock FLAX_CROP = (FlaxCropBlock)register(
		"flax_crop",
		FlaxCropBlock::new,
		AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP),
		false
	);

	public static final BlazePickleBlock BLAZE_PICKLE = (BlazePickleBlock)register(
		"blaze_pickle",
		BlazePickleBlock::new,
		AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.NETHER_WART).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).luminance(state -> 3 + 3 * (Integer)state.get(SeaPickleBlock.PICKLES)),
		true
	);

	
	public static final BlazePickleBlock BREEZE_CORAL = (BreezeCoralBlock)register(
		"breeze_coral",
		BreezeCoralBlock::new,
		AbstractBlock.Settings.create().mapColor(MapColor.GRAY).sounds(BlockSoundGroup.NETHER_WART).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).luminance(state -> 3 + 3 * (Integer)state.get(SeaPickleBlock.PICKLES)),
		true
	);

	public static final EffigyAltarBlock EFFIGY_ALTAR = (EffigyAltarBlock)register(
		"effigy_altar", EffigyAltarBlock::new, AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque(), true);

	public static final DragonBreathCauldronBlock DRAGON_BREATH_CAULDRON = (DragonBreathCauldronBlock)register(
		"dragon_breath_cauldron",
		DragonBreathCauldronBlock::new,
		AbstractBlock.Settings.copy(Blocks.CAULDRON).luminance(state -> 15).strength(2.0F, 6.0F).requiresTool().mapColor(MapColor.PURPLE),
		false
	);

	private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
		// Create a registry key for the block
		RegistryKey<Block> blockKey = keyOfBlock(name);
		// Create the block instance
		Block block = blockFactory.apply(settings.registryKey(blockKey));

		// Sometimes, you may not want to register an item for the block.
		// Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
		if (shouldRegisterItem) {
			// Items need to be registered with a different type of registry key, but the ID
			// can be the same.
			RegistryKey<Item> itemKey = keyOfItem(name);

			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
			Registry.register(Registries.ITEM, itemKey, blockItem);
		}

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	private static RegistryKey<Block> keyOfBlock(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(PeacefulMod.MOD_ID, name));
	}

	private static RegistryKey<Item> keyOfItem(String name) {
		return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PeacefulMod.MOD_ID, name));
	}
}
