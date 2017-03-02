package com.rixin.appstore.ui.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.http.protocol.HomeProtocol;
import com.rixin.appstore.ui.activity.DetailsActivity;
import com.rixin.appstore.ui.adapter.RXBaseAdapter;
import com.rixin.appstore.ui.holder.BaseHolder;
import com.rixin.appstore.ui.holder.HomeHeadHolder;
import com.rixin.appstore.ui.holder.HomeHolder;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.ui.view.MyListView;
import com.rixin.appstore.utils.UIUtils;

/**
 * 首页Fragment
 * 
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class HomeFragment extends BaseFragment {

	private HomeProtocol protocol;
	private ArrayList<AppInfo> data;
	private ArrayList<String> dataPic;

	// 只有加载数据成功，才回调此方法,在主线程运行
	@Override
	public View onCreateSuccessPage() {
		MyListView listView = new MyListView(UIUtils.getContext());
		HomeAdapter adapter = new HomeAdapter(data);

		// 给listView增加头布局展示轮播条
		HomeHeadHolder header = new HomeHeadHolder();
		listView.addHeaderView(header.getRootView());
		if (dataPic != null) {
			// 设置轮播条数据
			header.setData(dataPic);
		}

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo info = data.get(position - 1);
				if (info != null) {
					Intent intent = new Intent(UIUtils.getContext(),
							DetailsActivity.class);
					intent.putExtra("packageName", info.packageName);
					startActivity(intent);
				}
			}
		});

		return listView;
	}

	// 运行在子线程，可以直接执行耗时网络操作
	@Override
	public ResultState onLoadData() {

		protocol = new HomeProtocol();
		data = protocol.getData(0);
		dataPic = protocol.getPictures();

		return protocol.check(data);
	}

	class HomeAdapter extends RXBaseAdapter<AppInfo> {

		public HomeAdapter(ArrayList<AppInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder<AppInfo> getHolder(int position) {
			return new HomeHolder();
		}

		@Override
		public ArrayList<AppInfo> onLoadMore() {
			// 第二页的开始数据 = 集合的大小
			ArrayList<AppInfo> moreData = protocol.getData(getListSize());
			SystemClock.sleep(1000);

			return moreData;
		}

	}

}
