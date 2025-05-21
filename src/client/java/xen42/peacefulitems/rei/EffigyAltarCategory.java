package xen42.peacefulitems.rei;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplayMerger;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import xen42.peacefulitems.PeacefulModBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;


public class EffigyAltarCategory implements DisplayCategory<EffigyAltarREIDisplay> {

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(PeacefulModBlocks.EFFIGY_ALTAR);
	}

	@Override
	public Text getTitle() {
		return Text.translatable(PeacefulModBlocks.EFFIGY_ALTAR.getTranslationKey());
	}

	@Override
	public CategoryIdentifier<? extends EffigyAltarREIDisplay> getCategoryIdentifier() {
		return EffigyAltarServerPlugin.EFFIGY_ALTAR_CATEGORY;
	}

	@Override
	public List<Widget> setupDisplay(EffigyAltarREIDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - (display.getCost() > 0 ? 34 : 27));
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
		List<InputIngredient<EntryStack<?>>> input = display.getInputIngredients(3, 3);
		List<Slot> slots = Lists.newArrayList();
		for (int y = 0; y < 2; y++)
			for (int x = 0; x < 3; x++)
				slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
		slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + 1 * 18, startPoint.y + 1 + 2 * 18)).markInput());
		slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + 2 * 18, startPoint.y + 1 + 2 * 18)).markInput());
		for (InputIngredient<EntryStack<?>> ingredient : input) {
			slots.get(ingredient.getIndex()).entries(ingredient.get());
		}
		widgets.addAll(slots);
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		if (display.getCost() > 0) {
			widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
				TextRenderer font = MinecraftClient.getInstance().textRenderer;
				Text component = Text.translatable("container.repair.cost", display.getCost());
				int endX = startPoint.x + 102 + 26;
				int x = endX - font.getWidth(component) - 2;
				graphics.fill(x - 2, startPoint.y + 56, endX, startPoint.y + 56 + 12, 0x4f000000);
				graphics.drawTextWithShadow(font, component, x, startPoint.y + 56 + 2, 0x80ff20);
			}));
		}
		return widgets;
	}
	
	@Override
	@Nullable
	public DisplayMerger<EffigyAltarREIDisplay> getDisplayMerger() {
		return new DisplayMerger<EffigyAltarREIDisplay>() {
			@Override
			public boolean canMerge(EffigyAltarREIDisplay first, EffigyAltarREIDisplay second) {
				if (!first.getCategoryIdentifier().equals(second.getCategoryIdentifier())) return false;
				if (!equals(first.getOrganisedInputEntries(3, 3), second.getOrganisedInputEntries(3, 3))) return false;
				if (!equals(first.getOutputEntries(), second.getOutputEntries())) return false;
				return true;
			}
			
			@Override
			public int hashOf(EffigyAltarREIDisplay display) {
				return display.getCategoryIdentifier().hashCode() * 31 * 31 * 31 + display.getOrganisedInputEntries(3, 3).hashCode() * 31 * 31 + display.getOutputEntries().hashCode();
			}
			
			private boolean equals(List<EntryIngredient> l1, List<EntryIngredient> l2) {
				if (l1.size() != l2.size()) return false;
				Iterator<EntryIngredient> it1 = l1.iterator();
				Iterator<EntryIngredient> it2 = l2.iterator();
				while (it1.hasNext() && it2.hasNext()) {
					if (!it1.next().equals(it2.next())) return false;
				}
				return true;
			}
		};
	}

	@Override
	public int getDisplayWidth(EffigyAltarREIDisplay display) {
		return 150;
	}

	@Override
	public int getDisplayHeight() {
		return 78;
	}
}
