package xen42.peacefulitems.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class BreezeCoralBlock extends BlazePickleBlock {

    public BreezeCoralBlock(Settings settings) {
        super(settings);
    }

    public boolean canPlant(BlockState state) {
		return state.isOf(Blocks.GRAVEL);
	}
}
