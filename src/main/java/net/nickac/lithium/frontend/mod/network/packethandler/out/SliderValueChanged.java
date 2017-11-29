package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.nickac.lithium.backend.controls.impl.LSlider;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;

import java.util.Arrays;
import java.util.List;

public class SliderValueChanged implements PacketOut {

    private LSlider lSlider;

    public SliderValueChanged(LSlider lSlider) {
        this.lSlider = lSlider;
    }

    @Override
    public List<String> execute() {
        return Arrays.asList(key(),String.valueOf(lSlider.getUUID()),String.valueOf(lSlider.getValue()));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.SLIDER_VALUE_CHANGED;
    }
}
