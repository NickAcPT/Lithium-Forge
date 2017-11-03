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

import net.nickac.lithium.backend.controls.LControl;

import java.util.Arrays;

/**
 * Created by NickAc for Lithium!
 */
public class MiscUtils {
	public static String stringToStars(String input) {
		char[] ca = new char[input.length()];
		Arrays.fill(ca, '*');
		return new String(ca);
	}

	public static boolean isCenteredX(LControl c) {
		return c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.VERTICAL;
	}

	public static boolean isCenteredY(LControl c) {
		return c.getCentered() != LControl.CenterOptions.NONE && c.getCentered() != LControl.CenterOptions.HORIZONTAL;
	}

	public static int ConvertRange(int originalStart, int originalEnd, int newStart, int newEnd, int value) {
		double scale = (double) (newEnd - newStart) / (originalEnd - originalStart);
		return (int) (newStart + ((value - originalStart) * scale));
	}
}
