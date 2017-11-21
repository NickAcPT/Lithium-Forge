package net.nickac.lithium.frontend.mod.ui.renders;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.nickac.lithium.backend.controls.impl.LImage;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.managers.ImageManager;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

/**
 * Created by NickAc for Lithium!
 */
public class ImageRenderer implements ILithiumControlRenderer<LImage, GuiScreen> {

	@Override
	public void renderLithiumControl(LImage control, GuiScreen gui) {
		Point loc = NewLithiumGUI.centerControl(control);

		DynamicTexture imageTexture = ImageManager.getDynamicTexture(control);
		if (imageTexture != null) {
			TextureManager textureManager = ModCoderPackUtils.getTextureManager();

			textureManager.bindTexture(textureManager.getDynamicTextureLocation(control.getUUID().toString(), imageTexture));
			GlStateManager.color(1, 1, 1, 1);
			Gui.drawModalRectWithCustomSizedTexture(loc.getX(), loc.getY(), 0.0F, 0.0F, control.getSize().getWidth(), control.getSize().getHeight(), control.getSize().getWidth(), control.getSize().getHeight());
		}
	}

	@Override
	public void mouseClick(LImage control, GuiScreen gui, int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	public void mouseClickMove(LImage control, GuiScreen gui, int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}
}
