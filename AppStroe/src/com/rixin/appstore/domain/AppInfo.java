package com.rixin.appstore.domain;

import java.util.ArrayList;

/**
 * 应用信息的javaBean
 * 
 * @author 飘渺云轩
 * @date 2016-09-29
 */
public class AppInfo {

	public String id;   
	public String name;   
	public String des;	//应用介绍
	public String downloadUrl; //下载地址
	public String iconUrl;   //图标地址
	public String packageName; //应用包名
	public long size;
	public float stars;  //评分
	
	//补充字段，工详情页使用
	public String author;
	public String date;
	public String downloadNum;
	public String version;
	public ArrayList<SafeInfo> safe;
	public ArrayList<String> screen;
	
	/** 当一个内部类是public static修饰时，和外部类没区别 */
	public static class SafeInfo{
		public String safeDes;
		public String safeDesUrl;
		public String safeUrl;
	}
	
}
