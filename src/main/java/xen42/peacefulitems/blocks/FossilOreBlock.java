package xen42.peacefulitems.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FossilOreBlock extends BrushableBlock {
    public FossilOreBlock(Block baseBlock, SoundEvent brushingSound, SoundEvent brushingCompleteSound, Settings settings) {
        super(baseBlock, brushingSound, brushingCompleteSound, settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof BrushableBlockEntity brushableBlockEntity) {
            brushableBlockEntity.scheduledTick(world);
        }

        // Removes the part where suspicious sand falls
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Removes the part where suspicious sand falls
    }
 
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FossilOreBlockEntity(pos, state);
    }
}
