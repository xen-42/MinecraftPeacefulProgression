package xen42.peacefulitems;

import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetInstrumentLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.util.Identifier;

public class PeacefulModChestLootTableGenerator extends SimpleFabricLootTableProvider {
	public static final Identifier VILLAGE_DJ_CHEST = Identifier.of(PeacefulMod.MOD_ID, "chests/village/village_dj");

	public PeacefulModChestLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput, LootContextTypes.CHEST);
    }

    @Override
    public String getName() {
        return "PeacefulModChestLootTableProvider";
    }
	 
	@Override
	public void accept(BiConsumer<Identifier, LootTable.Builder> lootTableBiConsumer) {
		lootTableBiConsumer.accept(VILLAGE_DJ_CHEST, LootTable.builder()

		        // Guaranteed hostile disc
		        .pool(LootPool.builder()
		        	.rolls(ConstantLootNumberProvider.create(1))
		            .with(TagEntry.expandBuilder(PeacefulModTags.ItemTags.HOSTILE_MUSIC_DISCS))
		        )

		        // Guaranteed goat horn
		        .pool(LootPool.builder()
		        	.rolls(ConstantLootNumberProvider.create(1))
					.with(ItemEntry.builder(Items.GOAT_HORN))
					.apply(SetInstrumentLootFunction.builder(InstrumentTags.GOAT_HORNS))
		        )

		        // Note block instrument loot
		        .pool(LootPool.builder()
		            .rolls(UniformLootNumberProvider.create(2, 6))

		            .with(ItemEntry.builder(Items.BLACK_WOOL).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.BLACK_CONCRETE_POWDER).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.BLACK_STAINED_GLASS).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.HAY_BLOCK).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.PUMPKIN).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.PACKED_ICE).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.CLAY).weight(5)
	            		.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.BONE_BLOCK).weight(5)
		            	.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.SOUL_SAND).weight(4)
		            	.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))

		            .with(ItemEntry.builder(Items.REDSTONE).weight(4)
		                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 9))))

		            .with(ItemEntry.builder(Items.GLOWSTONE_DUST).weight(4)
		                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 9))))

		            .with(ItemEntry.builder(Items.COPPER_INGOT).weight(3)
		            	.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 9))))

		            .with(ItemEntry.builder(Items.GOLD_INGOT).weight(3)
		            	.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))

		            .with(ItemEntry.builder(Items.IRON_INGOT).weight(2)
		            	.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))

		            .with(ItemEntry.builder(Items.EMERALD).weight(2)
		            	.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))
		        )
		);
	}
}
