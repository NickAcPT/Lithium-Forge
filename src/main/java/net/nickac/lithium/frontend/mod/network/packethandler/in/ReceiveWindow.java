package net.nickac.lithium.frontend.mod.network.packethandler.in;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.impl.LWindow;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketIn;
import net.nickac.lithium.frontend.mod.network.packethandler.out.WindowOpen;
import net.nickac.lithium.frontend.mod.ui.NewLithiumGUI;
import net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils;

import java.util.List;

public class ReceiveWindow implements PacketIn {

    @SideOnly(Side.CLIENT)
    @Override
    public void execute(List<String> data) {
        String w = data.get(0);
        LWindow receivedWindow = SerializationUtils.stringToObject(w, LWindow.class);

        System.out.println(receivedWindow);

        if (receivedWindow != null) {
            ModCoderPackUtils.sendLithiumMessageToServer(new WindowOpen(receivedWindow));
            Minecraft.getMinecraft().addScheduledTask(() -> {
                NewLithiumGUI gui = new NewLithiumGUI(receivedWindow);
                Minecraft.getMinecraft().displayGuiScreen(gui);
            });

        }
    }

    @Override
    public String key() {
        return LithiumConstants.TO_CLIENT.RECEIVE_WINDOW;
    }
}
