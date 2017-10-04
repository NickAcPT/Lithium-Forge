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
import net.nickac.lithium.frontend.mod.events.NetworkEventHandler;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.ui.LithiumGUI;
import net.nickac.lithium.frontend.mod.utils.LithiumWindowManager;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
@Mod(modid = LithiumMod.MODID, version = LithiumMod.VERSION, clientSideOnly = true, canBeDeactivated = true)
public class LithiumMod {
	public static final String MODID = "lithium";
	public static final String CHANNELNAME = "Lithium";
	public static final String VERSION = "0.2.1-BETA";

	private static LithiumWindowManager windowManager = new LithiumWindowManager();
	private static LithiumGUI currentLithium;
	private static SimpleNetworkWrapper network;

	public static LithiumGUI getCurrentLithium() {
		return currentLithium;
	}

	public static void setCurrentLithium(LithiumGUI currentLithium) {
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
			if (control.getClass().equals(LContainer.class)) {
				replaceControl(((LContainer) control), u, c);
			} else if (control.getUUID().equals(u)) {
				c.setParent(null);
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
		network = NetworkRegistry.INSTANCE.newSimpleChannel(LithiumMod.CHANNELNAME);
		getSimpleNetworkWrapper().registerMessage(LithiumMessage.Handle.class, LithiumMessage.class, 0, Side.CLIENT);
	}
}
