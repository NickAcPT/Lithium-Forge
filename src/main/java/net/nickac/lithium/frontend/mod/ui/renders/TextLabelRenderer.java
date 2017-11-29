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

package net.nickac.lithium.frontend.mod.ui.renders;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.impl.LTextLabel;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

/**
 * Created by NickAc for Lithium!
 */
public class TextLabelRenderer implements ILithiumControlRenderer<LTextLabel, GuiScreen> {

	@SideOnly(Side.CLIENT)
	@Override
	public void renderLithiumControl(LTextLabel control, GuiScreen gui) {
		Point loc = NewLithiumGUI.centerControl(control);
		//Render text on screen

		gui.drawString(ModCoderPackUtils.getFontRenderer(), control.getText(), loc.getX(), loc.getY(), (int) control.getColor().getHexColor());
	}

	@Override
	public void mouseClick(LTextLabel control, GuiScreen gui, int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	public void mouseClickMove(LTextLabel control, GuiScreen gui, int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

}
