package com.jsoft.cocit.constant;

/*
 * N > S > P > D > I > R > H > E
 */
public abstract class FieldModes {
	public static final int N = 1;// 2^0

	public static final int S = 2;// 2^1

	public static final int P = 4;// 2^2

	public static final int D = 8;// 2^3

	public static final int I = 16;// 2^4

	public static final int R = 32;// 2^5

	public static final int H = 64;// 2^6

	public static final int E = 128;// 2^7

	public static final int M = 256;// 2^8

	/**
	 * 解析UI模式：返回值包含M
	 */
	public static int parseMode(String m) {
		if (m == null)
			return 0;

		int mode = 0;

		if (m.contains("M")) {
			mode += FieldModes.M;
		}
		if (m.contains("N")) {
			mode += FieldModes.N;
		}
		if (m.contains("S")) {
			mode += FieldModes.S;
		}
		if (m.contains("P")) {
			mode += FieldModes.P;
		}
		if (m.contains("D")) {
			mode += FieldModes.D;
		}
		if (m.contains("I")) {
			mode += FieldModes.I;
		}
		if (m.contains("R")) {
			mode += FieldModes.R;
		}
		if (m.contains("H")) {
			mode += FieldModes.H;
		}
		if (m.contains("E")) {
			mode += FieldModes.E;
		}

		return mode;
	}

	/**
	 * 解析UI模式：返回值不包含M
	 */
	public static int parseUiMode(int mode) {
		int ret = mode & N;
		if (ret == N)
			return N;

		ret = mode & S;
		if (ret == S)
			return S;

		ret = mode & P;
		if (ret == P)
			return P;

		ret = mode & D;
		if (ret == D)
			return D;

		ret = mode & I;
		if (ret == I)
			return I;

		ret = mode & R;
		if (ret == R)
			return R;

		ret = mode & H;
		if (ret == H)
			return H;

		return E;
	}

	public static boolean isE(int mode) {
		return contains(mode, E);
	}

	public static boolean isH(int mode) {
		return contains(mode, H);
	}

	public static boolean isR(int mode) {
		return contains(mode, R);
	}

	public static boolean isI(int mode) {
		return contains(mode, I);
	}

	public static boolean isD(int mode) {
		return contains(mode, D);
	}

	public static boolean isP(int mode) {
		return contains(mode, P);
	}

	public static boolean isS(int mode) {
		return contains(mode, S);
	}

	public static boolean isN(int mode) {
		return contains(mode, N);
	}

	public static boolean isM(int mode) {
		return contains(mode, M);
	}

	private static boolean contains(int mode, int modeCode) {
		return (mode & modeCode) == modeCode;
	}

	public static int mergeMode(int opMode, int runtimeMode) {
		return opMode | runtimeMode;
	}

}
