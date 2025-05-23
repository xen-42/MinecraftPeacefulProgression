package xen42.peacefulitems;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.data.DataOutput.OutputType;
import net.minecraft.data.DataOutput.PathResolver;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

public class PeacefulModEquipmentAssetGenerator implements DataProvider {
	private final PathResolver pathResolver;

	public PeacefulModEquipmentAssetGenerator(FabricDataOutput output) {
		this.pathResolver = output.getResolver(OutputType.RESOURCE_PACK, "equipment");
	}

	@Override
	public String getName() {
		return "PeacefulModEquipmentAssetGenerator";
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		Map<Identifier, EquipmentModel> map = new HashMap<Identifier, EquipmentModel>();
		map.put(
			PeacefulModItems.CAPE_EQUIPMENT_ASSET,
			EquipmentModel.builder().addLayers(EquipmentModel.LayerType.WINGS, new EquipmentModel.Layer(PeacefulModItems.CAPE_EQUIPMENT_ASSET, Optional.empty(), false)).build()
		);
		return DataProvider.writeAllToPath(writer, EquipmentModel.CODEC, this.pathResolver, map);
	}
}
