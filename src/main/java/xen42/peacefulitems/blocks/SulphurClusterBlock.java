package xen42.peacefulitems.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import xen42.peacefulitems.PeacefulModItems;

public class SulphurClusterBlock extends Block implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    protected final VoxelShape northShape;
    protected final VoxelShape southShape;
    protected final VoxelShape eastShape;
    protected final VoxelShape westShape;
    protected final VoxelShape upShape;
    protected final VoxelShape downShape;

    public SulphurClusterBlock(int height, int xzOffset, AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.getDefaultState().with(WATERLOGGED, false)).with(FACING, Direction.UP));
        this.upShape = Block.createCuboidShape(xzOffset, 0.0, xzOffset, 16 - xzOffset, height, 16 - xzOffset);
        this.downShape = Block.createCuboidShape(xzOffset, 16 - height, xzOffset, 16 - xzOffset, 16.0, 16 - xzOffset);
        this.northShape = Block.createCuboidShape(xzOffset, xzOffset, 16 - height, 16 - xzOffset, 16 - xzOffset, 16.0);
        this.southShape = Block.createCuboidShape(xzOffset, xzOffset, 0.0, 16 - xzOffset, 16 - xzOffset, height);
        this.eastShape = Block.createCuboidShape(0.0, xzOffset, xzOffset, height, 16 - xzOffset, 16 - xzOffset);
        this.westShape = Block.createCuboidShape(16 - height, xzOffset, xzOffset, 16.0, 16 - xzOffset, 16 - xzOffset);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case NORTH: {
                return this.northShape;
            }
            case SOUTH: {
                return this.southShape;
            }
            case EAST: {
                return this.eastShape;
            }
            case WEST: {
                return this.westShape;
            }
            case DOWN: {
                return this.downShape;
            }
            default:
                break;
        }
        return this.upShape;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction lv = state.get(FACING);
        BlockPos lv2 = pos.offset(lv.getOpposite());
        return world.getBlockState(lv2).isSideSolidFullSquare(world, lv2, lv);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World lv = ctx.getWorld();
        BlockPos lv2 = ctx.getBlockPos();
        return (BlockState)((BlockState)this.getDefaultState().with(WATERLOGGED, lv.getFluidState(lv2).getFluid() == Fluids.WATER)).with(FACING, ctx.getSide());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(PeacefulModItems.SULPHUR);
    }
}
