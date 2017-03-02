package com.rixin.appstore.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 自定义Application，初始化全局
 * 
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class AppStoreApplication extends Application {

	private static Context context;
	private static Handler handler;
	/**
	 * 主线程id
	 */
	private static int mainThreadId;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();
	}

	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return handler;
	}

	public static int getMainThreadId() {
		return mainThreadId;
	}
	
}
