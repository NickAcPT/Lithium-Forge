package net.nickac.lithium.frontend.mod.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.MessageImpl;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Handle implements IMessageHandler<LithiumMessage, IMessage> {

    private static PacketHandler packetHandler;

    public static void setPacketHandler(PacketHandler packetHandler) {
        Handle.packetHandler = packetHandler;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(LithiumMessage message, MessageContext ctx) {
        String receivedMessage = message.getText().trim();
        String[] msgArray = receivedMessage.split("\\|");
        String key = msgArray[0];

        List<String> data = new ArrayList<>();

        if(msgArray.length>1){
            List<String> par = Arrays.asList(msgArray).subList(1, msgArray.length);
            System.out.println(par);
            data.addAll(par);
        }
        packetHandler.handlePacket(new MessageImpl(key, data));
        return null;
    }



}
