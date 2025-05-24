package xen42.peacefulitems;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PeacefulModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(PeacefulModRecipeGenerator::new);
		pack.addProvider(PeacefulModModelGenerator::new);
		pack.addProvider(PeacefulModEquipmentAssetGenerator::new);
		pack.addProvider(PeacefulModBlockLootTableGenerator::new);
		pack.addProvider(PeacefulMobEntityLootTableGenerator::new);
		PeacefulModBlockTagGenerator blockTagProvider = pack.addProvider(PeacefulModBlockTagGenerator::new);
		pack.addProvider((output, registries) -> new PeacefulModItemTagGenerator(output, registries, blockTagProvider));
		pack.addProvider(PeacefulModPOITagGenerator::new);
		pack.addProvider(PeacefulModLanguageProvider.English::new);
		pack.addProvider(PeacefulModLanguageProvider.EnglishUpsideDown::new);
		pack.addProvider(PeacefulModLanguageProvider.ChineseSimplified::new);
	}
}
