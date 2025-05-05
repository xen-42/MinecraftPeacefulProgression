package xen42.peacefulitems.entity;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.state.AllayEntityRenderState;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModClient;
import xen42.peacefulitems.entities.GhastlingEntity;

public class GhastlingEntityRenderer extends MobEntityRenderer<GhastlingEntity, AllayEntityRenderState, GhastlingEntityModel<GhastlingEntity>> {

    public GhastlingEntityRenderer(Context context) {
        super(context, new GhastlingEntityModel<GhastlingEntity>(context.getPart(PeacefulModClient.MODEL_GHASTLING_LAYER)), 0.2F);
    }

    @Override
    public Identifier getTexture(AllayEntityRenderState state) {
        return Identifier.of(PeacefulMod.MOD_ID, "textures/entity/ghastling/ghastling.png");
    }

    @Override
    public AllayEntityRenderState createRenderState() {
        return new AllayEntityRenderState();
    }
}
