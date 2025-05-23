package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;

@Mixin(PlayerEntity.class)
public class ServerPlayerEntityMixin {
    // Redirect the peaceful+regen branch to always return false (skip it)
    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"), method = "tickMovement")
    private Difficulty redirectPeacefulCheck(net.minecraft.world.World world) {
        // Spoof the difficulty as something other than PEACEFUL
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return Difficulty.EASY;
        }
        return world.getDifficulty();
    }
}
