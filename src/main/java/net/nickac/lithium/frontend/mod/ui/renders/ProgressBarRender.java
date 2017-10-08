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

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.nickac.lithium.backend.controls.impl.LProgressBar;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;

/**
 * Created by NickAc for Lithium!
 */
public class ProgressBarRender implements ILithiumControlRenderer<LProgressBar, GuiScreen> {
	public static int ConvertRange(int originalStart, int originalEnd, int newStart, int newEnd, int value) {
		double scale = (double) (newEnd - newStart) / (originalEnd - originalStart);
		return (int) (newStart + ((value - originalStart) * scale));
	}

	@Override
	public void renderLithiumControl(LProgressBar control, GuiScreen gui) {
		Point loc = NewLithiumGUI.centerControl(control);

		int left = loc.getX();
		int top = loc.getY();
		int right = left + control.getSize().getWidth();
		int bottom = top + control.getSize().getHeight();

		Gui.drawRect(left, top, right, bottom, (int) control.getBorderColor().getHexColor());
		Gui.drawRect(left + 1, top + 1, right - 1, bottom - 1, (int) control.getInsideColor().getHexColor());

		int startX = left + 2;
		int endX = right - 2;

		Gui.drawRect(startX, top + 2, ConvertRange(control.getMinValue(), control.getMaxValue(), startX, endX, control.getProgress()), bottom - 2, (int) control.getProgressColor().getHexColor());

	}

	@Override
	public void mouseClick(LProgressBar control, GuiScreen gui, int mouseX, int mouseY) {

	}

	@Override
	public void mouseMove(LProgressBar control, GuiScreen gui, int mouseX, int mouseY) {

	}
}
