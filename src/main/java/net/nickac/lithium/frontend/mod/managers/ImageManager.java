package net.nickac.lithium.frontend.mod.managers;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.nickac.lithium.backend.controls.impl.LImage;
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

	public static void handleImage(LImage img) {
		if (isImageHandled(img)) {
			return;
		}
		try {
			handledControls.add(img.getUUID());
			bufferedImages.put(img.getUUID(), new DynamicTexture(ImageIO.read(new URL(img.getImageURL()))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
