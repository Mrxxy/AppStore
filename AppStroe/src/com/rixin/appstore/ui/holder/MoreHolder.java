package com.rixin.appstore.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rixin.appstore.R;
import com.rixin.appstore.utils.UIUtils;

/**
 * 加载更多布局
 *
 * @author 飘渺云轩
 * @data 2016-09-20
 */
public class MoreHolder extends BaseHolder<Integer> {
	 
	/** 
	 * 加载更多的几种状态：
	 * 1. 可以加载更多  
	 * 2. 加载更多失败  
	 * 3. 没有更多数据
	 */
	public final static int STATE_MORE_MORE = 1;
	public final static int STATE_MORE_ERROR = 2;
	public final static int STATE_MORE_NONE = 3;
	private LinearLayout llLoadMore;
	private TextView tvNone;
	
	public MoreHolder(boolean hasMore) {
		//如果有更多数据，状态为1，否则为3；将此状态传递给父类的data，同时刷新界面
		setData(hasMore ? STATE_MORE_MORE : STATE_MORE_NONE);  //setData()结束后一定会调用refreshView()
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_more);
		llLoadMore = (LinearLayout) view.findViewById(R.id.ll_load_more);
		tvNone = (TextView) view.findViewById(R.id.tv_load_error);
		return view;
	}

	@Override
	public void refreshView(Integer data) {
		switch (data) {
		case STATE_MORE_MORE:
			//TODO 显示加载更多
			llLoadMore.setVisibility(View.VISIBLE);
			tvNone.setVisibility(View.GONE);
			break;
		case STATE_MORE_ERROR:
			//TODO 显示加载失败
			llLoadMore.setVisibility(View.GONE);
			tvNone.setVisibility(View.VISIBLE);
			break;
		case STATE_MORE_NONE:
			//TODO 隐藏加载更多
			llLoadMore.setVisibility(View.GONE);
			tvNone.setVisibility(View.GONE);
			break;
		}
	}

}
