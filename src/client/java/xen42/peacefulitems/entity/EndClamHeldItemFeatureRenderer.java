package xen42.peacefulitems.entity;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import xen42.peacefulitems.entities.EndClamEntity;

public class EndClamHeldItemFeatureRenderer extends FeatureRenderer<EndClamEntity, EndClamEntityModel> {
	private final ItemRenderer itemRenderer;

    public EndClamHeldItemFeatureRenderer(
            FeatureRendererContext<EndClamEntity, EndClamEntityModel> context, ItemRenderer itemRenderer) {
        super(context);
		this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EndClamEntity entity,
			float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
			float headPitch) {
		ItemStack stack = entity.getMainHandStack();
		if (!stack.isEmpty()) {
			matrices.push();
			matrices.translate(this.getContextModel().clam.pivotX / 16.0f, this.getContextModel().clam.pivotY / 16.0f - 0.15f, this.getContextModel().clam.pivotZ / 16.0f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            // They're upsidedown for some reason
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f-33f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.getContextModel().clam.roll));
			this.itemRenderer
				.renderItem(entity, stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, entity.getWorld(), light, OverlayTexture.DEFAULT_UV, entity.getId() + ModelTransformationMode.GROUND.ordinal());
			matrices.pop();
		}
    }
}
