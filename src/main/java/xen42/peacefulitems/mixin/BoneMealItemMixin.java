package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3i;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.blocks.FlaxCropBlock;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Inject(at = @At("HEAD"), method = "useOnBlock")
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        // Make flax appear when bone mealing grass
        var world = context.getWorld();
        var blockPos = context.getBlockPos();

        if (world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK)) {
            for (int i = -4; i < 4; i++) {
                for (int j = -4; j < 4; j++) {
                    // Some reason doing the center block breaks this
                    if (i == 0 && j == 0) continue;

                    var newBlockPos = blockPos.add(new Vec3i(i, 0, j));
                    if (world.getRandom().nextFloat() < 0.05f && world.getBlockState(newBlockPos).isOf(Blocks.GRASS_BLOCK) && world.getBlockState(newBlockPos.up()).isAir()) {
                        world.setBlockState(newBlockPos.up(), PeacefulModBlocks.FLAX_CROP.getDefaultState()
                            .with(FlaxCropBlock.AGE, world.getRandom().nextBetween(0, FlaxCropBlock.MAX_AGE-2)));
                    }
                }
            }
        }
    }
}
