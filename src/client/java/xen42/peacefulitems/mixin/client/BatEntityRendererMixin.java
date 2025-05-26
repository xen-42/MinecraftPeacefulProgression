package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.BatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BatEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.Identifier;

@Mixin(BatEntityRenderer.class)
public class BatEntityRendererMixin extends MobEntityRenderer<BatEntity, BatEntityModel> {
    public BatEntityRendererMixin(Context context, BatEntityModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Shadow
    public Identifier getTexture(BatEntity entity) { return null; }

    @Inject(at = @At("TAIL"), method = "scale", cancellable = true)
    public void scale(BatEntity batEntity, MatrixStack matrixStack, float f, CallbackInfo info) {
        float scaleFactor = batEntity.isBaby() ? 0.5f : 1f;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
    }
}
