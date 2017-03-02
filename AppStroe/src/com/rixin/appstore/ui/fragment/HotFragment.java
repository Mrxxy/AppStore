package com.rixin.appstore.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rixin.appstore.http.protocol.HotProtocol;
import com.rixin.appstore.ui.view.FlowLayout;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.utils.DrawableUtils;
import com.rixin.appstore.utils.UIUtils;

/**
 * 排行Fragment
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class HotFragment extends BaseFragment {

	private ArrayList<String> data;
	private HotProtocol protocol;

	@Override
	public View onCreateSuccessPage() {
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		FlowLayout flow = new FlowLayout(UIUtils.getContext());
		
		int padding = UIUtils.dipTopx(10);
		flow.setPadding(padding, padding, padding, padding);
		flow.setHorizontalSpacing(UIUtils.dipTopx(6));  //水平间距
		flow.setVerticalSpacing(UIUtils.dipTopx(8)); //竖直间距
		
		for (int i = 0; i < data.size(); i++) {
			TextView text = new TextView(UIUtils.getContext());
			final String keyword = data.get(i);
			text.setText(keyword);
			text.setTextColor(Color.WHITE);
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			text.setPadding(padding, padding, padding, padding);
			text.setGravity(Gravity.CENTER);
			
			Random random = new Random();
			int red = 30 + random.nextInt(200);
			int green = 30 + random.nextInt(200);
			int blue = 30 + random.nextInt(200);
			int pressedColor = 0xffcecece;  //按下时偏白的背景色
			
			//封装前
//			GradientDrawable bgNormal = DrawableUtils.getGradientDrawable(Color.rgb(red, green, blue), UIUtils.dipTopx(6));
//			GradientDrawable bgPressed = DrawableUtils.getGradientDrawable(color, UIUtils.dipTopx(6));
//			StateListDrawable selector = DrawableUtils.getSelector(bgNormal, bgPressed);
			
			//封装后
			StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(red, green, blue), pressedColor, UIUtils.dipTopx(6));
			
			text.setBackgroundDrawable(selector);
			text.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
				}
			});
			
			flow.addView(text);
		}
		
		scrollView.addView(flow);
		return scrollView;
	}

	@Override
	public ResultState onLoadData() {
		protocol = new HotProtocol();
		data = protocol.getData(0);
		return protocol.check(data);
	}

}
