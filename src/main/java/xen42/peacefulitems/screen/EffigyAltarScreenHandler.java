package xen42.peacefulitems.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.PeacefulModItems;

public class EffigyAltarScreenHandler extends ScreenHandler {

    public static final int BRIMSTONE_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;

    public final Inventory inventory;
    private final CraftingResultInventory resultInventory;

    public ScreenHandlerContext context;

    private Slot[] _slots;

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PeacefulMod.EFFIGY_ALTAR_SCREEN_HANDLER, syncId);
        this.inventory = new EffigySimpleInventory(this, 8);
        this.resultInventory = new EffigyCraftingResultInventory(this);
        this.context = context;
        _slots = new Slot[] {
            this.addSlot(new Slot(this.inventory, 0, 22, 17)),
            this.addSlot(new Slot(this.inventory, 1, 40, 17)),
            this.addSlot(new Slot(this.inventory, 2, 58, 17)),
            this.addSlot(new Slot(this.inventory, 3, 22, 35)),
            this.addSlot(new Slot(this.inventory, 4, 40, 35)),
            this.addSlot(new Slot(this.inventory, 5, 58, 35)),
            this.addSlot(new Slot(this.inventory, 6, 40, 53)),
            this.addSlot(new BrimstoneSlot(this, this.inventory, 7, 89, 53))
        };

        this.addSlot(new OutputSlot(this, this.resultInventory, 0, 132, 29));
        this.addPlayerSlots(playerInventory, 8, 84);
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

    @Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
        if (!player.getWorld().isClient()) {
            for (Slot slot : _slots ) {
                var itemStack = slot.takeStack(slot.getMaxItemCount());
                if (!itemStack.isEmpty()) {
                    player.dropItem(itemStack, false);
                }
            }
        }
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

    private class BrimstoneSlot extends Slot {
        private EffigyAltarScreenHandler _altar;
        public BrimstoneSlot(EffigyAltarScreenHandler altar, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            _altar = altar;
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isOf(PeacefulModItems.SULPHUR);
        }

        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            super.onTakeItem(player, stack);
        }
    }

    private class OutputSlot extends Slot {
        private EffigyAltarScreenHandler _altar;
        public OutputSlot(EffigyAltarScreenHandler altar, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            _altar = altar;
        }

        public boolean canInsert(ItemStack stack) {
            return false;
        }

        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            super.onTakeItem(player, stack);
        }
    }
}
