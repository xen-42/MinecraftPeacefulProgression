package xen42.peacefulitems.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import xen42.peacefulitems.PeacefulMod;
import xen42.peacefulitems.PeacefulModBlocks;
import xen42.peacefulitems.PeacefulModItems;
import xen42.peacefulitems.recipe.EffigyAltarRecipe;
import xen42.peacefulitems.recipe.EffigyAltarRecipeInput;
import net.minecraft.entity.Entity;

public class EffigyAltarScreenHandler extends AbstractRecipeScreenHandler {

    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOTS_START = 1;
    public static final int INPUT_SLOTS_END = 7;
    public static final int BRIMSTONE_SLOT = 8;
    public static final int MAX_WIDTH_AND_HEIGHT = 3;
    public static final int MAX_WIDTH_END = 1;
    public static final int INVENTORY_SLOTS_START = 9;
    public static final int INVENTORY_SLOTS_END = 35;
    public static final int HOTBAR_SLOTS_START = 36;
    public static final int HOTBAR_SLOTS_END = 45;

    public final RecipeInputInventory inventory;
    private final CraftingResultInventory resultInventory;
    private final Property levelCost = Property.create();

    public ScreenHandlerContext context;
    private final PlayerEntity player;

    public boolean canTake(int xpCost) {
        return (player.isInCreativeMode() || player.experienceLevel >= xpCost) && xpCost > 0;
    }

    public boolean canTake() {
        return canTake(getOutputXPCost());
    }

    public boolean hasOutput() {
        return _outputSlot.hasStack();
    }
    
    public static int getXPCost(ServerWorld serverWorld, List<ItemStack> input) {
        EffigyAltarRecipeInput recipeInput = EffigyAltarRecipeInput.create(input);
        Optional<RecipeEntry<EffigyAltarRecipe>> optional = serverWorld.getRecipeManager().getFirstMatch(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE, recipeInput, serverWorld);
        if (optional.isPresent()) {
            RecipeEntry<EffigyAltarRecipe> recipeEntry = (RecipeEntry<EffigyAltarRecipe>)optional.get();
            EffigyAltarRecipe altarRecipe = recipeEntry.value();
            return altarRecipe.getCostOrDefault();
        }
        return 0;
    }

    public int getOutputXPCost() {
        return levelCost.get();
    }

    @SuppressWarnings("unused")
    private Slot[] _slots;
    private Slot _outputSlot;
    private Slot _brimstoneSlot;
    
    private boolean filling;

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public EffigyAltarScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PeacefulMod.EFFIGY_ALTAR_SCREEN_HANDLER, syncId);
        this.inventory = new EffigySimpleInventory(this, BRIMSTONE_SLOT);
        this.resultInventory = new EffigyCraftingResultInventory(this);
        this.addProperty(this.levelCost);
        this.context = context;
        this.player = playerInventory.player;

        _outputSlot = this.addSlot(new OutputSlot(this, this.player, this.inventory, this.resultInventory, 0, 132, 29 - 8));
        
        _slots = new Slot[] {
            this.addSlot(new CustomSlot(this, this.inventory, 0, 22, 17)),
            this.addSlot(new CustomSlot(this, this.inventory, 1, 40, 17)),
            this.addSlot(new CustomSlot(this, this.inventory, 2, 58, 17)),
            this.addSlot(new CustomSlot(this, this.inventory, 3, 22, 35)),
            this.addSlot(new CustomSlot(this, this.inventory, 4, 40, 35)),
            this.addSlot(new CustomSlot(this, this.inventory, 5, 58, 35)),
            this.addSlot(new CustomSlot(this, this.inventory, 6, 40, 53))
        };
        
        _brimstoneSlot = this.addSlot(new BrimstoneSlot(this, this.inventory, 7, 89, 53 - 8));

        this.addPlayerSlots(playerInventory, 8, 84);
    }
    
    public static boolean isBrimstone(ItemStack stack) {
        return stack.isOf(PeacefulModItems.SULPHUR);
    }
    
    public static boolean isAir(ItemStack stack) {
        return stack.isOf(Items.AIR);
    }
    
    public void updateResult(
        ServerWorld world,
        @Nullable RecipeEntry<EffigyAltarRecipe> recipe
    ) {
        EffigyAltarRecipeInput recipeInput = EffigyAltarRecipeInput.create(inventory.getHeldStacks());
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
        ItemStack resultStack = ItemStack.EMPTY;
        int cost = 0;
        
        if (isBrimstone(_brimstoneSlot.getStack())) {
            Optional<RecipeEntry<EffigyAltarRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE, recipeInput, world, recipe);
            if (optional.isPresent()) {
                RecipeEntry<EffigyAltarRecipe> recipeEntry = (RecipeEntry<EffigyAltarRecipe>)optional.get();
                EffigyAltarRecipe altarRecipe = recipeEntry.value();
                boolean shouldCraftRecipe = resultInventory.shouldCraftRecipe(serverPlayerEntity, recipeEntry);
                if (shouldCraftRecipe) {
                    ItemStack craftedStack = altarRecipe.craft(recipeInput, world.getRegistryManager());
                    boolean isItemEnabled = craftedStack.isItemEnabled(world.getEnabledFeatures());
                    if (isItemEnabled) {
                        resultStack = craftedStack;
                        cost = altarRecipe.getCostOrDefault();
                    }
                }
            }
        }

        levelCost.set(cost);
        resultInventory.setStack(0, resultStack);
        this.setReceivedStack(0, resultStack);
        serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(this.syncId, this.nextRevision(), 0, resultStack));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (!this.filling) {
            this.context.run((world, pos) -> {
                if (world instanceof ServerWorld serverWorld) {
                    updateResult(serverWorld, null);
                }
            });
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slotAtIndex = this.slots.get(slot);
        if (slotAtIndex != null && slotAtIndex.hasStack() && slotAtIndex.canTakeItems(player)) {
            ItemStack itemStackAtIndex = slotAtIndex.getStack();
            itemStack = itemStackAtIndex.copy();
            if (slot == OUTPUT_SLOT) {
                itemStackAtIndex.getItem().onCraftByPlayer(itemStackAtIndex, player);
                if (!this.insertItem(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END, true)) {
                    return ItemStack.EMPTY;
                }

                slotAtIndex.onQuickTransfer(itemStackAtIndex, itemStack);
            } else if (slot >= INVENTORY_SLOTS_START && slot < HOTBAR_SLOTS_END) {
                if (!this.insertItem(itemStackAtIndex, INPUT_SLOTS_START, INVENTORY_SLOTS_START, false)) {
                    if (slot < HOTBAR_SLOTS_START) {
                        if (!this.insertItem(itemStackAtIndex, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_START, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStackAtIndex, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStackAtIndex.isEmpty()) {
                slotAtIndex.setStack(ItemStack.EMPTY);
            } else {
                slotAtIndex.markDirty();
            }

            if (itemStackAtIndex.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slotAtIndex.onTakeItem(player, itemStackAtIndex);
            if (slot == OUTPUT_SLOT) {
                player.dropItem(itemStackAtIndex, false);
            }
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, PeacefulModBlocks.EFFIGY_ALTAR);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.resultInventory && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @SuppressWarnings("unchecked")
    @Override
    public PostFillAction fillInputSlots(
        boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory
    ) {
        RecipeEntry<EffigyAltarRecipe> recipeEntry = (RecipeEntry<EffigyAltarRecipe>)recipe;
        
        this.onInputSlotFillStart();

        Slot brimstoneSlot = this.getBrimstoneSlot();
        List<Slot> inputSlots = this.getInputSlots();
        PostFillAction postFillAction = new EffigyAltarInputSlotFiller(brimstoneSlot, inputSlots, inventory, recipeEntry, craftAll, creative).fill();

        this.onInputSlotFillFinish(world, recipeEntry);

        return postFillAction;
    }

    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        this.inventory.provideRecipeInputs(finder);
    }

    @Override
    public RecipeBookType getCategory() {
        return RecipeBookType.CRAFTING; // Return crafting because making a RecipeBookType is impossible.
    }

    public void onInputSlotFillStart() {
        this.filling = true;
    }

    public void onInputSlotFillFinish(ServerWorld world, RecipeEntry<EffigyAltarRecipe> recipe) {
        this.filling = false;
        updateResult(world, recipe);
    }

    public Slot getBrimstoneSlot() {
        return this._brimstoneSlot;
    }

    public Slot getOutputSlot() {
        return this._outputSlot;
    }

    public List<Slot> getInputSlots() {
        return this.slots.subList(INPUT_SLOTS_START, BRIMSTONE_SLOT);
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
    
    private class EffigySimpleInventory extends SimpleInventory implements RecipeInputInventory {
        private ScreenHandler _screen;
        EffigySimpleInventory(EffigyAltarScreenHandler screen, int size) {
            super(size);
            _screen = screen;
        }

        public void markDirty() {
            _screen.onContentChanged(this);
            super.markDirty();
        }

        @Override
        public int getWidth() {
            return MAX_WIDTH_AND_HEIGHT;
        }

        @Override
        public int getHeight() {
            return MAX_WIDTH_AND_HEIGHT;
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

        @Override
        public boolean canInsert(ItemStack stack) {
            return !isBrimstone(stack);
        }
    }

    private class BrimstoneSlot extends CustomSlot {
        public BrimstoneSlot(EffigyAltarScreenHandler altar, Inventory inventory, int index, int x, int y) {
            super(altar, inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return isAir(stack) || isBrimstone(stack);
        }
    }

    private class OutputSlot extends Slot {
        private final RecipeInputInventory input;
        private final PlayerEntity player;
        private final EffigyAltarScreenHandler handler;
        private int amount;
        public OutputSlot(EffigyAltarScreenHandler handler, PlayerEntity player, RecipeInputInventory input, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.player = player;
            this.input = input;
            this.handler = handler;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public boolean canTakeItems(PlayerEntity player) {
            return handler.canTake();
        }

        @Override
        public ItemStack takeStack(int amount) {
            if (this.hasStack()) {
                this.amount = this.amount + Math.min(amount, this.getStack().getCount());
            }

            return super.takeStack(amount);
        }

        @Override
        protected void onCrafted(ItemStack stack, int amount) {
            this.amount += amount;
            this.onCrafted(stack);
        }

        @Override
        protected void onTake(int amount) {
            this.amount += amount;
        }

        @Override
        protected void onCrafted(ItemStack stack) {
            if (this.amount > 0) {
                stack.onCraftByPlayer(this.player, this.amount);
            }

            if (this.inventory instanceof RecipeUnlocker recipeUnlocker) {
                recipeUnlocker.unlockLastRecipe(this.player, this.input.getHeldStacks());
            }

            this.amount = 0;
        }

        private static DefaultedList<ItemStack> copyInput(EffigyAltarRecipeInput input) {
            DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);

            for (int i = 0; i < defaultedList.size(); i++) {
                defaultedList.set(i, input.getStackInSlot(i));
            }

            return defaultedList;
        }

        private DefaultedList<ItemStack> getRecipeRemainders(EffigyAltarRecipeInput input, World world) {
            return world instanceof ServerWorld serverWorld
                ? (DefaultedList<ItemStack>)serverWorld.getRecipeManager()
                    .getFirstMatch(PeacefulMod.EFFIGY_ALTAR_RECIPE_TYPE, input, serverWorld)
                    .map(recipe -> ((EffigyAltarRecipe)recipe.value()).getRecipeRemainders(input))
                    .orElseGet(() -> copyInput(input))
                : EffigyAltarRecipe.collectRecipeRemainders(input);
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            EffigyAltarRecipeInput recipeInput = EffigyAltarRecipeInput.create(this.input.getHeldStacks());
            DefaultedList<ItemStack> defaultedList = this.getRecipeRemainders(recipeInput, player.getWorld());

            this.handler.context.run((world, pos) -> {
                if (!player.isInCreativeMode()) {
                    player.addExperienceLevels(-getOutputXPCost());
                }
                world.playSound((Entity)null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
            });

            for (int y = 0; y < MAX_WIDTH_AND_HEIGHT; y++) {
                for (int x = 0; x < (y == MAX_WIDTH_AND_HEIGHT - 1 ? MAX_WIDTH_AND_HEIGHT - MAX_WIDTH_END : MAX_WIDTH_AND_HEIGHT); x++) {
                    int z = x + y * MAX_WIDTH_AND_HEIGHT;
                    ItemStack itemStack = this.input.getStack(z);
                    ItemStack defaultStack = defaultedList.get(z);
                    if (!itemStack.isEmpty()) {
                        this.input.removeStack(z, 1);
                        itemStack = this.input.getStack(z);
                    }

                    if (!defaultStack.isEmpty()) {
                        if (itemStack.isEmpty()) {
                            this.input.setStack(z, defaultStack);
                        } else if (ItemStack.areItemsAndComponentsEqual(itemStack, defaultStack)) {
                            defaultStack.increment(itemStack.getCount());
                            this.input.setStack(z, defaultStack);
                        } else if (!this.player.getInventory().insertStack(defaultStack)) {
                            this.player.dropItem(defaultStack, false);
                        }
                    }
                }
            }
        }

        @Override
        public boolean disablesDynamicDisplay() {
            return true;
        }
    }


    private class EffigyAltarInputSlotFiller {
        private final Slot brimstoneSlot;
        private final List<Slot> inputSlots;
        private final List<Slot> slotsToReturn;
        private final PlayerInventory inventory;
        private final RecipeEntry<EffigyAltarRecipe> recipe;
        private final boolean craftAll;
        private final boolean creative;
        
        public EffigyAltarInputSlotFiller(
            Slot brimstoneSlot,
              List<Slot> inputSlots,
               PlayerInventory inventory,
               RecipeEntry<EffigyAltarRecipe> recipe,
            boolean craftAll,
               boolean creative
           ) {
            this(brimstoneSlot, inputSlots, inputSlots, inventory, recipe, craftAll, creative);
        }
        
        public EffigyAltarInputSlotFiller(
            Slot brimstoneSlot,
               List<Slot> inputSlots,
               List<Slot> slotsToReturn,
            PlayerInventory inventory,
            RecipeEntry<EffigyAltarRecipe> recipe,
            boolean craftAll,
               boolean creative
           ) {
            this.brimstoneSlot = brimstoneSlot;
            this.inputSlots = inputSlots;
            this.slotsToReturn = slotsToReturn;
            this.inventory = inventory;
            this.recipe = recipe;
            this.craftAll = craftAll;
            this.creative = creative;
        }
        
        public AbstractRecipeScreenHandler.PostFillAction fill() {
            if (!creative && !canReturnInputs()) {
                return AbstractRecipeScreenHandler.PostFillAction.NOTHING;
            } else {
                RecipeFinder recipeFinder = new RecipeFinder();
                inventory.populateRecipeFinder(recipeFinder);
                populateRecipeFinder(recipeFinder);
                return tryFill(recipe, recipeFinder);
            }
        }

        public void clear() {
            EffigyAltarScreenHandler.this.resultInventory.clear();
            EffigyAltarScreenHandler.this.inventory.clear();
        }

        public boolean matches(RecipeEntry<EffigyAltarRecipe> entry) {
            return entry.value()
                .matches(EffigyAltarRecipeInput.create(EffigyAltarScreenHandler.this.inventory.getHeldStacks()), getPlayer().getWorld());
        }

        private AbstractRecipeScreenHandler.PostFillAction tryFill(RecipeEntry<EffigyAltarRecipe> recipe, RecipeFinder finder) {
            if (finder.isCraftable(recipe.value(), null)) {
                this.fill(recipe, finder);
                this.inventory.markDirty();
                return AbstractRecipeScreenHandler.PostFillAction.NOTHING;
            } else {
                this.returnInputs();
                this.inventory.markDirty();
                return AbstractRecipeScreenHandler.PostFillAction.PLACE_GHOST_RECIPE;
            }
        }

        private void returnInputs() {
            for (Slot slot : this.slotsToReturn) {
                ItemStack itemStack = slot.getStack().copy();
                this.inventory.offer(itemStack, false);
                slot.setStackNoCallbacks(itemStack);
            }
            
            ItemStack brimstoneStack = brimstoneSlot.getStack().copy();
            this.inventory.offer(brimstoneStack, false);
            brimstoneSlot.setStackNoCallbacks(brimstoneStack);

            clear();
        }

        private void fill(RecipeEntry<EffigyAltarRecipe> recipe, RecipeFinder finder) {
            boolean match = matches(recipe);
            int i = finder.countCrafts(recipe.value(), null);
            if (match) {
                ItemStack brimstoneItemStack = brimstoneSlot.getStack();
                if (!brimstoneItemStack.isEmpty() && Math.min(i, brimstoneItemStack.getMaxCount()) < brimstoneItemStack.getCount() + 1) {
                    return;
                }
                for (Slot slot : this.inputSlots) {
                    ItemStack itemStack = slot.getStack();
                    if (!itemStack.isEmpty() && Math.min(i, itemStack.getMaxCount()) < itemStack.getCount() + 1) {
                        return;
                    }
                }
            }

            int j = this.calculateCraftAmount(i, match);
            List<RegistryEntry<Item>> entries = new ArrayList<RegistryEntry<Item>>();
            boolean isCraftable = finder.isCraftable(recipe.value(), j, entries::add);
            if (isCraftable) {
                int k = clampToMaxCount(j, entries);
                if (k != j) {
                    entries.clear();
                    if (!finder.isCraftable(recipe.value(), k, entries::add)) {
                        return;
                    }
                }

                this.returnInputs();
                EffigyAltarRecipe recipeValue = recipe.value();
                IntList placementSlots = recipeValue.getIngredientPlacement().getPlacementSlots();
                RegistryEntry<Item> brimstoneRegistryEntry = entries.get(placementSlots.getInt(inputSlots.size()));
                int jk = k;

                while (jk > 0) {
                    jk = this.fillInputSlot(brimstoneSlot, brimstoneRegistryEntry, jk);
                    if (jk == -1) {
                        return;
                    }
                }
                for (int index = 0; index < inputSlots.size(); index++) {
                    Slot slot = inputSlots.get(index);
                    int placementSlot = placementSlots.getInt(index);
                    RegistryEntry<Item> registryEntry = entries.get(placementSlot);
                    int jx = k;

                    while (jx > 0) {
                        jx = this.fillInputSlot(slot, registryEntry, jx);
                        if (jx == -1) {
                            return;
                        }
                    }
                }
            }
        }

        private static int clampToMaxCount(int count, List<RegistryEntry<Item>> entries) {
            for (RegistryEntry<Item> registryEntry : entries) {
                count = Math.min(count, registryEntry.value().getMaxCount());
            }

            return count;
        }

        private int calculateCraftAmount(int forCraftAll, boolean match) {
            if (this.craftAll) {
                return forCraftAll;
            } else if (match) {
                int i = Integer.MAX_VALUE;

                for (Slot slot : this.inputSlots) {
                    ItemStack itemStack = slot.getStack();
                    if (!itemStack.isEmpty() && i > itemStack.getCount()) {
                        i = itemStack.getCount();
                    }
                }

                if (i != Integer.MAX_VALUE) {
                    i++;
                }

                return i;
            } else {
                return 1;
            }
        }

        private int fillInputSlot(Slot slot, RegistryEntry<Item> item, int count) {
            ItemStack itemStack = slot.getStack();
            int i = this.inventory.getMatchingSlot(item, itemStack);
            if (i == -1) {
                return -1;
            } else {
                ItemStack itemStackAtIndex = this.inventory.getStack(i);
                ItemStack itemStack3;
                if (count < itemStackAtIndex.getCount()) {
                    itemStack3 = this.inventory.removeStack(i, count);
                } else {
                    itemStack3 = this.inventory.removeStack(i);
                }

                int j = itemStack3.getCount();
                if (itemStack.isEmpty()) {
                    slot.setStackNoCallbacks(itemStack3);
                } else {
                    itemStack.increment(j);
                }

                return count - j;
            }
        }

        private boolean canReturnInputs() {
            List<ItemStack> list = Lists.<ItemStack>newArrayList();
            int i = this.getFreeInventorySlots();

            for (Slot slot : this.inputSlots) {
                ItemStack itemStack = slot.getStack().copy();
                if (!itemStack.isEmpty()) {
                    int j = this.inventory.getOccupiedSlotWithRoomForStack(itemStack);
                    if (j == -1 && list.size() <= i) {
                        for (ItemStack itemStack2 : list) {
                            if (ItemStack.areItemsEqual(itemStack2, itemStack)
                                && itemStack2.getCount() != itemStack2.getMaxCount()
                                && itemStack2.getCount() + itemStack.getCount() <= itemStack2.getMaxCount()) {
                                itemStack2.increment(itemStack.getCount());
                                itemStack.setCount(0);
                                break;
                            }
                        }

                        if (!itemStack.isEmpty()) {
                            if (list.size() >= i) {
                                return false;
                            }

                            list.add(itemStack);
                        }
                    } else if (j == -1) {
                        return false;
                    }
                }
            }

            return true;
        }

        private int getFreeInventorySlots() {
            int i = 0;

            for (ItemStack itemStack : this.inventory.getMainStacks()) {
                if (itemStack.isEmpty()) {
                    i++;
                }
            }

            return i;
        }
    }
}
