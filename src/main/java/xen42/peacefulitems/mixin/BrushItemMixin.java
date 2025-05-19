package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(BrushItem.class)
public class BrushItemMixin extends Item {
	public BrushItemMixin(Settings settings) {
		super(settings);
	}
	
	@Shadow
	private HitResult getHitResult(PlayerEntity user) { return null; }

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (user != null && getHitResult(user).getType() == HitResult.Type.ENTITY) {
			user.setCurrentHand(hand);
		}
		
		return ActionResult.CONSUME;
	}

	@Inject(at = @At("HEAD"), method = "usageTick", cancellable = true)
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo info) {
		if (remainingUseTicks >= 0 && user instanceof PlayerEntity playerEntity) {
			HitResult hitResult = this.getHitResult(playerEntity);
			if (hitResult instanceof EntityHitResult entityHitResult && hitResult.getType() == HitResult.Type.ENTITY) {
				if ((this.getMaxUseTime(stack, user) - remainingUseTicks + 1) % 10 == 5) {
					Entity entity = entityHitResult.getEntity();
					BlockPos blockPos = entity.getBlockPos();
					world.playSound(playerEntity, blockPos, SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, SoundCategory.BLOCKS);
					if (world instanceof ServerWorld serverWorld && entity instanceof PandaEntity pandaEntity) {
						if (!pandaEntity.isSneezing()) {
							pandaEntity.setSneezing(true);
							EquipmentSlot equipmentSlot = stack.equals(playerEntity.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
							stack.damage(1, playerEntity, equipmentSlot);
						}
					}
				}
				info.cancel();
			}
		}
	}
}
