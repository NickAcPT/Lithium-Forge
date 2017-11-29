package net.nickac.lithium.frontend.mod.network.packethandler.in;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketIn;

import java.util.List;

public class ControlChanged implements PacketIn {

    @SideOnly(Side.CLIENT)
    @Override
    public void execute(List<String> data) {
        String c = data.get(0);
        LControl newC = SerializationUtils.stringToObject(c, LControl.class);

        if (newC.getParent() != null) {
            Minecraft.getMinecraft().addScheduledTask(() -> LithiumMod.replaceControl(newC.getParent(), newC.getUUID(), newC));
        }
    }

    @Override
    public String key() {
        return LithiumConstants.TO_CLIENT.CONTROL_CHANGED;
    }
}
