package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.nickac.lithium.backend.controls.impl.LWindow;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;

import java.util.Arrays;
import java.util.List;

public class WindowClose implements PacketOut{

    private LWindow window;

    public WindowClose(LWindow window) {
        this.window = window;
    }

    @Override
    public List<String> execute() {
        return Arrays.asList(key(),String.valueOf(window.getUUID()) );
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.WINDOW_CLOSE;
    }
}
