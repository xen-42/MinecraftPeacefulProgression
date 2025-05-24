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

    @Shadow
    private float saturationLevel;

    @Shadow
    private int foodLevel = 20;

    @Inject(at = @At("HEAD"), method = "update", cancellable = true)
    private void update(ServerPlayerEntity player, CallbackInfo info)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (player.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
            return;
        }

        var turboHeal = player.getServerWorld().getGameRules().getBoolean(PeacefulMod.ENABLE_SUPER_HEALING_PEACEFUL);
        var canStarve = player.getServerWorld().getGameRules().getBoolean(PeacefulMod.ENABLE_STARVING_PEACEFUL);
        var canHeal = player.getServerWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        var hungerManager = (HungerManager) ((Object) this);
        
        // Do the basic not peaceful thing
        if (exhaustion > 4f) {
            exhaustion = exhaustion - 4f;
            if (hungerManager.getSaturationLevel() > 0f) {
                hungerManager.setSaturationLevel(Math.max(hungerManager.getSaturationLevel() - 1f, 0));
            } else {
                // If canStarve is off or turbo heal is on keep hunger above 5 bars 
                hungerManager.setFoodLevel(Math.max(hungerManager.getFoodLevel() - 1, canStarve ? 0 : 8));
            }
        }

        if (turboHeal) {
            if (canHeal && player.canFoodHeal()) {
                // Apply healing
                foodTickTimer++;
                if (foodTickTimer >= 10) {
                    player.heal(1);
                    player.addExhaustion(2);
                    foodTickTimer = 0;
                }
            }
        }
        else {
            if (canHeal && this.saturationLevel > 0.0F && player.canFoodHeal() && this.foodLevel >= 20) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 10) {
                    float f = Math.min(this.saturationLevel, 6.0F);
                    player.heal(f / 6.0F);
                    hungerManager.addExhaustion(f);
                    this.foodTickTimer = 0;
                }
            } else if (canHeal && this.foodLevel >= 18 && player.canFoodHeal()) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 80) {
                    player.heal(1.0F);
                    hungerManager.addExhaustion(6.0F);
                    this.foodTickTimer = 0;
                }
            } else if (this.foodLevel <= 0) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 80) {
                    if (canStarve) {
                        player.damage(player.getServerWorld(), player.getDamageSources().starve(), 1.0F);
                    }

                    this.foodTickTimer = 0;
                }
            } else {
                this.foodTickTimer = 0;
            }
        }
            
        info.cancel();
    }
}