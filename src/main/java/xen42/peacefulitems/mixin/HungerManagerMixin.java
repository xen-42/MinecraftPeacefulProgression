package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import xen42.peacefulitems.PeacefulMod;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow
    private float exhaustion;

    @Shadow
    private int foodTickTimer;

    @Inject(at = @At("HEAD"), method = "update", cancellable = true)
    private void update(ServerPlayerEntity player, CallbackInfo info) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (player.getServerWorld().getGameRules().getBoolean(PeacefulMod.DISABLE_HUNGER_PEACEFUL)) {
            // Default behaviour
            return;
        }

        // Apply hunger even when in peaceful
        // Just gonna entirely hijack this method since it also does healing
        // We want the player to constantly heal like in normal peaceful, just allow the hunger to drop
        // It's entirely cosmetic and won't affect gameplay but eating food is fun? idk
        if (player.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            var hungerManager = (HungerManager)((Object)this);

            // Apply hunger
            if (exhaustion > 4f) {
                exhaustion = exhaustion - 4f;
                if (hungerManager.getSaturationLevel() > 0f) {
                    hungerManager.setSaturationLevel(Math.max(hungerManager.getSaturationLevel() - 1f, 0));
                }
                else {
                    hungerManager.setFoodLevel(Math.max(hungerManager.getFoodLevel() - 1, 10));
                }
            }

            if (player.getServerWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) && player.canFoodHeal()) {
                // Apply healing               
                foodTickTimer++;
                if (foodTickTimer >= 10) {
                    player.heal(1);
                    player.addExhaustion(2);
                    foodTickTimer = 0;
                }
            }

            info.cancel();
        }
    }
}