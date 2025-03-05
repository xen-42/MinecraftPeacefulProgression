package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.BatEntity;

@Mixin(AmbientEntity.class)
public class AmbientEntityMixin {
    // Has to be done here because this method actually defines the method whereas BatEntity doesnt change it
    @Inject(at = @At("RETURN"), method = "canBeLeashed", cancellable = true)
    public void canBeLeashedBy(CallbackInfoReturnable<Boolean> info) {
		var entity = ((AmbientEntity)(Object)this);
        if (entity instanceof BatEntity) {
            info.setReturnValue(!entity.isLeashed());
        }
    }
}
