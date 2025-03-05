package xen42.peacefulitems.mixin.client;

import java.rmi.registry.Registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CroakTask;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.FrogEatEntityTask;
import net.minecraft.entity.ai.brain.task.LayFrogSpawnTask;
import net.minecraft.entity.ai.brain.task.LookAtMobWithIntervalTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.brain.task.WalkTowardsWaterTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.intprovider.UniformIntProvider;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(at = @At("TAIL"), method = "loot")
	private void loot(ServerWorld world, ItemEntity item, CallbackInfo info) {
        if ((Object)this instanceof FrogEntity && !world.isClient) {
            var frog = (FrogEntity)((Object)this);
            if (item.getStack().isOf(Items.MAGMA_CREAM)) {
                var variant = (RegistryKey<FrogVariant>)frog.getVariant().getKey().orElse(null);
                if (variant == FrogVariant.TEMPERATE) {
                    frog.dropItem((ServerWorld)frog.getWorld(), Blocks.OCHRE_FROGLIGHT.asItem());
                }
                else if (variant == FrogVariant.WARM) {
                    frog.dropItem((ServerWorld)frog.getWorld(), Blocks.PEARLESCENT_FROGLIGHT.asItem());
                }
                else {
                    frog.dropItem((ServerWorld)frog.getWorld(), Blocks.VERDANT_FROGLIGHT.asItem());
                }

                item.discard();
            }
        }
	}
}
