package xen42.peacefulitems;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class FlaxCropBlock extends CropBlock {

    public static final IntProperty AGE = Properties.AGE_7;

    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[] {
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
        };
 
    public FlaxCropBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // Lets the top block keep existing
        BlockState lv = world.getBlockState(pos.down());
        if (lv.isIn(BlockTags.DIRT) || lv.isOf(Blocks.FARMLAND)) {
            return true;
        }
        else {
            return false;
        }
    }

    // Stop random ticking if its the second highest age but has a top block
	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(AGE) == this.getMaxAge() - 1 && world.getBlockState(pos.up()).isOf(this)) {
            return;
        }
		super.randomTick(state, world, pos, random);
        if (state.get(AGE) >= this.getMaxAge() - 1) {
            if (world.getBlockState(pos.up()).isAir()) {
                world.setBlockState(pos.up(), PeacefulModBlocks.FLAX_CROP.getDefaultState().with(AGE, this.getMaxAge()));
            }
            world.setBlockState(pos, this.withAge(this.getMaxAge() - 1), Block.NOTIFY_LISTENERS);
        }
	}

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView view, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (!state.canPlaceAt(world, pos) && !world.getBlockState(pos.down()).isOf(this)) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    protected IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }
 
    @Override
    protected ItemConvertible getSeedsItem() {
        return PeacefulModItems.FLAX;
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int j;
        int i = this.getAge(state) + this.getGrowthAmount(world);
        if (i > (j = this.getMaxAge() - 1)) {
            i = j;
            if (world.getBlockState(pos.up()).isAir()) {
                world.setBlockState(pos.up(), PeacefulModBlocks.FLAX_CROP.getDefaultState().with(AGE, this.getMaxAge()));
            }
        }
        world.setBlockState(pos, this.withAge(i), Block.NOTIFY_LISTENERS);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return !this.isMature(state) && !world.getBlockState(pos.up()).isOf(this);
    }
 
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[getAge(state)];
    }
}