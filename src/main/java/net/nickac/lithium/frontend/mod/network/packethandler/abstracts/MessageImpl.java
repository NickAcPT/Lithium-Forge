package net.nickac.lithium.frontend.mod.network.packethandler.abstracts;

import java.util.List;

public class MessageImpl implements Message {

    private String key;
    private List<String> data;

    public MessageImpl(String key, List<String> data) {
        this.key = key;
        this.data = data;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public List<String> getData() {
        return data;
    }
}
