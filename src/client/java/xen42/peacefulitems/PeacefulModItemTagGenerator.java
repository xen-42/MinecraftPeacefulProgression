package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Items;
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

		this.valueLookupBuilder(ConventionalItemTags.SEEDS)
			.add(PeacefulModItems.FLAX);
		this.valueLookupBuilder(ItemTags.CHICKEN_FOOD)
			.add(PeacefulModItems.FLAX);
		this.valueLookupBuilder(ItemTags.PARROT_FOOD)
			.add(PeacefulModItems.FLAX);
		this.valueLookupBuilder(ItemTags.VILLAGER_PLANTABLE_SEEDS)
			.add(PeacefulModItems.FLAX);
		this.valueLookupBuilder(ConventionalItemTags.CROPS)
			.add(PeacefulModItems.FLAX);
		this.valueLookupBuilder(ConventionalItemTags.CLUMPS)
			.add(PeacefulModItems.SULPHUR);
		this.valueLookupBuilder(ConventionalItemTags.CLUSTERS)
			.add(PeacefulModItems.SULPHUR);
		this.valueLookupBuilder(ConventionalItemTags.FOOD_POISONING_FOODS)
			.add(PeacefulModItems.CLAM);
		this.valueLookupBuilder(ConventionalItemTags.COOKED_FISH_FOODS)
			.add(PeacefulModItems.COOKED_CLAM);
		this.valueLookupBuilder(ConventionalItemTags.RAW_FISH_FOODS)
			.add(PeacefulModItems.CLAM);
		
		this.valueLookupBuilder(PeacefulModTags.ItemTags.EFFIGIES)
			.add(PeacefulModItems.DRAGON_EFFIGY)
			.add(PeacefulModItems.WITHER_EFFIGY)
			.add(PeacefulModItems.GUARDIAN_EFFIGY)
			.add(PeacefulModItems.RAID_EFFIGY);
		
		this.valueLookupBuilder(PeacefulModTags.ItemTags.GUANO)
			.add(PeacefulModItems.GUANO);
        this.getTagBuilder(PeacefulModTags.ItemTags.GUANO)
			.addOptional(Identifier.of("jaizmod", "guano"));
		
		this.valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
			.add(PeacefulModItems.CAPE);
		this.valueLookupBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE)
			.add(PeacefulModItems.CAPE);
		
		this.valueLookupBuilder(PeacefulModTags.ItemTags.WISP_LIKES)
			.addOptionalTag(ConventionalItemTags.COOKED_FISH_FOODS)
            .addOptionalTag(ConventionalItemTags.COOKED_MEAT_FOODS)
            .addOptionalTag(ConventionalItemTags.VEGETABLE_FOODS)
			.add(PeacefulModItems.SULPHUR)
			.add(Items.QUARTZ)
			.add(Items.GOLD_NUGGET);

		this.valueLookupBuilder(PeacefulModTags.ItemTags.WISP_DISLIKES)
			.addOptionalTag(PeacefulModTags.ItemTags.GUANO)
            .addOptionalTag(ConventionalItemTags.RAW_FISH_FOODS)
            .addOptionalTag(ConventionalItemTags.RAW_MEAT_FOODS);
	}
}
