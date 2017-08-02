package com.zhp.bos.sms;

import java.util.Arrays;

import com.zhp.bos.utils.PinYin4jUtils;

public class TestPinYin {
	public static void main(String[] args) {
		String[] headByString = PinYin4jUtils.getHeadByString("你好");

		System.out.println(Arrays.toString(headByString));
		String hanziToPinyin = PinYin4jUtils.hanziToPinyin("你好", "");
		System.out.println(hanziToPinyin);

	}

}
