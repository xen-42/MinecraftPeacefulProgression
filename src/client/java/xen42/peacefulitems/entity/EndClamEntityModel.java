package xen42.peacefulitems.entity;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;

public class EndClamEntityModel extends EntityModel<EndClamEntityRenderState> {
	public final ModelPart clam;
	public final ModelPart bottom_shell;
	public final ModelPart top_shell;

	protected EndClamEntityModel(ModelPart root) {
        super(root);
		this.clam = root.getChild("clam");
		this.bottom_shell = this.clam.getChild("bottom_shell");
		this.top_shell = this.clam.getChild("top_shell");
	}
    
	@SuppressWarnings("unused")
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData clam = modelPartData.addChild("clam", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));

		ModelPartData bottom_shell = clam.addChild("bottom_shell", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 0.0F, -9.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -3.0F, 5.0F));

		ModelPartData top_r1 = bottom_shell.addChild("top_r1", ModelPartBuilder.create().uv(0, 28).mirrored().cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.0F, -5.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData back_r1 = bottom_shell.addChild("back_r1", ModelPartBuilder.create().uv(0, 11).cuboid(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, -0.5F, 0.0F, 0.0F, -3.1416F));

		ModelPartData outer_r1 = bottom_shell.addChild("outer_r1", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-4.0F, -0.5F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.5F, -5.0F, 0.0F, 0.0F, 3.1416F));

		ModelPartData top_shell = clam.addChild("top_shell", ModelPartBuilder.create().uv(0, 17).cuboid(-4.0F, -2.0F, -9.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 11).cuboid(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 28).cuboid(-3.0F, -3.0F, -8.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -3.0F, 5.0F));

		ModelPartData inner_r1 = top_shell.addChild("inner_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -0.5F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.5F, -5.0F, 3.1416F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(EndClamEntityRenderState endClamEntityRenderState) {
		super.setAngles(endClamEntityRenderState);

		this.animate(endClamEntityRenderState.idleAnimationState, EndClamAnimation.IDLE, endClamEntityRenderState.age, 1.0F);
		this.animate(endClamEntityRenderState.hitAnimationState, EndClamAnimation.HIT, endClamEntityRenderState.age, 1.0F);
		this.animate(endClamEntityRenderState.yawnAnimationState, EndClamAnimation.YAWN, endClamEntityRenderState.age, 1.0F);
		this.animate(endClamEntityRenderState.openAnimationState, EndClamAnimation.OPEN, endClamEntityRenderState.age, 1.0F);
	}
}