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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.impl.LSlider;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.other.objects.Rectangle;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.network.packethandler.out.SliderValueChanged;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.MiscUtils;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

/**
 * Created by NickAc for Lithium!
 */
public class SliderRenderer implements ILithiumControlRenderer<LSlider, GuiScreen> {
	private final int HANDLE_LENGHT = 8;

	@SideOnly(Side.CLIENT)
	@Override
	public void renderLithiumControl(LSlider control, GuiScreen gui) {
		Point loc = NewLithiumGUI.centerControl(control);

		Rectangle rect = new Rectangle(loc.getX(), loc.getY(), control.getSize().getWidth(), control.getSize().getHeight());
		Rectangle rect2 = rect.inflate(-1, -1);

		//Draw border
		Gui.drawRect(rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom(), (int) control.getBorderColor().getHexColor());
		//Draw inside of the control
		Gui.drawRect(rect2.getLeft(), rect2.getTop(), rect2.getRight(), rect2.getBottom(), (int) control.getBackgroundColor().getHexColor());

		int startX = 0;
		int endX = 0;
		//Do some MATH to correctly render the slider
		switch (control.getSliderType()) {
			case HORIZONTAL:
				startX = rect.getLeft() + 1;
				endX = rect.getRight() - HANDLE_LENGHT - 1;
				break;
			case VERTICAL:
				startX = rect.getTop() + 1;
				endX = rect.getBottom() - HANDLE_LENGHT - 1;
				break;
		}

		switch (control.getSliderType()) {
			case HORIZONTAL:

				//Render stuff
				ButtonRenderer.drawButton(
						gui,
						"",
						0,
						0,
						MiscUtils.ConvertRange(control.getMinValue(), control.getMaxValue(), startX, endX, control.getValue()),
						rect.getY() + 1,
						HANDLE_LENGHT,
						rect.getHeight() - 2
				);
				break;
			case VERTICAL:
				//Render stuff
				ButtonRenderer.drawButton(
						gui,
						"",
						0,
						0,
						rect.getX() + 1,
						MiscUtils.ConvertRange(control.getMinValue(), control.getMaxValue(), startX, endX, control.getValue()),
						rect.getWidth() - 2,
						HANDLE_LENGHT
				);
				break;
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void mouseClick(LSlider control, GuiScreen gui, int mouseX, int mouseY, int mouseButton) {
		Point point = NewLithiumGUI.centerControl(control);
		Point mouseLoc = new Point(mouseX, mouseY);

		Rectangle rect = new Rectangle(point, control.getSize());

		if (rect.inflate(-2, -1).contains(mouseLoc)) {
			Point clickedPoint = new Point(mouseX - rect.getX(), mouseY - rect.getY());
			changeSlider(control, clickedPoint);
			ModCoderPackUtils.playButtonPressSound();
		}
	}

	public void changeSlider(LSlider control, Point clickedPoint) {
		Rectangle rect = new Rectangle(control.getLocation(), control.getSize()).inflate(-2, -1);

		//Looks confusing, but let me explain...
		//We turn the clicked position to relative coordinates...
		//Then we convert them to a value on the slider
		//Finally we "normalize" the value by respecting maximum an minimum value
		int val1 = (control.getSliderType() == LSlider.SliderType.HORIZONTAL) ? rect.getWidth() : rect.getHeight();
		int val2 = (control.getSliderType() == LSlider.SliderType.HORIZONTAL) ? clickedPoint.getX() : clickedPoint.getY();
		control.setValue(
				Math.max(
						control.getMinValue(),
						Math.min(
								MiscUtils.ConvertRange(
										1,
										val1 - HANDLE_LENGHT,
										control.getMinValue(),
										control.getMaxValue(),
										val2 - 2
								),
								control.getMaxValue()
						)
				)
		);
		ModCoderPackUtils.sendLithiumMessageToServer(new SliderValueChanged(control));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void mouseClickMove(LSlider control, GuiScreen gui, int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		Point point = NewLithiumGUI.centerControl(control);
		Point mouseLoc = new Point(mouseX, mouseY);

		Rectangle rect = new Rectangle(point, control.getSize());

		if (rect.inflate(-2, -1).contains(mouseLoc)) {
			Point clickedPoint = new Point(mouseX - rect.getX(), mouseY - rect.getY());
			changeSlider(control, clickedPoint);
		}
	}

}
