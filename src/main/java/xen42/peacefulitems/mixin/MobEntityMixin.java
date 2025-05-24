package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.passive.FrogVariants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xen42.peacefulitems.PeacefulMod;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(at = @At("HEAD"), method = "loot", cancellable = true)
    private void loot(ServerWorld world, ItemEntity item, CallbackInfo info) {

        // Make sure the base looting logic doesnt run for frogs else they take all your stuff
        if ((Object)this instanceof FrogEntity) {
            info.cancel();

            if (!world.isClient && world.getRegistryKey() == World.NETHER) {
                var frog = (FrogEntity)((Object)this);
                if (item.getStack().isOf(Items.MAGMA_CREAM)) {
                    var variant = (RegistryKey<FrogVariant>)frog.getVariant().getKey().orElse(null);
                    var block = Blocks.VERDANT_FROGLIGHT;
                    if (variant == FrogVariants.TEMPERATE) {
                        block = Blocks.OCHRE_FROGLIGHT;
                    }
                    else if (variant == FrogVariants.WARM) {
                        block = Blocks.PEARLESCENT_FROGLIGHT;
                    }
    
                    frog.playSound(SoundEvents.ENTITY_FROG_EAT);
                    frog.getWorld().playSoundFromEntity(null, frog, SoundEvents.ENTITY_FROG_EAT, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    frog.setPose(EntityPose.USING_TONGUE);
                    frog.lookAtEntity(item, 180, 180);
                    frog.tryAttack(world, item);
    
                    var count = item.getStack().getCount();
                    // Random number between half and all of the magma cream
                    var spawnCount = (int)(frog.getRandom().nextFloat() * count / 2) + count / 2;
                    if (spawnCount > 0) {
                        frog.dropStack(world, new ItemStack(block.asItem(), spawnCount)); 
                    }
                    item.discard();
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        
        if ((Object)this instanceof BatEntity) {
            ItemStack item = player.getStackInHand(hand);
            var bat = ((BatEntity)(Object)this);

            if (item.isOf(Items.MELON_SLICE) && bat.getDataTracker().get(PeacefulMod.BAT_BREEDING_TICKS) <= 0f 
                    && bat.getDataTracker().get(PeacefulMod.BAT_BREEDING_COOLDOWN) <= 0f) {                
                if (!player.getAbilities().creativeMode) {
                    item.decrement(1);
                }
                for (int i = 0; i < 7; ++i) {
                    double d = bat.getRandom().nextGaussian() * 0.02;
                    double e = bat.getRandom().nextGaussian() * 0.02;
                    double f = bat.getRandom().nextGaussian() * 0.02;
                    bat.getWorld().addParticleClient(ParticleTypes.HEART, bat.getParticleX(1.0), bat.getRandomBodyY() + 0.5, bat.getParticleZ(1.0), d, e, f);
                }
                // 40 seconds I think?
                bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_TICKS, 40*20);
                info.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "setBaby", cancellable = true)
    private void setBaby(boolean baby, CallbackInfo info) {
        if (((Object)this) instanceof BatEntity) {
            var bat = ((BatEntity)(Object)this);
            bat.getDataTracker().set(PeacefulMod.BAT_IS_BABY, baby);
            if (baby) {
                // Breeding cooldown doubles as age timer because why not
                bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, PeacefulMod.BatGrowUpTicks);
            }
            else {
                bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, 0);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "canImmediatelyDespawn", cancellable = true)
    public void canImmediatelyDespawn(double distanceSquared, CallbackInfoReturnable<Boolean> info) {
        if (((Object)this) instanceof BatEntity) {
            info.setReturnValue(false);
        }
    }
}
