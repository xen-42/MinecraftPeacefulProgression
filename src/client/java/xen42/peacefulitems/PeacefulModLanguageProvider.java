package xen42.peacefulitems;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.village.VillagerProfession;
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
		
		@Override
		public void add(Block block, String value) {
			add(block.asItem(), value);
		}

		public void addVillagerProfession(RegistryKey<VillagerProfession> key, String value) {
			add("entity." + key.getValue().getNamespace() + ".villager." + key.getValue().getPath(), value);
		}

		public void addFilledMap(TagKey<Structure> structure, String value) {
			add("filled_map." + structure.id().getNamespace() + "." + structure.id().getPath(), value);
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
			translationBuilder.add(PeacefulModItems.SULPHUR, "Brimstone");
			translationBuilder.add(PeacefulModItems.FLAX, "Flax Seeds");
			
			translationBuilder.add(PeacefulModBlocks.SULPHUR_BLOCK, "Brimstone Block");
			translationBuilder.add(PeacefulModBlocks.SULPHUR_ORE, "Brimstone Ore");
			translationBuilder.add(PeacefulModBlocks.FOSSIL_ORE, "Fossil Ore");
			translationBuilder.add(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE, "Deepslate Fossil Ore");
			translationBuilder.add(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE, "Nether Fossil Ore");
			translationBuilder.add(PeacefulModBlocks.EFFIGY_ALTAR, "Effigy Altar");
			
			translationBuilder.add(PeacefulModBlocks.BLAZE_PICKLE, "Blaze Coral");
			translationBuilder.add(PeacefulModBlocks.BREEZE_CORAL, "Breeze Coral");
			translationBuilder.add(PeacefulModItems.GHASTLING_SPAWN_EGG, "Wisp Spawn Egg");
			translationBuilder.add(PeacefulModItems.END_CLAM_SPAWN_EGG, "Enderclam Spawn Egg");

			translationBuilder.addVillagerProfession(PeacefulModVillagers.DJ_VILLAGER_KEY, "Musician");
			translationBuilder.add(PeacefulMod.GHASTLING_ENTITY, "Wisp");
			translationBuilder.add(PeacefulMod.END_CLAM_ENTITY, "Enderclam");

			translationBuilder.add(PeacefulModItems.WITHER_EFFIGY, "Wither Effigy");
			translationBuilder.add(PeacefulModItems.DRAGON_EFFIGY, "Dragon Effigy");
			translationBuilder.add(PeacefulModItems.GUARDIAN_EFFIGY, "Elder Effigy");
			
			translationBuilder.addFilledMap(PeacefulModTags.StructureTags.EFFIGY_ALTAR_DUNGEON, "Effigy Altar Dungeon Map");
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
}
