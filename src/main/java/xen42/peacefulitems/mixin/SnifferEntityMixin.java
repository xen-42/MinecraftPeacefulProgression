package xen42.peacefulitems.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import xen42.peacefulitems.PeacefulModBlocks;

@Mixin(SnifferEntity.class)
public class SnifferEntityMixin {

    @Shadow
    private static TrackedData<Integer> FINISH_DIG_TIME;

    @Inject(at = @At("HEAD"), method = "dropSeeds", cancellable = true)
    public void dropSeeds(CallbackInfo info) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var sniffer = ((SnifferEntity)(Object)this);

        if (sniffer.getWorld() instanceof ServerWorld serverWorld && sniffer.getDataTracker().get(FINISH_DIG_TIME) == sniffer.age) {

            var digLocation = sniffer.getPos().add(sniffer.getRotationVecClient().multiply(2.25));
            var blockPos = BlockPos.ofFloored(digLocation.getX(), sniffer.getY() + 0.2F, digLocation.getZ());

            var blockState = sniffer.getWorld().getBlockState(blockPos.down());

            Item customDrop = null;
            var r = sniffer.getRandom().nextFloat();
            if (blockState.isOf(Blocks.SOUL_SAND) || blockState.isOf(Blocks.SOUL_SOIL)) {
                if (r < 0.05) {
                    customDrop = Items.WITHER_SKELETON_SKULL;
                }
                else if (r < 0.1) {
                    customDrop = Items.SKELETON_SKULL;
                }
                else if (r < 0.15) {
                    customDrop = Items.WITHER_ROSE;
                }
                else if (r < 0.65) {
                    customDrop = PeacefulModBlocks.BLAZE_PICKLE.asItem();
                }
                else {
                    customDrop = Items.BONE;
                }
            }
            else if (blockState.isOf(Blocks.SAND)) {
                if (r < 0.2) {
                    customDrop = Items.NAUTILUS_SHELL;
                }
                else if (r < 0.6) {
                    customDrop = Items.PRISMARINE_SHARD;
                }
                else {
                    customDrop = Items.PRISMARINE_CRYSTALS;
                }
            }
            else if (blockState.isOf(Blocks.GRAVEL)) {
                if (r < 0.4) {
                    customDrop = PeacefulModBlocks.BREEZE_CORAL.asItem();
                }
                else {
                    customDrop = Items.FLINT;
                }
            }

            // Replacing the base game implementation with our custom drops if needed
            if (customDrop != null) {
                var itemStack = new ItemStack(customDrop);
                ItemEntity itemEntity = new ItemEntity(sniffer.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
                itemEntity.setToDefaultPickupDelay();
                serverWorld.spawnEntity(itemEntity);

                sniffer.playSound(SoundEvents.ENTITY_SNIFFER_DROP_SEED, 1.0F, 1.0F);
                info.cancel();
            }
        }
    }
}
