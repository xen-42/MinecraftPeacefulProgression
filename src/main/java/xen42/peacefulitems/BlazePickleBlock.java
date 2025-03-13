package xen42.peacefulitems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class BlazePickleBlock extends SeaPickleBlock {

    public BlazePickleBlock(Settings settings) {
        super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(PICKLES, 1).with(WATERLOGGED, false));
    }

    @Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos.down()).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);
	}

    @Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int j = 1;
		int l = 0;
		int m = pos.getX() - 2;
		int n = 0;

		for (int o = 0; o < 5; o++) {
			for (int p = 0; p < j; p++) {
				int q = 2 + pos.getY() - 1;

				for (int r = q - 2; r < q; r++) {
					BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
					if (blockPos != pos && random.nextInt(6) == 0 && world.getBlockState(blockPos).isOf(Blocks.AIR)) {
						BlockState blockState = world.getBlockState(blockPos.down());
						if (blockState.isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
							world.setBlockState(blockPos, PeacefulModBlocks.BLAZE_PICKLE.getDefaultState().with(PICKLES, random.nextInt(4) + 1), Block.NOTIFY_ALL);
						}
					}
				}
			}

			if (l < 2) {
				j += 2;
				n++;
			} else {
				j -= 2;
				n--;
			}

			l++;
		}

		world.setBlockState(pos, state.with(PICKLES, 4), Block.NOTIFY_LISTENERS);
	}

    @Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return super.canPlantOnTop(floor, world, pos) && world.getBlockState(pos).isAir();
	}
}
