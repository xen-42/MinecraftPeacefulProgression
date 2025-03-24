package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.passive.FrogEntity;

@Mixin(FrogEntity.class)
public class FrogEntityMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
	private void ctor(CallbackInfo info) {
        var frog = (FrogEntity)((Object)this);
        frog.setCanPickUpLoot(true);
	}
}
