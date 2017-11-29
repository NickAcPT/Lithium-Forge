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

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.LContainer;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LOverlay;
import net.nickac.lithium.frontend.mod.events.NetworkEventHandler;
import net.nickac.lithium.frontend.mod.managers.LithiumWindowManager;
import net.nickac.lithium.frontend.mod.network.Handle;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketHandlerImpl;
import net.nickac.lithium.frontend.mod.ui.LithiumOverlay;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
@Mod(modid = LithiumMod.MODID, version = LithiumMod.VERSION, clientSideOnly = true, canBeDeactivated = true)
public class LithiumMod {
	public static final String MODID = "lithium";
	public static final String CHANNELNAME = "Lithium";
	public static final String VERSION = "1.2-BETA";

	private static LithiumWindowManager windowManager = new LithiumWindowManager();
	private static NewLithiumGUI currentLithium;
	private static LOverlay currentLithiumOverlay;
	private static SimpleNetworkWrapper network;
	private static LithiumOverlay overlayRenderer;
	private static ResourceLocation mainResourceLocation;

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

	@SideOnly(Side.CLIENT)
	public static void replaceControl(LContainer cc, UUID u, LControl c) {
		for (LControl control : cc.getControls()) {
			if (control instanceof LContainer) {
				replaceControl(((LContainer) control), u, c);
			} else if (control.getUUID().equals(u)) {
				if (currentLithium != null) {
					//Try to check if it is a window
					currentLithium.removeControl(control);
					currentLithium.addControlToGUI(c);
				} else if (currentLithiumOverlay != null) {
					//It might be the overlay
					currentLithiumOverlay.addControl(c);
				}
			}
		}
	}

	public static SimpleNetworkWrapper getSimpleNetworkWrapper() {
		return network;
	}

	public static ResourceLocation getMainResourceLocation() {
		return mainResourceLocation;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mainResourceLocation = new ResourceLocation(event.getModConfigurationDirectory() + "/lithium/images/");

	}

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void init(FMLInitializationEvent event) {
		overlayRenderer = new LithiumOverlay();
		MinecraftForge.EVENT_BUS.register(NetworkEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(overlayRenderer);
		network = NetworkRegistry.INSTANCE.newSimpleChannel(LithiumMod.CHANNELNAME);
		Handle.setPacketHandler(new PacketHandlerImpl());
		getSimpleNetworkWrapper().registerMessage(Handle.class, LithiumMessage.class, 0, Side.CLIENT);

	}
}
