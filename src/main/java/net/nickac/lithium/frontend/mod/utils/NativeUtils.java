/*
 * MIT License
 *
 * Copyright (c) 2017 NickAc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
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
