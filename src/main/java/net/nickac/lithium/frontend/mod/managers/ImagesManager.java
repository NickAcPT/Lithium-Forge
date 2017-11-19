package net.nickac.lithium.frontend.mod.managers;

import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.nickac.lithium.backend.controls.impl.LImage;
import net.nickac.lithium.frontend.mod.utils.MiscUtils;
import net.nickac.lithium.frontend.mod.utils.NickHashMap;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.nickac.lithium.frontend.mod.utils.ModCoderPackUtils.getTextureManager;

/**
 * Created by NickAc for Lithium!
 */
public class ImagesManager {

	private static NickHashMap<UUID, String> imagesUrl = new NickHashMap<>();
	private static NickHashMap<UUID, DynamicTexture> bufferedImages = new NickHashMap<>();
	private static NickHashMap<UUID, ResourceLocation> imagesLocation = new NickHashMap<>();
	private static List<UUID> handledControls = new ArrayList<>();


	public static ResourceLocation getResourceLocation(LImage ctrl) {
		return imagesLocation.get(imagesUrl.getKey(ctrl.getImageURL()));
	}

	public static DynamicTexture getDynamicTexture(LImage img) {
		return bufferedImages.get(img.getUUID());
	}

	public static boolean isImageHandled(LImage img) {
		return handledControls.contains(img.getUUID());
	}

	public static void handleImage(LImage img) {
		try {
			ResourceLocation finalLoc;
			//First of all, we need to take the url of the image and correlate it to a uuid.
			//UUID uuid = UUID.randomUUID();
			//imagesUrl.put(uuid, img.getImageURL());

			//finalLoc = new ResourceLocation(LithiumMod.getMainResourceLocation() + uuid.toString());
			//downloadImageData(img.getImageURL(), finalLoc);
			//imagesLocation.put(uuid, finalLoc);
			handledControls.add(img.getUUID());
			bufferedImages.put(img.getUUID(), new DynamicTexture(ImageIO.read(new URL(img.getImageURL()))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ThreadDownloadImageData downloadImageData(String url, ResourceLocation rl) {
		File imgFile = getImageFile(url, rl);
		imgFile.deleteOnExit();
		if (imgFile.exists()) {
			imgFile.delete();
		}
		TextureManager txtMan = getTextureManager();
		ITextureObject img = new ThreadDownloadImageData(imgFile, url, rl, null);
		txtMan.loadTexture(rl, img);

		return (ThreadDownloadImageData) img;
	}

	private static File getImageFile(String url, ResourceLocation rl) {
		return new File(rl.getResourcePath() + MiscUtils.getURLFileExt(url));
	}


}
