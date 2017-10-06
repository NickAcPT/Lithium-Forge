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
import net.minecraft.client.gui.*;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.*;
import net.nickac.lithium.backend.other.objects.Color;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.ui.renders.ProgressBarRender;
import net.nickac.lithium.frontend.mod.utils.NickHashMap;

import java.io.IOException;
import java.util.*;

import static net.nickac.lithium.backend.other.LithiumConstants.*;

/**
 * Created by NickAc for Lithium!
 */
public class LithiumGUI extends GuiScreen {
	private static ProgressBarRender progressBarRender = new ProgressBarRender();
	//The base window
	private LWindow baseWindow;
	//All controls that are centered horizontally
	private List<UUID> centeredHoriz = new ArrayList<>();
	//All controls that are centered horizontally
	private List<UUID> centeredVert = new ArrayList<>();
	private Map<UUID, GuiTextField> textBoxes = new HashMap<>();
	private Map<Integer, UUID> textBoxesReverse = new HashMap<>();
	private Map<UUID, LTextBox> textBoxesLReverse = new HashMap<>();
	private Map<UUID, LProgressBar> progressBars = new NickHashMap<>();

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
	private int BUTTON_HEIGHT = 20;

	public LithiumGUI(LWindow base) {
		this.baseWindow = base;
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
	private int centerLoc(LControl c, int s, int w, int x, boolean centered) {

	/*int parentSizeW = c.getParent() == null ? 0 : c.getParent().getClass().equals(LPanel.class) ? ((LPanel) c.getParent()).getTotalWidth() : ((LControl) c.getParent()).getSize().getWidth();
	int parentSizeH = c.getParent() == null ? 0 : c.getParent().getClass().equals(LPanel.class) ? ((LPanel) c.getParent()).getTotalHeight() : ((LControl) c.getParent()).getSize().getHeight();
	*/
		if (centered) {
			return (s / 2) - (w / 2)/* + (parentSizeW != 0 ? parentSizeW / 2 - w /2 : 0)*/;
		}

		return x;
	}

	/**
	 * Goes thru all controls and adds them to the gui
	 *
	 * @param ctrls The collection of Lithium controls to be added.
	 */
	private void allControls(Collection<LControl> ctrls) {
		/*textBoxes.clear();
		textBoxesLReverse.clear();
		textBoxesReverse.clear();
		reverseButtonsCounter.clear();
		reverseLButtonsCounter.clear();
		buttonsCounter.clear();
		buttonList.clear();*/
		centeredHoriz.clear();
		centeredVert.clear();

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
	@SuppressWarnings("ConstantConditions")
	public void addControlToGUI(LControl c) {
		//Get scaled resolutin
		ScaledResolution sr = getScaledResolution();

		//Here we check if control is a panel, and if it is, check if it's centered on x or y axis.
		boolean centerPanelX = (c.getClass().equals(LPanel.class)) && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.NONE && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.VERTICAL;
		boolean centerPanelY = (c.getClass().equals(LPanel.class)) && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.NONE && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.HORIZONTAL;

		//Add the controls to the centered lists!
		if (c.getLocation().getX() == CENTERED_CONSTANT || centerPanelX) {
			centeredHoriz.add(c.getUUID());
		}
		if (c.getLocation().getY() == CENTERED_CONSTANT || centerPanelY) {
			centeredVert.add(c.getUUID());
		}


		//Then, we need to get the offset of the parent
		//This offset is the X and Y coordinated of the parent
		int parentOffsetX = (c.getParent() instanceof LControl) ? ((LControl) c.getParent()).getLeft() : 0;
		int parentOffsetY = (c.getParent() instanceof LControl) ? ((LControl) c.getParent()).getTop() : 0;

		//Then we finally calculate the location of the control.
		//Minecraft has some limitations regarding button height, so it's always equal to the constant
		int controlX = centerLoc(c, sr.getScaledWidth(), c.getClass().equals(LPanel.class) ? ((LPanel) c).getTotalWidth() : c.getSize().getWidth(), c.getLocation().getX() + parentOffsetX, centeredHoriz.contains(c.getUUID()) || centerPanelX);
		int controlY = centerLoc(c, sr.getScaledHeight(), ((c.getClass().equals(LButton.class)) ? BUTTON_HEIGHT : (c.getClass().equals(LPanel.class) ? ((LPanel) c).getTotalHeight() : c.getSize().getHeight())), c.getLocation().getY() + parentOffsetY, centeredVert.contains(c.getUUID()) || centerPanelY);


		//The cool part!
		//Adding the control
		if (c.getClass().equals(LPanel.class)) {
			LPanel pnl = (LPanel) c;
			boolean var1 = c.getLocation().getX() == CENTERED_CONSTANT || (pnl.getCenterOptions() != LControl.CenterOptions.NONE && pnl.getCenterOptions() != LControl.CenterOptions.VERTICAL);
			boolean var2 = c.getLocation().getY() == CENTERED_CONSTANT || (pnl.getCenterOptions() != LControl.CenterOptions.NONE && pnl.getCenterOptions() != LControl.CenterOptions.HORIZONTAL);
			pnl.setCenterOptions(var1 && var2 ? LControl.CenterOptions.HORIZONTAL_VERTICAL : (var1 ? LControl.CenterOptions.HORIZONTAL : (var2 ? LControl.CenterOptions.VERTICAL : LControl.CenterOptions.NONE)));
			c.setLocation(new Point(controlX, controlY));
			for (LControl lControl : pnl.getControls()) {
				lControl.setParent(pnl);
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
			GuiTextField txt = new GuiTextField(globalCounter, Minecraft.getMinecraft().fontRenderer, parentOffsetX + c.getLocation().getX(), parentOffsetY + c.getLocation().getY(), c.getSize().getWidth(), c.getSize().getHeight());
			txt.setText(c.getText() != null ? c.getText() : "");
			textBoxes.put(c.getUUID(), txt);
			textBoxesReverse.put(txt.getId(), c.getUUID());
			textBoxesLReverse.put(c.getUUID(), (LTextBox) c);

		} else if (c.getClass().equals(LProgressBar.class)) {
			c.setLocation(new Point(controlX, controlY));
			progressBars.put(c.getUUID(), (LProgressBar) c);
		}
		if (c.getParent() == null || (c.getParent() != null && c.getParent().equals(baseWindow))) {
			baseWindow.addControl(c);
		}
		globalCounter++;
	}


	@Override
	public void updateScreen() {
		super.updateScreen();
		textBoxes.values().forEach(GuiTextField::updateCursorCounter);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		textBoxes.values().forEach(t -> {
			if (t.isFocused()) {
				if (t.textboxKeyTyped(typedChar, keyCode)) {
					LTextBox lTextBox = textBoxesLReverse.get(textBoxesReverse.get(t.getId()));
					if (lTextBox != null) {
						LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_TEXTBOX_TEXT_CHANGED + lTextBox.getUUID() + "|" + t.getText()));
					}
				}
			}
		});
	}

	public void removeControl(LControl g) {
		baseWindow.removeControl(g);
		softRemoveControl(g);
	}

	private GuiButton generateGuiButton(LButton b) {
		ScaledResolution sr = getScaledResolution();

		int parentOffsetX = (b.getParent() instanceof LControl) ? ((LControl) b.getParent()).getLeft() : 0;
		int parentOffsetY = (b.getParent() instanceof LControl) ? ((LControl) b.getParent()).getTop() : 0;

		int controlX = centerLoc(b, sr.getScaledWidth(), b.getSize().getWidth(), b.getLocation().getX() + parentOffsetX, centeredHoriz.contains(b.getUUID()));
		int controlY = centerLoc(b, sr.getScaledHeight(), BUTTON_HEIGHT, b.getLocation().getY(), centeredVert.contains(b.getUUID())) + parentOffsetY;

		return new GuiButton(globalCounter, controlX, controlY, b.getSize().getWidth(), BUTTON_HEIGHT, b.getText());

	}

	@Override
	public void initGui() {
		//We need to clear the button list
		buttonList.clear();
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
			for (GuiTextField gg : textBoxes.values()) {
				UUID txtUUID = textBoxesReverse.getOrDefault(gg.getId(), null);
				if (txtUUID != null && g.getUUID().equals(txtUUID)) {
					textBoxesReverse.remove(gg.getId());
					textBoxesLReverse.remove(txtUUID);
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

	/**
	 * Returns a new scaled resolution from Minecraft.<br>
	 * This method exists to easier backport of the mod.<br>
	 * Between versions, the constructor was changed and
	 *
	 * @return A new scaled resolution object
	 */
	private ScaledResolution getScaledResolution() {
		return new ScaledResolution(Minecraft.getMinecraft());
	}

	private FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		textBoxes.values().forEach(t -> t.mouseClicked(mouseX, mouseY, mouseButton));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Just get a scaled resolution
		ScaledResolution sr = getScaledResolution();

		//Then we draw a background to make it easier to see
		this.drawDefaultBackground();

		for (Object lControl : baseWindow.getControls().stream().filter(cc -> cc instanceof LPanel).toArray()) {
			LPanel p = (LPanel) lControl;
			drawRect(p.getLeft(), p.getTop(), p.getLeft() + p.getTotalWidth(), p.getTop() + p.getTotalHeight(), (int) Color.WHITE.getHexColor());
			for (Object l2 : p.getControls().stream().filter(cc -> cc instanceof LPanel).toArray()) {
				LPanel p2 = (LPanel) l2;
				drawRect(p2.getLeft(), p2.getTop(), p2.getLeft() + p2.getTotalWidth(), p2.getTop() + p2.getTotalHeight(), (int) Color.GRAY.getHexColor());
			}
		}
		//Then, we render all textboxes
		textBoxes.values().forEach(GuiTextField::drawTextBox);


		//Then we render the labels
		for (LTextLabel l : labelsToRender) {
			//Since the labels aren't a real GUI control on forge, we must calculate the location independently.
			int parentOffsetX = (l.getParent() instanceof LControl) ? ((LControl) l.getParent()).getLeft() : 0;
			int parentOffsetY = (l.getParent() instanceof LControl) ? ((LControl) l.getParent()).getTop() : 0;
			int width = getFontRenderer().getStringWidth(l.getText());
			int height = getFontRenderer().FONT_HEIGHT;

			drawString(getFontRenderer(), l.getText(), centerLoc(l, sr.getScaledWidth(), width, l.getLocation().getX() + parentOffsetX, centeredHoriz.contains(l.getUUID())), centerLoc(l, sr.getScaledWidth(), height, l.getLocation().getY(), centeredVert.contains(l.getUUID())) + parentOffsetY, (int) l.getColor().getHexColor());
		}

		progressBars.values().forEach(l -> progressBarRender.renderLithiumControl(l, this));

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
