package xen42.peacefulitems;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public class PeacefulModTags {
	public class BlockTags {
		public static final TagKey<Block> FOSSIL_ORES = ofBlock("fossil_ores");
		public static final TagKey<Block> FOSSIL_ORES_C = ofBlockC("ores/fossil");
		public static final TagKey<Block> SULPHUR_ORES = ofBlock("sulphur_ores");
		public static final TagKey<Block> SULPHUR_ORES_C = ofBlockC("ores/sulphur");
		public static final TagKey<Block> SULFUR_ORES = ofBlockC("ores/sulfur");
		public static final TagKey<Block> ORE_BEARING_GROUND_SOUL_SOIL = ofBlockC("ore_bearing_ground/soul_soil");
		public static final TagKey<Block> ORES_IN_GROUND_SOUL_SOIL = ofBlockC("ores_in_ground/soul_soil");

		public static final TagKey<Block> ORES_IN_GROUND_STONE = ofBlockC("ores_in_ground/stone");
		public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = ofBlockC("ores_in_ground/deepslate");
	}
	
	public class ItemTags {
		public static final TagKey<Item> EFFIGIES = ofItem("effigies");
		public static final TagKey<Item> FOSSIL_ORES = ofItem("fossil_ores");
		public static final TagKey<Item> FOSSIL_ORES_C = ofItemC("ores/fossil");
		public static final TagKey<Item> SULPHUR_ORES = ofItem("sulphur_ores");
		public static final TagKey<Item> SULPHUR_ORES_C = ofItemC("ores/sulphur");
		public static final TagKey<Item> SULFUR_ORES = ofItemC("ores/sulfur");
		public static final TagKey<Item> ORE_BEARING_GROUND_SOUL_SOIL = ofItemC("ore_bearing_ground/soul_soil");
		public static final TagKey<Item> ORES_IN_GROUND_SOUL_SOIL = ofItemC("ores_in_ground/soul_soil");
		public static final TagKey<Item> GUANO = ofItemC("guano");
		public static final TagKey<Item> WISP_LIKES = ofItem("wisp_likes");
		public static final TagKey<Item> WISP_DISLIKES = ofItem("wisp_dislikes");

		public static final TagKey<Item> ORES_IN_GROUND_STONE = ofItemC("ores_in_ground/stone");
		public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = ofItemC("ores_in_ground/deepslate");
        public static final TagKey<Item> SEEDS = ofItemC("seeds");
        public static final TagKey<Item> CROPS = ofItemC("crops");
		public static final TagKey<Item> MEAT = ofItemVanilla("meat");
	}
	
	public class StructureTags {
		public static final TagKey<Structure> EFFIGY_ALTAR_DUNGEON = ofStructure("effigy_altar_dungeon");
		public static final TagKey<Structure> TRAIL_RUINS = ofStructure("trail_ruins");
	}

	public static TagKey<Block> ofBlock(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of(PeacefulMod.MOD_ID, name));
	}

	public static TagKey<Block> ofBlockC(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", name));
	}

	public static TagKey<Item> ofItem(String name) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of(PeacefulMod.MOD_ID, name));
	}

	public static TagKey<Item> ofItemC(String name) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
	}

	public static TagKey<Item> ofItemVanilla(String name) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of("minecraft", name));
	}
	
	public static TagKey<EntityType<?>> ofEntity(String name) {
		return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(PeacefulMod.MOD_ID, name));
	}
	
	public static TagKey<Structure> ofStructure(String name) {
		return TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(PeacefulMod.MOD_ID, name));
	}
}
