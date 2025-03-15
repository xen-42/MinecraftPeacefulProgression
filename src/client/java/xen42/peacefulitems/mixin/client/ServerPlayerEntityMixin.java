package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;
import xen42.peacefulitems.PeacefulMod;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "tickHunger", cancellable = true)
    private void tickHunger(CallbackInfo info) {
        var player = (ServerPlayerEntity)((Object)this);
        // This method just makes you heal hunger at all times
        if (!player.getServerWorld().getGameRules().getBoolean(PeacefulMod.DISABLE_HUNGER_PEACEFUL)) {
            info.cancel();
        }
    }
}
