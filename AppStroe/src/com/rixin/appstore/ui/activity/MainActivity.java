package com.rixin.appstore.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.rixin.appstore.R;
import com.rixin.appstore.ui.fragment.BaseFragment;
import com.rixin.appstore.ui.fragment.FragmentFactory;
import com.rixin.appstore.ui.view.PagerTab;
import com.rixin.appstore.utils.UIUtils;

/**
 * 当项目和appcompat关联在一起时，就必须在清单文件中设置Theme.AppCompat的主题，否则会崩溃 Theme.AppCompat.Light
 * 
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class MainActivity extends BaseActivity {

	private PagerTab pagerTab;
	private ViewPager viewpager;
	private RXAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pagerTab = (PagerTab) this.findViewById(R.id.pagertab);
		viewpager = (ViewPager) this.findViewById(R.id.viewpager);

		adapter = new RXAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);

		pagerTab.setViewPager(viewpager); // 将指针和viewpager绑定在一起
		
		pagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BaseFragment fragment = FragmentFactory.createFragment(position);
				
				//开始加载数据
				fragment.loadData();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
	}

	/**
	 * FragmentPagerAdapter是PagerAdapter的子类，如果ViewPager的页面是Fragment就继承此类
	 */
	class RXAdapter extends FragmentPagerAdapter {

		private String[] tabNames;

		public RXAdapter(FragmentManager fm) {
			super(fm);
			// 加载页面标题数组
			tabNames = UIUtils.getStringArray(R.array.tab_names);
		}

		// 返回当前页面位置的fragment对象
		@Override
		public Fragment getItem(int position) {
			BaseFragment fragment = FragmentFactory.createFragment(position);
			return fragment;
		}

		// fragment的数量
		@Override
		public int getCount() {
			return tabNames.length;
		}

		// 返回页签的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return tabNames[position];
		}

	}

}
