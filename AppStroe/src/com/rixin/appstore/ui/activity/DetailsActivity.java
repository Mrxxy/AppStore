package com.rixin.appstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.http.protocol.DetailsProtocol;
import com.rixin.appstore.ui.holder.DetailsAppInfoHolder;
import com.rixin.appstore.ui.holder.DetailsDesHolder;
import com.rixin.appstore.ui.holder.DetailsDownLoadHolder;
import com.rixin.appstore.ui.holder.DetailsPicsHolder;
import com.rixin.appstore.ui.holder.DetailsSafeHolder;
import com.rixin.appstore.ui.view.LoadingPage;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.utils.UIUtils;

/**
 * 应用详情页
 * 
 * @author 飘渺云轩
 * @date 2016-10-05
 */
public class DetailsActivity extends BaseActivity {

	private LoadingPage loadingPage;
	private String packageName;
	private AppInfo data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		packageName = intent.getStringExtra("packageName");

		loadingPage = new LoadingPage(this) {
			@Override
			public ResultState onLoadData() {
				return DetailsActivity.this.onLoadData();
			}

			@Override
			public View onCreateSuccessPage() {
				return DetailsActivity.this.onCreateSuccessPage();
			}
		};

		setContentView(loadingPage);

		// 加载网络数据
		loadingPage.loadData();

		// 初始化ActionBar
		initActionBar();
	}

	public ResultState onLoadData() {
		DetailsProtocol protocol = new DetailsProtocol(packageName);
		data = protocol.getData(0);
		if (data != null) {
			return ResultState.STATE_SUCCESS;
		}
		return ResultState.STATE_ERROR;
	}

	public View onCreateSuccessPage() {
		View view = UIUtils.inflate(R.layout.page_details);

		// ///////// 初始化应用信息模块 ///////////////
		FrameLayout flAppInfo = (FrameLayout) view
				.findViewById(R.id.fl_appinfo);
		// 动态给帧布局填充模块
		DetailsAppInfoHolder appInfoHolder = new DetailsAppInfoHolder();
		flAppInfo.addView(appInfoHolder.getRootView());
		appInfoHolder.setData(data);

		// ////////// 初始化安全模块 //////////////////
		FrameLayout flSafeInfo = (FrameLayout) view
				.findViewById(R.id.fl_safeinfo);
		// 动态给帧布局填充模块
		DetailsSafeHolder safeHolder = new DetailsSafeHolder();
		flSafeInfo.addView(safeHolder.getRootView());
		safeHolder.setData(data);

		// ////////// 初始化截图模块 ///////////////////
		HorizontalScrollView hsPics = (HorizontalScrollView) view
				.findViewById(R.id.hs_pics);
		// 动态给帧布局填充模块
		DetailsPicsHolder picsHolder = new DetailsPicsHolder();
		hsPics.addView(picsHolder.getRootView());
		picsHolder.setData(data);

		// ////////// 初始化简介模块 /////////////////
		FrameLayout flDesInfo = (FrameLayout) view
				.findViewById(R.id.fl_desinfo);
		// 动态给帧布局填充模块
		DetailsDesHolder desHolder = new DetailsDesHolder();
		flDesInfo.addView(desHolder.getRootView());
		desHolder.setData(data);

		// ////////// 初始化下载模块 /////////////////
		FrameLayout flDownLoad = (FrameLayout) view
				.findViewById(R.id.fl_download);
		// 动态给帧布局填充模块
		DetailsDownLoadHolder downloadHolder = new DetailsDownLoadHolder();
		flDownLoad.addView(downloadHolder.getRootView());
		downloadHolder.setData(data);
		
		return view;
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); // 设置返回按钮显示
		actionBar.setHomeButtonEnabled(true); // 设置home处可点击
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
