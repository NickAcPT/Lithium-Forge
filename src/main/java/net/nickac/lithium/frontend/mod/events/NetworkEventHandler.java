package net.nickac.lithium.frontend.mod.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * Created by NickAc for Lithium!
 */
public class NetworkEventHandler {

	public static final NetworkEventHandler INSTANCE = new NetworkEventHandler();

	@SubscribeEvent
	public void clientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		Minecraft.getMinecraft().addScheduledTask(() -> MinecraftForge.EVENT_BUS.register(EntityEventHandler.INSTANCE));
	}
}
