package com.rixin.appstore.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 应用详情页 - 应用信息模块
 * 
 * @author 飘渺云轩
 * @date 2016-10-06
 */
public class DetailsAppInfoHolder extends BaseHolder<AppInfo> {

	private ImageView ivIcon;
	private TextView tvName, tvDownLoad, tvDate, tvVersion, tvSize;
	private RatingBar ratingBar;
	private BitmapUtils bitmap;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_details_appinfo);
		ivIcon = (ImageView) view.findViewById(R.id.iv_details_icon);
		tvName = (TextView) view.findViewById(R.id.tv_details_name);
		tvDownLoad = (TextView) view.findViewById(R.id.tv_details_downloadNum);
		tvDate = (TextView) view.findViewById(R.id.tv_details_date);
		tvVersion = (TextView) view.findViewById(R.id.tv_details_version);
		tvSize = (TextView) view.findViewById(R.id.tv_details_size);
		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_details);
		
		bitmap = BitmapHelper.getBitmapUtils();
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		tvName.setText(data.name);
		tvDownLoad.setText("下载量：" + data.downloadNum);
		tvDate.setText(data.date);
		tvVersion.setText("版本：" + data.version);
		tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
		ratingBar.setRating(data.stars);
		bitmap.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
	}

}
