package com.rixin.appstore.ui.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.http.protocol.AppProtocol;
import com.rixin.appstore.ui.activity.DetailsActivity;
import com.rixin.appstore.ui.adapter.RXBaseAdapter;
import com.rixin.appstore.ui.holder.AppHolder;
import com.rixin.appstore.ui.holder.BaseHolder;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.ui.view.MyListView;
import com.rixin.appstore.utils.UIUtils;

/**
 * 应用Fragment
 * 
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class AppFragment extends BaseFragment {

	private ArrayList<AppInfo> data;
	private AppProtocol protocol;

	@Override
	public View onCreateSuccessPage() {
		MyListView listView = new MyListView(UIUtils.getContext());
		AppAdapter adapter = new AppAdapter(data);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo info = data.get(position);
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

	@Override
	public ResultState onLoadData() {
		protocol = new AppProtocol();
		data = protocol.getData(0);

		return protocol.check(data);
	}

	class AppAdapter extends RXBaseAdapter<AppInfo> {

		public AppAdapter(ArrayList<AppInfo> data) {
			super(data);
		}

		@Override
		public ArrayList<AppInfo> onLoadMore() {

			ArrayList<AppInfo> moreData = protocol.getData(getListSize());
			SystemClock.sleep(1000);
			return moreData;
		}

		@Override
		public BaseHolder<AppInfo> getHolder(int position) {
			return new AppHolder();
		}

	}

}
