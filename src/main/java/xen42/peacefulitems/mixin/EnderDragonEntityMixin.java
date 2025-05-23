package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import xen42.peacefulitems.PeacefulMod;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin {
    @Inject(at = @At("HEAD"), method = "checkDespawn")
    public void checkDespawn(CallbackInfo info) {
        var dragon = (EnderDragonEntity)((Object)this);
        if (dragon.getWorld() instanceof ServerWorld serverWorld && 
            serverWorld.getDifficulty() == Difficulty.PEACEFUL &&
            !serverWorld.getGameRules().getBoolean(PeacefulMod.ENABLE_ENDER_DRAGON_FIGHT_PEACEFUL))
        {
            dragon.discard();
        } 
    }
}
