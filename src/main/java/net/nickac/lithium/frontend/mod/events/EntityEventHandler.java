/*
 * This file is part of Lithium.
 *
 * Lithium is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lithium is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lithium.  If not, see <http://www.gnu.org/licenses/>.
 */

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
		LithiumMod.getSimpleNetworkWrapper().sendToServer(new LithiumMessage("Lithium|OK"));
		MinecraftForge.EVENT_BUS.unregister(this);
	}
}
