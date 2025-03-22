package xen42.peacefulitems.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;

public class EffigyAltarScreenHandler extends ScreenHandler {

    public static final int BRIMSTONE_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public final Inventory inventory;
    private final CraftingResultInventory resultInventory;

    public ScreenHandlerContext context;

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PeacefulMod.EFFIGY_ALTAR_SCREEN_HANDLER, syncId);
        this.inventory = new EffigySimpleInventory(this, 8);
        this.resultInventory = new EffigyCraftingResultInventory(this);
        this.context = context;
    }

    private class EffigySimpleInventory extends SimpleInventory {
        private ScreenHandler _screen;
        EffigySimpleInventory(EffigyAltarScreenHandler screen, int size) {
            super(size);
            _screen = screen;
        }

        public void markDirty() {
            _screen.onContentChanged(this);
            super.markDirty();
        }
    }

    private class EffigyCraftingResultInventory extends CraftingResultInventory {
        private ScreenHandler _screen;
        EffigyCraftingResultInventory(EffigyAltarScreenHandler screen) {
            super();
            _screen = screen;
        }

        public void markDirty() {
            _screen.onContentChanged(this);
            super.markDirty();
        }
    }

    public void OnContentChanged(Inventory inventory) {

    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, PeacefulModBlocks.EFFIGY_ALTAR);
    }
    
}
