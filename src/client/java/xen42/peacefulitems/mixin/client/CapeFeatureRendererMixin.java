package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import xen42.peacefulitems.PeacefulModItems;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider,
			int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity,
			float f,
			float g,
			float h,
			float j,
			float k,
			float l,
			CallbackInfo info) {
		ItemStack itemStack = abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
		if (itemStack.isOf(PeacefulModItems.CAPE)) {
			info.cancel();
		}
	}
}
