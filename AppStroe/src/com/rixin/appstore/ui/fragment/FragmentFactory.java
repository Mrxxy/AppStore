package com.rixin.appstore.ui.fragment;

import java.util.HashMap;

/**
 * 生产Fragment的工厂
 * 
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class FragmentFactory {

	private static HashMap<Integer, BaseFragment> fragmentMap = new HashMap<Integer, BaseFragment>();

	public static BaseFragment createFragment(int position) {

		//先从集合中取，如果没有才创建对象
		BaseFragment fragment = fragmentMap.get(position);

		if (fragment == null) {
			switch (position) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new AppFragment();
				break;
			case 2:
				fragment = new GameFragment();
				break;
			case 3:
				fragment = new SubjectFragment();
				break;
			case 4:
				fragment = new RecommendFragment();
				break;
			case 5:
				fragment = new CategoryFragment();
				break;
			case 6:
				fragment = new HotFragment();
				break;
			}
			fragmentMap.put(position, fragment); // 将fragment保存到集合中
		}
		return fragment;
	}

}
