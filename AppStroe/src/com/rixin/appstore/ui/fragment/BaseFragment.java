package com.rixin.appstore.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.appstore.ui.view.LoadingPage;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.utils.UIUtils;

/**
 * 基类
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public abstract class BaseFragment extends Fragment {

	private LoadingPage loadingPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		loadingPage = new LoadingPage(UIUtils.getContext()){

			@Override
			public View onCreateSuccessPage() {
				//注意：此处一定要写BaseFragment的onCreateSuccessPage方法，否则会栈溢出
				return BaseFragment.this.onCreateSuccessPage();
			}

			@Override
			public ResultState onLoadData() {
				return BaseFragment.this.onLoadData();
			}
			
		};
		return loadingPage;
	}
	
	/**
	 * 开始加载数据
	 */
	public void loadData(){
		if(loadingPage != null){
			loadingPage.loadData();
		}
	}
	
	/**
	 * 加载成功的布局，必须由子类实现
	 * @return
	 */
	public abstract View onCreateSuccessPage();
	
	/**
	 * 加载网络数据，必须由子类实现
	 * @return
	 */
	public abstract ResultState onLoadData();
	
}
