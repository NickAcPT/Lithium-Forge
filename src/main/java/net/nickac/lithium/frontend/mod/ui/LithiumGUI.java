/*
 * This file is part of Lithium.
 *
 * Lithium is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lithium is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lithium.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.nickac.lithium.frontend.mod.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.*;
import net.nickac.lithium.backend.other.objects.Point;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;

import java.io.IOException;
import java.util.*;

import static net.nickac.lithium.backend.other.LithiumConstants.*;

/**
 * Created by NickAc for Lithium!
 */
public class LithiumGUI extends GuiScreen {
	//The base window
	private LWindow baseWindow;
	//All controls that are centered horizontally
	private List<UUID> centeredHoriz = new ArrayList<>();
	//All controls that are centered horizontally
	private List<UUID> centeredVert = new ArrayList<>();
	private HashMap<UUID, GuiTextField> textBoxes = new HashMap<>();
	private HashMap<Integer, UUID> textBoxesReverse = new HashMap<>();
	private HashMap<UUID, LTextBox> textBoxesLReverse = new HashMap<>();

	//Button stuff
	//We take a global count number and give a Lithium button
	private HashMap<Integer, LButton> buttonsCounter = new HashMap<>();
	//We take an UUID (of a control) and we get the global count button
	private HashMap<UUID, Integer> reverseLButtonsCounter = new HashMap<>();
	//We take a global count button and give a GuiButton id
	private HashMap<Integer, Integer> reverseButtonsCounter = new HashMap<>();

	//Labels to be rendered!
	private ArrayList<LTextLabel> labelsToRender = new ArrayList<>();
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
	 * @param s
	 * @param w
	 * @param x
	 * @param centered Is the control centered
	 * @return the corrdinate on the screen
	 */
	private int centerLoc(LControl c, int s, int w, int x, boolean centered) {

		int parentSizeW = c.getParent() == null ? 0 : c.getParent().getClass().equals(LPanel.class) ? ((LPanel) c.getParent()).getTotalWidth() : ((LControl) c.getParent()).getSize().getWidth();
		int parentSizeH = c.getParent() == null ? 0 : c.getParent().getClass().equals(LPanel.class) ? ((LPanel) c.getParent()).getTotalHeight() : ((LControl) c.getParent()).getSize().getHeight();

		if (centered)
			return (s / 2) - (w / 2)/* + (parentSizeW != 0 ? parentSizeW / 2 - w /2 : 0)*/;

		return x;
	}

	private void allControls(Collection<LControl> ctrls) {
		buttonList.clear();
		centeredHoriz.clear();
		centeredVert.clear();

		for (LControl c : ctrls) {
			addControlToGUI(c);
		}
	}

	public void addControlToGUI(LControl c) {
		ScaledResolution sr = getScaledResolution();
		if (c.getLocation().getX() == CENTERED_CONSTANT) centeredHoriz.add(c.getUUID());
		if (c.getLocation().getY() == CENTERED_CONSTANT) centeredVert.add(c.getUUID());

		boolean centerPanelX = (c.getClass().equals(LPanel.class)) && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.NONE && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.VERTICAL;
		boolean centerPanelY = (c.getClass().equals(LPanel.class)) && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.NONE && ((LPanel) c).getCenterOptions() != LControl.CenterOptions.HORIZONTAL;

		int parentOffsetX = (c.getParent() instanceof LControl) ? ((LControl) c.getParent()).getLeft() : 0;
		int parentOffsetY = (c.getParent() instanceof LControl) ? ((LControl) c.getParent()).getTop() : 0;

		int controlX = centerLoc(c, sr.getScaledWidth(), c.getClass().equals(LPanel.class) ? ((LPanel) c).getTotalWidth() : c.getSize().getWidth(), c.getLocation().getX() + parentOffsetX, centeredHoriz.contains(c.getUUID()) || centerPanelX);
		int controlY = centerLoc(c, sr.getScaledHeight(), (c.getClass().equals(LButton.class)) ? BUTTON_HEIGHT : (c.getClass().equals(LPanel.class) ? ((LPanel) c).getTotalHeight() : c.getSize().getHeight()), c.getLocation().getY(), centeredVert.contains(c.getUUID()) || centerPanelY) + parentOffsetY;
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
			if (!labelsToRender.contains(lbl)) labelsToRender.add(lbl);
		} else if (c.getClass().equals(LTextBox.class)) {
			GuiTextField txt = new GuiTextField(globalCounter, Minecraft.getMinecraft().fontRenderer, parentOffsetX + c.getLocation().getX(), parentOffsetY + c.getLocation().getY(), c.getSize().getWidth(), c.getSize().getHeight());

			textBoxes.put(c.getUUID(), txt);
			textBoxesReverse.put(txt.getId(), c.getUUID());
			textBoxesLReverse.put(c.getUUID(), (LTextBox) c);

		}
		if (c.getLocation().getX() == CENTERED_CONSTANT) {
			centeredHoriz.add(c.getUUID());
		}
		if (c.getLocation().getY() == CENTERED_CONSTANT) {
			centeredVert.add(c.getUUID());
		}
		if (c.getParent() == null || (c.getParent() != null && c.getParent().equals(baseWindow)))
			baseWindow.addControl(c);
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
					if (lTextBox != null)
						LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_TEXTBOX_TEXT_CHANGED + lTextBox.getUUID() + "|" + t.getText()));
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
		//softRemoveControl(baseWindow);
		buttonList.clear();
		super.initGui();
//		baseWindow.getControls().forEach(this::softRemoveControl);
		LithiumMod.getWindowManager().registerWindow(baseWindow);
		LithiumMod.setCurrentLithium(this);
		allControls(baseWindow.getControls());

	}

	private void softRemoveControl(LControl g) {
		if (g.getClass().equals(LButton.class)) {
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
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		LithiumMod.getWindowManager().unregisterWindow(baseWindow);
		LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_WINDOW_CLOSE + baseWindow.getUUID()));
		LithiumMod.setCurrentLithium(null);
	}


	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		int buttonId = reverseButtonsCounter.getOrDefault(button.id, -1);
		if (buttonId != -1)
			LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_BUTTON_ACTION + buttonsCounter.get(buttonId).getUUID()));
	}

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
		ScaledResolution sr = getScaledResolution();

		this.drawDefaultBackground();

		textBoxes.values().forEach(GuiTextField::drawTextBox);

		for (LTextLabel l : labelsToRender) {
			int parentOffsetX = (l.getParent() instanceof LControl) ? ((LControl) l.getParent()).getLeft() : 0;
			int parentOffsetY = (l.getParent() instanceof LControl) ? ((LControl) l.getParent()).getTop() : 0;
			int width = getFontRenderer().getStringWidth(l.getText());
			int height = getFontRenderer().FONT_HEIGHT;

			String r = Integer.toHexString(l.getColor().getRed());
			r = r.equals("0") ? "00" : r;

			String g = Integer.toHexString(l.getColor().getGreen());
			g = g.equals("0") ? "00" : g;
			String b = Integer.toHexString(l.getColor().getBlue());
			b = b.equals("0") ? "00" : b;
			String a = Integer.toHexString(l.getColor().getAlpha());
			a = a.equals("0") ? "00" : a;
			long color = Long.parseLong(a + r + g + b, 16);

			drawString(Minecraft.getMinecraft().fontRenderer, l.getText(), centerLoc(l, sr.getScaledWidth(), width, l.getLocation().getX() + parentOffsetX, centeredHoriz.contains(l.getUUID())), centerLoc(l, sr.getScaledWidth(), height, l.getLocation().getY(), centeredVert.contains(l.getUUID())) + parentOffsetY, (int) color);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
