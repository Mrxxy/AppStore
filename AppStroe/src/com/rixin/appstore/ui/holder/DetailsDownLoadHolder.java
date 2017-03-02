package com.rixin.appstore.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.domain.DownLoadInfo;
import com.rixin.appstore.manager.DownLoadManager;
import com.rixin.appstore.manager.DownLoadManager.DownLoadObserver;
import com.rixin.appstore.ui.view.ProgressHorizontal;
import com.rixin.appstore.utils.UIUtils;

/**
 * 详情页 - 下载模块
 * 
 * @author 飘渺云轩
 * @date 2016-10-10
 */
public class DetailsDownLoadHolder extends BaseHolder<AppInfo> implements
		DownLoadObserver, OnClickListener {

	private Button btnDownLoad;
	private ProgressHorizontal progress;
	private FrameLayout fl;
	private DownLoadManager dm;
	private int currentState;
	private float currentPos;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_details_download);
		btnDownLoad = (Button) view.findViewById(R.id.btn_download);
		btnDownLoad.setOnClickListener(this);

		fl = (FrameLayout) view.findViewById(R.id.fl_progress);
		fl.setOnClickListener(this);
		progress = new ProgressHorizontal(UIUtils.getContext());
		progress.setBackgroundResource(R.drawable.progress_bg); // 背景图片
		progress.setProgressResource(R.drawable.progress_normal); // 进度条图片
		progress.setProgressTextColor(Color.WHITE);
		progress.setProgressTextSize(UIUtils.dipTopx(18));
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);

		fl.addView(progress, params);

		dm = DownLoadManager.getInstance();
		dm.registerObserver(this); // 注册观察者，监听进度和状态变化

		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		DownLoadInfo downloadInfo = dm.getDownLoadInfo(data);
		// 判断是否下载过
		if (downloadInfo != null) {
			currentState = downloadInfo.currentState;
			currentPos = downloadInfo.getProgress();
		} else {
			currentState = DownLoadManager.STATE_UNDO;
			currentPos = 0;
		}

		refreshUI(currentState, currentPos);
	}

	/** 根据当前的进度和状态更新界面 */
	private void refreshUI(int currentState, float currentPos) {
		this.currentState = currentState;
		this.currentPos = currentPos;

		switch (currentState) {
		case DownLoadManager.STATE_UNDO:
			visibledButton();
			btnDownLoad.setText("DownLoad");
			break;
		case DownLoadManager.STATE_WAITING:
			visibledButton();
			btnDownLoad.setText("Waiting...");
			break;
		case DownLoadManager.STATE_DOWNLOADING:
			visibledProgress();
			progress.setCenterText("");
			progress.setProgress(currentPos);
			break;
		case DownLoadManager.STATE_PAUSE:
			visibledProgress();
			progress.setProgress(currentPos);
			progress.setCenterText("Pause");
			break;
		case DownLoadManager.STATE_ERROR:
			visibledButton();
			btnDownLoad.setText("Error");
			break;
		case DownLoadManager.STATE_SUCCESS:
			visibledButton();
			btnDownLoad.setText("Install");
			break;
		}
	}

	/** 显示进度条，隐藏按钮 */
	private void visibledProgress() {
		fl.setVisibility(View.VISIBLE);
		btnDownLoad.setVisibility(View.GONE);
	}

	/** 显示按钮，隐藏进度条 */
	private void visibledButton() {
		fl.setVisibility(View.GONE);
		btnDownLoad.setVisibility(View.VISIBLE);
	}

	// //////////////////////////////////////////////////

	/** 运行在主线程中，在主线程更新UI */
	private void refreshUIOnMainThread(final DownLoadInfo downLoadInfo) {
		// 判断下载对象是否是当前应用
		AppInfo appInfo = getData();
		if ((appInfo.id).equals(downLoadInfo.id)) {
			UIUtils.runOnUIThread(new Runnable() {

				@Override
				public void run() {
					refreshUI(downLoadInfo.currentState,
							downLoadInfo.getProgress());
				}
			});
		}
	}

	@Override
	// 主/子线程
	public void onDownLoadStateChanged(DownLoadInfo downLoadInfo) {
		refreshUIOnMainThread(downLoadInfo);
	}

	@Override
	// 子线程
	public void onDownLoadProgressChanged(DownLoadInfo downLoadInfo) {
		refreshUIOnMainThread(downLoadInfo);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
		case R.id.fl_progress:

			if (currentState == DownLoadManager.STATE_UNDO
					|| currentState == DownLoadManager.STATE_ERROR
					|| currentState == DownLoadManager.STATE_PAUSE) {
				dm.download(getData()); // 开始下载

			} else if (currentState == DownLoadManager.STATE_DOWNLOADING
					|| currentState == DownLoadManager.STATE_WAITING) {
				dm.pause(getData()); // 暂停下载

			} else if (currentState == DownLoadManager.STATE_SUCCESS) {
				dm.install(getData()); // 开始安装
			}

			break;
		}
	}

}
