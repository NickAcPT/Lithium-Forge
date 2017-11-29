package net.nickac.lithium.frontend.mod.network.packethandler.abstracts;

import java.util.List;

public interface PacketIn {

    void execute(List<String> data);

    String key();
}
