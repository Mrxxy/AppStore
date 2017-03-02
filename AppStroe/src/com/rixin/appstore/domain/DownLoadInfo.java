package com.rixin.appstore.domain;

import java.io.File;

import android.os.Environment;

import com.rixin.appstore.manager.DownLoadManager;

/**
 * 下载javaBean
 * 
 * @author 飘渺云轩
 * @date 2016-10-12
 */
public class DownLoadInfo {

	public String id;
	public String name;
	public String packageName;
	public String downloadUrl;
	public long size;
	
	public long currentPos;  //当前下载位置
	public int currentState;  //当前下载状态
	public String path;  //下载到本地的路径
	
	public final static String APPSTORE = "AppStore";  //sd卡根目录下的文件夹
	public final static String DOWNLOAD = "DownLoad";  //子文件夹，存放下载文件
	
	/** 获取下载进度（0-1） */
	public float getProgress(){
		if(size == 0){
			return 0;
		}
		return currentPos / (float)size;
	}
	
	/** 返回文件下载到sdcard的路径 */
	public String getFilePath(){
		StringBuffer sb = new StringBuffer();
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		sb.append(sdcard);
		sb.append(File.separator);  // “/”
		sb.append(APPSTORE);
		sb.append(File.separator);
		sb.append(DOWNLOAD);
		
		//文件夹存在或者已经创建完成
		if(isCreateDir(sb.toString())){
			return sb.toString() + File.separator + name + ".apk";
		}
		return null;
	}
	
	/** 拷贝对象，从AppInfo中拷贝出一个DownLoadInfo出来 */
	public static DownLoadInfo copy(AppInfo info){
		DownLoadInfo downloadInfo = new DownLoadInfo();
		downloadInfo.id = info.id;
		downloadInfo.name = info.name;
		downloadInfo.packageName = info.packageName;
		downloadInfo.downloadUrl = info.downloadUrl;
		downloadInfo.size = info.size;
		
		downloadInfo.currentPos = 0;
		downloadInfo.currentState = DownLoadManager.STATE_UNDO;
		downloadInfo.path = downloadInfo.getFilePath();
		
		return downloadInfo;
	}
	
	/** 判断文件夹是否存在  -不存在就创建*/
	private boolean isCreateDir(String dir){
		File fileDir = new File(dir);
		//文件不存在或者不是一个文件夹
		if(!fileDir.exists() || !fileDir.isDirectory()){
			boolean result = fileDir.mkdirs();
			return result;
		}
		return true;
	}
	
}
