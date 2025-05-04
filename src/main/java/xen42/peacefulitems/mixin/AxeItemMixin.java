package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Shadow
    private static boolean shouldCancelStripAttempt(ItemUsageContext context) {
        return false;
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock")
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        if (!shouldCancelStripAttempt(context)) {
            var state = world.getBlockState(blockPos);
            if (state.isOf(Blocks.PALE_OAK_LOG) && !world.isClient()) {
                var offset = playerEntity.getPos().subtract(blockPos.toCenterPos()).normalize();
                var dropPos = offset.add(blockPos.toCenterPos());
                var itemEntity = new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), new ItemStack(Items.RESIN_CLUMP));
                world.spawnEntity(itemEntity);
            }
        }
    }
}
