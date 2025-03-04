package xen42.peacefulitems;

import java.util.function.Function;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AmethystBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class PeacefulModBlocks {

    public static void initialize() { }

    public static final Block SULPHUR_BLOCK = register(
		"sulphur_block",
		Block::new,
		AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).strength(3.0f, 3.0f).requiresTool(),
		true
    );

	public static final Block SULPHUR_CLUSTER = register(
		"sulphur_cluster",
		setting -> new SulphurClusterBlock(7, 3, setting),
		AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).solid().nonOpaque().pistonBehavior(PistonBehavior.DESTROY)
			.sounds(BlockSoundGroup.NETHERRACK).breakInstantly(),
		true
	);

	public static final Block SULPHUR_ORE = register(
		"sulphur_ore",
		Block::new,
		AbstractBlock.Settings.copy(Blocks.SOUL_SOIL),
		true
	);

	public static final Block FOSSIL_ORE = register(
		"fossil_ore",
		Block::new,
		AbstractBlock.Settings.copy(Blocks.COAL_ORE),
		true
	);

	public static final Block DEEPSLATE_FOSSIL_ORE = register(
		"deepslate_fossil_ore",
		Block::new,
		AbstractBlock.Settings.copy(Blocks.DEEPSLATE_COAL_ORE),
		true
	);

	public static final Block SOUL_SOIL_FOSSIL_ORE = register(
		"soul_soil_fossil_ore",
		Block::new,
		AbstractBlock.Settings.copy(Blocks.SOUL_SOIL),
		true
	);

	public static final FlaxCropBlock FLAX_CROP = (FlaxCropBlock)register(
		"flax_crop",
		FlaxCropBlock::new,
		AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP),
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

			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
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
