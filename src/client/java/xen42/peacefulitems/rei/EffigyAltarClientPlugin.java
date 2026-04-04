package xen42.peacefulitems.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.village.raid.Raid;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.PeacefulModFluids;
import xen42.peacefulitems.PeacefulModItems;
import xen42.peacefulitems.recipe.EffigyAltarRecipeDisplay;
import xen42.peacefulitems.screen.EffigyAltarHandledScreen;

public class EffigyAltarClientPlugin implements REIClientPlugin {
	public EffigyAltarClientPlugin() {
		PeacefulMod.LOGGER.info("Creating REI client plugin");
	}
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		PeacefulMod.LOGGER.info("Registering categories");
		
		registry.add(new EffigyAltarCategory());

		PeacefulMod.LOGGER.info("Registering workstations");
		
		registry.addWorkstations(EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY, EntryStacks.of(PeacefulModBlocks.EFFIGY_ALTAR));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		PeacefulMod.LOGGER.info("Registering displays");
		
		registry.beginRecipeFiller(EffigyAltarRecipeDisplay.class)
			.filterType(EffigyAltarRecipeDisplay.SERIALIZER)
			.fill(ClientsidedEffigyAltarREIDisplay::new);
		
		registerInformation();
	}
	
	public void registerInformation() {
		PeacefulMod.LOGGER.info("Registering information");
		DynamicRegistryManager registryManager = MinecraftClient.getInstance().world.getRegistryManager();

		// Effigy Altar
		registerInformation(PeacefulModBlocks.EFFIGY_ALTAR.asItem(), "dungeon");
		
		// Effigy
		Text dragon_effigy = Text.translatable(Items.DRAGON_BREATH.getTranslationKey() + ".dragon_effigy_information");
		registerInformation(Items.DRAGON_BREATH, dragon_effigy);
		registerInformation(PeacefulModFluids.DRAGON_BREATH, dragon_effigy);
		registerInformation(Items.NETHER_STAR, "wither_effigy");
		
		// Elder Effigy
		Text guardian_effigy = Text.translatable(PeacefulModItems.GUARDIAN_EFFIGY.getTranslationKey() + ".drop_information");
		registerInformation(Items.COOKED_COD, guardian_effigy);
		registerInformation(Items.PRISMARINE_CRYSTALS, guardian_effigy);
		registerInformation(Items.PRISMARINE_SHARD, guardian_effigy);
		
		// Raid Effigy
		Text raid_effigy = Text.translatable(PeacefulModItems.RAID_EFFIGY.getTranslationKey() + ".drop_information");
		registerInformation(Items.OMINOUS_BOTTLE, raid_effigy);
		registerInformation(Raid.createOminousBanner(registryManager.getOrThrow(RegistryKeys.BANNER_PATTERN)).getItem(), raid_effigy);
		registerInformation(Items.CROSSBOW, raid_effigy);
		registerInformation(Items.SADDLE, raid_effigy);
		registerInformation(Items.IRON_AXE, raid_effigy);
		registerInformation(Items.EMERALD, raid_effigy);
		registerInformation(Blocks.REDSTONE_WIRE.asItem(), raid_effigy);
		registerInformation(Items.GUNPOWDER, raid_effigy);
		registerInformation(Items.STICK, raid_effigy);
		registerInformation(Items.GLOWSTONE_DUST, raid_effigy);
		registerInformation(Items.SUGAR, raid_effigy);
		registerInformation(Items.SPIDER_EYE, raid_effigy);
		registerInformation(Items.GLASS_BOTTLE, raid_effigy);
		
		// Enderclam
		Text clam = Text.translatable(PeacefulMod.END_CLAM_ENTITY.getTranslationKey() + ".drop_information");
		registerInformation(Items.ENDER_PEARL, clam);
		registerInformation(PeacefulModItems.SULPHUR, clam);
		registerInformation(Items.GOLD_NUGGET, clam);
		
		// Panda
		registerInformation(Items.SLIME_BALL, "sneeze");
		
		// Frog
		Text froglight = Text.translatable(Items.MAGMA_CREAM.getTranslationKey() + ".frog_information");
		registerInformation(Items.MAGMA_CREAM, froglight);
		registerInformation(Blocks.OCHRE_FROGLIGHT.asItem(), froglight);
		registerInformation(Blocks.PEARLESCENT_FROGLIGHT.asItem(), froglight);
		registerInformation(Blocks.VERDANT_FROGLIGHT.asItem(), froglight);
		
		// Wisp
		registerInformation(Items.GHAST_TEAR, "wisp");
		
		// Bat
		registerInformation(PeacefulModItems.GUANO, "bat");
		
		// Sniffer
		Text soul_sand = Text.translatable(Blocks.SOUL_SAND.getTranslationKey() + ".sniffer_information");
		registerInformation(Blocks.SOUL_SAND.asItem(), soul_sand);
		registerInformation(Blocks.SOUL_SOIL.asItem(), soul_sand);
		registerInformation(Items.WITHER_SKELETON_SKULL, soul_sand);
		registerInformation(Items.SKELETON_SKULL, soul_sand);
		registerInformation(Items.WITHER_ROSE, soul_sand);
		registerInformation(PeacefulModBlocks.BLAZE_PICKLE.asItem(), soul_sand);
		registerInformation(Items.BONE, soul_sand);
		Text sand = Text.translatable(Blocks.SAND.getTranslationKey() + ".sniffer_information");
		registerInformation(Blocks.SAND.asItem(), sand);
		registerInformation(Items.NAUTILUS_SHELL, sand);
		registerInformation(Items.PRISMARINE_SHARD, sand);
		registerInformation(Items.PRISMARINE_CRYSTALS, sand);
		Text gravel = Text.translatable(Blocks.GRAVEL.getTranslationKey() + ".sniffer_information");
		registerInformation(Blocks.GRAVEL.asItem(), gravel);
		registerInformation(PeacefulModBlocks.BREEZE_CORAL.asItem(), gravel);
		registerInformation(Items.FLINT, gravel);
		
		// Fossil
		Text brush = Text.translatable(PeacefulModBlocks.FOSSIL_ORE.getTranslationKey() + ".brush_information");
		registerInformation(Items.BRUSH, brush);
		registerInformation(PeacefulModBlocks.FOSSIL_ORE.asItem(), brush);
		registerInformation(PeacefulModBlocks.DEEPSLATE_FOSSIL_ORE.asItem(), brush);
		registerInformation(PeacefulModBlocks.SOUL_SOIL_FOSSIL_ORE.asItem(), brush);
		
		// Wandering Trader
		Text wandering_trader_key = Text.translatable(EntityType.WANDERING_TRADER.getTranslationKey() + ".key_head_information");
		registerInformation(Items.TRIAL_KEY, wandering_trader_key);
		registerInformation(Items.OMINOUS_TRIAL_KEY, wandering_trader_key);
		registerInformation(Items.ZOMBIE_HEAD, wandering_trader_key);
		registerInformation(Items.CREEPER_HEAD, wandering_trader_key);
		registerInformation(Items.PIGLIN_HEAD, wandering_trader_key);
		
		// Dungeon Chest
		Text head_key = Text.translatable(Items.CREEPER_HEAD.getTranslationKey() + ".dungeon_chest_information");
		registerInformation(Items.CREEPER_HEAD, head_key);
		registerInformation(Items.PIGLIN_HEAD, head_key);
		registerInformation(Items.ZOMBIE_HEAD, head_key);
	}
	
	private static void registerInformation(Item item, String prefix) {
		BuiltinClientPlugin.getInstance().registerInformation(
				EntryStacks.of(item),
				Text.translatable(item.getTranslationKey()),
				(list) -> {
					//PeacefulMod.LOGGER.info(item.getTranslationKey() + "." + prefix + "_information");
					list.add(Text.translatable(item.getTranslationKey() + "." + prefix + "_information"));
					return list;
				});
	}
	
	private static void registerInformation(Item item, Text information) {
		BuiltinClientPlugin.getInstance().registerInformation(
				EntryStacks.of(item),
				Text.translatable(item.getTranslationKey()),
				(list) -> {
					//PeacefulMod.LOGGER.info(((TranslatableTextContent)information.getContent()).getKey());
					list.add(information);
					return list;
				});
	}

	private static void registerInformation(Fluid fluid, Text information) {
		RegistryKey<Fluid> key = fluid.getRegistryEntry().registryKey();
		BuiltinClientPlugin.getInstance().registerInformation(
				EntryStacks.of(fluid),
				Text.translatable("block." + key.getValue().getNamespace() + "." + key.getValue().getPath()),
				(list) -> {
					//PeacefulMod.LOGGER.info(((TranslatableTextContent)information.getContent()).getKey());
					list.add(information);
					return list;
				});
	}
	
	@Override
	public void registerScreens(ScreenRegistry registry) {
		PeacefulMod.LOGGER.info("Registering screens");
		
		registry.registerContainerClickArea(new Rectangle(88, 24 - 8, 28, 23), EffigyAltarHandledScreen.class, EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY);
	}
	
	@Override
	public void registerTransferHandlers(TransferHandlerRegistry registry) {
		PeacefulMod.LOGGER.info("Registering transfer handlers");
		
		registry.register(new EffigyAltarTransferHandler());
	}
}