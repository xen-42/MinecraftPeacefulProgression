package xen42.peacefulitems.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class EndClamHeldItemFeatureRenderer extends FeatureRenderer<EndClamEntityRenderState, EndClamEntityModel> {

    public EndClamHeldItemFeatureRenderer(
            FeatureRendererContext<EndClamEntityRenderState, EndClamEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EndClamEntityRenderState state, float limbAngle, float limbDistance) {
        var itemRenderState = state.itemRenderState;
		if (!itemRenderState.isEmpty()) {

			matrices.push();
			matrices.translate(this.getContextModel().clam.originX / 16.0f, this.getContextModel().clam.originY / 16.0f - 0.15f, this.getContextModel().clam.originZ / 16.0f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            // They're upsidedown for some reason
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f-33f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.getContextModel().clam.roll));
			itemRenderState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
			matrices.pop();
		}
    }
    
}
