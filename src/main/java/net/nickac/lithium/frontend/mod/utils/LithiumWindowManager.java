package net.nickac.lithium.frontend.mod.utils;

import net.minecraft.client.gui.Gui;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LWindow;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by NickAc for Lithium!
 */
public class LithiumWindowManager {
	private HashMap<UUID, LWindow> windows = new HashMap<>();
	private HashMap<UUID, LControl> controls = new HashMap<>();
	private HashMap<UUID, Gui> nativeControls = new HashMap<>();

	public void clear() {
		windows.clear();
		controls.clear();
		nativeControls.clear();
	}

	public LWindow getWindowById(UUID uuid) {
		return windows.getOrDefault(uuid, null);
	}

	public LControl getControlById(UUID uuid) {
		return controls.getOrDefault(uuid, null);
	}

	public Gui getNativeControlByLithium(UUID uuid) {
		return nativeControls.getOrDefault(uuid, null);
	}

	public Gui getNativeControlByLithium(LControl c) {
		return getNativeControlByLithium(c.getUUID());
	}

	public void registerWindow(LWindow w) {
		windows.put(w.getUUID(), w);
		for (LControl c : w.getControls()) {
			registerControl(c);
		}
	}

	public void unregisterWindow(LWindow w) {
		windows.remove(w.getUUID());
		w.getControls().forEach(this::removeControl);
	}

	public void registerControl(LControl c) {
		controls.put(c.getUUID(), c);
	}

	public void removeControl(LControl c) {
		controls.remove(c.getUUID());
		nativeControls.remove(c.getUUID());
	}

	public void registerNativeControl(LControl c, Gui nC) {
		nativeControls.put(c.getUUID(), nC);
	}


}
