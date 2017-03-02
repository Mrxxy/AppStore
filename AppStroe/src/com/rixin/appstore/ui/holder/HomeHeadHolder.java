package com.rixin.appstore.ui.holder;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 首页轮播条的holder
 * 
 * @author 飘渺云轩
 * @date 2016-10-05
 */
public class HomeHeadHolder extends BaseHolder<ArrayList<String>> {

	private ViewPager pager;
	private LinearLayout ll;
	private ImageView point;

	private ArrayList<String> data;
	private int previousPos; //上一个指示器点位置
	
	@Override
	public View initView() {
		// 创建根布局
		RelativeLayout rlRoot = new RelativeLayout(UIUtils.getContext());
		// 初始化布局参数，根布局上层控件是listView，所以要使用listView定义的LayoutParams
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dipTopx(140));
		rlRoot.setLayoutParams(params);

		// ViewPager
		pager = new ViewPager(UIUtils.getContext());
		int padding = UIUtils.dipTopx(3);
		pager.setPadding(padding, padding, padding, padding);
		RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		rlRoot.addView(pager, vpParams);

		ll = new LinearLayout(UIUtils.getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		padding = UIUtils.dipTopx(10);
		ll.setPadding(padding, padding, padding, padding);
		RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// 添加规则，设置展示位置
		rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		rlRoot.addView(ll, rlParams);

		return rlRoot;
	}

	@Override
	public void refreshView(final ArrayList<String> data) {
		this.data = data;
		pager.setAdapter(new HomeHeadAdapter());
		pager.setCurrentItem(data.size() * 1000);

		// 初始化指示器
		LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < data.size(); i++) {
			point = new ImageView(UIUtils.getContext());
			if (i == 0) {
				point.setImageResource(R.drawable.indicator_selected);
			} else {
				point.setImageResource(R.drawable.indicator_normal);
				llparams.leftMargin = UIUtils.dipTopx(2);
			}
			point.setLayoutParams(llparams);
			ll.addView(point);
		}
		
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				position = position % data.size();
				
				ImageView point = (ImageView) ll.getChildAt(position);
				point.setImageResource(R.drawable.indicator_selected);
				
				//上一个点变为不选中
				ImageView prePoint = (ImageView) ll.getChildAt(previousPos);
				prePoint.setImageResource(R.drawable.indicator_normal);
				
				previousPos = position;
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		HomeHeadTask task = new HomeHeadTask();
		task.start();
	}

	class HomeHeadTask implements Runnable {

		public void start() {
			// 移除所有消息
			UIUtils.getHandler().removeCallbacksAndMessages(null);

			UIUtils.getHandler().postDelayed(this, 3000);
		}

		@Override
		public void run() {
			int currentItem = pager.getCurrentItem();
			currentItem++;
			pager.setCurrentItem(currentItem);

			// 继续发延时消息，实现内循环
			UIUtils.getHandler().postDelayed(this, 3000);
		}

	}

	class HomeHeadAdapter extends PagerAdapter {

		private BitmapUtils bitmap;

		private HomeHeadAdapter() {
			bitmap = BitmapHelper.getBitmapUtils();
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			position = position % data.size();

			String url = data.get(position);
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setScaleType(ScaleType.FIT_XY);
			bitmap.display(iv, HttpHelper.URL + "image?name=" + url);

			container.addView(iv);

			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
