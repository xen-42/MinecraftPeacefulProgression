package xen42.peacefulitems;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PeacefulModAdvancementGenerator extends FabricAdvancementProvider {
    public PeacefulModAdvancementGenerator(FabricDataOutput generator, CompletableFuture<WrapperLookup> registriesFuture) {
        super(generator, registriesFuture);
    }

    @Override
    public String getName() {
        return "PeacefulModAdvancementGenerator";
    }

    private static final AdvancementEntry findEffigyAltarDungeon = new AdvancementEntry(Identifier.of(PeacefulMod.MOD_ID, "find_effigy_altar_dungeon"), null);

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(WrapperLookup registryLookup, Consumer<AdvancementEntry> exporter) {
        AdvancementEntry root = build(Advancement.Builder.create()
                .display(
                        PeacefulModBlocks.SULPHUR_BLOCK,
                        Text.translatable("advancements.peaceful_items.root.title"),
                        Text.translatable("advancements.peaceful_items.root.description"),
                        Identifier.of(PeacefulMod.MOD_ID, "gui/advancements/backgrounds/root"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion("crafting_table", InventoryChangedCriterion.Conditions.items(Blocks.CRAFTING_TABLE))
                , exporter, "root");

        // Moved to JSON because it doesn't allow you to access structures that are from resources
        /*AdvancementEntry findEffigyAltarDungeon = build(Advancement.Builder.create()
                .display(
                        PeacefulModBlocks.EFFIGY_ALTAR,
                        Text.translatable("advancements.peaceful_items.find_effigy_altar_dungeon.title"),
                        Text.translatable("advancements.peaceful_items.find_effigy_altar_dungeon.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criterion("in_effigy_altar_dungeon", TickCriterion.Conditions.createLocation(
                        LocationPredicate.Builder.createStructure(
                                registryLookup.getOrThrow(RegistryKeys.STRUCTURE).getOrThrow(PeacefulMod.EFFIGY_ALTAR_DUNGEON_KEY)//.getOrThrow(StructureKeys.ANCIENT_CITY)
                        )
                ))
                , exporter, "find_effigy_altar_dungeon");*/
    }
    
    public AdvancementEntry build(Advancement.Builder builder, Consumer<AdvancementEntry> exporter, String id) {
        AdvancementEntry advancementEntry = builder.build(Identifier.of(PeacefulMod.MOD_ID, id));
        exporter.accept(advancementEntry);
        return advancementEntry;
    }
}
