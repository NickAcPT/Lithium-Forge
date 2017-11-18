package net.nickac.lithium.frontend.mod.utils;

/**
 * Created by NickAc for Lithium!
 */
public class ImageUtils {
	private static String[] allowedExtensions = {"png"};

	public static boolean isUrlSafe(String url) {
		if (url.contains("?")) {
			//Remove GET parameters
			url = url.substring(0, url.indexOf('?'));
		}
		return (urlEndsWithSafeExtension(url));
	}

	public static boolean urlEndsWithSafeExtension(String url) {
		for (String ext : allowedExtensions) {
			if (url.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}
