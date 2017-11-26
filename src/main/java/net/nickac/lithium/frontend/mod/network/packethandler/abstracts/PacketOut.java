package net.nickac.lithium.frontend.mod.network.packethandler.abstracts;

import java.util.List;

public interface PacketOut {
    List<String> execute();
    String key();
}
