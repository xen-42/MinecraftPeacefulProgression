package xen42.peacefulitems.rrv;

import cc.cassian.rrv.api.TagUtil;
import cc.cassian.rrv.api.recipe.ReliableServerRecipe;
import cc.cassian.rrv.api.recipe.ReliableServerRecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

import static xen42.peacefulitems.PeacefulMod.MOD_ID;

public class EffigyAltarServerRecipe implements ReliableServerRecipe {
    public static final ReliableServerRecipeType<EffigyAltarServerRecipe> TYPE = ReliableServerRecipeType.register(
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
        tag.put("ingredients", TagUtil.writeList(this.ingredients, (origin, tag1) -> origin.map(TagUtil::writeIngredient).orElseGet(NbtCompound::new)));
        tag.put("result", TagUtil.encodeItemStackOnServer(this.result));
        tag.putInt("cost", this.cost);
    }

    @Override
    public void loadFromTag(NbtCompound tag) {
        this.ingredients = TagUtil.readList(tag, "ingredients", (nbtCompound)-> Optional.ofNullable(TagUtil.readIngredient(nbtCompound)));
        this.result = TagUtil.decodeItemStackOnClient(tag.getCompound("result").orElseGet(NbtCompound::new));
        this.cost = tag.getInt("experience", 5);
    }

    @Override
    public ReliableServerRecipeType<? extends ReliableServerRecipe> getRecipeType() {
        return TYPE;
    }

    public List<Optional<Ingredient>> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }
}
