package com.rixin.appstore.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 封装ListView
 * 
 * @author 飘渺云轩
 * @date 2016-10-01
 */
public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyListView(Context context) {
		this(context, null);
	}
	
	public void init(){
		
		this.setSelector(new ColorDrawable());  //设置listView的背景选择器为透明
		this.setDivider(null);   //去掉listView的分隔线
		//有时候滑动listView背景会变成黑色，设置背景为透明
		this.setCacheColorHint(Color.TRANSPARENT);
		
	}

}
