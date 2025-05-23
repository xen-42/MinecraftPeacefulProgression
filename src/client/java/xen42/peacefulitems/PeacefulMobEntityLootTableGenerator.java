package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class PeacefulMobEntityLootTableGenerator extends SimpleFabricLootTableProvider {
    private final CompletableFuture<WrapperLookup> registryLookup;

    protected PeacefulMobEntityLootTableGenerator(FabricDataOutput dataOutput,
            CompletableFuture<WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup, LootContextTypes.ENTITY);
        this.registryLookup = registryLookup;
    }

    @Override
    public String getName() {
        return "PeacefulMobEntityLootTableGenerator";
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, Builder> consumer) {
        consumer.accept(
                EntityType.BAT.getLootTableKey().orElseThrow(), 
                LootTable.builder()
                .pool(
                    LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(
                            ItemEntry.builder(PeacefulModItems.BAT_WING)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseLootFunction.builder(registryLookup.join(), UniformLootNumberProvider.create(0.0F, 1.0F)))
                        )
                )
            );
        this.register(
                PeacefulMod.GHASTLING_ENTITY, 
                LootTable.builder()
                .pool(
                    LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(
                            ItemEntry.builder(PeacefulModItems.ECTOPLASM)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseLootFunction.builder(registries, UniformLootNumberProvider.create(0.0F, 1.0F)))
                        )
                )
            );
        this.register(
                PeacefulMod.END_CLAM_ENTITY, 
                LootTable.builder()
                .pool(
                    LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(
                            ItemEntry.builder(PeacefulModItems.CLAM)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))     
                                .apply((LootFunction.Builder)FurnaceSmeltLootFunction.builder()
                                    .conditionally((LootCondition.Builder)createSmeltLootCondition()))                           
                        )
                )
            );
    }
}