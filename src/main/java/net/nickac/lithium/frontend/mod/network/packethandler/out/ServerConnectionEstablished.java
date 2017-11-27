package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;

import java.util.Arrays;
import java.util.List;

public class ServerConnectionEstablished implements PacketOut {
    @Override
    public List<String> execute() {
        return Arrays.asList(key(),String.valueOf(LithiumConstants.VERSION));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.CONNECTION_ESTABLISHED;
    }
}
