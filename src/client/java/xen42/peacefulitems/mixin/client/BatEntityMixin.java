package xen42.peacefulitems.mixin.client;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.event.GameEvent;
import xen42.peacefulitems.PeacefulModItems;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BatEntity.class)
public class BatEntityMixin {
	private static final Random random = new Random();

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);

		// On average once every five seconds, I think!
		if (!bat.getWorld().isClient && bat.isAlive() && random.nextFloat() < 1f / (5f * 60f)) {
			bat.emitGameEvent(GameEvent.ENTITY_PLACE);
			bat.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
            bat.dropItem((ServerWorld)bat.getWorld(), PeacefulModItems.GUANO);
		}
	}

	@Inject(at = @At("TAIL"), method = "damage")
	private void damage(CallbackInfoReturnable<Boolean> info) {
		var bat = ((BatEntity)(Object)this);

		if (!bat.getWorld().isClient && !bat.isAlive()) {
			var r = random.nextFloat();
			if (r > 0.8) {
				bat.dropStack((ServerWorld)bat.getWorld(), new ItemStack(PeacefulModItems.BAT_WING, 2));
			}
			else if (r > 0.3) {
				bat.dropItem((ServerWorld)bat.getWorld(), PeacefulModItems.BAT_WING);
			}
		}
	}
}