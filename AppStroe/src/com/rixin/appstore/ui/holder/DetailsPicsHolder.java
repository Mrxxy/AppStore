package com.rixin.appstore.ui.holder;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 应用详情页 - 截图模块
 * @author 飘渺云轩
 * @date 2016-10-06
 */
public class DetailsPicsHolder extends BaseHolder<AppInfo> {

	private ImageView[] pics;
	private BitmapUtils bitmap;
	
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_details_picinfo);
		
		pics = new ImageView[5];
		pics[0] = (ImageView) view.findViewById(R.id.iv_pic1);
		pics[1] = (ImageView) view.findViewById(R.id.iv_pic2);
		pics[2] = (ImageView) view.findViewById(R.id.iv_pic3);
		pics[3] = (ImageView) view.findViewById(R.id.iv_pic4);
		pics[4] = (ImageView) view.findViewById(R.id.iv_pic5);
		
		bitmap = BitmapHelper.getBitmapUtils();
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		ArrayList<String> screenInfo = data.screen;
		for (int i = 0; i < 5; i++) {
			if(i < screenInfo.size()){
				String img = screenInfo.get(i);
				bitmap.display(pics[i], HttpHelper.URL + "image?name=" + img);
			}else{
				pics[i].setVisibility(View.GONE);
			}
		}
	}

}
