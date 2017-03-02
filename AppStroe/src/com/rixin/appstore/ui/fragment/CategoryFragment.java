package com.rixin.appstore.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.rixin.appstore.domain.CategoryInfo;
import com.rixin.appstore.http.protocol.CategoryProtocol;
import com.rixin.appstore.ui.adapter.RXBaseAdapter;
import com.rixin.appstore.ui.holder.BaseHolder;
import com.rixin.appstore.ui.holder.CategoryInfosHolder;
import com.rixin.appstore.ui.holder.CategoryTitleHolder;
import com.rixin.appstore.ui.view.MyListView;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.utils.UIUtils;

/**
 * 分类Fragment
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class CategoryFragment extends BaseFragment {

	private ArrayList<CategoryInfo> data;
	private CategoryProtocol protocol;

	@Override
	public View onCreateSuccessPage() {
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new CategoryAdapter(data));
		return listView;
	}

	@Override
	public ResultState onLoadData() {
		protocol = new CategoryProtocol();
		data = protocol.getData(0);
		return protocol.check(data);
	}
	
	class CategoryAdapter extends RXBaseAdapter<CategoryInfo> {

		public CategoryAdapter(ArrayList<CategoryInfo> data) {
			super(data);
		}
		
		@Override
		public boolean hasMore() {
			return false;  //没有加载更多数据
		}
		
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;  //在原来的基础上增加一个布局
		}
		
		@Override
		public int getInnerType(int position) {
			//判断是标题类型还是普通分类类型
			CategoryInfo info = data.get(position);
			if(info.isTitle){
				//返回标题类型
				return super.getInnerType(position) + 1;
			}else{
				//返回普通类型
				return super.getInnerType(position);
			}
		}

		@Override
		public ArrayList<CategoryInfo> onLoadMore() {
			return null;
		}

		@Override
		public BaseHolder<CategoryInfo> getHolder(int position) {
			//判断是标题类型还是普通分类类型，返回对应的holder
			CategoryInfo info = data.get(position);
			if(info.isTitle){
				return new CategoryTitleHolder();
			}else{
				return new CategoryInfosHolder();
			}
		}
		
	}

}
