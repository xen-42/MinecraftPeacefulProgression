package xen42.peacefulitems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class PeacefulModModelGenerator extends FabricModelProvider {

    public PeacefulModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SULPHUR_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(PeacefulModBlocks.SULPHUR_ORE);
        blockStateModelGenerator.registerAmethyst(PeacefulModBlocks.SULPHUR_CLUSTER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(PeacefulModItems.BAT_WING, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.GUANO, Models.GENERATED);
        itemModelGenerator.register(PeacefulModItems.SULPHUR, Models.GENERATED);
    }

    @Override
    public String getName() {
        return "PeacefulModModelGenerator";
    }
    
}
