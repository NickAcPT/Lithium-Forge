package net.nickac.lithium.frontend.mod.network.packethandler.abstracts;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface PacketHandler {
    @SideOnly(Side.CLIENT)
    void handlePacket(Message message);
}
