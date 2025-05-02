package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class PeacefulModItemTagGenerator extends ItemTagProvider {
    public PeacefulModItemTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        this.copy(BlockTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.SOUL_FIRE_BASE_BLOCKS);
        this.copy(PeacefulModTags.BlockTags.FOSSIL_ORES, PeacefulModTags.ItemTags.FOSSIL_ORES);
        
        this.getOrCreateTagBuilder(PeacefulModTags.ItemTags.EFFIGIES)
            .add(PeacefulModItems.DRAGON_EFFIGY)
            .add(PeacefulModItems.WITHER_EFFIGY)
            .add(PeacefulModItems.GUARDIAN_EFFIGY);
    }
}
