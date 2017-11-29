package net.nickac.lithium.frontend.mod.managers;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.nickac.lithium.backend.controls.impl.LImage;
import net.nickac.lithium.frontend.mod.LithiumMod;
import net.nickac.lithium.frontend.mod.utils.NickHashMap;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by NickAc for Lithium!
 */
public class ImageManager {

	private static NickHashMap<UUID, DynamicTexture> bufferedImages = new NickHashMap<>();
	private static List<UUID> handledControls = new ArrayList<>();


	public static DynamicTexture getDynamicTexture(LImage img) {
		return bufferedImages.get(img.getUUID());
	}

	public static boolean isImageHandled(LImage img) {
		return handledControls.contains(img.getUUID());
	}

	@SideOnly(Side.CLIENT)
	public static void handleImage(LImage img) {
		if (isImageHandled(img)) {
			return;
		}
		try {
			LithiumMod.log("Loading image for control with UUID[" + img.getUUID() + "].");
			handledControls.add(img.getUUID());
			bufferedImages.put(img.getUUID(), new DynamicTexture(ImageIO.read(new URL(img.getImageURL()))));
			LithiumMod.log("Finished loading image for control with UUID[" + img.getUUID() + "].");

		} catch (IOException e) {
			LithiumMod.log("An error occured while trying to load image for control with UUID[" + img.getUUID() + "].");
			e.printStackTrace();
		}
	}


}
