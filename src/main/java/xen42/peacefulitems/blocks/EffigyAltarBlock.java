package xen42.peacefulitems.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import xen42.peacefulitems.screen.EffigyAltarScreenHandler;

public class EffigyAltarBlock extends Block {
    public static final MapCodec<EffigyAltarBlock> CODEC = createCodec(EffigyAltarBlock::new);

    private static VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 14, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public MapCodec<EffigyAltarBlock> getCodec() {
        return CODEC;
    }

    public EffigyAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }
    
    public Text getTitle() {
        return Text.translatable(getTranslationKey());
    }

    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (NamedScreenHandlerFactory)new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> 
            new EffigyAltarScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), getTitle());
    }
}
