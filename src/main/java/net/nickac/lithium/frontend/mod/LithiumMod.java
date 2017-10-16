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

package net.nickac.lithium.frontend.mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.nickac.lithium.backend.controls.LContainer;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LOverlay;
import net.nickac.lithium.frontend.mod.events.NetworkEventHandler;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.ui.LithiumOverlay;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.LithiumWindowManager;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
@Mod(modid = LithiumMod.MODID, version = LithiumMod.VERSION, clientSideOnly = true, canBeDeactivated = true)
public class LithiumMod {
	public static final String MODID = "lithium";
	public static final String CHANNELNAME = "Lithium";
	public static final String VERSION = "0.2.1-BETA";

	private static LithiumWindowManager windowManager = new LithiumWindowManager();
	private static NewLithiumGUI currentLithium;
	private static LOverlay currentLithiumOverlay;
	private static SimpleNetworkWrapper network;
	private LithiumOverlay overlayRenderer;

	public static LOverlay getCurrentLithiumOverlay() {
		return currentLithiumOverlay;
	}

	public static void setCurrentLithiumOverlay(LOverlay currentLithiumOverlay) {
		LithiumMod.currentLithiumOverlay = currentLithiumOverlay;
	}

	public static void log(String s) {
		System.out.println(s);
	}

	public static NewLithiumGUI getCurrentLithium() {
		return currentLithium;
	}

	public static void setCurrentLithium(NewLithiumGUI currentLithium) {
		LithiumMod.currentLithium = currentLithium;
	}

	public static LithiumWindowManager getWindowManager() {
		return windowManager;
	}

	public static void setWindowManager(LithiumWindowManager windowManager) {
		LithiumMod.windowManager = windowManager;
	}

	public static void replaceControl(LContainer cc, UUID u, LControl c) {
		for (LControl control : cc.getControls()) {
			if (control instanceof LContainer) {
				replaceControl(((LContainer) control), u, c);
			} else if (control.getUUID().equals(u)) {
				currentLithium.removeControl(control);
				currentLithium.addControlToGUI(c);
				return;
			}
		}
	}

	public static SimpleNetworkWrapper getSimpleNetworkWrapper() {
		return network;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(NetworkEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(overlayRenderer);
		network = NetworkRegistry.INSTANCE.newSimpleChannel(LithiumMod.CHANNELNAME);
		getSimpleNetworkWrapper().registerMessage(LithiumMessage.Handle.class, LithiumMessage.class, 0, Side.CLIENT);
	}
}
