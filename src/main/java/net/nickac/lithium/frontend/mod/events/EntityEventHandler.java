package net.nickac.lithium.frontend.mod.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.network.LithiumMessage;


/**
 * Created by NickAc for Lithium!
 */
public class EntityEventHandler {
	public static final EntityEventHandler INSTANCE = new EntityEventHandler();

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		//After Joining, we need to clear the suff because we are nice!
		LithiumMod.getWindowManager().clear();
		LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage("Lithium|OK"));
		MinecraftForge.EVENT_BUS.unregister(this);
	}
}
