package xen42.peacefulitems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import xen42.peacefulitems.blocks.BlazePickleBlock;
import xen42.peacefulitems.blocks.FlaxCropBlock;

public class PeacefulModBlockLootTableGenerator extends FabricBlockLootTableProvider {

    protected PeacefulModBlockLootTableGenerator(FabricDataOutput dataOutput,
            CompletableFuture<WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public String getName() {
        return "PeacefulModBlockLootTableGenerator";
    }

    @Override
    public void generate() {
        addDrop(PeacefulModBlocks.SULPHUR_BLOCK);
        addDrop(PeacefulModBlocks.SULPHUR_STAIRS);
		addDrop(PeacefulModBlocks.SULPHUR_SLAB, block -> slabDrops(block));
        addDrop(PeacefulModBlocks.SULPHUR_WALL);
        addDrop(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK);
        addDrop(PeacefulModBlocks.FOSSIL_ORE, block -> fossilOreDrops(block));
        addDrop(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE, block -> fossilOreDrops(block));
        addDrop(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE, block -> fossilOreDrops(block));
        addDrop(PeacefulModBlocks.SULPHUR_ORE, block -> oreDrops(PeacefulModBlocks.SULPHUR_ORE, PeacefulModItems.SULPHUR));
        addDrop(PeacefulModBlocks.SULPHUR_CLUSTER, block -> dropItem(PeacefulModItems.SULPHUR, 1, 1));
        this.addDrop(PeacefulModBlocks.BLAZE_PICKLE, (Block block) 
            -> LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
            .with(this.applyExplosionDecay(PeacefulModBlocks.BLAZE_PICKLE, ItemEntry.builder(block)
            .apply(List.of(Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), pickles 
            -> SetCountLootFunction.builder(ConstantLootNumberProvider.create(pickles.intValue()))
            .conditionally(BlockStatePropertyLootCondition.builder(block)
            .properties(StatePredicate.Builder.create().exactMatch(BlazePickleBlock.PICKLES, pickles.intValue()))))))));
        
        this.addDrop(PeacefulModBlocks.BREEZE_CORAL, (Block block) 
            -> LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
            .with(this.applyExplosionDecay(PeacefulModBlocks.BREEZE_CORAL, ItemEntry.builder(block)
            .apply(List.of(Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), pickles 
            -> SetCountLootFunction.builder(ConstantLootNumberProvider.create(pickles.intValue()))
            .conditionally(BlockStatePropertyLootCondition.builder(block)
            .properties(StatePredicate.Builder.create().exactMatch(BlazePickleBlock.PICKLES, pickles.intValue()))))))));

        BlockStatePropertyLootCondition.Builder flax_condition = BlockStatePropertyLootCondition.builder(PeacefulModBlocks.FLAX_CROP)
            .properties(StatePredicate.Builder.create().exactMatch(FlaxCropBlock.AGE, 7));
        addDrop(PeacefulModBlocks.FLAX_CROP, block -> this.cropDrops(block, Items.STRING, PeacefulModItems.FLAX, flax_condition));

        addDrop(PeacefulModBlocks.DRAGON_BREATH_CAULDRON, Blocks.CAULDRON);
    }

    private LootTable.Builder fossilOreDrops(Block block) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);

        var boneMealDrop = ItemEntry.builder(Items.BONE_MEAL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
            .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)));

        var normalDrop = (LootTable.Builder)LootTable.builder()
            .pool(LootPool.builder().with(boneMealDrop).conditionally(createWithoutSilkTouchCondition()))
            .pool(LootPool.builder().with(ItemEntry.builder(block)).conditionally(createSilkTouchCondition()));

        return normalDrop;
    }

    private LootTable.Builder dropItem(Item item, int min, int max) {
        var dropItem = (ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max))));

        return LootTable.builder().pool(LootPool.builder().with(dropItem));
    }
}