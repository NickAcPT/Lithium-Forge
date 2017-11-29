package net.nickac.lithium.frontend.mod.network.packethandler.abstracts;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.frontend.mod.network.packethandler.PacketMap;
import net.nickac.lithium.frontend.mod.network.packethandler.in.*;

import java.util.Arrays;
import java.util.List;

public class PacketHandlerImpl implements PacketHandler {


    static {
        List<PacketIn> packetIns = Arrays.asList(
                new AddToContainer(),
                new CloseWindow(),
                new ControlChanged(),
                new ReceiveWindow(),
                new RemoveFromContainer(),
                new ShowOverlay());
        packetIns.forEach((p) -> PacketMap.instance.registerPacketIn(p.key(), p));
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void handlePacket(Message message) {

        System.out.printf("key: "+ message.getKey()+" data: "+message.getData());

        PacketIn packetIn = PacketMap.instance.getByString(message.getKey());
        if (packetIn != null) {
            packetIn.execute(message.getData());
        }
    }
}
