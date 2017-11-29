package net.nickac.lithium.frontend.mod.network.packethandler.abstracts;

import java.util.List;

public interface Message {

    String getKey();

    List<String> getData();
}
