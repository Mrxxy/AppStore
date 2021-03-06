package com.rixin.appstore.utils;

import java.io.Closeable;
import java.io.IOException;

import android.text.TextUtils;

public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
