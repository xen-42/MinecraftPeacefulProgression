package xen42.peacefulitems.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import xen42.peacefulitems.PeacefulModBlocks;

public class FossilOreBlockEntity extends BrushableBlockEntity {

    public FossilOreBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = PeacefulModBlocks.FOSSIL_ORE_ENTITY;
    }

    @Override
    public boolean supports(BlockState state) {
        return PeacefulModBlocks.FOSSIL_ORE_ENTITY.supports(state);
    }

    @Override
    public void generateItem(PlayerEntity player) {
        this.item = new ItemStack(Items.BONE, player.getWorld().getRandom().nextBetween(1, 3));
        this.markDirty();
    }
}
