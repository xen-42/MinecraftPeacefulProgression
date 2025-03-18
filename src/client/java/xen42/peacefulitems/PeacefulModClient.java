package xen42.peacefulitems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.entity.EndClamEntityModel;
import xen42.peacefulitems.entity.EndClamEntityRenderer;
import xen42.peacefulitems.entity.GhastlingEntityModel;
import xen42.peacefulitems.entity.GhastlingEntityRenderer;

public class PeacefulModClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_GHASTLING_LAYER = new EntityModelLayer(Identifier.of(PeacefulMod.MOD_ID, "ghastling"), "main");
    public static final EntityModelLayer MODEL_END_CLAM_LAYER = new EntityModelLayer(Identifier.of(PeacefulMod.MOD_ID, "end_clam"), "main");

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(PeacefulModBlocks.SULPHUR_CLUSTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(PeacefulModBlocks.FLAX_CROP, RenderLayer.getCutout());

        EntityRendererRegistry.register(PeacefulMod.GHASTLING_ENTITY, context -> new GhastlingEntityRenderer(context)); 
        EntityModelLayerRegistry.registerModelLayer(MODEL_GHASTLING_LAYER, GhastlingEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(PeacefulMod.END_CLAM_ENTITY, context -> new EndClamEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_END_CLAM_LAYER, EndClamEntityModel::getTexturedModelData);
	}
}