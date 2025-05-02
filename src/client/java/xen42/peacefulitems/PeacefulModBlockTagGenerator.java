package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class PeacefulModBlockTagGenerator extends BlockTagProvider {
    public PeacefulModBlockTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(BlockTags.SOUL_FIRE_BASE_BLOCKS)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
            .add(PeacefulModBlocks.SULPHUR_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.SOUL_SPEED_BLOCKS)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
            .add(PeacefulModBlocks.FLAX_CROP); // similar to other crops
        
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(PeacefulModBlocks.SULPHUR_BLOCK)
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
        
        this.getOrCreateTagBuilder(PeacefulModTags.BlockTags.FOSSIL_ORES)
            .add(PeacefulModBlocks.FOSSIL_ORE)
            .add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE)
            .add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
    }
}
