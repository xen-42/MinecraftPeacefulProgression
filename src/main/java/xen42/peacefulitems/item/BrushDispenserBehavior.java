package xen42.peacefulitems.item;

import java.util.List;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class BrushDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        ServerWorld serverWorld = pointer.world();
        BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
        List<ArmadilloEntity> armadilloList = serverWorld.getEntitiesByClass(ArmadilloEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR);
        List<PandaEntity> pandaList = serverWorld.getEntitiesByClass(PandaEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR);
        if (armadilloList.isEmpty() && pandaList.isEmpty()) {
            this.setSuccess(false);
            return stack;
        } else {
            for (ArmadilloEntity armadilloEntity : armadilloList) {
                if (armadilloEntity.brushScute()) {
                    this.setSuccess(true);
                    stack.damage(16, serverWorld, null, item -> {});
                    return stack;
                }
            }
            for (PandaEntity pandaEntity : pandaList) {
                if (!pandaEntity.isSneezing()) {
                    pandaEntity.setSneezing(true);
                    this.setSuccess(true);
                    stack.damage(1, serverWorld, null, item -> {});
                    return stack;
                }
            }

            this.setSuccess(false);
            return stack;
        }
    }
}