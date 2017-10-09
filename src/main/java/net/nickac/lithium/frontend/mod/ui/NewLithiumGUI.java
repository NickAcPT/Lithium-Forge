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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.*;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.ui.renders.CheckBoxRender;
import net.nickac.lithium.frontend.mod.ui.renders.NickGuiTextField;
import net.nickac.lithium.frontend.mod.ui.renders.ProgressBarRender;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;
import net.nickac.lithium.frontend.mod.utils.NickHashMap;

import java.io.IOException;
import java.util.*;

import static net.nickac.lithium.backend.other.LithiumConstants.*;

/**
 * Created by NickAc for Lithium!
 */
public class NewLithiumGUI extends GuiScreen {
	private static ProgressBarRender progressBarRender = new ProgressBarRender();
	private static CheckBoxRender checkboxRender = new CheckBoxRender();
	@SuppressWarnings("FieldCanBeLocal")
	private final int BUTTON_HEIGHT = 20;

	//The base window
	private LWindow baseWindow;
	private Map<UUID, NickGuiTextField> textBoxes = new HashMap<>();
	private Map<Integer, UUID> textBoxesReverse = new HashMap<>();
	private Map<UUID, LTextBox> textBoxesLReverse = new HashMap<>();
	private Map<UUID, LProgressBar> progressBars = new NickHashMap<>();
	private Map<UUID, LCheckBox> checkBoxes = new NickHashMap<>();

	//Button stuff
	//We take a global count number and give a Lithium button
	private Map<Integer, LButton> buttonsCounter = new HashMap<>();
	//We take an UUID (of a control) and we get the global count button
	private Map<UUID, Integer> reverseLButtonsCounter = new HashMap<>();
	//We take a global count button and give a GuiButton id
	private Map<Integer, Integer> reverseButtonsCounter = new HashMap<>();
	//Labels to be rendered!
	private List<LTextLabel> labelsToRender = new ArrayList<>();
	private int globalCounter = 0;

	public NewLithiumGUI(LWindow base) {
		this.baseWindow = base;
	}

	public static Point centerControl(LControl c) {
		Point parentLoc = (c.getParent() != null) && (c.getParent() instanceof LControl) && !(c.getParent() instanceof LWindow) ? centerControl((LControl) c.getParent()) : Point.EMPTY;

		if (c.getCentered() == LControl.CenterOptions.NONE) {
			return new Point(parentLoc.getX() + c.getLocation().getX(), parentLoc.getY() + c.getLocation().getY());
		}
		ScaledResolution sr = ModCoderPackUtils.getScaledResolution();
		int parentWidth = sr.getScaledWidth();
		int parentHeight = sr.getScaledHeight();

		int newX = c.getLocation().getX();
		int newY = parentLoc.getY() + c.getLocation().getY();

		int sizeW = c instanceof LPanel ? ((LPanel) c).getTotalWidth() : c.getSize().getWidth();
		int sizeH = c instanceof LPanel ? ((LPanel) c).getTotalHeight() : c.getSize().getHeight();

		if (c instanceof LTextLabel) {
			sizeW = ModCoderPackUtils.getFontRenderer().getStringWidth(c.getText());
			sizeH = ModCoderPackUtils.getFontRenderer().FONT_HEIGHT;
		}

		boolean centeredX = c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.VERTICAL;
		boolean centeredY = c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.HORIZONTAL;
		if (centeredX)
			newX = ((parentWidth / 2) - (sizeW / 2));
		if (centeredY)
			newY = parentLoc.getY() + ((parentHeight / 2) - (sizeH / 2));
		return new Point(newX, newY);
	}

	public LWindow getBaseWindow() {
		return baseWindow;
	}

	/**
	 * Get the center location of control.<br>
	 * Width and height are taken in account.
	 *
	 * @param s        - scaled size
	 * @param w        - size
	 * @param x        - original coordinate
	 * @param centered Is the control centered
	 * @return the corrdinate on the screen
	 */
	private int centerLoc(LControl c, int s, int w, int x, boolean centered, boolean atX) {
		if (centered) {
			return (s / 2) - (w / 2);
		}
		return x;
	}

	/**
	 * Goes thru all controls and adds them to the gui
	 *
	 * @param ctrls The collection of Lithium controls to be added.
	 */
	private void allControls(Collection<LControl> ctrls) {
		for (LControl c : ctrls) {
			addControlToGUI(c);
		}
	}

	/**
	 * Adds a Lithium control to the GUI.<br>
	 * This is the method that does the heavy lifting..
	 *
	 * @param c Control to be added
	 */
	public void addControlToGUI(LControl c) {

		//Here we check if control is a panel, and if it is, check if it's centered on x or y axis.
		boolean centeredX = c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.VERTICAL;
		boolean centeredY = c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.HORIZONTAL;


		//Then we finally calculate the location of the control.
		//Minecraft has some limitations regarding button height, so it's always equal to the constant
		Point newLoc = centerControl(c);
		int controlX = newLoc.getX();
		int controlY = newLoc.getY();

		//The cool part!
		//Adding the control
		if (c.getClass().equals(LPanel.class)) {
			LPanel pnl = (LPanel) c;
			for (LControl lControl : pnl.getControls()) {
				addControlToGUI(lControl);
			}
		} else if (c.getClass().equals(LButton.class)) {
			LButton b = (LButton) c;
			GuiButton bb = generateGuiButton(b);
			addButton(bb);

			buttonsCounter.put(globalCounter, b);
			reverseLButtonsCounter.put(b.getUUID(), bb.id);
			reverseButtonsCounter.put(bb.id, globalCounter);
		} else if (c.getClass().equals(LTextLabel.class)) {
			LTextLabel lbl = (LTextLabel) c;
			if (!labelsToRender.contains(lbl)) {
				labelsToRender.add(lbl);
			}
		} else if (c.getClass().equals(LTextBox.class)) {
			NickGuiTextField txt = new NickGuiTextField(globalCounter, Minecraft.getMinecraft().fontRenderer, controlX, controlY, c.getSize().getWidth(), c.getSize().getHeight(), ((LTextBox) c).isPasswordField());
			txt.setText(c.getText() != null ? c.getText() : "");
			textBoxes.put(c.getUUID(), txt);
			textBoxesReverse.put(txt.getId(), c.getUUID());
			textBoxesLReverse.put(c.getUUID(), (LTextBox) c);

		} else if (c.getClass().equals(LProgressBar.class)) {
			progressBars.put(c.getUUID(), (LProgressBar) c);
		} else if (c.getClass().equals(LCheckBox.class)) {
			checkBoxes.put(c.getUUID(), (LCheckBox) c);
		}
		if (c.getParent() == null || (c.getParent() != null && c.getParent().equals(baseWindow))) {
			baseWindow.addControl(c);
		}
		globalCounter++;
	}


	@Override
	public void updateScreen() {
		super.updateScreen();
		textBoxes.values().forEach(NickGuiTextField::updateCursorCounter);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		textBoxes.values().forEach(t -> {
			if (t.isFocused()) {
				if (t.textboxKeyTyped(typedChar, keyCode)) {
					LTextBox lTextBox = textBoxesLReverse.get(textBoxesReverse.get(t.getId()));
					if (lTextBox != null) {
						LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_TEXTBOX_TEXT_CHANGED + lTextBox.getUUID() + "|" + SerializationUtils.objectToString(t.getText())));
					}
				}
			}
		});
	}

	private boolean isCenteredX(LControl c) {
		return c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.VERTICAL;
	}

	private boolean isCenteredY(LControl c) {
		return c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.HORIZONTAL;
	}

	public void removeControl(LControl g) {
		baseWindow.removeControl(g);
		softRemoveControl(g);
	}

	private GuiButton generateGuiButton(LButton b) {
		ScaledResolution sr = ModCoderPackUtils.getScaledResolution();
		Point centerLoc = centerControl(b);

		int controlX = centerLoc.getX();
		int controlY = centerLoc.getY();

		return new GuiButton(globalCounter, controlX, controlY, b.getSize().getWidth(), BUTTON_HEIGHT, b.getText());

	}

	@Override
	public void initGui() {

		//Remove everything....... Sorry. Please don't sue me!
		baseWindow.getControls().forEach(this::softRemoveControl);

		//Then we need to initialize the gui
		super.initGui();
		//Then we need to register the window
		LithiumMod.getWindowManager().registerWindow(baseWindow);
		//Then we set the current Lithium GUI to this.
		LithiumMod.setCurrentLithium(this);
		//Then we add all controls to gui
		allControls(baseWindow.getControls());

	}

	/**
	 * Removes a Lithium control from the GUI
	 *
	 * @param g The control that will be removed
	 */
	private void softRemoveControl(LControl g) {
		if (g.getClass().equals(LTextBox.class)) {
			for (NickGuiTextField gg : textBoxes.values()) {
				UUID txtUUID = textBoxesReverse.getOrDefault(gg.getId(), null);
				if (txtUUID != null && g.getUUID().equals(txtUUID)) {
					textBoxesReverse.remove(gg.getId());
					textBoxesLReverse.remove(txtUUID);

					//We should take any important data from the GUI control and move it to the LControl instance.
					NickGuiTextField gui = textBoxes.getOrDefault(txtUUID, null);
					if (gui != null)
						g.setText(gui.getText());

					textBoxes.remove(txtUUID);
				}
			}
		} else if (g.getClass().equals(LButton.class)) {
			for (GuiButton guiButton : buttonList) {
				if (guiButton.id == reverseLButtonsCounter.get(g.getUUID())) {
					Integer id = reverseButtonsCounter.get(guiButton.id);
					buttonsCounter.remove(id);
					reverseLButtonsCounter.remove(g.getUUID());
					reverseButtonsCounter.remove(guiButton.id);
					buttonList.remove(guiButton);
					break;
				}
			}
		} else if (g.getClass().equals(LTextLabel.class)) {
			for (LTextLabel lTextLabel : labelsToRender) {
				if (lTextLabel.getUUID().equals(g.getUUID())) {
					labelsToRender.remove(lTextLabel);
					break;
				}
			}
		} else if (g.getClass().equals(LPanel.class)) {
			LPanel p = (LPanel) g;
			p.getControls().forEach(this::softRemoveControl);
		} else if (g.getClass().equals(LProgressBar.class)) {
			LProgressBar gg = (LProgressBar) g;
			LProgressBar gui = progressBars.getOrDefault(g.getUUID(), null);
			if (gui != null) {
				//We should take any important data from the GUI control and move it to the LControl instance.
				gg.setProgress(gui.getProgress());
				gg.setMaxValue(gui.getMaxValue());
				gg.setMinValue(gui.getMinValue());
				gg.setBorderColor(gui.getBorderColor());
				gg.setInsideColor(gui.getInsideColor());
				gg.setProgressColor(gui.getProgressColor());
			}
			//gg.setProgress();
			progressBars.remove(g.getUUID());
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		//We can unregister the window, because everything has an UUID, and it wouldn't make sense to reuse a window or its controls.
		LithiumMod.getWindowManager().unregisterWindow(baseWindow);
		//Then we need to the server that the window was closed (event)
		LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_WINDOW_CLOSE + baseWindow.getUUID()));
		//Then, we can "safely" set the current LithiumGUI to null.
		LithiumMod.setCurrentLithium(null);
	}


	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		//Get the id of the button. It's safer to use and store the button's own id(integer) instead of the instance itself.
		int buttonId = reverseButtonsCounter.getOrDefault(button.id, -1);
		//If we have a button, we send an event to the server with the UUID of the LButton instance.
		//Later, it will invoke an event on the spigot side.
		if (buttonId != -1) {
			LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_BUTTON_ACTION + buttonsCounter.get(buttonId).getUUID()));
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		textBoxes.values().forEach(t -> t.mouseClicked(mouseX, mouseY, mouseButton));
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Just get a scaled resolution
		ScaledResolution sr = ModCoderPackUtils.getScaledResolution();

		//Then we draw a background to make it easier to see
		this.drawDefaultBackground();

		//Then, we render all textboxes
		textBoxes.values().forEach(NickGuiTextField::drawTextBox);


		//Then we render the labels
		for (LTextLabel l : labelsToRender) {
			//Since the labels aren't a real GUI control on forge, we must calculate the location independently.
	/*int width = getFontRenderer().getStringWidth(l.getText());
	int height = getFontRenderer().FONT_HEIGHT;
	*/
			Point loc = centerControl(l);

			drawString(ModCoderPackUtils.getFontRenderer(), l.getText(), loc.getX(), loc.getY(), (int) l.getColor().getHexColor());
		}
		checkBoxes.values().forEach(c -> checkboxRender.renderLithiumControl(c, this));
		progressBars.values().forEach(l -> progressBarRender.renderLithiumControl(l, this));

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
