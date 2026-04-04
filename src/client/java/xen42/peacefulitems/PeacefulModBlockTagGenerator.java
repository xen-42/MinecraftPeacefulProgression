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
        this.valueLookupBuilder(BlockTags.SOUL_FIRE_BASE_BLOCKS)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_STAIRS)
            .add(PeacefulModBlocks.SULPHUR_SLAB)
            .add(PeacefulModBlocks.SULPHUR_WALL)
            .add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.valueLookupBuilder(BlockTags.INFINIBURN_OVERWORLD)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_STAIRS)
            .add(PeacefulModBlocks.SULPHUR_SLAB)
            .add(PeacefulModBlocks.SULPHUR_WALL)
            .add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.valueLookupBuilder(BlockTags.SOUL_SPEED_BLOCKS)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.valueLookupBuilder(BlockTags.AXE_MINEABLE)
            .add(PeacefulModBlocks.FLAX_CROP); // similar to other crops
        
        this.valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_STAIRS)
            .add(PeacefulModBlocks.SULPHUR_SLAB)
            .add(PeacefulModBlocks.SULPHUR_WALL)
            .add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK)
            .add(PeacefulModBlocks.FOSSIL_ORE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        
        this.valueLookupBuilder(BlockTags.SHOVEL_MINEABLE)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.valueLookupBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .add(Blocks.SAND)
            .add(Blocks.GRAVEL)
            .add(Blocks.SOUL_SAND)
            .add(Blocks.SOUL_SOIL);

        this.valueLookupBuilder(BlockTags.CROPS)
            .add(PeacefulModBlocks.FLAX_CROP);
        
        this.valueLookupBuilder(BlockTags.MAINTAINS_FARMLAND)
            .add(PeacefulModBlocks.FLAX_CROP);
        
        this.valueLookupBuilder(ConventionalBlockTags.VILLAGER_JOB_SITES)
            .add(Blocks.JUKEBOX);
        
        this.valueLookupBuilder(ConventionalBlockTags.CLUSTERS)
            .add(PeacefulModBlocks.SULPHUR_CLUSTER);
        
        this.valueLookupBuilder(ConventionalBlockTags.STORAGE_BLOCKS)
            .add(PeacefulModBlocks.SULPHUR_BLOCK);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.SULPHUR_ORES)
            .add(PeacefulModBlocks.SULPHUR_ORE);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.SULPHUR_ORES_C)
            .addOptionalTag(PeacefulModTags.BlockTags.SULPHUR_ORES);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.SULFUR_ORES)
            .addOptionalTag(PeacefulModTags.BlockTags.SULPHUR_ORES);

        this.valueLookupBuilder(BlockTags.STAIRS)
            .add(PeacefulModBlocks.SULPHUR_STAIRS);
        
        this.valueLookupBuilder(BlockTags.SLABS)
            .add(PeacefulModBlocks.SULPHUR_SLAB);
        
        this.valueLookupBuilder(BlockTags.WALLS)
            .add(PeacefulModBlocks.SULPHUR_WALL);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.FOSSIL_ORES)
            .add(PeacefulModBlocks.FOSSIL_ORE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.FOSSIL_ORES_C)
            .addOptionalTag(PeacefulModTags.BlockTags.FOSSIL_ORES);

        this.valueLookupBuilder(ConventionalBlockTags.ORES)
            .addOptionalTag(PeacefulModTags.BlockTags.SULPHUR_ORES)
            .addOptionalTag(PeacefulModTags.BlockTags.FOSSIL_ORES);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.ORE_BEARING_GROUND_SOUL_SOIL)
            .add(Blocks.SOUL_SOIL);
        
        this.valueLookupBuilder(ConventionalBlockTags.ORES_IN_GROUND_STONE)
            .add(PeacefulModBlocks.FOSSIL_ORE);
        
        this.valueLookupBuilder(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        
        this.valueLookupBuilder(PeacefulModTags.BlockTags.ORES_IN_GROUND_SOUL_SOIL)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);

        this.aliasGroup("ores/sulphur").add(PeacefulModTags.BlockTags.SULPHUR_ORES);
        this.aliasGroup("ores/sulfur").add(PeacefulModTags.BlockTags.SULFUR_ORES);
        this.aliasGroup("ores/fossil").add(PeacefulModTags.BlockTags.FOSSIL_ORES);
        
        this.valueLookupBuilder(BlockTags.CAULDRONS)
	        .add(PeacefulModBlocks.DRAGON_BREATH_CAULDRON);
    }
}
