package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.Difficulty;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin {
    @Inject(at = @At("HEAD"), method = "checkDespawn")
    public void checkDespawn(CallbackInfo info) {
        var dragon = (EnderDragonEntity)((Object)this);
        if (dragon.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            dragon.discard();
        } 
    }
}
