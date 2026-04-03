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
import xen42.peacefulitems.criterion.BredBatsCriterion;
import xen42.peacefulitems.criterion.GhastlingTearCriterion;

public class PeacefulModAdvancementGenerator extends FabricAdvancementProvider {
    public PeacefulModAdvancementGenerator(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public String getName() {
        return "PeacefulModAdvancementGenerator";
    }

    private static final AdvancementEntry findEffigyAltarDungeon = new AdvancementEntry(Identifier.of(PeacefulMod.MOD_ID, "find_effigy_altar_dungeon"), null);

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(Consumer<AdvancementEntry> exporter) {
        AdvancementEntry root = build(Advancement.Builder.create()
                .display(
                        PeacefulModBlocks.SULPHUR_BLOCK,
                        Text.translatable("advancements.peaceful_items.root.title"),
                        Text.translatable("advancements.peaceful_items.root.description"),
                        Identifier.of(PeacefulMod.MOD_ID, "textures/gui/advancements/backgrounds/root.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion("crafting_table", InventoryChangedCriterion.Conditions.items(Blocks.CRAFTING_TABLE))
                , exporter, "root");
        
        AdvancementEntry flax_crop = build(Advancement.Builder.create()
                .display(
                        PeacefulModItems.FLAX,
                        Text.translatable("advancements.peaceful_items.flax_crop.title"),
                        Text.translatable("advancements.peaceful_items.flax_crop.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .criterion("placed_flax_crop", ItemCriterion.Conditions.createPlacedBlock(PeacefulModBlocks.FLAX_CROP))
                , exporter, "flax_crop");
        
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

        AdvancementEntry dragon_effigy = build(Advancement.Builder.create()
                .display(
                        PeacefulModItems.DRAGON_EFFIGY,
                        Text.translatable("advancements.peaceful_items.dragon_effigy.title"),
                        Text.translatable("advancements.peaceful_items.dragon_effigy.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(findEffigyAltarDungeon)
                .criterion("has_dragon_effigy", InventoryChangedCriterion.Conditions.items(PeacefulModItems.DRAGON_EFFIGY))
                , exporter, "dragon_effigy");

        AdvancementEntry wither_effigy = build(Advancement.Builder.create()
                .display(
                        PeacefulModItems.WITHER_EFFIGY,
                        Text.translatable("advancements.peaceful_items.wither_effigy.title"),
                        Text.translatable("advancements.peaceful_items.wither_effigy.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(findEffigyAltarDungeon)
                .criterion("has_wither_effigy", InventoryChangedCriterion.Conditions.items(PeacefulModItems.WITHER_EFFIGY))
                , exporter, "wither_effigy");

        AdvancementEntry guardian_effigy = build(Advancement.Builder.create()
                .display(
                		PeacefulModItems.GUARDIAN_EFFIGY,
                        Text.translatable("advancements.peaceful_items.guardian_effigy.title"),
                        Text.translatable("advancements.peaceful_items.guardian_effigy.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(findEffigyAltarDungeon)
                .criterion("has_guardian_effigy", InventoryChangedCriterion.Conditions.items(PeacefulModItems.GUARDIAN_EFFIGY))
                , exporter, "guardian_effigy");

        AdvancementEntry raid_effigy = build(Advancement.Builder.create()
                .display(
                		PeacefulModItems.RAID_EFFIGY,
                        Text.translatable("advancements.peaceful_items.raid_effigy.title"),
                        Text.translatable("advancements.peaceful_items.raid_effigy.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(findEffigyAltarDungeon)
                .criterion("has_raid_effigy", InventoryChangedCriterion.Conditions.items(PeacefulModItems.RAID_EFFIGY))
                , exporter, "raid_effigy");

        AdvancementEntry totem_of_undying = build(Advancement.Builder.create()
                .display(
                        Items.TOTEM_OF_UNDYING,
                        Text.translatable("advancements.peaceful_items.totem_of_undying.title"),
                        Text.translatable("advancements.peaceful_items.totem_of_undying.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(findEffigyAltarDungeon)
                .criterion("has_totem_of_undying", InventoryChangedCriterion.Conditions.items(Items.TOTEM_OF_UNDYING))
                , exporter, "totem_of_undying");

        AdvancementEntry brimstone = build(Advancement.Builder.create()
                .display(
                        PeacefulModItems.SULPHUR,
                        Text.translatable("advancements.peaceful_items.brimstone.title"),
                        Text.translatable("advancements.peaceful_items.brimstone.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criterion("has_brimstone", InventoryChangedCriterion.Conditions.items(PeacefulModItems.SULPHUR))
                , exporter, "brimstone");
        
        AdvancementEntry end_clam_pearl = build(Advancement.Builder.create()
                .display(
                        Items.ENDER_PEARL,
                        Text.translatable("advancements.peaceful_items.end_clam_pearl.title"),
                        Text.translatable("advancements.peaceful_items.end_clam_pearl.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criterion("has_ender_pearl", InventoryChangedCriterion.Conditions.items(Items.ENDER_PEARL))
                , exporter, "end_clam_pearl");
        
        AdvancementEntry wisp_tear = build(Advancement.Builder.create()
                .display(
                        Items.GHAST_TEAR,
                        Text.translatable("advancements.peaceful_items.wisp_tear.title"),
                        Text.translatable("advancements.peaceful_items.wisp_tear.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criterion(
                        "wisp_cried", 
                        GhastlingTearCriterion.Conditions.create(Optional.of(
                                EntityPredicate.contextPredicateFromEntityPredicate(
                                        EntityPredicate.Builder.create().type(
                                                PeacefulMod.GHASTLING_ENTITY
                                        )
                                )
                        ))
                )
                , exporter, "wisp_tear");
        
        AdvancementEntry breed_a_bat = build(Advancement.Builder.create()
                .display(
                        Items.MELON_SLICE,
                        Text.translatable("advancements.peaceful_items.breed_a_bat.title"),
                        Text.translatable("advancements.peaceful_items.breed_a_bat.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .criterion("bred_bats", BredBatsCriterion.Conditions.any())
                , exporter, "breed_a_bat");
        
        AdvancementEntry sniffer_blaze = build(Advancement.Builder.create()
                .display(
                        PeacefulModBlocks.BLAZE_PICKLE,
                        Text.translatable("advancements.peaceful_items.sniffer_blaze.title"),
                        Text.translatable("advancements.peaceful_items.sniffer_blaze.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criterion("has_blaze_coral", InventoryChangedCriterion.Conditions.items(PeacefulModBlocks.BLAZE_PICKLE))
                , exporter, "sniffer_blaze");
        
        AdvancementEntry blaze_rod = Advancement.Builder.create()
                .display(
                        Items.BLAZE_ROD,
                        Text.translatable("advancements.nether.obtain_blaze_rod.title"),
                        Text.translatable("advancements.peaceful_items.blaze_rod.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(sniffer_blaze)
                .criterion("blaze_rod", InventoryChangedCriterion.Conditions.items(Items.BLAZE_ROD))
                .build(exporter, "nether/obtain_blaze_rod");

        AdvancementEntry mine_fossil_ore = build(Advancement.Builder.create()
                .display(
                        PeacefulModBlocks.FOSSIL_ORE,
                        Text.translatable("advancements.peaceful_items.mine_fossil_ore.title"),
                        Text.translatable("advancements.peaceful_items.mine_fossil_ore.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(root)
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .criterion("has_bone", InventoryChangedCriterion.Conditions.items(Items.BONE))
                .criterion("has_bone_meal", InventoryChangedCriterion.Conditions.items(Items.BONE_MEAL))
                , exporter, "mine_fossil_ore");
    }
    
    public AdvancementEntry build(Advancement.Builder builder, Consumer<AdvancementEntry> exporter, String id) {
        AdvancementEntry advancementEntry = builder.build(Identifier.of(PeacefulMod.MOD_ID, id));
        exporter.accept(advancementEntry);
        return advancementEntry;
    }
}
