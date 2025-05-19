package xen42.peacefulitems.item;

import java.util.List;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PandaSneezeDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        ServerWorld serverWorld = pointer.world();
        BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
        List<PandaEntity> list = serverWorld.getEntitiesByClass(PandaEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR);
        if (list.isEmpty()) {
            this.setSuccess(false);
            return stack;
        } else {
            for (PandaEntity pandaEntity : list) {
                if (!pandaEntity.isSneezing()) {
                    pandaEntity.setSneezing(true);
                    stack.damage(1, serverWorld, null, item -> {});
                    return stack;
                }
            }

            this.setSuccess(false);
            return stack;
        }
    }
}