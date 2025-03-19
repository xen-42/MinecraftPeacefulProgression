package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Inject(at = @At("HEAD"), method = "createDragon", cancellable = true)
    public void createDragon(CallbackInfoReturnable<EnderDragonEntity> info) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var fight = (EnderDragonFight)((Object)this);

        var field = fight.getClass().getDeclaredField("world");
        field.setAccessible(true);
        var world = (ServerWorld)field.get(fight);

		if (world.getDifficulty() == Difficulty.PEACEFUL) {
            info.setReturnValue(null);
            info.cancel();
        }
    }
}
