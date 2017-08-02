package com.zhp.bos.utils;

import java.util.Random;

public class RandStringUtils {
	public static String getCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(getRandomString());
		}
		return sb.toString();
	}

	public static int getRandomString() {
		int num = new Random().nextInt(9);
		return num;
	}
}
