package com.rixin.appstore.ui.holder;

import android.view.View;

/**
 * 对adapter的第二层封装
 * 
 * @author 飘渺云轩
 * @date 2016-09-19
 */
public abstract class BaseHolder<T> {
	
	private View rootView; //一个item的根布局
	private T data;

	/**
	 * 当new这个对象时，就会加载布局，初始化控件，设置tag
	 */
	public BaseHolder() {
		
		rootView = initView();
		
		//3. 打一个标记
		rootView.setTag(this);
	}
	
	/**
	 * 1. 加载布局文件
	 * 2. 初始化控件findViewById
	 * @return
	 */
	public abstract View initView();
	
	/**
	 * 4. 刷新数据
	 */
	public abstract void refreshView(T data);
	
	/**
	 * @return item的布局对象
	 */
	public View getRootView(){
		return rootView;
	}
	
	/**
	 * 设置当前item的数据
	 * @param data
	 */
	public void setData(T data){
		this.data = data;
		
		refreshView(data);
	}
	
	/**
	 * 获取当前item的数据
	 * @return
	 */
	public T getData(){
		return data;
	}
	
}
