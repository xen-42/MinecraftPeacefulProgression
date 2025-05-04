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
    }
    
    public class ItemTags {
        public static final TagKey<Item> FOSSIL_ORES = ofItem("fossil_ores");
        public static final TagKey<Item> EFFIGIES = ofItem("effigies");
    }

    public static TagKey<Block> ofBlock(String name) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(PeacefulMod.MOD_ID, name));
    }

    public static TagKey<Item> ofItem(String name) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(PeacefulMod.MOD_ID, name));
    }
    
    public static TagKey<EntityType<?>> ofEntity(String name) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(PeacefulMod.MOD_ID, name));
    }
    
    public static TagKey<Structure> ofStructure(String name) {
        return TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(PeacefulMod.MOD_ID, name));
    }
}
