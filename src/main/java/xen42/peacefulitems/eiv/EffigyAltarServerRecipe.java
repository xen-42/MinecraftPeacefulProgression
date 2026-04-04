package xen42.peacefulitems.eiv;

import de.crafty.eiv.common.api.recipe.EivRecipeType;
import de.crafty.eiv.common.api.recipe.IEivServerRecipe;
import de.crafty.eiv.common.recipe.util.EivTagUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

import static xen42.peacefulitems.PeacefulMod.MOD_ID;

public class EffigyAltarServerRecipe implements IEivServerRecipe {
    public static final EivRecipeType<EffigyAltarServerRecipe> TYPE = EivRecipeType.register(
            Identifier.of(MOD_ID,"effigy"),
            () -> new EffigyAltarServerRecipe(null, null, 0)
    );
    private int cost;
    private List<Optional<Ingredient>> ingredients;
    private ItemStack result;

    public EffigyAltarServerRecipe(List<Optional<Ingredient>> ingredients, ItemStack result, int cost) {
        this.ingredients = ingredients;
        this.result = result;
        this.cost = cost;
    }


    @Override
    public void writeToTag(NbtCompound tag) {
        tag.put("ingredients", EivTagUtil.writeList(this.ingredients, (origin, tag1) -> origin.map(EivTagUtil::writeIngredient).orElseGet(NbtCompound::new)));
        tag.put("result", EivTagUtil.encodeItemStackOnServer(this.result));
        tag.putInt("cost", this.cost);
    }

    @Override
    public void loadFromTag(NbtCompound tag) {
        this.ingredients = EivTagUtil.readList(tag, "ingredients", (nbtCompound)-> Optional.ofNullable(EivTagUtil.readIngredient(nbtCompound)));
        this.result = EivTagUtil.decodeItemStackOnClient(tag.getCompound("result").orElseGet(NbtCompound::new));
        this.cost = tag.getInt("cost", 5);
    }

    @Override
    public EivRecipeType<? extends IEivServerRecipe> getRecipeType() {
        return TYPE;
    }

    public List<Optional<Ingredient>> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }
}
