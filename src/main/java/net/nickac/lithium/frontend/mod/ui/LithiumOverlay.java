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

package net.nickac.lithium.frontend.mod.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LImage;
import net.nickac.lithium.backend.controls.impl.LOverlay;
import net.nickac.lithium.backend.controls.impl.LProgressBar;
import net.nickac.lithium.backend.controls.impl.LTextLabel;
import net.nickac.lithium.backend.other.rendering.ILithiumControlRenderer;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.managers.ImageManager;
import net.nickac.lithium.frontend.mod.ui.renders.ImageRenderer;
import net.nickac.lithium.frontend.mod.ui.renders.ProgressBarRenderer;
import net.nickac.lithium.frontend.mod.ui.renders.TextLabelRenderer;

/**
 * Created by NickAc for Lithium!
 */
public class LithiumOverlay extends GuiScreen {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			if (LithiumMod.getCurrentLithiumOverlay() != null) {
				LOverlay o = LithiumMod.getCurrentLithiumOverlay();
				if (o.getControls() != null) {
					for (LControl lControl : o.getControls()) {
						ILithiumControlRenderer renderer = null;
						if (lControl.getClass().equals(LImage.class)) {
							renderer = new ImageRenderer();
							if (!ImageManager.isImageHandled((LImage) lControl)) {
								ImageManager.handleImage((LImage) lControl);
							}
						} else if (lControl.getClass().equals(LProgressBar.class)) {
							renderer = new ProgressBarRenderer();
						} else if (lControl.getClass().equals(LTextLabel.class)) {
							renderer = new TextLabelRenderer();
						}


						if (renderer != null) {
							renderer.renderLithiumControl(lControl, this);
						}
					}
				}
			}
		}
	}
}
