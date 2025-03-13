package xen42.peacefulitems.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import xen42.peacefulitems.PeacefulMod;

@Mixin(SnifferEntity.class)
public class SnifferEntityMixin {

    @Inject(at = @At("HEAD"), method = "dropSeeds", cancellable = true)
    public void dropSeeds(CallbackInfo info) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var sniffer = ((SnifferEntity)(Object)this);

        var field = sniffer.getClass().getDeclaredField("FINISH_DIG_TIME");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        var finish_dig_time = (TrackedData<Integer>)field.get(null);

        if (sniffer.getWorld() instanceof ServerWorld serverWorld && sniffer.getDataTracker().get(finish_dig_time) == sniffer.age) {

            var digLocation = sniffer.getPos().add(sniffer.getRotationVecClient().multiply(2.25));
            var blockPos = BlockPos.ofFloored(digLocation.getX(), sniffer.getY() + 0.2F, digLocation.getZ());

            var blockState = sniffer.getWorld().getBlockState(blockPos.down());

            // If trying to dig soul sand or soul soil we'll drop nether-y things
            if (blockState.isOf(Blocks.SOUL_SAND) || blockState.isOf(Blocks.SOUL_SOIL)) {
                var itemStack = new ItemStack(Items.BLAZE_ROD);
                ItemEntity itemEntity = new ItemEntity(sniffer.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
                itemEntity.setToDefaultPickupDelay();
                serverWorld.spawnEntity(itemEntity);

                sniffer.playSound(SoundEvents.ENTITY_SNIFFER_DROP_SEED, 1.0F, 1.0F);
                info.cancel();
            }
        }
    }
}
