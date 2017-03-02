package com.rixin.appstore.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * 单例模式（懒汉式，双重校验锁）,确保BitmapUtils只有一个对象存在
 * 
 * @author 飘渺云轩
 * @date 2016-10-01
 */
public class BitmapHelper {
	
	private static BitmapUtils bitmapUtils = null;
	
	private BitmapHelper(){}

	public static BitmapUtils getBitmapUtils(){
		if(bitmapUtils == null){
			synchronized (BitmapHelper.class) {
				if(bitmapUtils == null){
					bitmapUtils = new BitmapUtils(UIUtils.getContext());
				}
			}
		}
		return bitmapUtils;
	}
}
