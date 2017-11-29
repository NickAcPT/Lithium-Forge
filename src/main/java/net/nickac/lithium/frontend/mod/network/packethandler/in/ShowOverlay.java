package net.nickac.lithium.frontend.mod.network.packethandler.in;

import net.nickac.lithium.backend.controls.impl.LOverlay;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketIn;

import java.util.List;

public class ShowOverlay implements PacketIn {
    @Override
    public void execute(List<String> data) {
        try {
            LOverlay overlay = SerializationUtils.stringToObject(data.get(0), LOverlay.class);
            LithiumMod.setCurrentLithiumOverlay(overlay);
        } catch (Exception e) {
            LithiumMod.log("An error occured while creating an overlay.");
            LithiumMod.log("Please send this to a Lithium Developer:");
            LithiumMod.log("*" + SerializationUtils.objectToString(e.toString()) + "*");
        }
    }

    @Override
    public String key() {
        return LithiumConstants.TO_CLIENT.SHOW_OVERLAY;
    }
}
