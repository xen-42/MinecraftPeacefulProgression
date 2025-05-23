package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModItems;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	@Unique
	private static final Identifier CAPE_TEXTURE = Identifier.of(PeacefulMod.MOD_ID, "textures/entity/equipment/wings/cape.png");
	@Shadow
	private ElytraEntityModel<T> elytra;
	
	public ElytraFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider,
			int light,
			T livingEntity,
			float limbAngle,
			float limbDistance,
			float tickDelta,
			float animationProgress,
			float headYaw,
			float headPitch,
			CallbackInfo info) {
		ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
		if (itemStack.isOf(PeacefulModItems.CAPE)) {
			matrixStack.push();
			matrixStack.translate(0.0F, 0.0F, 0.125F);
			this.getContextModel().copyStateTo(this.elytra);
			this.elytra.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(
				vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(CAPE_TEXTURE), itemStack.hasGlint()
			);
			this.elytra.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			matrixStack.pop();
			info.cancel();
		}
	}

	@Shadow
	public void render(MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider,
			int light,
			T livingEntity,
			float limbAngle,
			float limbDistance,
			float tickDelta,
			float animationProgress,
			float headYaw,
			float headPitch) {
	}
}
