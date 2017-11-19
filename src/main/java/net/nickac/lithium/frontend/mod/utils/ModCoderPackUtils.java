/*
 * MIT License
 *
 * Copyright (c) 2017 NickAc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package net.nickac.lithium.frontend.mod.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;

/**
 * Created by NickAc for Lithium!
 */
public class ModCoderPackUtils {
	/**
	 * Returns a new scaled resolution from Minecraft.<br>
	 * This method exists to easier backport of the mod.<br>
	 * Between versions, the constructor was changed and
	 *
	 * @return A new scaled resolution object
	 */
	public static ScaledResolution getScaledResolution() {
		return new ScaledResolution(getMinecraft());
	}

	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}


	public static FontRenderer getFontRenderer() {
		return getMinecraft().fontRenderer;
	}

	public static void sendLithiumMessageToServer(LithiumMessage m) {
		LithiumMod.getSimpleNetworkWrapper().sendToServer(m);
	}

	public static GuiScreen getCurrentScreen() {
		return getMinecraft().currentScreen;
	}

	public static ResourceLocation getButtonTextures() {
		return new ResourceLocation("textures/gui/widgets.png");
	}

	public static Point convertPointToScaled(Point original) {
		int originalWidth = getDisplayWidth();
		int originalHeight = getDisplayHeight();

		ScaledResolution sr = getScaledResolution();

		return new Point(
				MiscUtils.ConvertRange(
						0,
						originalWidth,
						0,
						sr.getScaledWidth(),
						original.getX()
				) * sr.getScaleFactor(),
				MiscUtils.ConvertRange(
						0,
						originalHeight,
						0,
						sr.getScaledHeight(),
						original.getY()
				) * sr.getScaleFactor()
		);

	}

	public static SoundHandler getSoundHandler() {
		return getMinecraft().getSoundHandler();
	}

	public static void playButtonPressSound()
	{
		getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public static int getDisplayHeight() {
		return getMinecraft().displayHeight;
	}

	public static int getDisplayWidth() {
		return getMinecraft().displayWidth;
	}

	public static TextureManager getTextureManager() {
		return getMinecraft().getTextureManager();
	}

	public static Tessellator getTesselator() {
		return Tessellator.getInstance();
	}

}
