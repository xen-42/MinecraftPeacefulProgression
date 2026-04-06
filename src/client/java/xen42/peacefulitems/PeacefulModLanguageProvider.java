package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.structure.Structure;

public abstract class PeacefulModLanguageProvider extends FabricLanguageProvider {
    public PeacefulModLanguageProvider(FabricDataOutput output, String languageCode, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
    	super(output, languageCode, registryLookup);
    }

	public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
		generate(registryLookup, new ModTranslationBuilder(translationBuilder));
	}
	
	public String processValue(String value) {
		return value;
	}

	public abstract void generate(RegistryWrapper.WrapperLookup registryLookup, ModTranslationBuilder translationBuilder);
	
	public class ModTranslationBuilder implements TranslationBuilder {
		private final TranslationBuilder original;
		
		public ModTranslationBuilder(TranslationBuilder original) {
			this.original = original;
		}
		
		@Override
		public void add(String key, String value) {
			original.add(key, processValue(value));
		}
		
		public void add(TagKey<?> key, String value) {
			add(key.getTranslationKey(), value);
		}
		
		public void addTags(String value, TagKey<?>... keys) {
			for (TagKey<?> key : keys) {
				add(key, value);
			}
		}
		
		public void add(GameRules.Key<?> key, String value) {
			add(key.getTranslationKey(), value);
		}
		
		public void add(GameRules.Key<?> key, String title, String description) {
			add(key.getTranslationKey(), title);
			add(key.getTranslationKey() + ".description", description);
		}

		@SuppressWarnings("deprecation")
		public void add(Fluid fluid, String value) {
			RegistryKey<Fluid> key = fluid.getRegistryEntry().registryKey();
			add("block." + key.getValue().getNamespace() + "." + key.getValue().getPath(), value);
		}

		public void addVillagerProfession(RegistryKey<VillagerProfession> key, String value) {
			add("entity." + key.getValue().getNamespace() + ".villager." + key.getValue().getPath(), value);
		}

		public void addFilledMap(TagKey<Structure> structure, String value) {
			add("filled_map." + structure.id().getNamespace() + "." + structure.id().getPath(), value);
		}

		public void addAdvancement(String path, String title, String description) {
			add("advancements.peaceful_items." + path + ".title", title);
			add("advancements.peaceful_items." + path + ".description", description);
		}

		private String getInformationKey(String translationKey, String prefix) {
			return translationKey + "." + prefix + "_information";
		}

		public void addInformation(Item item, String prefix, String value) {
			add(getInformationKey(item.getTranslationKey(), prefix), value);
		}

		public void addInformation(EntityType<?> entity, String prefix, String value) {
			add(getInformationKey(entity.getTranslationKey(), prefix), value);
		}

		public void addJadePlugin(Identifier plugin, String value) {
			add("config.jade.plugin_" + plugin.getNamespace() + "." + plugin.getPath(), value);
		}
	}
	
	public static class English extends PeacefulModLanguageProvider {

		public English(FabricDataOutput output, String languageCode, CompletableFuture<WrapperLookup> registryLookup) {
			super(output, languageCode, registryLookup);
		}

		public English(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
			this(output, "en_us", registryLookup);
		}

		@Override
		public void generate(WrapperLookup registryLookup, ModTranslationBuilder translationBuilder) {
			translationBuilder.add(PeacefulModItems.BAT_WING, "Bat Wing");
			translationBuilder.add(PeacefulModItems.GUANO, "Guano");
			translationBuilder.add(PeacefulModItems.ECTOPLASM, "Ectoplasm");
			translationBuilder.add(PeacefulModItems.SULPHUR, "Brimstone");
			translationBuilder.add(PeacefulModItems.FLAX, "Flax Seeds");
			
			translationBuilder.add(PeacefulModBlocks.SULPHUR_BLOCK, "Block of Brimstone");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_STAIRS, "Brimstone Stairs");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_SLAB, "Brimstone Slab");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_WALL, "Brimstone Wall");
			translationBuilder.add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK, "Chiseled Brimstone Block");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_ORE, "Brimstone Ore");
			translationBuilder.add(PeacefulModBlocks.FOSSIL_ORE, "Fossil Ore");
			translationBuilder.add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE, "Deepslate Fossil Ore");
			translationBuilder.add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE, "Nether Fossil Ore");
			translationBuilder.add(PeacefulModBlocks.EFFIGY_ALTAR, "Effigy Altar");
			translationBuilder.add(PeacefulModItems.CLAM, "Clam");
			translationBuilder.add(PeacefulModItems.COOKED_CLAM, "Cooked Clam");
			
			translationBuilder.add(PeacefulModBlocks.BLAZE_PICKLE, "Blaze Coral");
			translationBuilder.add(PeacefulModBlocks.BREEZE_CORAL, "Breeze Coral");
			translationBuilder.add(PeacefulModItems.GHASTLING_SPAWN_EGG, "Wisp Spawn Egg");
			translationBuilder.add(PeacefulModItems.END_CLAM_SPAWN_EGG, "Enderclam Spawn Egg");

			translationBuilder.addVillagerProfession(PeacefulModVillagers.DJ_VILLAGER_KEY, "Musician");
			translationBuilder.add(PeacefulMod.GHASTLING_ENTITY, "Wisp");
			translationBuilder.add(PeacefulMod.END_CLAM_ENTITY, "Enderclam");

			translationBuilder.add(PeacefulModItems.CAPE, "Cape");
			translationBuilder.add(PeacefulModItems.WITHER_EFFIGY, "Wither Effigy");
			translationBuilder.add(PeacefulModItems.DRAGON_EFFIGY, "Dragon Effigy");
			translationBuilder.add(PeacefulModItems.GUARDIAN_EFFIGY, "Elder Effigy");
			translationBuilder.add(PeacefulModItems.RAID_EFFIGY, "Raid Effigy");
			
			translationBuilder.add(PeacefulModFluids.DRAGON_BREATH, "Dragon's Breath");
			translationBuilder.add(PeacefulModBlocks.DRAGON_BREATH_CAULDRON, "Dragon's Breath Cauldron");
			
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "Effigy Altar Dungeon Map");
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.TRAIL_RUINS, "Trail Ruins Map");
			translationBuilder.addFilledMap(StructureTags.OCEAN_RUIN, "Ocean Ruin Map");
			
			translationBuilder.add(PeacefulMod.ENABLE_STARVING_PEACEFUL, "Enable starving in peaceful");
			translationBuilder.add(PeacefulMod.ENABLE_SUPER_HEALING_PEACEFUL, "Enable super healing in peaceful");
			translationBuilder.add(PeacefulMod.ENABLE_ENDER_DRAGON_FIGHT_PEACEFUL, "Enable Ender Dragon fight in peaceful");

			translationBuilder.add(PeacefulModTags.ItemTags.GUANO, "Guano");
			translationBuilder.add(PeacefulModTags.ItemTags.EFFIGIES, "Effigies");
			translationBuilder.add(PeacefulModTags.ItemTags.WISP_LIKES, "Wisp Likes");
			translationBuilder.add(PeacefulModTags.ItemTags.WISP_DISLIKES, "Wisp Dislikes");
			translationBuilder.add(PeacefulModTags.ItemTags.HOSTILE_MUSIC_DISCS, "Hostile Music Discs");
			translationBuilder.add(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "Effigy Altar Dungeon");
			translationBuilder.add(PeacefulModTags.StructureTags.TRAIL_RUINS, "Trail Ruins");
			translationBuilder.add(StructureTags.OCEAN_RUIN, "Ocean Ruin");
			translationBuilder.addTags("Soul Soil Ore Bearing Ground", PeacefulModTags.ItemTags.ORE_BEARING_GROUND_SOUL_SOIL, PeacefulModTags.BlockTags.ORE_BEARING_GROUND_SOUL_SOIL);
			translationBuilder.addTags("Soul Soil Ores In Ground", PeacefulModTags.ItemTags.ORES_IN_GROUND_SOUL_SOIL, PeacefulModTags.BlockTags.ORES_IN_GROUND_SOUL_SOIL);
			translationBuilder.addTags("Fossil Ores", PeacefulModTags.ItemTags.FOSSIL_ORES, PeacefulModTags.ItemTags.FOSSIL_ORES_C, PeacefulModTags.BlockTags.FOSSIL_ORES, PeacefulModTags.BlockTags.FOSSIL_ORES_C);
			translationBuilder.addTags("Brimstone Ores", PeacefulModTags.ItemTags.SULPHUR_ORES, PeacefulModTags.ItemTags.SULPHUR_ORES_C, PeacefulModTags.ItemTags.SULFUR_ORES, PeacefulModTags.BlockTags.SULPHUR_ORES, PeacefulModTags.BlockTags.SULPHUR_ORES_C, PeacefulModTags.BlockTags.SULFUR_ORES);

			translationBuilder.addAdvancement("root", "Peaceful Progression", "A better way to be peaceful");
			translationBuilder.addAdvancement("find_effigy_altar_dungeon", "Altar to None", "Enter an Effigy Altar Dungeon");
			translationBuilder.addAdvancement("dragon_effigy", "Echo of the End", "Craft an effigy of the Ender Dragon");
			translationBuilder.addAdvancement("wither_effigy", "A Grim Reminder", "Craft an effigy of the Wither");
			translationBuilder.addAdvancement("guardian_effigy", "From the Depths", "Craft an effigy of the Elder Guardian");
			translationBuilder.addAdvancement("raid_effigy", "Sound the Horn", "Craft an effigy of a village raid");
			translationBuilder.addAdvancement("totem_of_undying", "Fail-Safe", "Craft a Totem of Undying at an Effigy Altar");
			translationBuilder.addAdvancement("flax_crop", "Delicate Threads", "Plant flax seeds");
			translationBuilder.addAdvancement("breed_a_bat", "Love at First Bite", "Breed two bats using melon slices");
			translationBuilder.addAdvancement("wisp_tear", "Tears by Force", "Feed guano to a Wisp");
			translationBuilder.addAdvancement("end_clam_pearl", "By Hand or By Heart", "Get an Ender Pearl from an Enderclam");
			translationBuilder.addAdvancement("brimstone", "Awakening Flame", "Find Brimstone in the Soul Sand Valleys");
			translationBuilder.addAdvancement("sniffer_blaze", "Cinders in the Soil", "Let a Sniffer dig up a Blaze Coral");
			translationBuilder.addAdvancement("blaze_rod", "Into Fire", "Smelt a Blaze Coral into a Blaze Rod");
			translationBuilder.addAdvancement("sniffer_breeze", "Whispers in the Gravel", "Let a Sniffer dig up a Breeze Coral");
			translationBuilder.addAdvancement("breeze_rod", "Into Wind", "Smelt a Breeze Coral into a Breeze Rod");
			translationBuilder.addAdvancement("mine_fossil_ore", "Bone to Pick", "Mine or brush Bones from Fossil Ore");
			translationBuilder.addAdvancement("strip_resin", "Sap Tap", "Strip a Pale Oak Log to get a Resin Clump");
			
			translationBuilder.addJadePlugin(PeacefulMod.END_CLAM_ENTITY_ID, "Enderclam");

			translationBuilder.addInformation(PeacefulModBlocks.EFFIGY_ALTAR.asItem(), "dungeon", 
			    "Found in Effigy Altar dungeon structures. Cartographers sell maps to locate them."
			);
			translationBuilder.addInformation(Items.DRAGON_BREATH, "dragon_effigy", 
			    "Use a Dragon Effigy on a cauldron to fill it with dragon breath, then bottle it."
			);
			translationBuilder.addInformation(Items.NETHER_STAR, "wither_effigy", 
			    "Obtained by using a Wither Effigy."
			);
			translationBuilder.addInformation(PeacefulModItems.GUARDIAN_EFFIGY, "drop", 
				"Obtained by using a Elder Effigy."
			);
			translationBuilder.addInformation(PeacefulModItems.RAID_EFFIGY, "drop", 
			    "Obtained by using a Raid Effigy."
			);
			translationBuilder.addInformation(PeacefulMod.END_CLAM_ENTITY, "drop", 
			    "Items can be found inside Enderclam shells. Swap items or kill it to get loot."
			);
			translationBuilder.addInformation(Items.SLIME_BALL, "sneeze", 
			    "Dropped when a panda sneezes (can be triggered with a brush)."
			);
			translationBuilder.addInformation(Items.MAGMA_CREAM, "frog", 
			    "Frogs can eat dropped magma cream items to produce froglights when in the Nether."
			);
			translationBuilder.addInformation(Items.GHAST_TEAR, "wisp", 
			    "Produced by Wisps when fed guano."
			);
			translationBuilder.addInformation(PeacefulModItems.GUANO, "bat", 
			    "Dropped randomly by bats."
			);
			translationBuilder.addInformation(Blocks.SOUL_SAND.asItem(), "sniffer", 
			    "Sniffers can dig up items from soul sand/soil."
			);
			translationBuilder.addInformation(Blocks.SAND.asItem(), "sniffer", 
			    "Sniffers can dig up items from sand."
			);
			translationBuilder.addInformation(Blocks.GRAVEL.asItem(), "sniffer", 
			    "Sniffers can dig up items from gravel."
			);
			translationBuilder.addInformation(PeacefulModBlocks.FOSSIL_ORE.asItem(), "brush", 
			    "Use a brush on Fossil Ore to extract bones."
			);
			translationBuilder.addInformation(EntityType.WANDERING_TRADER, "key_head", 
			    "Can be sold by a Wandering Trader."
			);
			translationBuilder.addInformation(Items.CREEPER_HEAD, "dungeon_chest", 
			    "Can be found in dungeon and Effigy Altar dungeon chests."
			);
			translationBuilder.addInformation(Items.RESIN_CLUMP, "strip_pale_oak", 
			    "Stripping pale oak logs drops resin."
			);
		}
	}
	
	public static class EnglishUpsideDown extends English {
		private static final String NORMAL_CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_,;.?!/\\'\"";
		private static final String UPSIDE_DOWN_CHARS = " ɐqɔpǝɟᵷɥᴉɾʞꞁɯuodbɹsʇnʌʍxʎzⱯᗺƆᗡƎℲ⅁HIՐꞰꞀWNOԀὉᴚS⟘∩ΛMXʎZ0⥝ᘔƐ߈ϛ9ㄥ86‾'⸵˙¿¡\\/,„";

		public EnglishUpsideDown(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
			super(output, "en_ud", registryLookup);
		}

		@Override
		public String processValue(String value) {
			return toUpsideDown(value);
		}

		private static String toUpsideDown(String name) {
			StringBuilder builder = new StringBuilder();

			for (int i = name.length() - 1; i >= 0; i--) {
				if (i > 2 && name.substring(i - 3, i + 1).equals("%1$s")) {
					builder.append(name, i - 3, i + 1);
					i -= 4;
					continue;
				}

				if (i < 0)
					continue;

				char normalChar = name.charAt(i);
				int normalIndex = NORMAL_CHARS.indexOf(normalChar);
				if (normalIndex < 0) {
					builder.append(normalChar);
				} else {
					char upsideDown = UPSIDE_DOWN_CHARS.charAt(normalIndex);
					builder.append(upsideDown);
				}
			}

			return builder.toString();
		}
		
	}
	
	public static class ChineseSimplified extends PeacefulModLanguageProvider {

		public ChineseSimplified(FabricDataOutput output, String languageCode, CompletableFuture<WrapperLookup> registryLookup) {
			super(output, languageCode, registryLookup);
		}

		public ChineseSimplified(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
			this(output, "zh_cn", registryLookup);
		}

		@Override
		public void generate(WrapperLookup registryLookup, ModTranslationBuilder translationBuilder) {
			translationBuilder.add(PeacefulModItems.BAT_WING, "蝙蝠翅膀");
			translationBuilder.add(PeacefulModItems.GUANO, "粪便");
			translationBuilder.add(PeacefulModItems.ECTOPLASM, "灵质");
			translationBuilder.add(PeacefulModItems.SULPHUR, "硫磺");
			translationBuilder.add(PeacefulModItems.FLAX, "亚麻种子");
			
			translationBuilder.add(PeacefulModBlocks.SULPHUR_BLOCK, "硫磺块");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_STAIRS, "硫磺楼梯");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_SLAB, "硫磺台阶");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_WALL, "硫磺围墙");
			translationBuilder.add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK, "雕纹硫磺块");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_ORE, "硫磺矿石");
			translationBuilder.add(PeacefulModBlocks.FOSSIL_ORE, "化石矿石");
			translationBuilder.add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE, "深层化石矿石");
			translationBuilder.add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE, "下界化石矿石");
			translationBuilder.add(PeacefulModBlocks.EFFIGY_ALTAR, "塑像祭坛");
			translationBuilder.add(PeacefulModItems.CLAM, "蛤");
			translationBuilder.add(PeacefulModItems.COOKED_CLAM, "熟蛤");
			
			translationBuilder.add(PeacefulModBlocks.BLAZE_PICKLE, "烈焰珊瑚");
			translationBuilder.add(PeacefulModBlocks.BREEZE_CORAL, "旋风珊瑚");
			translationBuilder.add(PeacefulModItems.GHASTLING_SPAWN_EGG, "小精灵刷怪蛋");
			translationBuilder.add(PeacefulModItems.END_CLAM_SPAWN_EGG, "末影蛤刷怪蛋");

			translationBuilder.addVillagerProfession(PeacefulModVillagers.DJ_VILLAGER_KEY, "音乐家");
			translationBuilder.add(PeacefulMod.GHASTLING_ENTITY, "小精灵");
			translationBuilder.add(PeacefulMod.END_CLAM_ENTITY, "末影蛤");

			translationBuilder.add(PeacefulModItems.CAPE, "披风");
			translationBuilder.add(PeacefulModItems.WITHER_EFFIGY, "凋灵塑像");
			translationBuilder.add(PeacefulModItems.DRAGON_EFFIGY, "龙塑像");
			translationBuilder.add(PeacefulModItems.GUARDIAN_EFFIGY, "远古塑像");
			translationBuilder.add(PeacefulModItems.RAID_EFFIGY, "袭击塑像");
			
			translationBuilder.add(PeacefulModFluids.DRAGON_BREATH, "龙息");
			translationBuilder.add(PeacefulModBlocks.DRAGON_BREATH_CAULDRON, "装有龙息的炼药锅");
			
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "塑像祭坛地图");
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.TRAIL_RUINS, "古迹废墟地图");
			translationBuilder.addFilledMap(StructureTags.OCEAN_RUIN, "海底废墟地图");
			
			translationBuilder.add(PeacefulMod.ENABLE_STARVING_PEACEFUL, "启用和平中的饿死");
			translationBuilder.add(PeacefulMod.ENABLE_SUPER_HEALING_PEACEFUL, "启用和平中的超级生命恢复");
			translationBuilder.add(PeacefulMod.ENABLE_ENDER_DRAGON_FIGHT_PEACEFUL, "启用和平中的末影龙");

			translationBuilder.add(PeacefulModTags.ItemTags.GUANO, "粪便");
			translationBuilder.add(PeacefulModTags.ItemTags.EFFIGIES, "塑像");
			translationBuilder.add(PeacefulModTags.ItemTags.WISP_LIKES, "小精灵的喜欢之物");
			translationBuilder.add(PeacefulModTags.ItemTags.WISP_DISLIKES, "小精灵的厌恶之物");
			translationBuilder.add(PeacefulModTags.ItemTags.HOSTILE_MUSIC_DISCS, "Hostile Music Discs");
			translationBuilder.add(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "塑像祭坛");
			translationBuilder.add(PeacefulModTags.StructureTags.TRAIL_RUINS, "古迹废墟");
			translationBuilder.add(StructureTags.OCEAN_RUIN, "海底废墟");
			translationBuilder.addTags("灵魂土含矿石地", PeacefulModTags.ItemTags.ORE_BEARING_GROUND_SOUL_SOIL, PeacefulModTags.BlockTags.ORE_BEARING_GROUND_SOUL_SOIL);
			translationBuilder.addTags("地下灵魂土矿石", PeacefulModTags.ItemTags.ORES_IN_GROUND_SOUL_SOIL, PeacefulModTags.BlockTags.ORES_IN_GROUND_SOUL_SOIL);
			translationBuilder.addTags("化石矿石", PeacefulModTags.ItemTags.FOSSIL_ORES, PeacefulModTags.ItemTags.FOSSIL_ORES_C, PeacefulModTags.BlockTags.FOSSIL_ORES, PeacefulModTags.BlockTags.FOSSIL_ORES_C);
			translationBuilder.addTags("硫磺矿石", PeacefulModTags.ItemTags.SULPHUR_ORES, PeacefulModTags.ItemTags.SULPHUR_ORES_C, PeacefulModTags.ItemTags.SULFUR_ORES, PeacefulModTags.BlockTags.SULPHUR_ORES, PeacefulModTags.BlockTags.SULPHUR_ORES_C, PeacefulModTags.BlockTags.SULFUR_ORES);

			translationBuilder.addAdvancement("root", "Peaceful Progression", "A better way to be peaceful");
			translationBuilder.addAdvancement("find_effigy_altar_dungeon", "Altar to None", "Enter an Effigy Altar Dungeon");
			translationBuilder.addAdvancement("dragon_effigy", "Echo of the End", "Craft an effigy of the Ender Dragon");
			translationBuilder.addAdvancement("wither_effigy", "A Grim Reminder", "Craft an effigy of the Wither");
			translationBuilder.addAdvancement("guardian_effigy", "From the Depths", "Craft an effigy of the Elder Guardian");
			translationBuilder.addAdvancement("raid_effigy", "Sound the Horn", "Craft an effigy of a village raid");
			translationBuilder.addAdvancement("totem_of_undying", "Fail-Safe", "Craft a Totem of Undying at an Effigy Altar");
			translationBuilder.addAdvancement("flax_crop", "Delicate Threads", "Plant flax seeds");
			translationBuilder.addAdvancement("breed_a_bat", "Love at First Bite", "Breed two bats using melon slices");
			translationBuilder.addAdvancement("wisp_tear", "Tears by Force", "Feed guano to a Wisp");
			translationBuilder.addAdvancement("end_clam_pearl", "By Hand or By Heart", "Get an Ender Pearl from an Enderclam");
			translationBuilder.addAdvancement("brimstone", "Awakening Flame", "Find Brimstone in the Soul Sand Valleys");
			translationBuilder.addAdvancement("sniffer_blaze", "Cinders in the Soil", "Let a Sniffer dig up a Blaze Coral");
			translationBuilder.addAdvancement("blaze_rod", "Into Fire", "Smelt a Blaze Coral into a Blaze Rod");
			translationBuilder.addAdvancement("sniffer_breeze", "Whispers in the Gravel", "Let a Sniffer dig up a Breeze Coral");
			translationBuilder.addAdvancement("breeze_rod", "Into Wind", "Smelt a Breeze Coral into a Breeze Rod");
			translationBuilder.addAdvancement("mine_fossil_ore", "Bone to Pick", "Mine or brush Bones from Fossil Ore");
			translationBuilder.addAdvancement("strip_resin", "Sap Tap", "Strip a Pale Oak Log to get a Resin Clump");
			
			translationBuilder.addJadePlugin(PeacefulMod.END_CLAM_ENTITY_ID, "末影蛤");

			translationBuilder.addInformation(PeacefulModBlocks.EFFIGY_ALTAR.asItem(), "dungeon", 
			    "Found in Effigy Altar dungeon structures. Cartographers sell maps to locate them."
			);
			translationBuilder.addInformation(Items.DRAGON_BREATH, "dragon_effigy", 
			    "Use a Dragon Effigy on a cauldron to fill it with dragon breath, then bottle it."
			);
			translationBuilder.addInformation(Items.NETHER_STAR, "wither_effigy", 
			    "Obtained by using a Wither Effigy."
			);
			translationBuilder.addInformation(PeacefulModItems.GUARDIAN_EFFIGY, "drop", 
				"Obtained by using a Elder Effigy."
			);
			translationBuilder.addInformation(PeacefulModItems.RAID_EFFIGY, "drop", 
			    "Obtained by using a Raid Effigy."
			);
			translationBuilder.addInformation(PeacefulMod.END_CLAM_ENTITY, "drop", 
			    "Items can be found inside Enderclam shells. Swap items or kill it to get loot."
			);
			translationBuilder.addInformation(Items.SLIME_BALL, "sneeze", 
			    "Dropped when a panda sneezes (can be triggered with a brush)."
			);
			translationBuilder.addInformation(Items.MAGMA_CREAM, "frog", 
			    "Frogs can eat dropped magma cream items to produce froglights when in the Nether."
			);
			translationBuilder.addInformation(Items.GHAST_TEAR, "wisp", 
			    "Produced by Wisps when fed guano."
			);
			translationBuilder.addInformation(PeacefulModItems.GUANO, "bat", 
			    "Dropped randomly by bats."
			);
			translationBuilder.addInformation(Blocks.SOUL_SAND.asItem(), "sniffer", 
			    "Sniffers can dig up items from soul sand/soil."
			);
			translationBuilder.addInformation(Blocks.SAND.asItem(), "sniffer", 
			    "Sniffers can dig up items from sand."
			);
			translationBuilder.addInformation(Blocks.GRAVEL.asItem(), "sniffer", 
			    "Sniffers can dig up items from gravel."
			);
			translationBuilder.addInformation(PeacefulModBlocks.FOSSIL_ORE.asItem(), "brush", 
			    "Use a brush on Fossil Ore to extract bones."
			);
			translationBuilder.addInformation(EntityType.WANDERING_TRADER, "key_head", 
			    "Can be sold by a Wandering Trader."
			);
			translationBuilder.addInformation(Items.CREEPER_HEAD, "dungeon_chest", 
			    "Can be found in dungeon and Effigy Altar dungeon chests."
			);
			translationBuilder.addInformation(Items.RESIN_CLUMP, "strip_pale_oak", 
			    "Stripping pale oak logs drops resin."
			);
		}
	}
	
	public static class Russian extends PeacefulModLanguageProvider {

		public Russian(FabricDataOutput output, String languageCode, CompletableFuture<WrapperLookup> registryLookup) {
			super(output, languageCode, registryLookup);
		}

		public Russian(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
			this(output, "ru_ru", registryLookup);
		}

		@Override
		public void generate(WrapperLookup registryLookup, ModTranslationBuilder translationBuilder) {
			translationBuilder.add(PeacefulModItems.BAT_WING, "Крыло летучей мыши");
			translationBuilder.add(PeacefulModItems.GUANO, "Гуано");
			translationBuilder.add(PeacefulModItems.ECTOPLASM, "Эктоплазма");
			translationBuilder.add(PeacefulModItems.SULPHUR, "Сера");
			translationBuilder.add(PeacefulModItems.FLAX, "Семена льна");
			
			translationBuilder.add(PeacefulModBlocks.SULPHUR_BLOCK, "Серный блок");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_STAIRS, "Серные ступеньки");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_SLAB, "Серная плита");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_WALL, "Серная ограда");
			translationBuilder.add(PeacefulModBlocks.CHISELED_SULPHUR_BLOCK, "Резной серный блок");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_ORE, "Серная руда");
			translationBuilder.add(PeacefulModBlocks.FOSSIL_ORE, "Окаменелость");
			translationBuilder.add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE, "Глубинносланцевая окаменелость");
			translationBuilder.add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE, "Незерская окаменелость");
			translationBuilder.add(PeacefulModBlocks.EFFIGY_ALTAR, "Алтарь тотемов");
			translationBuilder.add(PeacefulModItems.CLAM, "Моллюск");
			translationBuilder.add(PeacefulModItems.COOKED_CLAM, "Жареный моллюск");
			
			translationBuilder.add(PeacefulModBlocks.BLAZE_PICKLE, "Полыхающий коралл");
			translationBuilder.add(PeacefulModBlocks.BREEZE_CORAL, "Ветренный коралл");
			translationBuilder.add(PeacefulModItems.GHASTLING_SPAWN_EGG, "Яйцо призыва души");
			translationBuilder.add(PeacefulModItems.END_CLAM_SPAWN_EGG, "Яйцо призыва эндер-моллюска");

			translationBuilder.addVillagerProfession(PeacefulModVillagers.DJ_VILLAGER_KEY, "Музыкант");
			translationBuilder.add(PeacefulMod.GHASTLING_ENTITY, "Душа");
			translationBuilder.add(PeacefulMod.END_CLAM_ENTITY, "Эндер-моллюск");

			translationBuilder.add(PeacefulModItems.CAPE, "Плащ");
			translationBuilder.add(PeacefulModItems.WITHER_EFFIGY, "Тотем визера");
			translationBuilder.add(PeacefulModItems.DRAGON_EFFIGY, "Тотем Эндер-дракона");
			translationBuilder.add(PeacefulModItems.GUARDIAN_EFFIGY, "Тотем древнего стража");
			translationBuilder.add(PeacefulModItems.RAID_EFFIGY, "Тотем рейда");
			
			translationBuilder.add(PeacefulModFluids.DRAGON_BREATH, "Дыхание дракона");
			translationBuilder.add(PeacefulModBlocks.DRAGON_BREATH_CAULDRON, "Котёл с дыханием дракона");
			
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "Карта искателя алтарей");
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.TRAIL_RUINS, "Карта исследователя руины троп");
			translationBuilder.addFilledMap(StructureTags.OCEAN_RUIN, "Карта исследователя подводных руин");
			
			translationBuilder.add(PeacefulMod.ENABLE_STARVING_PEACEFUL, "Включить голодание на мирной сложности");
			translationBuilder.add(PeacefulMod.ENABLE_SUPER_HEALING_PEACEFUL, "Включить быстрое восстановление здоровья на мирной сложности");
			translationBuilder.add(PeacefulMod.ENABLE_ENDER_DRAGON_FIGHT_PEACEFUL, "Включить битву с драконом на мирной сложности");

			translationBuilder.add(PeacefulModTags.ItemTags.GUANO, "Гуано");
			translationBuilder.add(PeacefulModTags.ItemTags.EFFIGIES, "Тотемы");
			translationBuilder.add(PeacefulModTags.ItemTags.WISP_LIKES, "Предпочтения души");
			translationBuilder.add(PeacefulModTags.ItemTags.WISP_DISLIKES, "Отвращения души");
			translationBuilder.add(PeacefulModTags.ItemTags.HOSTILE_MUSIC_DISCS, "Hostile Music Discs");
			translationBuilder.add(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "Алтари тотемов");
			translationBuilder.add(PeacefulModTags.StructureTags.TRAIL_RUINS, "Руины троп");
			translationBuilder.add(StructureTags.OCEAN_RUIN, "Подводные руины");
			translationBuilder.addTags("Рудоносная почва душ", PeacefulModTags.ItemTags.ORE_BEARING_GROUND_SOUL_SOIL, PeacefulModTags.BlockTags.ORE_BEARING_GROUND_SOUL_SOIL);
			translationBuilder.addTags("Руды почвы душ в земле", PeacefulModTags.ItemTags.ORES_IN_GROUND_SOUL_SOIL, PeacefulModTags.BlockTags.ORES_IN_GROUND_SOUL_SOIL);
			translationBuilder.addTags("Окаменелости", PeacefulModTags.ItemTags.FOSSIL_ORES, PeacefulModTags.ItemTags.FOSSIL_ORES_C, PeacefulModTags.BlockTags.FOSSIL_ORES, PeacefulModTags.BlockTags.FOSSIL_ORES_C);
			translationBuilder.addTags("Серные руды", PeacefulModTags.ItemTags.SULPHUR_ORES, PeacefulModTags.ItemTags.SULPHUR_ORES_C, PeacefulModTags.ItemTags.SULFUR_ORES, PeacefulModTags.BlockTags.SULPHUR_ORES, PeacefulModTags.BlockTags.SULPHUR_ORES_C, PeacefulModTags.BlockTags.SULFUR_ORES);

			translationBuilder.addAdvancement("root", "Peaceful Progression", "A better way to be peaceful");
			translationBuilder.addAdvancement("find_effigy_altar_dungeon", "Altar to None", "Enter an Effigy Altar Dungeon");
			translationBuilder.addAdvancement("dragon_effigy", "Echo of the End", "Craft an effigy of the Ender Dragon");
			translationBuilder.addAdvancement("wither_effigy", "A Grim Reminder", "Craft an effigy of the Wither");
			translationBuilder.addAdvancement("guardian_effigy", "From the Depths", "Craft an effigy of the Elder Guardian");
			translationBuilder.addAdvancement("raid_effigy", "Sound the Horn", "Craft an effigy of a village raid");
			translationBuilder.addAdvancement("totem_of_undying", "Fail-Safe", "Craft a Totem of Undying at an Effigy Altar");
			translationBuilder.addAdvancement("flax_crop", "Delicate Threads", "Plant flax seeds");
			translationBuilder.addAdvancement("breed_a_bat", "Love at First Bite", "Breed two bats using melon slices");
			translationBuilder.addAdvancement("wisp_tear", "Tears by Force", "Feed guano to a Wisp");
			translationBuilder.addAdvancement("end_clam_pearl", "By Hand or By Heart", "Get an Ender Pearl from an Enderclam");
			translationBuilder.addAdvancement("brimstone", "Awakening Flame", "Find Brimstone in the Soul Sand Valleys");
			translationBuilder.addAdvancement("sniffer_blaze", "Cinders in the Soil", "Let a Sniffer dig up a Blaze Coral");
			translationBuilder.addAdvancement("blaze_rod", "Into Fire", "Smelt a Blaze Coral into a Blaze Rod");
			translationBuilder.addAdvancement("sniffer_breeze", "Whispers in the Gravel", "Let a Sniffer dig up a Breeze Coral");
			translationBuilder.addAdvancement("breeze_rod", "Into Wind", "Smelt a Breeze Coral into a Breeze Rod");
			translationBuilder.addAdvancement("mine_fossil_ore", "Bone to Pick", "Mine or brush Bones from Fossil Ore");
			translationBuilder.addAdvancement("strip_resin", "Sap Tap", "Strip a Pale Oak Log to get a Resin Clump");
			
			translationBuilder.addJadePlugin(PeacefulMod.END_CLAM_ENTITY_ID, "Эндер-моллюск");

			translationBuilder.addInformation(PeacefulModBlocks.EFFIGY_ALTAR.asItem(), "dungeon", 
			    "Found in Effigy Altar dungeon structures. Cartographers sell maps to locate them."
			);
			translationBuilder.addInformation(Items.DRAGON_BREATH, "dragon_effigy", 
			    "Use a Dragon Effigy on a cauldron to fill it with dragon breath, then bottle it."
			);
			translationBuilder.addInformation(Items.NETHER_STAR, "wither_effigy", 
			    "Obtained by using a Wither Effigy."
			);
			translationBuilder.addInformation(PeacefulModItems.GUARDIAN_EFFIGY, "drop", 
				"Obtained by using a Elder Effigy."
			);
			translationBuilder.addInformation(PeacefulModItems.RAID_EFFIGY, "drop", 
			    "Obtained by using a Raid Effigy."
			);
			translationBuilder.addInformation(PeacefulMod.END_CLAM_ENTITY, "drop", 
			    "Items can be found inside Enderclam shells. Swap items or kill it to get loot."
			);
			translationBuilder.addInformation(Items.SLIME_BALL, "sneeze", 
			    "Dropped when a panda sneezes (can be triggered with a brush)."
			);
			translationBuilder.addInformation(Items.MAGMA_CREAM, "frog", 
			    "Frogs can eat dropped magma cream items to produce froglights when in the Nether."
			);
			translationBuilder.addInformation(Items.GHAST_TEAR, "wisp", 
			    "Produced by Wisps when fed guano."
			);
			translationBuilder.addInformation(PeacefulModItems.GUANO, "bat", 
			    "Dropped randomly by bats."
			);
			translationBuilder.addInformation(Blocks.SOUL_SAND.asItem(), "sniffer", 
			    "Sniffers can dig up items from soul sand/soil."
			);
			translationBuilder.addInformation(Blocks.SAND.asItem(), "sniffer", 
			    "Sniffers can dig up items from sand."
			);
			translationBuilder.addInformation(Blocks.GRAVEL.asItem(), "sniffer", 
			    "Sniffers can dig up items from gravel."
			);
			translationBuilder.addInformation(PeacefulModBlocks.FOSSIL_ORE.asItem(), "brush", 
			    "Use a brush on Fossil Ore to extract bones."
			);
			translationBuilder.addInformation(EntityType.WANDERING_TRADER, "key_head", 
			    "Can be sold by a Wandering Trader."
			);
			translationBuilder.addInformation(Items.CREEPER_HEAD, "dungeon_chest", 
			    "Can be found in dungeon and Effigy Altar dungeon chests."
			);
			translationBuilder.addInformation(Items.RESIN_CLUMP, "strip_pale_oak", 
			    "Stripping pale oak logs drops resin."
			);
		}
	}
}
