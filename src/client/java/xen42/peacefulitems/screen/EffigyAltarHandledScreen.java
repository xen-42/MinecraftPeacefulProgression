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
    private final EffigyAltarRecipeBookWidget recipeBookWidget;

    public EffigyAltarHandledScreen(EffigyAltarScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new EffigyAltarRecipeBookWidget(handler), inventory, title);
        this.recipeBookWidget = (EffigyAltarRecipeBookWidget)this.recipeBook;
    }
 
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, 
            this.backgroundWidth, this.backgroundHeight, 256, 256);
        
        if (handler.hasOutput() || recipeBookWidget.isShowingGhostRecipes()) {
            var cost = recipeBookWidget.isShowingGhostRecipes()
            		? ClientData.getGhostXPCost()
            		: handler.getOutputXPCost();
            if (cost > 0) {
                var string = Text.translatable("container.repair.cost", new Object[] { Integer.valueOf(cost) });;
                int colour = 8453920;
                if (!handler.canTake(cost)) {
                    colour = 16736352;
                }
                
                int k = i + 166 - this.textRenderer.getWidth(string);
                context.fill(k - 2, j + 71 - 6, i + this.backgroundWidth - 8, j + 81 - 2, 1325400064);
                context.drawTextWithShadow(this.textRenderer, string, k, j + 72 - 4, colour);
            }
        }
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
        return new ScreenPos(this.x + 132, this.height / 2 - 31 - 8);
    }
    
    public static class ClientData {
        private static int ghostXPCost = 0;

        public static void setGhostXPCost(int cost) {
            ghostXPCost = cost;
        }

        public static int getGhostXPCost() {
            return ghostXPCost;
        }
    }
}