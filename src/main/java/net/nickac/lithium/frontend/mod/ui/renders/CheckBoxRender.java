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
import net.nickac.lithium.backend.controls.impl.LCheckBox;
import net.nickac.lithium.backend.other.objects.Color;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.other.objects.Rectangle;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

/**
 * Created by NickAc for Lithium!
 */
public class CheckBoxRender implements ILithiumControlRenderer<LCheckBox, GuiScreen> {

	private final int PADDING = 4;
	private final int PADDING_LEFT = 3;

	private Rectangle getCheckBoxRect(LCheckBox c) {
		Point loc = NewLithiumGUI.centerControl(c);

		int left = loc.getX();
		int top = loc.getY();
		int sz = c.getSize().getHeight() - PADDING * 2;
		return new Rectangle(left + PADDING_LEFT, top + PADDING, sz, sz);
	}

	@Override
	public void renderLithiumControl(LCheckBox control, GuiScreen gui) {
		Rectangle rect = getCheckBoxRect(control);
		Rectangle rect2 = getCheckBoxRect(control).inflate(-1, -1);
		//TODO: Allow color change
		GuiScreen.drawRect(rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom(), (int) Color.WHITE.getHexColor());
		GuiScreen.drawRect(rect2.getLeft(), rect2.getTop(), rect2.getRight(), rect2.getBottom(), (int) Color.BLACK.getHexColor());
		ModCoderPackUtils.getFontRenderer().drawString(control.getText(), rect2.getRight() + PADDING, rect2.getTop() + PADDING / 2, (int) Color.WHITE.getHexColor());
	}

	@Override
	public void mouseClick(LCheckBox control, GuiScreen gui, int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	public void mouseMove(LCheckBox control, GuiScreen gui, int mouseX, int mouseY) {

	}
}
