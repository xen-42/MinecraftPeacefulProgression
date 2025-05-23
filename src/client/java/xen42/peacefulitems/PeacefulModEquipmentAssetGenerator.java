package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput.OutputType;
import net.minecraft.data.DataOutput.PathResolver;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;

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
		return DataProvider.writeToPath(writer, new JsonObject(), this.pathResolver.resolveJson(PeacefulModItems.CAPE_EQUIPMENT_ASSET));
	}
}
