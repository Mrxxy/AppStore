package com.rixin.appstore.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.domain.SubjectInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 专题的Holder
 * 
 * @author 飘渺云轩
 * @date 2016-10-02
 */
public class SubjectHolder extends BaseHolder<SubjectInfo> {

	private ImageView ivSubject;
	private TextView tvDes;
	private BitmapUtils bitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_subject);
		ivSubject = (ImageView) view.findViewById(R.id.iv_subject);
		tvDes = (TextView) view.findViewById(R.id.tv_subject_des);
		
		bitmapUtils = BitmapHelper.getBitmapUtils();
		
		return view;
	}

	@Override
	public void refreshView(SubjectInfo data) {
		tvDes.setText(data.des);
		bitmapUtils.display(ivSubject, HttpHelper.URL + "image?name=" + data.url);
	}

}
