package xen42.peacefulitems.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.event.GameEvent;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModItems;
import xen42.peacefulitems.entities.BatHelper;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BatEntity.class)
public class BatEntityMixin {
	private static final Random random = new Random();

	@Inject(at = @At("TAIL"), method = "initDataTracker")
    public void initDataTracker(DataTracker.Builder builder, CallbackInfo info) {
		builder.add(PeacefulMod.BAT_BREEDING_TICKS, 0);
		builder.add(PeacefulMod.BAT_IS_BABY, false);
		builder.add(PeacefulMod.BAT_BREEDING_COOLDOWN, 0);
    }

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);

		// On average once every X seconds, I think!
		// 20 ticks per second
		if (!bat.getWorld().isClient && bat.isAlive() && !bat.isBaby() && random.nextFloat() < 1f / (300f * 20f)) {
			bat.emitGameEvent(GameEvent.ENTITY_PLACE);
			bat.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
            bat.dropItem((ServerWorld)bat.getWorld(), PeacefulModItems.GUANO);
		}
	}

	@Inject(at = @At("RETURN"), method = "isPushable", cancellable = true)
	private void isPushable(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(true);
		info.cancel();
	}

	@Inject(at = @At("HEAD"), method = "pushAway")
	private void pushAway(Entity entity, CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);
		entity.pushAwayFrom(bat);
	}

	@Inject(at = @At("HEAD"), method = "tickCramming")
	private void tickCramming(CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);

		if (!(bat.getWorld() instanceof ServerWorld serverWorld)) {
			bat.getWorld()
				.getEntitiesByType(TypeFilter.instanceOf(PlayerEntity.class), bat.getBoundingBox(), EntityPredicates.canBePushedBy(bat))
				.forEach((entitity) -> entitity.pushAwayFrom(bat));
		} else {
			List<Entity> list = bat.getWorld().getOtherEntities(bat, bat.getBoundingBox(), EntityPredicates.canBePushedBy(bat));
			if (!list.isEmpty()) {
				int i = serverWorld.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
				if (i > 0 && list.size() > i - 1 && random.nextInt(4) == 0) {
					int j = 0;

					for (Entity entity : list) {
						if (!entity.hasVehicle()) {
							j++;
						}
					}

					if (j > i - 1) {
						bat.damage(serverWorld, bat.getDamageSources().cramming(), 6.0F);
					}
				}

				for (Entity entity2 : list) {
					entity2.pushAwayFrom(bat);
				}
			}
		}
	}

	// Injecting into onDeath didnt work, probably because thats an inherited method that the BatEntity class doesnt override
	@Inject(at = @At("TAIL"), method = "damage")
	private void damage(CallbackInfoReturnable<Boolean> info) {
		var bat = ((BatEntity)(Object)this);

		if (!bat.getWorld().isClient && !bat.isAlive() && !bat.isBaby()) {
			var r = random.nextFloat();
			if (r > 0.8) {
				bat.dropStack((ServerWorld)bat.getWorld(), new ItemStack(PeacefulModItems.BAT_WING, 2));
			}
			else if (r > 0.3) {
				bat.dropItem((ServerWorld)bat.getWorld(), PeacefulModItems.BAT_WING);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "mobTick", cancellable = true)
    public void mobTick(CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);
		var player = bat.getWorld().getClosestPlayer(bat, 10);
		if (!bat.isRoosting()) {
			var breedingTicks = bat.getDataTracker().get(PeacefulMod.BAT_BREEDING_TICKS);
			var batBreedingCooldown = bat.getDataTracker().get(PeacefulMod.BAT_BREEDING_COOLDOWN);
			if (bat.isBaby() && batBreedingCooldown <= 0) {
				bat.getDataTracker().set(PeacefulMod.BAT_IS_BABY, false);
			}
			if (batBreedingCooldown > 0) {
				bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, batBreedingCooldown - 1);
			}
			if (!bat.isBaby() && breedingTicks > 0) {
				bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_TICKS, breedingTicks - 1);

        		List<BatEntity> list = bat.getWorld().getNonSpectatingEntities(BatEntity.class, bat.getBoundingBox().expand(10.0));
				BatEntity mate = null;
				for (var otherBat : list) {
					if (otherBat != bat && otherBat.getDataTracker().get(PeacefulMod.BAT_BREEDING_TICKS) > 0) {
						mate = otherBat;
					}
				}
				if (mate != null) {
					if (mate.distanceTo(bat) < 0.5f) {
						var baby = EntityType.BAT.create(bat.getWorld(), SpawnReason.BREEDING);
						baby.refreshPositionAndAngles(bat.getX(), bat.getY(), bat.getZ(), 0.0f, 0.0f);
						bat.getWorld().spawnEntity(baby);
						for (int i = 0; i < 7; ++i) {
							double d = bat.getRandom().nextGaussian() * 0.02;
							double e = bat.getRandom().nextGaussian() * 0.02;
							double f = bat.getRandom().nextGaussian() * 0.02;
							bat.getWorld().addParticle(ParticleTypes.HEART, bat.getParticleX(1.0), bat.getRandomBodyY() + 0.5, bat.getParticleZ(1.0), d, e, f);
						}
						bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_TICKS, 0);
						mate.getDataTracker().set(PeacefulMod.BAT_BREEDING_TICKS, 0);
						baby.getDataTracker().set(PeacefulMod.BAT_IS_BABY, true);

						bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, PeacefulMod.BatBreedingCooldown);
						mate.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, PeacefulMod.BatBreedingCooldown);
						// Breeding cooldown doubles as age timer because why not
						baby.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, PeacefulMod.BatGrowUpTicks);
					}
					else {
						BatHelper.FlyTowards(bat, mate.getPos());
					}
					info.cancel();
					return;
				}
			}
			if (player != null && player.isHolding(Items.MELON_SLICE)) {
				var playerPos = player.getPos().add(player.getHorizontalFacing().getDoubleVector());

				BatHelper.FlyTowards(bat, playerPos.add(new Vec3d(0f, 1.0f, 0f)));
				info.cancel();
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);

		nbt.putBoolean("IsBaby", bat.getDataTracker().get(PeacefulMod.BAT_IS_BABY));
		nbt.putInt("BreedingTicks", bat.getDataTracker().get(PeacefulMod.BAT_BREEDING_TICKS));
		nbt.putInt("BreedingCooldown", bat.getDataTracker().get(PeacefulMod.BAT_BREEDING_COOLDOWN));
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
		var bat = ((BatEntity)(Object)this);

		bat.getDataTracker().set(PeacefulMod.BAT_IS_BABY, nbt.getBoolean("IsBaby"));
		bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_TICKS, nbt.getInt("BreedingTicks"));
		bat.getDataTracker().set(PeacefulMod.BAT_BREEDING_COOLDOWN, nbt.getInt("BreedingCooldown"));
	}
}