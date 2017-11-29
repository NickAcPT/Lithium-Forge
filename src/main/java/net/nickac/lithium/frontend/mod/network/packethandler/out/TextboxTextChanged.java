package net.nickac.lithium.frontend.mod.network.packethandler.out;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.impl.LTextBox;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.network.packethandler.abstracts.PacketOut;
import net.nickac.lithium.frontend.mod.ui.NickGuiTextField;

import java.util.Arrays;
import java.util.List;

public class TextboxTextChanged implements PacketOut {

    private NickGuiTextField nickGuiTextField;
    private LTextBox lTextBox;

    public TextboxTextChanged(NickGuiTextField nickGuiTextField, LTextBox lTextBox) {
        this.nickGuiTextField = nickGuiTextField;
        this.lTextBox = lTextBox;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public List<String> execute() {
        return Arrays.asList(key(), String.valueOf(lTextBox.getUUID()), SerializationUtils.objectToString(nickGuiTextField.getText()));
    }

    @Override
    public String key() {
        return LithiumConstants.TO_SERVER.TEXTBOX_TEXT_CHANGED;
    }
}
