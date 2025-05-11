package xen42.peacefulitems.mixin;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {

    @Shadow
    private ServerWorld world;

    @Shadow
    private UUID dragonUuid;

    @Shadow
    private ServerBossBar bossBar;

    @Shadow
    private boolean previouslyKilled;

    @Shadow
    @Nullable
    private BlockPos exitPortalLocation;

    @Shadow
    private void generateEndPortal(boolean previouslyKilled) {}

    @Shadow
    @Nullable
    private BlockPattern.Result findEndPortal() { return null; }

    @Inject(at = @At("HEAD"), method = "createDragon", cancellable = true)
    public void createDragon(CallbackInfoReturnable<EnderDragonEntity> info) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            info.setReturnValue(null);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick(CallbackInfo info) {
        var fight = (EnderDragonFight)((Object)this);

        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            // Set the current dragon to null this way if they switch off peaceful itll come back
            dragonUuid = null;

            if (!fight.hasPreviouslyKilled()) {
                exitPortalLocation = null; // Set to null for either findEndPortal or generateEndPortal to set the value.
                findEndPortal(); // find existing location if there is one
                generateEndPortal(true); // if no existing it will generate new one. if existing it will open the portal.

                previouslyKilled = true;
            }

            bossBar.setVisible(false);
            info.cancel();
        }
    }
}
