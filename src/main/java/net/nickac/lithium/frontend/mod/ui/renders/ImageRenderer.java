package net.nickac.lithium.frontend.mod.ui.renders;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLithiumControl(LImage control, GuiScreen gui) {
        Point loc = NewLithiumGUI.centerControl(control);

        //Ask ImageManager to give us a dynamic texture
        DynamicTexture imageTexture = ImageManager.getDynamicTexture(control);
        if (imageTexture != null) {
            //Get Minecraft's texture manager
            TextureManager textureManager = ModCoderPackUtils.getTextureManager();

            //Bind the texture. (In other words, tell Minecraft to use this texture)
            textureManager.bindTexture(textureManager.getDynamicTextureLocation(control.getUUID().toString(), imageTexture));
            //Correct colors
            GlStateManager.color(1, 1, 1, 1);
            //Draw the image to the screen
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
