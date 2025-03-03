package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class PeacefulModLootTableGenerator extends FabricBlockLootTableProvider {

    protected PeacefulModLootTableGenerator(FabricDataOutput dataOutput,
            CompletableFuture<WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(PeacefulModBlocks.SULPHUR_BLOCK);
        addDrop(PeacefulModBlocks.FOSSIL_ORE, block -> fossilOreDrops(block));
        addDrop(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE, block -> fossilOreDrops(block));
        addDrop(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE, block -> fossilOreDrops(block));
    }

    private LootTable.Builder fossilOreDrops(Block block) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);

        var boneDrop = ItemEntry.builder(Items.BONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f)))
        .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)));

        var boneMealDrop = (ItemEntry.builder(Items.BONE_MEAL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))));

        var normalDrop = (LootTable.Builder)LootTable.builder()
            .pool(LootPool.builder().with(boneDrop).conditionally(createWithoutSilkTouchCondition()))
            .pool(LootPool.builder().with(boneMealDrop).conditionally(createWithoutSilkTouchCondition()))
            .pool(LootPool.builder().with(ItemEntry.builder(block)).conditionally(createSilkTouchCondition()));

        return normalDrop;
    }
}