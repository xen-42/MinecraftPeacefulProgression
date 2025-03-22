package xen42.peacefulitems.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
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
    private Slot _outputSlot;

    private static final Item[] GUARDIAN_RECIPE = new Item[] { 
        Items.TROPICAL_FISH, Items.PUFFERFISH, Items.TROPICAL_FISH,
        Items.TROPICAL_FISH, Items.TROPICAL_FISH, Items.TROPICAL_FISH,
        Items.TROPICAL_FISH
    };

    private static final Item[] DRAGON_RECIPE = new Item[] { 
        Items.ENDER_EYE, Items.CRYING_OBSIDIAN, Items.ENDER_EYE,
        PeacefulModItems.BAT_WING, Items.OBSIDIAN, PeacefulModItems.BAT_WING,
        Items.OBSIDIAN
    };

    private static final Item[] WITHER_RECIPE = new Item[] { 
        Items.WITHER_SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.WITHER_SKELETON_SKULL,
        Items.SOUL_SAND, Items.SOUL_SAND, Items.SOUL_SAND,
        Items.SOUL_SAND
    };

    private static final Item[] UNDYING_RECIPE = new Item[] { 
        Items.EMERALD, Items.GOLD_BLOCK, Items.EMERALD,
        Items.GOLD_INGOT, Items.GOLD_INGOT, Items.GOLD_INGOT,
        Items.GOLD_INGOT
    };

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PeacefulMod.EFFIGY_ALTAR_SCREEN_HANDLER, syncId);
        this.inventory = new EffigySimpleInventory(this, 8);
        this.resultInventory = new EffigyCraftingResultInventory(this);
        this.context = context;
        _slots = new Slot[] {
            this.addSlot(new CustomSlot(this, this.inventory, 0, 22, 17)),
            this.addSlot(new CustomSlot(this, this.inventory, 1, 40, 17)),
            this.addSlot(new CustomSlot(this, this.inventory, 2, 58, 17)),
            this.addSlot(new CustomSlot(this, this.inventory, 3, 22, 35)),
            this.addSlot(new CustomSlot(this, this.inventory, 4, 40, 35)),
            this.addSlot(new CustomSlot(this, this.inventory, 5, 58, 35)),
            this.addSlot(new CustomSlot(this, this.inventory, 6, 40, 53)),
            this.addSlot(new BrimstoneSlot(this, this.inventory, 7, 89, 53))
        };

        _outputSlot = this.addSlot(new OutputSlot(this, this.resultInventory, 0, 132, 29));
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (_slots[7].getStack().isOf(PeacefulModItems.SULPHUR)) {
            if (checkRecipe(GUARDIAN_RECIPE)) {
                TrySetOutput(PeacefulModItems.GUARDIAN_EFFIGY);
                return;
            }
            if (checkRecipe(DRAGON_RECIPE)) {
                TrySetOutput(PeacefulModItems.DRAGON_EFFIGY);
                return;
            }
            if (checkRecipe(WITHER_RECIPE)) {
                TrySetOutput(PeacefulModItems.WITHER_EFFIGY);
                return;
            }
            if (checkRecipe(UNDYING_RECIPE)) {
                TrySetOutput(Items.TOTEM_OF_UNDYING);
                return;
            }
        }
        if (!_outputSlot.getStack().isEmpty()) {
            _outputSlot.setStack(ItemStack.EMPTY);
        }
    }

    private void TrySetOutput(Item item) {
        if (!_outputSlot.getStack().isOf(item)) {
            _outputSlot.setStack(new ItemStack(item));
        }
    }

    private boolean checkRecipe(Item[] recipe) {
        var flag = true;
        for (int i = 0; i < _slots.length - 1; i++) {
            if (!_slots[i].getStack().isOf(recipe[i])) {
                flag = false;
            }
        }
        return flag;
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

    public void OnCraftItem() {
        for (Slot slot : _slots) {
            var itemStack = slot.takeStack(slot.getMaxItemCount());
            if (!itemStack.isEmpty()) {
                itemStack.decrement(1);
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

    private class CustomSlot extends Slot {
        private EffigyAltarScreenHandler _altar;
        public CustomSlot(EffigyAltarScreenHandler altar, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            _altar = altar;
        }

        @Override
        public void markDirty() {
            super.markDirty();
            _altar.onContentChanged(_altar.inventory);
        }
    }

    private class BrimstoneSlot extends CustomSlot {
        public BrimstoneSlot(EffigyAltarScreenHandler altar, Inventory inventory, int index, int x, int y) {
            super(altar, inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isOf(PeacefulModItems.SULPHUR);
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
            _altar.OnCraftItem();
        }
    }
}
