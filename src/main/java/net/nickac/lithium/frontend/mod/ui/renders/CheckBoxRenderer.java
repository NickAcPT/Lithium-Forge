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
import net.nickac.lithium.backend.controls.impl.LCheckBox;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.other.objects.Rectangle;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.network.packethandler.out.ToggleAction;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

/**
 * Created by NickAc for Lithium!
 */
public class CheckBoxRenderer implements ILithiumControlRenderer<LCheckBox, GuiScreen> {

	private final int PADDING = 4;
	private final int PADDING_LEFT = 3;

	@SideOnly(Side.CLIENT)
	private Rectangle getCheckBoxRect(LCheckBox c) {
		Point loc = NewLithiumGUI.centerControl(c);

		int left = loc.getX();
		int top = loc.getY();
		int sz = c.getSize().getHeight() - PADDING * 2;
		return new Rectangle(left + PADDING_LEFT, top + PADDING, sz, sz);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderLithiumControl(LCheckBox control, GuiScreen gui) {
		//Get the outer most rectangle
		Rectangle rect = getCheckBoxRect(control);
		//Get the inside rectangle
		Rectangle rect2 = getCheckBoxRect(control).inflate(-1, -1);
		//Get the check rectangle
		Rectangle rect3 = getCheckBoxRect(control).inflate(-3, -3);
		//Draw outside rectangle
		GuiScreen.drawRect(rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom(), (int) control.getOutsideColor().getHexColor());
		//Draw inside rect
		GuiScreen.drawRect(rect2.getLeft(), rect2.getTop(), rect2.getRight(), rect2.getBottom(), (int) control.getInsideColor().getHexColor());
		//Draw the string of the text
		ModCoderPackUtils.getFontRenderer().drawString(control.getText(), rect2.getRight() + PADDING, rect2.getTop() + PADDING / 2, (int) control.getForeColor().getHexColor());

		//If the checkbox is checked, render the check rectangle
		if (control.isChecked()) {
			GuiScreen.drawRect(rect3.getLeft(), rect3.getTop(), rect3.getRight(), rect3.getBottom(), (int) control.getCheckedColor().getHexColor());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void mouseClick(LCheckBox control, GuiScreen gui, int mouseX, int mouseY, int mouseButton) {
		Rectangle rect = getCheckBoxRect(control).inflate(-1, -1);
		if (rect.contains(new Point(mouseX, mouseY))) {
			control.setChecked(!control.isChecked());
			//Here, we risk having a desync from the server, but I'll try my best to sync it.
			ModCoderPackUtils.sendLithiumMessageToServer(new ToggleAction(control));
		}
	}

	@Override
	public void mouseClickMove(LCheckBox control, GuiScreen gui, int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

}
