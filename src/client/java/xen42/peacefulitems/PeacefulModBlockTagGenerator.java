package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class PeacefulModBlockTagGenerator extends BlockTagProvider {
    public PeacefulModBlockTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "PeacefulModBlockTagGenerator";
    }
    
    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(BlockTags.SOUL_FIRE_BASE_BLOCKS)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_STAIRS)
            .add(PeacefulModBlocks.SULPHUR_SLAB)
            .add(PeacefulModBlocks.SULPHUR_WALL)
            .add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_STAIRS)
            .add(PeacefulModBlocks.SULPHUR_SLAB)
            .add(PeacefulModBlocks.SULPHUR_WALL)
            .add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.SOUL_SPEED_BLOCKS)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
            .add(PeacefulModBlocks.FLAX_CROP); // similar to other crops
        
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_STAIRS)
            .add(PeacefulModBlocks.SULPHUR_SLAB)
            .add(PeacefulModBlocks.SULPHUR_WALL)
            .add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK)
            .add(PeacefulModBlocks.FOSSIL_ORE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .add(Blocks.SAND)
            .add(Blocks.GRAVEL)
            .add(Blocks.SOUL_SAND)
            .add(Blocks.SOUL_SOIL);

        this.getOrCreateTagBuilder(BlockTags.CROPS)
            .add(PeacefulModBlocks.FLAX_CROP);
        
        this.getOrCreateTagBuilder(BlockTags.MAINTAINS_FARMLAND)
            .add(PeacefulModBlocks.FLAX_CROP);
        
        this.getOrCreateTagBuilder(ConventionalBlockTags.VILLAGER_JOB_SITES)
            .add(Blocks.JUKEBOX);
        
        this.getOrCreateTagBuilder(ConventionalBlockTags.CLUSTERS)
            .add(PeacefulModBlocks.SULPHUR_CLUSTER);
        
        this.getOrCreateTagBuilder(ConventionalBlockTags.STORAGE_BLOCKS)
            .add(PeacefulModBlocks.SULPHUR_BLOCK);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.SULPHUR_ORES)
            .add(PeacefulModBlocks.SULPHUR_ORE);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.SULPHUR_ORES_C)
            .addOptionalTag(PeacefulModTags.BlockTags.SULPHUR_ORES);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.SULFUR_ORES)
            .addOptionalTag(PeacefulModTags.BlockTags.SULPHUR_ORES);

        this.getOrCreateTagBuilder(BlockTags.STAIRS)
            .add(PeacefulModBlocks.SULPHUR_STAIRS);
        
        this.getOrCreateTagBuilder(BlockTags.SLABS)
            .add(PeacefulModBlocks.SULPHUR_SLAB);
        
        this.getOrCreateTagBuilder(BlockTags.WALLS)
            .add(PeacefulModBlocks.SULPHUR_WALL);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.FOSSIL_ORES)
            .add(PeacefulModBlocks.FOSSIL_ORE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.FOSSIL_ORES_C)
            .addOptionalTag(PeacefulModTags.BlockTags.FOSSIL_ORES);

        this.getOrCreateTagBuilder(ConventionalBlockTags.ORES)
            .addOptionalTag(PeacefulModTags.BlockTags.SULPHUR_ORES)
            .addOptionalTag(PeacefulModTags.BlockTags.FOSSIL_ORES);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.ORE_BEARING_GROUND_SOUL_SOIL)
            .add(Blocks.SOUL_SOIL);
        
        this.getOrCreateTagBuilder(ConventionalBlockTags.ORES_IN_GROUND_STONE)
            .add(PeacefulModBlocks.FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.ORES_IN_GROUND_SOUL_SOIL)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);

        this.aliasGroup("ores/sulphur").add(PeacefulModTags.BlockTags.SULPHUR_ORES);
        this.aliasGroup("ores/sulfur").add(PeacefulModTags.BlockTags.SULFUR_ORES);
        this.aliasGroup("ores/fossil").add(PeacefulModTags.BlockTags.FOSSIL_ORES);
        
        this.getOrCreateTagBuilder(BlockTags.CAULDRONS)
	        .add(PeacefulModBlocks.DRAGON_BREATH_CAULDRON);
    }
}
