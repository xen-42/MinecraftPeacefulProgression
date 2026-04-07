package xen42.peacefulitems;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

public class PeacefulModVillageAdditions {

	public static void initialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(PeacefulModVillageAdditions::addNewVillageBuilding);
	}

	public static final Identifier PROCESSOR_EMPTY = Identifier.of("minecraft", "empty");
	public static final Identifier PROCESSOR_MOSSIFY_10_PERCENT = Identifier.of("minecraft", "mossify_10_percent");

	public static void addNewVillageBuilding(MinecraftServer server) {
		PeacefulMod.LOGGER.info("Adding new village buildings");
		
		Registry <StructurePool> templatePools = server.getRegistryManager().getOptional(RegistryKeys.TEMPLATE_POOL).get();
		Registry<StructureProcessorList> processorLists = server.getRegistryManager().getOptional(RegistryKeys.PROCESSOR_LIST).get();

		addLegacyBuildingToPool(templatePools, processorLists, 
			Identifier.of("minecraft", "village/plains/houses"), 
			Identifier.of(PeacefulMod.MOD_ID , "village/plains/houses/plains_dj_house_1"), 
			PROCESSOR_MOSSIFY_10_PERCENT, 2, StructurePool.Projection.RIGID);

		addLegacyBuildingToPool(templatePools, processorLists, 
			Identifier.of("minecraft", "village/taiga/houses"), 
			Identifier.of(PeacefulMod.MOD_ID, "village/taiga/houses/taiga_dj_house_1"), 
			PROCESSOR_MOSSIFY_10_PERCENT, 2, StructurePool.Projection.RIGID);

		addLegacyBuildingToPool(templatePools, processorLists, 
			Identifier.of("minecraft", "village/savanna/houses"), 
			Identifier.of(PeacefulMod.MOD_ID, "village/savanna/houses/savanna_dj_house_1"), 
			PROCESSOR_EMPTY, 2, StructurePool.Projection.RIGID);

		addLegacyBuildingToPool(templatePools, processorLists, 
			Identifier.of("minecraft", "village/desert/houses"), 
			Identifier.of(PeacefulMod.MOD_ID, "village/desert/houses/desert_dj_house_1"), 
			PROCESSOR_EMPTY, 2, StructurePool.Projection.RIGID);

		addLegacyBuildingToPool(templatePools, processorLists, 
			Identifier.of("minecraft", "village/snowy/houses"), 
			Identifier.of(PeacefulMod.MOD_ID, "village/snowy/houses/snowy_dj_house_1"), 
			PROCESSOR_EMPTY, 2, StructurePool.Projection.RIGID);
	}

	public static void addLegacyBuildingToPool(
		Registry<StructurePool> templatePoolRegistry, 
		Registry<StructureProcessorList> processorListRegistry, 
		Identifier poolRL, 
		Identifier nbtPieceRL, 
		Identifier processorListRL, 
		int weight,
		StructurePool.Projection projection
	) {
        addBuildingToPool(
            templatePoolRegistry,
            processorListRegistry,
            poolRL,
            nbtPieceRL,
            processorListRL,
            weight,
            projection,
            true
        );
	}
	
	public static void addBuildingToPool(
		Registry<StructurePool> templatePoolRegistry, 
		Registry<StructureProcessorList> processorListRegistry, 
		Identifier poolRL, 
		Identifier nbtPieceRL, 
		Identifier processorListRL, 
		int weight,
		StructurePool.Projection projection
	) {
        addBuildingToPool(
            templatePoolRegistry,
            processorListRegistry,
            poolRL,
            nbtPieceRL,
            processorListRL,
            weight,
            projection,
            false
        );
	}
	
	public static void addBuildingToPool(
		Registry<StructurePool> templatePoolRegistry, 
		Registry<StructureProcessorList> processorListRegistry, 
		Identifier poolRL, 
		Identifier nbtPieceRL, 
		Identifier processorListRL, 
		int weight,
		StructurePool.Projection projection,
		boolean shouldUseLegacySingePoolElement
	) {
		PeacefulMod.LOGGER.info("Adding {}single pool element {} with processor {} to pool {} at weight {} and projection {}", shouldUseLegacySingePoolElement ? "legacy " : "", nbtPieceRL, processorListRL, poolRL, weight, projection);
		
		StructurePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) {
			PeacefulMod.LOGGER.info("Failed to get pool {}", poolRL);
			return;
		}

		var optionalPH = processorListRegistry.getEntry(RegistryKey.of(RegistryKeys.PROCESSOR_LIST, processorListRL));
		if (optionalPH == null) {
			PeacefulMod.LOGGER.info("Failed to get processor {}", processorListRL);
			return;
		}
		
		RegistryEntry <StructureProcessorList> processorHolder = optionalPH.get();

		SinglePoolElement piece = shouldUseLegacySingePoolElement ?
            LegacySinglePoolElement.ofProcessedLegacySingle(nbtPieceRL.toString(), processorHolder).apply(projection) :
            SinglePoolElement.ofProcessedSingle(nbtPieceRL.toString(), processorHolder).apply(projection);

		for (int i = 0; i < weight; i++) {
			pool.elements.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<Pair<StructurePoolElement, Integer>>(pool.elementCounts);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.elementCounts = listOfPieceEntries;
	}
}
