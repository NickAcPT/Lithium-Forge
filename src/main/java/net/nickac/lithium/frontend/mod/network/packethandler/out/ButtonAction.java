package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.nickac.lithium.backend.controls.impl.LButton;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;

import java.util.Arrays;
import java.util.List;

public class ButtonAction implements PacketOut {

    private LButton lButton;

    public ButtonAction(LButton lButton) {
        this.lButton = lButton;
    }

    @Override
    public List<String> execute() {
        return Arrays.asList(key(),String.valueOf(lButton.getUUID()));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.BUTTON_ACTION;
    }
}
