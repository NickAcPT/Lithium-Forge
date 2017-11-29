package net.nickac.lithium.frontend.mod.network.packethandler.in;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketIn;

import java.util.List;

public class CloseWindow implements PacketIn {
    @SideOnly(Side.CLIENT)
    @Override
    public void execute(List<String> data) {
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(null));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_CLIENT.CLOSE_WINDOW;
    }
}
