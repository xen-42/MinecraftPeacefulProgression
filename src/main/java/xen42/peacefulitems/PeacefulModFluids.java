package xen42.peacefulitems;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class PeacefulModFluids {

    public static void initialize() { }
    
    public static final FlowableFluid DRAGON_BREATH = (FlowableFluid)register("dragon_breath", new FlowableFluid() {
            @Override
            public Fluid getFlowing() {
                return null;
            }
    
            @Override
            public Fluid getStill() {
                return DRAGON_BREATH;
            }
    
            @Override
            protected boolean isInfinite(ServerWorld world) {
                return false;
            }
    
            @Override
            protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
                BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                Block.dropStacks(state, world, pos, blockEntity);
            }
    
            @Override
            protected int getMaxFlowDistance(WorldView world) {
                return 4;
            }
    
            @Override
            public boolean matchesType(Fluid fluid) {
                return fluid == DRAGON_BREATH;
            }
    
            @Override
            public int getLevelDecreasePerBlock(WorldView world) {
                return 1;
            }
    
            @Override
            public int getLevel(FluidState state) {
                return 8;
            }
    
            @Override
            public Item getBucketItem() {
                return PeacefulModItems.DRAGON_EFFIGY;
            }
    
            @Override
            protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid,
                    Direction direction) {
                return direction == Direction.DOWN && !matchesType(fluid);
            }
    
            @Override
            public int getTickRate(WorldView world) {
                return 5;
            }
    
            @Override
            protected float getBlastResistance() {
                return 100;
            }
    
            @Override
            protected BlockState toBlockState(FluidState state) {
                return Blocks.AIR.getDefaultState();
            }
    
            @Override
            public boolean isStill(FluidState state) {
                return true;
            }

            @Override
            public Vec3d getVelocity(BlockView block, BlockPos pos, FluidState state) {
                return Vec3d.ZERO;
            }

            @Override
            public float getHeight(FluidState state) {
                return 1;
            }

            @Override
            public float getHeight(FluidState state, BlockView block, BlockPos pos) {
                return 1;
            }

            @Override
            public VoxelShape getShape(FluidState state, BlockView block, BlockPos pos) {
                return VoxelShapes.fullCube();
            }
    });

    private static Fluid register(String name, Fluid fluid) {
        // Create a registry key for the fluid
        RegistryKey<Fluid> fluidKey = keyOfFluid(name);

        return Registry.register(Registries.FLUID, fluidKey, fluid);
    }

    private static RegistryKey<Fluid> keyOfFluid(String name) {
        return RegistryKey.of(RegistryKeys.FLUID, Identifier.of(PeacefulMod.MOD_ID, name));
    }
}
