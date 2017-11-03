package net.nickac.lithium.frontend.mod.ui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

/**
 * Criado por NickAc em 11:40 AM 01/11/2017
 */
public class ButtonRenderer {
	protected static final ResourceLocation BUTTON_TEXTURES = ModCoderPackUtils.getButtonTextures();


	/**
	 * Draws this button to the screen.
	 */
	public static void drawButton(GuiScreen gui, String displayString, int mouseX, int mouseY, int x, int y, int width, int height) {
		int i = 1; //Hover state
		int j = 14737632; // ?
		FontRenderer fontrenderer = ModCoderPackUtils.getFontRenderer();
		ModCoderPackUtils.getMinecraft().getTextureManager().bindTexture(BUTTON_TEXTURES);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		gui.drawTexturedModalRect(x, y, 0, 46 + i * 20, width / 2, height);
		gui.drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + i * 20, width / 2, height);

		gui.drawCenteredString(fontrenderer, displayString, x + width / 2, y + (height - 8) / 2, j);
	}

}
