package xen42.peacefulitems.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SlotDisplay.ItemSlotDisplay;
import net.minecraft.recipe.display.SlotDisplay.StackSlotDisplay;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.context.ContextParameterMap;
import xen42.peacefulitems.PeacefulModItems;

public record EffigyAltarRecipeDisplay(List<SlotDisplay> ingredients, BrimstoneSlotDisplay brimstone, StackSlotDisplay result, int cost, ItemSlotDisplay craftingStation)
	implements RecipeDisplay {
	public static final MapCodec<EffigyAltarRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(EffigyAltarRecipeDisplay::ingredients),
				BrimstoneSlotDisplay.CODEC.fieldOf("brimstone").forGetter(EffigyAltarRecipeDisplay::brimstone),
				StackSlotDisplay.CODEC.fieldOf("result").forGetter(EffigyAltarRecipeDisplay::result),
				Codec.INT.fieldOf("cost").forGetter(EffigyAltarRecipeDisplay::cost),
				ItemSlotDisplay.CODEC.fieldOf("crafting_station").forGetter(EffigyAltarRecipeDisplay::craftingStation)
			)
			.apply(instance, EffigyAltarRecipeDisplay::new)
	);
	public static final PacketCodec<RegistryByteBuf, EffigyAltarRecipeDisplay> PACKET_CODEC = PacketCodec.tuple(
		SlotDisplay.PACKET_CODEC.collect(PacketCodecs.toList()),
		EffigyAltarRecipeDisplay::ingredients,
		BrimstoneSlotDisplay.PACKET_CODEC,
		EffigyAltarRecipeDisplay::brimstone,
		StackSlotDisplay.PACKET_CODEC,
		EffigyAltarRecipeDisplay::result,
		PacketCodecs.INTEGER,
		EffigyAltarRecipeDisplay::cost,
		ItemSlotDisplay.PACKET_CODEC,
		EffigyAltarRecipeDisplay::craftingStation,
		EffigyAltarRecipeDisplay::new
	);
	public static final RecipeDisplay.Serializer<EffigyAltarRecipeDisplay> SERIALIZER = new RecipeDisplay.Serializer<>(CODEC, PACKET_CODEC);

	public static final int MAX_INGREDIENTS = (3 * 2) + 1;
	
	public EffigyAltarRecipeDisplay(List<SlotDisplay> ingredients, BrimstoneSlotDisplay brimstone, StackSlotDisplay result, int cost, ItemSlotDisplay craftingStation) {
		List<SlotDisplay> subbedIngredients = ingredients.subList(0, MAX_INGREDIENTS);
		if (subbedIngredients.size() != MAX_INGREDIENTS) {
			throw new IllegalArgumentException("Invalid shaped recipe display contents");
		} else {
			this.ingredients = subbedIngredients;
			this.brimstone = brimstone;
			this.result = result;
			this.cost = cost;
			this.craftingStation = craftingStation;
		}
	}

	@Override
	public RecipeDisplay.Serializer<EffigyAltarRecipeDisplay> serializer() {
		return SERIALIZER;
	}

	@Override
	public boolean isEnabled(FeatureSet features) {
		return this.ingredients.stream().allMatch(ingredient -> ingredient.isEnabled(features)) && RecipeDisplay.super.isEnabled(features);
	}
	
	public static class BrimstoneSlotDisplay implements SlotDisplay {
		public static final BrimstoneSlotDisplay INSTANCE = new BrimstoneSlotDisplay();
		public static final MapCodec<BrimstoneSlotDisplay> CODEC = MapCodec.unit(INSTANCE);
		public static final PacketCodec<RegistryByteBuf, BrimstoneSlotDisplay> PACKET_CODEC = PacketCodec.unit(INSTANCE);
		public static final SlotDisplay.Serializer<BrimstoneSlotDisplay> SERIALIZER = new SlotDisplay.Serializer<>(CODEC, PACKET_CODEC);

		private BrimstoneSlotDisplay() {
		}

		@Override
		public SlotDisplay.Serializer<BrimstoneSlotDisplay> serializer() {
			return SERIALIZER;
		}

		public String toString() {
			return "Brimstone";
		}

		@Override
		public <T> Stream<T> appendStacks(ContextParameterMap parameters, DisplayedItemFactory<T> factory) {
			if (factory instanceof DisplayedItemFactory.FromStack<T> fromStack) {
				return Stream.of(fromStack.toDisplayed(PeacefulModItems.SULPHUR));
			}

			return Stream.empty();
		}
	}
}
