package net.nickac.lithium.frontend.mod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.LWindow;
import net.nickac.lithium.backend.serializer.SerializationUtils;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.ui.LithiumGUI;

import static net.nickac.lithium.backend.other.LithiumConstants.*;
import static net.nickac.lithium.frontend.mod.utils.NativeUtils.readUTF8String;

/**
 * Created by NickAc for Lithium!<br><br>
 * Original class made by diesieben07
 */
public class LithiumMessage implements IMessage {

	private String text;

	public LithiumMessage() {
	}

	public LithiumMessage(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		text = readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
	}

	public static class Handle implements IMessageHandler<LithiumMessage, IMessage> {

		@Override
		public IMessage onMessage(LithiumMessage message, MessageContext ctx) {
			//TODO: Handle all other stuff
			String receivedMessage = message.text.trim();
			//System.out.println(String.format("Received %s.", message.text.trim()));
			if (receivedMessage.startsWith(LITHIUM_RECEIVE_WINDOW)) {
				String w = receivedMessage.substring(LITHIUM_RECEIVE_WINDOW.length());

				LWindow receivedWindow = SerializationUtils.stringToObject(w, LWindow.class);

				if (receivedWindow != null) {
					LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage(LITHIUM_WINDOW_OPEN + receivedWindow.getUUID()));
					Minecraft.getMinecraft().addScheduledTask(() -> {
						LithiumGUI gui = new LithiumGUI(receivedWindow);
						Minecraft.getMinecraft().displayGuiScreen(gui);
					});

				}
			} else if (receivedMessage.startsWith(LITHIUM_CONTROL_CHANGED)) {
				String c = receivedMessage.substring(LITHIUM_CONTROL_CHANGED.length());
				LControl newC = SerializationUtils.stringToObject(c, LWindow.class);
				if (LithiumMod.getCurrentLithium() != null && newC != null) {
					LithiumMod.replaceControl(LithiumMod.getCurrentLithium().getBaseWindow(), newC.getUUID(), newC);
				}

			} else if (receivedMessage.equals(LITHIUM_CLOSE_WINDOW)) {
				Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(null));
			}
			//System.out.println(String.format("Received %s.", message.text.trim()));
			return null;
		}
	}

}