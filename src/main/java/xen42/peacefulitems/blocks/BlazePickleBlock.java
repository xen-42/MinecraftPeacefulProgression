package xen42.peacefulitems.blocks;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class BlazePickleBlock extends PlantBlock implements Fertilizable {
	public static final MapCodec<BlazePickleBlock> CODEC = createCodec(BlazePickleBlock::new);
	public static final int MAX_PICKLES = 4;
	public static final IntProperty PICKLES = Properties.PICKLES;
	protected static final VoxelShape ONE_PICKLE_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);
	protected static final VoxelShape TWO_PICKLES_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 6.0, 13.0);
	protected static final VoxelShape THREE_PICKLES_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);
	protected static final VoxelShape FOUR_PICKLES_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 7.0, 14.0);

	@Override
	public MapCodec<BlazePickleBlock> getCodec() {
		return CODEC;
	}

    public BlazePickleBlock(Settings settings) {
        super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(PICKLES, 1));
    }

    @Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!this.canPlaceAt(state, world, pos)) {
			world.breakBlock(pos, true);
		}
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
						if (canPlant(blockState)) {
							world.setBlockState(blockPos, this.getDefaultState().with(PICKLES, random.nextInt(4) + 1), Block.NOTIFY_ALL);
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
		return canPlant(floor) && (world.getBlockState(pos.up()).isAir() || world.getBlockState(pos.up()).isOf(this));
	}

	public boolean canPlant(BlockState state) {
		return state.isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);
	}

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var ground = pos.down();
        return this.canPlantOnTop(world.getBlockState(ground), world, ground);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PICKLES);
	}

    @Override
	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

    @Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(PICKLES)) {
			case 1:
			default:
				return ONE_PICKLE_SHAPE;
			case 2:
				return TWO_PICKLES_SHAPE;
			case 3:
				return THREE_PICKLES_SHAPE;
			case 4:
				return FOUR_PICKLES_SHAPE;
		}
	}

    @Override
	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		return !context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(PICKLES) < 4 ? true : super.canReplace(state, context);
	}

    @Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
		if (blockState.isOf(this)) {
			return blockState.with(PICKLES, Math.min(4, (Integer)blockState.get(PICKLES) + 1));
		} else {
			return super.getPlacementState(ctx);
		}
	}
}
