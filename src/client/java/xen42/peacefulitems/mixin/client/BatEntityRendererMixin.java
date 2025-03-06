package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.BatEntityRenderer;
import net.minecraft.client.render.entity.state.BatEntityRenderState;
import net.minecraft.entity.passive.BatEntity;

@Mixin(BatEntityRenderer.class)
public class BatEntityRendererMixin {
    @Inject(at = @At("TAIL"), method = "updateRenderState")
    private void updateRenderState(BatEntity batEntity, BatEntityRenderState batEntityRenderState, float f, CallbackInfo info) {
        batEntityRenderState.baseScale = batEntity.getScale() * (batEntity.isBaby() ? 0.5f : 1f);
    }
}
