package xen42.peacefulitems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BrushableBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.entity.EndClamEntityModel;
import xen42.peacefulitems.entity.EndClamEntityRenderer;
import xen42.peacefulitems.entity.GhastlingEntityModel;
import xen42.peacefulitems.entity.GhastlingEntityRenderer;
import xen42.peacefulitems.payloads.EffigyParticlePayload;
import xen42.peacefulitems.payloads.GhostRecipeCostResponse;
import xen42.peacefulitems.screen.EffigyAltarHandledScreen;

public class PeacefulModClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_GHASTLING_LAYER = new EntityModelLayer(Identifier.of(PeacefulMod.MOD_ID, "ghastling"), "main");
	public static final EntityModelLayer MODEL_END_CLAM_LAYER = new EntityModelLayer(Identifier.of(PeacefulMod.MOD_ID, "end_clam"), "main");

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(PeacefulModBlocks.SULPHUR_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(PeacefulModBlocks.FLAX_CROP, RenderLayer.getCutout());
		
		BlockEntityRendererFactories.register(PeacefulModBlocks.FOSSIL_ORE_ENTITY, BrushableBlockEntityRenderer::new);
		
		Identifier dragonBreathTexture = Identifier.of(PeacefulMod.MOD_ID, "dragon_breath").withPrefixedPath("block/");
		FluidRenderHandlerRegistry.INSTANCE.register(PeacefulModFluids.DRAGON_BREATH, new SimpleFluidRenderHandler(dragonBreathTexture, dragonBreathTexture));
		
		EntityRendererRegistry.register(PeacefulMod.GHASTLING_ENTITY, context -> new GhastlingEntityRenderer(context)); 
		EntityModelLayerRegistry.registerModelLayer(MODEL_GHASTLING_LAYER, GhastlingEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(PeacefulMod.END_CLAM_ENTITY, context -> new EndClamEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_END_CLAM_LAYER, EndClamEntityModel::getTexturedModelData);

		HandledScreens.register(PeacefulMod.EFFIGY_ALTAR_SCREEN_HANDLER, EffigyAltarHandledScreen::new);

		ClientPlayNetworking.registerGlobalReceiver(EffigyParticlePayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				context.client().particleManager.addEmitter(context.player(), (ParticleEffect)ParticleTypes.TOTEM_OF_UNDYING, 30);
				switch (payload.particleID()) {
					case "wither_effigy":
						context.client().gameRenderer.showFloatingItem(new ItemStack(PeacefulModItems.WITHER_EFFIGY));
						break;
					case "dragon_effigy":
						context.client().gameRenderer.showFloatingItem(new ItemStack(PeacefulModItems.DRAGON_EFFIGY));
						break;
					case "guardian_effigy":
						context.client().gameRenderer.showFloatingItem(new ItemStack(PeacefulModItems.GUARDIAN_EFFIGY));
						break;
					case "raid_effigy":
						context.client().gameRenderer.showFloatingItem(new ItemStack(PeacefulModItems.RAID_EFFIGY));
						break;
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(GhostRecipeCostResponse.PAYLOAD_ID, (payload, context) -> {
			EffigyAltarHandledScreen.ClientData.setGhostXPCost(payload.cost());
		});
	}
}