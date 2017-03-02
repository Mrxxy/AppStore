package com.rixin.appstore.ui.fragment;

import java.util.ArrayList;

import android.os.SystemClock;
import android.view.View;

import com.rixin.appstore.domain.SubjectInfo;
import com.rixin.appstore.http.protocol.SubjectProtocol;
import com.rixin.appstore.ui.adapter.RXBaseAdapter;
import com.rixin.appstore.ui.holder.BaseHolder;
import com.rixin.appstore.ui.holder.SubjectHolder;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.ui.view.MyListView;
import com.rixin.appstore.utils.UIUtils;

/**
 * 专题Fragment
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class SubjectFragment extends BaseFragment {
	
	private ArrayList<SubjectInfo> data;
	private SubjectProtocol protocol;

	@Override
	public View onCreateSuccessPage() {
		MyListView listView = new MyListView(UIUtils.getContext());
		SubjectAdapter adapter = new SubjectAdapter(data);
		listView.setAdapter(adapter);
		return listView;
	}

	@Override
	public ResultState onLoadData() {
		protocol = new SubjectProtocol();
		data = protocol.getData(0);
		
		return protocol.check(data);
	}
	
	class SubjectAdapter extends RXBaseAdapter<SubjectInfo> {

		public SubjectAdapter(ArrayList<SubjectInfo> data) {
			super(data);
		}

		@Override
		public ArrayList<SubjectInfo> onLoadMore() {
			ArrayList<SubjectInfo> moreData = protocol.getData(getListSize());
			SystemClock.sleep(1000);
			return moreData;
		}

		@Override
		public BaseHolder<SubjectInfo> getHolder(int position) {
			return new SubjectHolder();
		}
		
	}

}
