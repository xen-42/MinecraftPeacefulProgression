package xen42.peacefulitems.entity;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModClient;
import xen42.peacefulitems.entities.EndClamEntity;

public class EndClamEntityRenderer extends MobEntityRenderer<EndClamEntity, EndClamEntityRenderState, EndClamEntityModel> {

    public EndClamEntityRenderer(Context context) {
        super(context, new EndClamEntityModel(context.getPart(PeacefulModClient.MODEL_END_CLAM_LAYER)), 0.2f);
        this.addFeature(new EndClamHeldItemFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(EndClamEntityRenderState state) {
        return Identifier.of(PeacefulMod.MOD_ID, "textures/entity/end_clam/end_clam.png");
    }

    @Override
    public EndClamEntityRenderState createRenderState() {
        return new EndClamEntityRenderState();
    }

    @Override
    public void updateRenderState(EndClamEntity endClamEntity, EndClamEntityRenderState endClamEntityRenderState, float f) {
		super.updateRenderState(endClamEntity, endClamEntityRenderState, f);

		endClamEntityRenderState.idleAnimationState.copyFrom(endClamEntity.idleAnimationState);
		endClamEntityRenderState.yawnAnimationState.copyFrom(endClamEntity.yawnAnimationState);
		endClamEntityRenderState.hitAnimationState.copyFrom(endClamEntity.hitAnimationState);

        ItemHolderEntityRenderState.update(endClamEntity, endClamEntityRenderState, this.itemModelResolver);
	}
}
