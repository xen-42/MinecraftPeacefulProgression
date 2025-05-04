package xen42.peacefulitems.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xen42.peacefulitems.PeacefulMod;

public class EffigyAltarHandledScreen extends RecipeBookScreen<EffigyAltarScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(PeacefulMod.MOD_ID, "textures/gui/effigy_altar_gui.png");

    public EffigyAltarHandledScreen(EffigyAltarScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new EffigyAltarRecipeBookWidget(handler), inventory, title);
    }
 
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, 
            this.backgroundWidth, this.backgroundHeight, 256, 256);
    }
 
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
 
    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected ScreenPos getRecipeBookButtonPos() {
        return new ScreenPos(this.x + 132, this.height / 2 - 29);
    }
}