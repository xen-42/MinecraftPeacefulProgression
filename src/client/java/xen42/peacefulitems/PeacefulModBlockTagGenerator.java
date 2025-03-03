package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class PeacefulModBlockTagGenerator extends BlockTagProvider {
    public PeacefulModBlockTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(BlockTags.SOUL_FIRE_BASE_BLOCKS)
            .add(PeacefulModBlocks.SULPHUR_BLOCK);
        
        this.getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD)
            .add(PeacefulModBlocks.SULPHUR_BLOCK);
        
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(PeacefulModBlocks.SULPHUR_BLOCK);
    }
}
