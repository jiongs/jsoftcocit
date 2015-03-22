package com.jsoft.cocit.util;

public abstract class UIPositionUtil {
	public static boolean isTop1(int pos, int... defaultValues) {
		if (defaultValues != null) {
			for (int i : defaultValues) {
				if (pos == i)
					return true;
			}
		}

		return pos / 10 == 1;
	}

	public static boolean isTop2(int pos, int... defaultValues) {
		if (defaultValues != null) {
			for (int i : defaultValues) {
				if (pos == i)
					return true;
			}
		}

		return pos / 20 == 1;
	}
}
