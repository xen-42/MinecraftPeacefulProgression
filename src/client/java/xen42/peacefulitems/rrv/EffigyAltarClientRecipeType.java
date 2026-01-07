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
        return 150;
    }

    @Override
    public int getDisplayHeight() {
        return 75;
    }

    @Override
    public Identifier getGuiTexture() {
        return Identifier.of(MOD_ID, "textures/gui/effigy_altar_eiv.png");
    }

    @Override
    public int getSlotCount() {
        return 9;
    }

    @Override
    public void placeSlots(RecipeViewMenu.SlotDefinition builder) {
        //ingredients
        builder.addItemSlot(0, 11, 13);
        builder.addItemSlot(1, 29, 13);
        builder.addItemSlot(2, 47, 13);
        builder.addItemSlot(3, 11, 31);
        builder.addItemSlot(4, 29, 31);
        builder.addItemSlot(5, 47, 31);
        builder.addItemSlot(6, 29, 49);
        //brimstone
        builder.addItemSlot(7, 78, 41);
        //output
        builder.addItemSlot(8, 121, 18);
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
