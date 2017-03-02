package com.rixin.appstore.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.domain.DownLoadInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.manager.DownLoadManager;
import com.rixin.appstore.manager.DownLoadManager.DownLoadObserver;
import com.rixin.appstore.ui.view.ProgressArc;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 游戏的Holder
 * 
 * @author 飘渺云轩
 * @date 2016-10-01
 */
public class GameHolder extends BaseHolder<AppInfo> implements
		DownLoadObserver, OnClickListener {

	private TextView tvName, tvSize, tvDes;
	private ImageView ivIvon;
	private RatingBar ratingBar;
	private BitmapUtils bitmapUtils;
	private FrameLayout fl;
	private TextView tvProgress;
	private ProgressArc progress;
	private DownLoadManager dm;

	private int currentState;
	private float currentPos;

	@Override
	public View initView() {
		// 1. 加载布局文件
		View view = UIUtils.inflate(R.layout.list_item_home);
		// 2. 初始化控件
		tvName = (TextView) view.findViewById(R.id.tv_name);
		tvSize = (TextView) view.findViewById(R.id.tv_size);
		tvDes = (TextView) view.findViewById(R.id.tv_des);
		tvProgress = (TextView) view.findViewById(R.id.tv_list_progress);
		ivIvon = (ImageView) view.findViewById(R.id.iv_icon);
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

		fl = (FrameLayout) view.findViewById(R.id.fl_list_download);
		fl.setOnClickListener(this);

		// 初始化进度条
		progress = new ProgressArc(UIUtils.getContext());
		// 设置直径
		progress.setArcDiameter(UIUtils.dipTopx(28));
		// 设置颜色
		progress.setProgressColor(UIUtils.getColor(R.color.progress));
		// 设置宽高
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				UIUtils.dipTopx(29), UIUtils.dipTopx(29));
		fl.addView(progress, params);

		// 注册观察者
		dm = DownLoadManager.getInstance();
		dm.registerObserver(this);

		bitmapUtils = BitmapHelper.getBitmapUtils();

		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		tvName.setText(data.name);
		tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
		tvDes.setText(data.des);
		ratingBar.setRating(data.stars);
		bitmapUtils.display(ivIvon, HttpHelper.URL + "image?name="
				+ data.iconUrl);

		DownLoadInfo downLoadInfo = dm.getDownLoadInfo(data);
		if (downLoadInfo != null) {
			currentState = downLoadInfo.currentState;
			currentPos = downLoadInfo.getProgress();
		} else {
			currentState = DownLoadManager.STATE_UNDO;
			currentPos = 0;
		}

		refreshUI(currentState, currentPos, data.id);

	}

	private void refreshUI(int currentState, float currentPos, String id) {
		// 如果不是当前应用，则直接返回
		if (!getData().id.equals(id)) {
			return;
		}

		this.currentState = currentState;
		this.currentPos = currentPos;

		switch (currentState) {
		case DownLoadManager.STATE_UNDO:
			progress.setBackgroundResource(R.drawable.ic_download);
			progress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvProgress.setText("下载");
			break;
		case DownLoadManager.STATE_WAITING:
			progress.setBackgroundResource(R.drawable.ic_download);
			progress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
			tvProgress.setText("等待下载");
			break;
		case DownLoadManager.STATE_DOWNLOADING:
			progress.setBackgroundResource(R.drawable.ic_pause);
			progress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
			progress.setProgress(currentPos, true);
			tvProgress.setText((int) (currentPos * 100) + "%");
			break;
		case DownLoadManager.STATE_PAUSE:
			progress.setBackgroundResource(R.drawable.ic_resume);
			progress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvProgress.setText("继续下载");
			break;
		case DownLoadManager.STATE_ERROR:
			progress.setBackgroundResource(R.drawable.ic_redownload);
			progress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvProgress.setText("下载失败");
			break;
		case DownLoadManager.STATE_SUCCESS:
			progress.setBackgroundResource(R.drawable.ic_install);
			progress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvProgress.setText("安装");
			break;
		}
	}

	private void refreshUIOnMainThread(final DownLoadInfo downLoadInfo) {
		AppInfo info = getData();
		if (info.id.equals(downLoadInfo.id)) {
			UIUtils.runOnUIThread(new Runnable() {

				@Override
				public void run() {
					refreshUI(downLoadInfo.currentState,
							downLoadInfo.getProgress(), downLoadInfo.id);
				}
			});
		}
	}

	@Override
	public void onDownLoadStateChanged(DownLoadInfo downLoadInfo) {
		refreshUIOnMainThread(downLoadInfo);
	}

	@Override
	public void onDownLoadProgressChanged(DownLoadInfo downLoadInfo) {
		refreshUIOnMainThread(downLoadInfo);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_list_download:

			if (currentState == DownLoadManager.STATE_UNDO
					|| currentState == DownLoadManager.STATE_PAUSE
					|| currentState == DownLoadManager.STATE_ERROR) {
				dm.download(getData()); // 开始下载

			} else if (currentState == DownLoadManager.STATE_WAITING
					|| currentState == DownLoadManager.STATE_DOWNLOADING) {
				dm.pause(getData()); // 暂停下载

			} else if (currentState == DownLoadManager.STATE_ERROR) {
				dm.install(getData()); // 开始安装
			}

			break;
		}
	}

}
