package com.rixin.appstore.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.domain.CategoryInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 分类页面的分类holder
 * 
 * @author 飘渺云轩
 * @date 2016-10-05
 */
public class CategoryInfosHolder extends BaseHolder<CategoryInfo> {

	private TextView tvName1, tvName2, tvName3;
	private ImageView ivIcon1, ivIcon2, ivIcon3;
	private LinearLayout llGrid1, llGrid2, llGrid3;
	private BitmapUtils bitmap;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_category_info);
		tvName1 = (TextView) view.findViewById(R.id.tv_category_name1);
		tvName2 = (TextView) view.findViewById(R.id.tv_category_name2);
		tvName3 = (TextView) view.findViewById(R.id.tv_category_name3);
		
		bitmap = BitmapHelper.getBitmapUtils();
		ivIcon1 = (ImageView) view.findViewById(R.id.iv_category_icon1);
		ivIcon2 = (ImageView) view.findViewById(R.id.iv_category_icon2);
		ivIcon3 = (ImageView) view.findViewById(R.id.iv_category_icon3);
		
		llGrid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
		llGrid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);
		llGrid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);
		return view;
	}

	@Override
	public void refreshView(final CategoryInfo data) {
		tvName1.setText(data.name1);
		tvName2.setText(data.name2);
		tvName3.setText(data.name3);
		
		bitmap.display(ivIcon1, HttpHelper.URL + "image?name=" + data.url1);
		bitmap.display(ivIcon2, HttpHelper.URL + "image?name=" + data.url2);
		bitmap.display(ivIcon3, HttpHelper.URL + "image?name=" + data.url3);
		
		llGrid1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(UIUtils.getContext(), data.name1, Toast.LENGTH_SHORT).show();
			}
		});
		llGrid2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(UIUtils.getContext(), data.name2, Toast.LENGTH_SHORT).show();
			}
		});
		llGrid3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(UIUtils.getContext(), data.name3, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
