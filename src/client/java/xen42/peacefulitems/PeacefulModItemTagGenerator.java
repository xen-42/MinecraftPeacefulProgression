package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class PeacefulModItemTagGenerator extends ItemTagProvider {
	public PeacefulModItemTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture,
			PeacefulModBlockTagGenerator blockTagProvider) {
		super(output, registriesFuture, blockTagProvider);
	}

	@Override
	public String getName() {
		return "PeacefulModItemTagGenerator";
	}
	
	@Override
	protected void configure(WrapperLookup wrapperLookup) {
		this.copy(BlockTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.SOUL_FIRE_BASE_BLOCKS);
		this.copy(ConventionalBlockTags.VILLAGER_JOB_SITES, ConventionalItemTags.VILLAGER_JOB_SITES);
		this.copy(ConventionalBlockTags.ORES, ConventionalItemTags.ORES);
		this.copy(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE, ConventionalItemTags.ORES_IN_GROUND_DEEPSLATE);
		this.copy(ConventionalBlockTags.ORES_IN_GROUND_STONE, ConventionalItemTags.ORES_IN_GROUND_STONE);
		this.copy(PeacefulModTags.BlockTags.ORE_BEARING_GROUND_SOUL_SOIL, PeacefulModTags.ItemTags.ORE_BEARING_GROUND_SOUL_SOIL);
		this.copy(PeacefulModTags.BlockTags.ORES_IN_GROUND_SOUL_SOIL, PeacefulModTags.ItemTags.ORES_IN_GROUND_SOUL_SOIL);
		this.copy(PeacefulModTags.BlockTags.FOSSIL_ORES, PeacefulModTags.ItemTags.FOSSIL_ORES);
		this.copy(PeacefulModTags.BlockTags.FOSSIL_ORES_C, PeacefulModTags.ItemTags.FOSSIL_ORES_C);
		this.copy(PeacefulModTags.BlockTags.SULPHUR_ORES, PeacefulModTags.ItemTags.SULPHUR_ORES);
		this.copy(PeacefulModTags.BlockTags.SULPHUR_ORES_C, PeacefulModTags.ItemTags.SULPHUR_ORES_C);
		this.copy(PeacefulModTags.BlockTags.SULFUR_ORES, PeacefulModTags.ItemTags.SULFUR_ORES);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);

		this.getOrCreateTagBuilder(ConventionalItemTags.SEEDS)
			.add(PeacefulModItems.FLAX);
		this.getOrCreateTagBuilder(ConventionalItemTags.CROPS)
			.add(PeacefulModItems.FLAX);
		this.getOrCreateTagBuilder(ConventionalItemTags.CLUMPS)
			.add(PeacefulModItems.SULPHUR);
		this.getOrCreateTagBuilder(ConventionalItemTags.CLUSTERS)
			.add(PeacefulModItems.SULPHUR);
		
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.EFFIGIES)
			.add(PeacefulModItems.DRAGON_EFFIGY)
			.add(PeacefulModItems.WITHER_EFFIGY)
			.add(PeacefulModItems.GUARDIAN_EFFIGY)
			.add(PeacefulModItems.RAID_EFFIGY);
		
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.GUANO)
			.add(PeacefulModItems.GUANO)
			.addOptional(Identifier.of("jaizmod", "guano"));
		
		this.getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
			.add(PeacefulModItems.CAPE);
		this.getOrCreateTagBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE)
			.add(PeacefulModItems.CAPE);
	}
}
