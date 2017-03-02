package com.rixin.appstore.ui.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.rixin.appstore.manager.ThreadManager;
import com.rixin.appstore.ui.holder.BaseHolder;
import com.rixin.appstore.ui.holder.MoreHolder;
import com.rixin.appstore.utils.UIUtils;

/**
 * 对Adapter的第一层封装
 * 
 * @author 飘渺云轩
 * @date 2016-09-19
 */
public abstract class RXBaseAdapter<T> extends BaseAdapter {

	// 注意：此处必须从0开始
	private final static int TYPE_NORMAL = 1; // 普通布局类型
	private final static int TYPE_MORE = 0; // 加载更多布局类型

	private boolean isLoadMore = false; // 是否已经在加载更多数据

	private ArrayList<T> data;

	public RXBaseAdapter(ArrayList<T> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size() + 1; // 增加（加载更多布局）
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 返回布局类型的个数
	@Override
	public int getViewTypeCount() {

		// 返回两种布局，普通布局+加载更多布局
		return 2;
	}

	// 返回当前位置应该展示哪种布局类型
	@Override
	public int getItemViewType(int position) {

		if (position == getCount() - 1) { // list最后一个item
			return TYPE_MORE;
		} else {
			return getInnerType(position);
		}
	}

	/**
	 * 如果有三种布局类型，子类可以重写此方法来更改返回的布局类型
	 * 
	 * @return
	 */
	public int getInnerType(int position) {
		return TYPE_NORMAL; // 默认是普通布局类型
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder;

		if (convertView == null) {

			/** 1.加载布局文件 2.初始化控件findViewById 3.打一个标记tag */
			if (getItemViewType(position) == TYPE_MORE) {

				// 加载更多的类型
				holder = new MoreHolder(hasMore());
			} else {
				holder = getHolder(position);
			}

		} else {
			holder = (BaseHolder) convertView.getTag();
		}

		/** 4.根据数据来刷新界面 */
		if (getItemViewType(position) != TYPE_MORE) {
			holder.setData(getItem(position));
		} else {
			// TODO 加载更多 ，一旦加载更多布局出现就开始加载数据
			MoreHolder moreHolder = (MoreHolder) holder;
			// 不过只有在有更多数据时才进行加载
			if (moreHolder.getData() == MoreHolder.STATE_MORE_MORE) {
				loadMore(moreHolder);
			}
		}

		return holder.getRootView();
	}

	/**
	 * 子类可以重写此方法来决定是否可以加载更多数据
	 * 
	 * @return true: 默认都是有更多数据的
	 */
	public boolean hasMore() {
		return true;
	}

	/**
	 * 加载更多数据
	 */
	public void loadMore(final MoreHolder holder) {
		// 当前没有在加载更多数据时才进行加载
		if (!isLoadMore) {
			isLoadMore = true;
			// 线程池
			ThreadManager.getThreadPool().execute(new Runnable() {

				@Override
				public void run() {
					final ArrayList<T> moreData = onLoadMore();
					// 涉及到刷新界面，所以得在主线程中
					UIUtils.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							if (moreData != null) {
								// 每一页有20条数据，如果返回小于20，则表示最后一页了
								if (moreData.size() < 20) {
									holder.setData(MoreHolder.STATE_MORE_NONE);
									Toast.makeText(UIUtils.getContext(),
											"没有更多数据了", Toast.LENGTH_SHORT)
											.show();
								} else {
									holder.setData(MoreHolder.STATE_MORE_MORE);
								}

								// 将数据追加到集合中
								data.addAll(moreData);
								// 刷新
								RXBaseAdapter.this.notifyDataSetChanged();

							} else {
								// 加载更多数据失败
								holder.setData(MoreHolder.STATE_MORE_ERROR);
							}
							isLoadMore = false;
						}
					});
				};
			});
		}
	}

	/**
	 * 返回集合的大小
	 * 
	 * @return
	 */
	public int getListSize() {
		return data.size();
	}

	/**
	 * 加载更多数据，必须由子类进行实现
	 * 
	 * @return
	 */
	public abstract ArrayList<T> onLoadMore();

	/**
	 * @return 当前页面的holder对象，由子类进行实现
	 */
	public abstract BaseHolder<T> getHolder(int position);

}
