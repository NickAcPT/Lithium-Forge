package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.nickac.lithium.backend.controls.impl.LWindow;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;

import java.util.Arrays;
import java.util.List;

public class WindowOpen implements PacketOut {

    private LWindow lWindow;

    public WindowOpen(LWindow lWindow) {
        this.lWindow = lWindow;
    }


    @Override
    public List<String> execute() {
        return Arrays.asList(key(), String.valueOf(lWindow.getUUID()));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.WINDOW_OPEN;
    }
}
