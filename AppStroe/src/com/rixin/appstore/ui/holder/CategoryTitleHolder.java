package com.rixin.appstore.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.rixin.appstore.R;
import com.rixin.appstore.domain.CategoryInfo;
import com.rixin.appstore.utils.UIUtils;

/**
 * 分类页面的标题holder
 * 
 * @author 飘渺云轩
 * @date 2016-10-05
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {

	private TextView tvTitle;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_category_title);
		tvTitle = (TextView) view.findViewById(R.id.tv_category_title);
		return view;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		tvTitle.setText(data.title);
	}

}
