package xen42.peacefulitems.rei;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.displays.ClientsidedRecipeBookDisplay;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.NetworkRecipeId;
import xen42.peacefulitems.recipe.EffigyAltarRecipeDisplay;

public class ClientsidedEffigyAltarREIDisplay extends EffigyAltarREIDisplay implements ClientsidedRecipeBookDisplay {
	public static final DisplaySerializer<ClientsidedEffigyAltarREIDisplay> SERIALIZER = DisplaySerializer.of(
			RecordCodecBuilder.mapCodec(instance -> instance.group(
					EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(ClientsidedEffigyAltarREIDisplay::getInputEntries),
					EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(ClientsidedEffigyAltarREIDisplay::getOutputEntries),
					Codec.INT.xmap(NetworkRecipeId::new, NetworkRecipeId::index).optionalFieldOf("id").forGetter(ClientsidedEffigyAltarREIDisplay::recipeDisplayId)
			).apply(instance, ClientsidedEffigyAltarREIDisplay::new)),
			PacketCodec.tuple(
					EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
					ClientsidedEffigyAltarREIDisplay::getInputEntries,
					EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
					ClientsidedEffigyAltarREIDisplay::getOutputEntries,
					PacketCodecs.optional(PacketCodecs.INTEGER.xmap(NetworkRecipeId::new, NetworkRecipeId::index)),
					ClientsidedEffigyAltarREIDisplay::recipeDisplayId,
					ClientsidedEffigyAltarREIDisplay::new
			), false);
	
	private final Optional<NetworkRecipeId> id;
	
	public ClientsidedEffigyAltarREIDisplay(EffigyAltarRecipeDisplay recipe, Optional<NetworkRecipeId> id) {
		this(EntryIngredients.ofSlotDisplays(recipe.ingredients()),
				List.of(EntryIngredients.ofSlotDisplay(recipe.result())), id);
	}
	
	public ClientsidedEffigyAltarREIDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<NetworkRecipeId> id) {
		super(inputs, outputs);
		this.id = id;
	}

	@Override
	public Optional<NetworkRecipeId> recipeDisplayId() {
		return id;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY;
	}
}
