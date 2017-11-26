package net.nickac.lithium.frontend.mod.network.packethandler.in;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.LContainer;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LWindow;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketIn;

import java.util.List;
import java.util.UUID;

public class AddToContainer implements PacketIn {

    @SideOnly(Side.CLIENT)
    @Override
    public void execute(List<String> data) {
        try {
            //Deserialize the control
            LControl newC = SerializationUtils.stringToObject(data.get(1), LControl.class);

            UUID uuid = UUID.fromString(data.get(0));
            LControl l = LithiumMod.getWindowManager().getControlById(uuid);
            if (l != null) {
                //Check if it is a container
                if (l instanceof LContainer) {
                    ((LContainer) l).addControl(newC);
                    if (LithiumMod.getCurrentLithium() != null) {
                        //Lets try adding this control.
                        //It might work...
                        LithiumMod.getCurrentLithium().addControlToGUI(newC);
                    }
                }
            } else {
                //It might be a window....
                LWindow window = LithiumMod.getWindowManager().getWindowById(uuid);
                if (window != null) {
                    window.addControl(newC);
                    if (LithiumMod.getCurrentLithium() != null &&
                            LithiumMod.getCurrentLithium().getBaseWindow().getUUID().equals(uuid)) {
                        LithiumMod.getCurrentLithium().addControlToGUI(newC);
                    }
                }

            }

        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            LithiumMod.log("Received malformed packet from server. Ignoring!");
        }
    }

    @Override
    public String key() {
        return LithiumConstants.TO_CLIENT.ADD_TO_CONTAINER;
    }
}
