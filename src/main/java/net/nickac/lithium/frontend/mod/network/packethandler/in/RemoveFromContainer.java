package net.nickac.lithium.frontend.mod.network.packethandler.in;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.LContainer;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketIn;

import java.util.List;
import java.util.UUID;

public class RemoveFromContainer implements PacketIn {

    @SideOnly(Side.CLIENT)
    @Override
    public void execute(List<String> data) {
        UUID containerUUID = UUID.fromString(data.get(0));
        UUID controlUUID = UUID.fromString(data.get(1));


        LControl container = LithiumMod.getWindowManager().getControlById(containerUUID);
        if (container != null) {
            if (container instanceof LContainer && LithiumMod.getCurrentLithium() != null) {
                LControl toRemove = LithiumMod.getWindowManager().getControlById(controlUUID);
                if (toRemove != null) {
                    LithiumMod.getCurrentLithium().removeControl(toRemove);
                }
            }
        }
    }

    @Override
    public String key() {
        return LithiumConstants.TO_CLIENT.REMOVE_FROM_CONTAINER;
    }
}
