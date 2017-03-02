package com.rixin.appstore.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Intent;
import android.net.Uri;

import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.domain.DownLoadInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.http.HttpHelper.HttpResult;
import com.rixin.appstore.utils.IOUtils;
import com.rixin.appstore.utils.UIUtils;

/**
 * 下载管理器
 * 
 * -未下载 -等待下载 -下载中 -暂停下载 -下载失败 -下载成功
 * 
 * DownLoadManager：被观察者
 * 
 * @author 飘渺云轩
 * @date 2016-10-11
 */
public class DownLoadManager {

	public final static int STATE_UNDO = 1;
	public final static int STATE_WAITING = 2;
	public final static int STATE_DOWNLOADING = 3;
	public final static int STATE_PAUSE = 4;
	public final static int STATE_ERROR = 5;
	public final static int STATE_SUCCESS = 6;

	// 观察者集合
	private ArrayList<DownLoadObserver> observers = new ArrayList<DownLoadManager.DownLoadObserver>();
	// downloadInfo集合,使用线程安全的HashMap
	private ConcurrentHashMap<String, DownLoadInfo> infoMap = new ConcurrentHashMap<String, DownLoadInfo>();
	// 任务集合,使用线程安全的HashMap
	private ConcurrentHashMap<String, DownLoadTask> taskMap = new ConcurrentHashMap<String, DownLoadManager.DownLoadTask>();

	public final static DownLoadManager downloadManager = new DownLoadManager();

	private DownLoadManager() {
	}

	public static DownLoadManager getInstance() {
		return downloadManager;
	}
	
	//////////////////////////////////////////////////

	/** 开始下载 */
	public synchronized void download(AppInfo info) {
		// 如果对象是第一次下载，需要创建一个新的downLoadInfo对象，从头下载；如果之前下载过，要接着下载，实现断点续传
		DownLoadInfo downLoadInfo = infoMap.get(info.id);
		if (downLoadInfo == null) {
			downLoadInfo = DownLoadInfo.copy(info); // 生成一个下载的对象
		}

		downLoadInfo.currentState = STATE_WAITING; // 切换为等待下载
		notifyDownLoadStateChanged(downLoadInfo); // 通知所有观察者状态发生变化了

		// 将对象放到集合中
		infoMap.put(info.id, downLoadInfo);

		// 初始化下载任务，并放到线程池中运行
		DownLoadTask task = new DownLoadTask(downLoadInfo);
		ThreadManager.getThreadPool().execute(task);

		// 将任务放到集合中
		taskMap.put(info.id, task);
	}

	/** 暂停下载 */
	public synchronized void pause(AppInfo info) {
		// 取出下载对象
		DownLoadInfo downLoadInfo = infoMap.get(info.id);
		if (downLoadInfo != null) {
			// 只有在正在下载和等待下载时才需要暂停
			if (downLoadInfo.currentState == STATE_DOWNLOADING
					|| downLoadInfo.currentState == STATE_WAITING) {
				// 切换为暂停状态
				downLoadInfo.currentState = STATE_PAUSE;
				notifyDownLoadStateChanged(downLoadInfo);

				DownLoadTask task = taskMap.get(downLoadInfo.id);
				if (task != null) {
					// 移除下载任务，如果任务还没开始正在等待，可以通过此方法移除
					//如果任务已经开始运行，需要在run方法中进行中断
					ThreadManager.getThreadPool().cancal(task);
				}
			}
		}
	}

	/** 开始安装 */
	public synchronized void install(AppInfo info) {
		DownLoadInfo downloadInfo = infoMap.get(info.id);
		if (downloadInfo != null) {
			// 跳到系统的安装页面进行安装
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
					"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(intent);
		}
	}
	
	/** 根据应用信息返回下载对象 */
	public DownLoadInfo getDownLoadInfo(AppInfo info){
		return infoMap.get(info.id);
	}
	
	//////////////////////////////////////////////////////

	/**
	 * 下载任务
	 */
	class DownLoadTask implements Runnable {

		private DownLoadInfo downloadInfo;

		public DownLoadTask(DownLoadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		@Override
		public void run() {
			// 切换到正在下载状态
			downloadInfo.currentState = STATE_DOWNLOADING;
			notifyDownLoadStateChanged(downloadInfo);

			File file = new File(downloadInfo.path);
			HttpResult httpResult;
			if (!file.exists() || file.length() != downloadInfo.currentPos
					|| downloadInfo.currentPos == 0) { // 从头开始下载
				// 删除无效文件
				file.delete();
				downloadInfo.currentPos = 0;

				httpResult = HttpHelper.get(HttpHelper.URL + "download?name="
						+ downloadInfo.downloadUrl);

			} else { // 断点续传
				// range: 表示请求服务器从哪个位置开始返回数据
				httpResult = HttpHelper.get(HttpHelper.URL + "download?name="
						+ downloadInfo.downloadUrl + "&range=" + file.length());
			}

			if (httpResult != null && httpResult.getInputStream() != null) {
				InputStream in = httpResult.getInputStream();
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file, true); // 在原有文件上追加
					int len = 0;
					byte[] buffer = new byte[1024];
					// 只有状态是正在下载才可以进行轮询，解决下载过程中中途暂停的问题
					while ((len = in.read(buffer)) != -1
							&& downloadInfo.currentState == STATE_DOWNLOADING) {
						out.write(buffer, 0, len);
						out.flush();

						// 更新下载进度
						downloadInfo.currentPos += len;
						notifyDownLoadProgressChanged(downloadInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.close(out);
					IOUtils.close(in);
				}
				
				// 文件下载结束后
				if (file.length() == downloadInfo.size) { // 文件完整
					downloadInfo.currentState = STATE_SUCCESS;
					notifyDownLoadStateChanged(downloadInfo);
				} else if (downloadInfo.currentState == STATE_PAUSE) { // 中途暂停
					notifyDownLoadStateChanged(downloadInfo);
				} else { // 下载失败
					file.delete(); // 删除无效文件
					downloadInfo.currentPos = 0;
					downloadInfo.currentState = STATE_ERROR;
					notifyDownLoadStateChanged(downloadInfo);
				}
				
			} else {
				// 网络异常
				file.delete(); // 删除无效文件
				downloadInfo.currentPos = 0;
				downloadInfo.currentState = STATE_ERROR;
				notifyDownLoadStateChanged(downloadInfo);
			}
			
			// 从集合中移除下载任务
			taskMap.remove(downloadInfo.id);
		}

	}
	
	///////////////////////////////////////////////////

	/** 通知状态发生改变,回调接口的onDownLoadStateChanged()方法 */
	public void notifyDownLoadStateChanged(DownLoadInfo downLoadInfo) {
		for (DownLoadObserver observer : observers) {
			observer.onDownLoadStateChanged(downLoadInfo);
		}
	}

	/** 通知进度发生改变,回调接口的onDownLoadProgressChanged()方法 */
	public void notifyDownLoadProgressChanged(DownLoadInfo downLoadInfo) {
		for (DownLoadObserver observer : observers) {
			observer.onDownLoadProgressChanged(downLoadInfo);
		}
	}

	/** 注册观察者 */
	public void registerObserver(DownLoadObserver observer) {
		if (observer != null && !observers.contains(observer)) {
			observers.add(observer);
		}
	}

	/** 注销观察者 */
	public void unregisterObserver(DownLoadObserver observer) {
		if (observer != null && observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	/**
	 * 观察者的接口
	 * 
	 * @author 飘渺云轩
	 */
	public interface DownLoadObserver {
		/** 下载状态发生变化 */
		void onDownLoadStateChanged(DownLoadInfo downLoadInfo);

		/** 下载进度发生变化 */
		void onDownLoadProgressChanged(DownLoadInfo downLoadInfo);
	}

}
