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

package net.nickac.lithium.frontend.mod.managers;

import net.minecraft.client.gui.Gui;
import net.nickac.lithium.backend.controls.LContainer;
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
		if (c instanceof LContainer) {
			((LContainer) c).getControls().forEach(this::registerControl);
		}
	}

	public void removeControl(LControl c) {
		controls.remove(c.getUUID());
		if (c instanceof LContainer) {
			((LContainer) c).getControls().forEach(this::removeControl);
		}
		nativeControls.remove(c.getUUID());
	}

	public void registerNativeControl(LControl c, Gui nC) {
		nativeControls.put(c.getUUID(), nC);
	}


}
