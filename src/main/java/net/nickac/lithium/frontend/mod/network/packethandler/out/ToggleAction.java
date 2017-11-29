package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;

import java.util.Arrays;
import java.util.List;

public class ToggleAction implements PacketOut {

    private LControl lControl;

    public ToggleAction(LControl lControl) {
        this.lControl = lControl;
    }

    @Override
    public List<String> execute() {
        return Arrays.asList(key(),String.valueOf(lControl.getUUID()));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.TOGGLE_ACTION;
    }
}
