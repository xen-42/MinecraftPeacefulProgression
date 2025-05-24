package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xen42.peacefulitems.PeacefulModItems;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (user.isGliding() && user.getEquippedStack(EquipmentSlot.CHEST).isOf(PeacefulModItems.CAPE)) {
            info.setReturnValue(ActionResult.FAIL);
            info.cancel();
        }
    }
}
