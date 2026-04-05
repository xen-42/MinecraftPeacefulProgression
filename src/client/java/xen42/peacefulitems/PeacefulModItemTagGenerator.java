package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
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
		this.copy(PeacefulModTags.BlockTags.ORES_IN_GROUND_DEEPSLATE, PeacefulModTags.ItemTags.ORES_IN_GROUND_DEEPSLATE);
		this.copy(PeacefulModTags.BlockTags.ORES_IN_GROUND_STONE, PeacefulModTags.ItemTags.ORES_IN_GROUND_STONE);
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

		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.SEEDS)
			.add(PeacefulModItems.FLAX);
		this.getOrCreateTagBuilder(ItemTags.VILLAGER_PLANTABLE_SEEDS)
			.add(PeacefulModItems.FLAX);
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.CROPS)
			.add(PeacefulModItems.FLAX);
		this.getOrCreateTagBuilder(ConventionalItemTags.CLUSTERS)
			.add(PeacefulModItems.SULPHUR);
		this.getOrCreateTagBuilder(ConventionalItemTags.FOODS)
			.add(PeacefulModItems.FLAX)
			.add(PeacefulModItems.CLAM)
			.add(PeacefulModItems.COOKED_CLAM);
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.MEAT)
			.add(Items.BEEF)
			.add(Items.COOKED_BEEF)
			.add(Items.CHICKEN)
			.add(Items.COOKED_CHICKEN)
			.add(Items.MUTTON)
			.add(Items.COOKED_MUTTON)
			.add(Items.PORKCHOP)
			.add(Items.COOKED_PORKCHOP)
			.add(Items.RABBIT)
			.add(Items.COOKED_RABBIT);
		
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.EFFIGIES)
			.add(PeacefulModItems.DRAGON_EFFIGY)
			.add(PeacefulModItems.WITHER_EFFIGY)
			.add(PeacefulModItems.GUARDIAN_EFFIGY)
			.add(PeacefulModItems.RAID_EFFIGY);
		
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.GUANO)
			.add(PeacefulModItems.GUANO)
			.addOptional(Identifier.of("jaizmod", "guano"));
		
		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.WISP_LIKES)
			.add(Items.COOKED_COD)
			.add(Items.COOKED_SALMON)
			.add(Items.COOKED_BEEF)
			.add(Items.COOKED_PORKCHOP)
			.add(Items.COOKED_CHICKEN)
			.add(Items.COOKED_RABBIT)
			.add(Items.COOKED_MUTTON)
			.add(Items.CARROT)
			.add(Items.GOLDEN_CARROT)
			.add(Items.POTATO)
			.add(Items.BEETROOT)
			.add(PeacefulModItems.COOKED_CLAM)
			.add(PeacefulModItems.SULPHUR)
			.add(Items.QUARTZ)
			.add(Items.GOLD_NUGGET);

		this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.WISP_DISLIKES)
			.add(Items.COD)
			.add(Items.SALMON)
			.add(Items.TROPICAL_FISH)
			.add(Items.PUFFERFISH)
			.add(Items.BEEF)
			.add(Items.PORKCHOP)
			.add(Items.CHICKEN)
			.add(Items.RABBIT)
			.add(Items.MUTTON)
			.add(PeacefulModItems.CLAM)
			.addOptionalTag(PeacefulModTags.ItemTags.CROPS_ONION)
			.addOptionalTag(PeacefulModTags.ItemTags.FOODS_VEGETABLES_ONION)
			.addOptionalTag(PeacefulModTags.ItemTags.FOODS_VEGETABLES_ONIONS)
			.addOptionalTag(PeacefulModTags.ItemTags.GUANO);
	}
}
