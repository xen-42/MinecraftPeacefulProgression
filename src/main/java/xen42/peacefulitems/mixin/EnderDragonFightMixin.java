package xen42.peacefulitems.mixin;

import java.lang.reflect.InvocationTargetException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.boss.ServerBossBar;
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

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick(CallbackInfo info) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        var fight = (EnderDragonFight)((Object)this);

        var field = fight.getClass().getDeclaredField("world");
        field.setAccessible(true);
        var world = (ServerWorld)field.get(fight);

        // Set the current dragon to null this way if they switch off peaceful itll come back
        var dragonUUIDField = fight.getClass().getDeclaredField("dragonUuid");
        dragonUUIDField.setAccessible(true);
        dragonUUIDField.set(fight, null);

		if (world.getDifficulty() == Difficulty.PEACEFUL) {

            var bossBarField = fight.getClass().getDeclaredField("bossBar");
            bossBarField.setAccessible(true);
            var bossBar = (ServerBossBar)bossBarField.get(fight);

            if (!fight.hasPreviouslyKilled()) {
                var generateEndPortalMethod = fight.getClass().getDeclaredMethod("generateEndPortal", boolean.class);
                generateEndPortalMethod.invoke(fight, true);

                var previouslyKilledField = fight.getClass().getDeclaredField("previouslyKilled");
                previouslyKilledField.setAccessible(true);
                previouslyKilledField.set(fight, true);
            }

            bossBar.setVisible(false);
            info.cancel();
        }
    }
}
