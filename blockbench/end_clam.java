// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class end_clam extends EntityModel<Entity> {
	private final ModelPart hitbox;
	private final ModelPart clam;
	private final ModelPart bottom_shell;
	private final ModelPart top_shell;
	public end_clam(ModelPart root) {
		this.hitbox = root.getChild("hitbox");
		this.clam = root.getChild("clam");
		this.bottom_shell = this.clam.getChild("bottom_shell");
		this.top_shell = this.clam.getChild("top_shell");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData hitbox = modelPartData.addChild("hitbox", ModelPartBuilder.create().uv(25, 43).cuboid(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData clam = modelPartData.addChild("clam", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData bottom_shell = clam.addChild("bottom_shell", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 0.0F, -9.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 5.0F));

		ModelPartData top_r1 = bottom_shell.addChild("top_r1", ModelPartBuilder.create().uv(0, 28).mirrored().cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.0F, -5.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData back_r1 = bottom_shell.addChild("back_r1", ModelPartBuilder.create().uv(0, 11).cuboid(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, -0.5F, 0.0F, 0.0F, -3.1416F));

		ModelPartData outer_r1 = bottom_shell.addChild("outer_r1", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-4.0F, -0.5F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.5F, -5.0F, 0.0F, 0.0F, 3.1416F));

		ModelPartData top_shell = clam.addChild("top_shell", ModelPartBuilder.create().uv(0, 17).cuboid(-4.0F, -2.0F, -9.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 11).cuboid(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 28).cuboid(-3.0F, -3.0F, -8.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 5.0F));

		ModelPartData inner_r1 = top_shell.addChild("inner_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -0.5F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.5F, -5.0F, 3.1416F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		hitbox.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		clam.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}