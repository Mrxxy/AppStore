package com.rixin.appstore.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.rixin.appstore.global.AppStoreApplication;

/**
 * 工具类
 * 
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class UIUtils {

	/////////////////// 加载全局变量 //////////////////////////////////
	/**
	 * 获取context
	 * @return
	 */
	public static Context getContext(){
		return AppStoreApplication.getContext();
	}
	
	/**
	 * 获取handler
	 * @return
	 */
	public static Handler getHandler(){
		return AppStoreApplication.getHandler();
	}
	
	/**
	 * 获取主线程id
	 * @return
	 */
	public static int getMainThreadId(){
		return AppStoreApplication.getMainThreadId();
	}
	
	///////////////////// 加载资源文件 ///////////////////////////////
	/**
	 * 获取字符串
	 * @param id
	 * @return
	 */
	public static String getString(int id){
		return getContext().getResources().getString(id);
	}

	/**
	 * 获取字符串数组
	 * @param id
	 * @return
	 */
	public static String[] getStringArray(int id){
		return getContext().getResources().getStringArray(id);
	}
	
	/**
	 * 获取图片
	 * @param id
	 * @return
	 */
	public static Drawable getDrawable(int id){
		return getContext().getResources().getDrawable(id);
	}
	
	/**
	 * 获取颜色
	 * @param id
	 * @return
	 */
	public static int getColor(int id){
		return getContext().getResources().getColor(id);
	}
	
	/**
	 * 根据id获取颜色的状态选择器
	 * @param id
	 * @return
	 */
	public static ColorStateList getColorStateList(int id){
		return getContext().getResources().getColorStateList(id);
	}
	
	/**
	 * 获取尺寸
	 * @param id
	 * @return 返回具体的像素值
	 */
	public static int getDimen(int id){
		return getContext().getResources().getDimensionPixelSize(id);
	}
	
	////////////////////// dip和px的转换 ////////////////////////////
	/**
	 * 把dp转换为px
	 * @param dip
	 * @return
	 */
	public static int dipTopx(float dip){
		float density = getContext().getResources().getDisplayMetrics().density; //获得设备密度
		return (int) (dip * density + 0.5f);
	}
	
	public static float pxTodip(int px){
		float density = getContext().getResources().getDisplayMetrics().density; //获得设备密度
		return px / density;
	}
	
	////////////////////////封装加载布局文件//////////////////////////
	/**
	 * 加载布局文件
	 * @param id
	 * @return
	 */
	public static View inflate(int id){
		return View.inflate(getContext(), id, null);
	}
	
	//////////////////////是否运行在主线程/////////////////////////
	/**
	 * 判断是否运行在主线程
	 * @return true：运行在主线程   false：运行在子线程
	 */
	public static boolean isRunOnUIThread(){
		//获取当前线程id，如果当前线程和主线程id相同，那么当前就是主线程
		int myTid = android.os.Process.myTid();
		if(myTid == getMainThreadId()){
			return true;
		}
		return false;
	}
	/**
	 * 将对象运行在主线程中
	 * @param r
	 */
	public static void runOnUIThread(Runnable r){
		if(isRunOnUIThread()){
			r.run(); //已经是主线程，直接运行
		}else{
			getHandler().post(r); //如果是子线程，借助handler让其运行在主线程中
		}
	}
	
}
