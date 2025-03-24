package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BatEntity;
import xen42.peacefulitems.PeacefulMod;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("RETURN"), method = "isBaby", cancellable = true)
    private void isBaby(CallbackInfoReturnable<Boolean> info) {
        if (((Object)this) instanceof BatEntity) {
            var bat = (BatEntity)((Object)this);
            info.setReturnValue(bat.getDataTracker().get(PeacefulMod.BAT_IS_BABY));
            info.cancel();
        }
    }
}
