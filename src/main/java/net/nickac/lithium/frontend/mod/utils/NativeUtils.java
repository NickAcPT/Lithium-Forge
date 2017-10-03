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

package net.nickac.lithium.frontend.mod.utils;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.Validate;

import java.nio.charset.StandardCharsets;

/**
 * Created by NickAc for Lithium!
 */
public class NativeUtils {

	public static int readVarInt(ByteBuf buf, int maxSize) {
		Validate.isTrue(maxSize < 6 && maxSize > 0, "Varint length is between 1 and 5, not %d", maxSize);
		int i = 0;
		int j = 0;
		byte b0;

		do {
			b0 = buf.readByte();
			i |= (b0 & 127) << j++ * 7;

			if (j > maxSize) {
				throw new RuntimeException("VarInt too big");
			}
		}
		while ((b0 & 128) == 128);

		return i;
	}

	public static String readUTF8String(ByteBuf from) {
		int len = readVarInt(from, 2);
		return from.toString(StandardCharsets.UTF_8);
	}

	public static int varIntByteCount(int toCount) {
		return (toCount & 0xFFFFFF80) == 0 ? 1 : ((toCount & 0xFFFFC000) == 0 ? 2 : ((toCount & 0xFFE00000) == 0 ? 3 : ((toCount & 0xF0000000) == 0 ? 4 : 5)));
	}

}
