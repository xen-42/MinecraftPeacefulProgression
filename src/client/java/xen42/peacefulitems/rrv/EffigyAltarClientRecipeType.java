package xen42.peacefulitems.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulModBlocks;

import java.util.List;

import static xen42.peacefulitems.PeacefulMod.MOD_ID;

public class EffigyAltarClientRecipeType implements ReliableClientRecipeType {

    public static final EffigyAltarClientRecipeType INSTANCE = new EffigyAltarClientRecipeType();

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.peaceful-items.effigy_altar");
    }

    @Override
    public int getDisplayWidth() {
        return 134;
    }

    @Override
    public int getDisplayHeight() {
        return 57;
    }

    @Override
    public Identifier getGuiTexture() {
        return Identifier.of(MOD_ID, "textures/gui/effigy_altar_rrv.png");
    }

    @Override
    public int getSlotCount() {
        return 9;
    }

    @Override
    public void placeSlots(RecipeViewMenu.SlotDefinition builder) {
        //ingredients
        builder.addItemSlot(0, 2, 2);
        builder.addItemSlot(1, 20, 2);
        builder.addItemSlot(2, 38, 2);
        builder.addItemSlot(3, 2, 20);
        builder.addItemSlot(4, 20, 20);
        builder.addItemSlot(5, 38, 20);
        builder.addItemSlot(6, 20, 38);
        //brimstone
        builder.addItemSlot(7, 69, 30);
        //output
        builder.addItemSlot(8, 112, 6);
    }

    @Override
    public Identifier getId() {
        return Identifier.of(MOD_ID, "effigy_altar");
    }

    @Override
    public ItemStack getIcon() {
        return PeacefulModBlocks.EFFIGY_ALTAR.asItem().getDefaultStack();
    }

    @Override
    public List<ItemStack> getCraftReferences() {
        return List.of(PeacefulModBlocks.EFFIGY_ALTAR.asItem().getDefaultStack());
    }

}
