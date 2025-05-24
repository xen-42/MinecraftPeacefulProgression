package xen42.peacefulitems.mixin;

import java.util.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(PandaEntity.class)
public class PandaEntityMixin {
	private static final Random random = new Random();

    @Inject(at = @At("HEAD"), method = "sneeze", cancellable = true)
	private void sneeze(CallbackInfo info) {
		var panda = ((PandaEntity)(Object)this);

        var nosePos = new Vec3d(
            panda.getX() - (panda.isBaby() ? 1.0 : 1.3) * (double)(panda.getWidth() + 1.0f) * 0.5 * (double)MathHelper.sin(panda.bodyYaw * ((float)Math.PI / 180)), 
            panda.getEyeY() - (panda.isBaby() ? 0.1 : 0.3), 
            panda.getZ() + (panda.isBaby() ? 1.0 : 1.3) * (double)(panda.getWidth() + 1.0f) * 0.5 * (double)MathHelper.cos(panda.bodyYaw * ((float)Math.PI / 180)));

        // Base game pandas have a 1 in 700 chance of dropping slime
        // Bump that up to 3 (technically theres a 1 in 2100 chance now that they drop two at once)
        // Also make it spawn near their face
        if (!panda.getWorld().isClient() && random.nextInt(3) == 0) {
            var item = panda.dropItem((ServerWorld)panda.getWorld(), Items.SLIME_BALL);
            item.setPosition(nosePos);
            // Play a sound so you know it happened
            panda.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
        }

        // For adult pandas we pitch shift down the sound so we will just call the method stuff ourselves
        // We will also skip the part where all other pandas jump
        if (!panda.isBaby()) {
            panda.playSound(SoundEvents.ENTITY_PANDA_SNEEZE, 1.0f, 0.7f);
            var vel = panda.getVelocity();
            panda.getWorld().addParticleClient(ParticleTypes.SNEEZE, nosePos.x, nosePos.y, nosePos.z, vel.x, 0.0, vel.z);
        
            info.cancel();
        }
	}
}
