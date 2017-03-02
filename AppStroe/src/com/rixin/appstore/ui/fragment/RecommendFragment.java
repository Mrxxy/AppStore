package com.rixin.appstore.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rixin.appstore.http.protocol.RecommendProtocol;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.ui.view.fly.ShakeListener;
import com.rixin.appstore.ui.view.fly.ShakeListener.OnShakeListener;
import com.rixin.appstore.ui.view.fly.StellarMap;
import com.rixin.appstore.utils.UIUtils;

/**
 * 推荐Fragment
 * @author 飘渺云轩
 * @date 2016-09-11
 */
public class RecommendFragment extends BaseFragment {

	private ArrayList<String> data;
	private RecommendProtocol protocol;

	@Override
	public View onCreateSuccessPage() {
		final StellarMap stellar = new StellarMap(UIUtils.getContext());
		stellar.setAdapter(new RecommendAdapter());
		
		//随机方式，将控件划分为9行6列，然后在格子中随机显示
		stellar.setRegularity(6, 9);
		
		//设置内边距10dp
		int padding = UIUtils.dipTopx(10);
		stellar.setInnerPadding(padding, padding, padding, padding);
		
		//设置默认页面
		stellar.setGroup(0, true);
		
		ShakeListener shake = new ShakeListener(UIUtils.getContext());
		shake.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake() {
				stellar.zoomIn();  //跳到下一页
			}
		});
		
		return stellar;
	}

	@Override
	public ResultState onLoadData() {
		protocol = new RecommendProtocol();
		data = protocol.getData(0);
		return protocol.check(data);
	}
	
	class RecommendAdapter implements StellarMap.Adapter {

		/** 返回组的个数 */
		@Override
		public int getGroupCount() {
			return 2;
		}

		/** 返回某组item的个数 */
		@Override
		public int getCount(int group) {
			int count = data.size() / getGroupCount();
			//最后一页将除不尽余下来的数量追加在最后一页，保证数据完整性
			if(group == getGroupCount() - 1){
				count += data.size() % getGroupCount();
			}
			return count;
		}

		/** 初始化布局 */
		@Override
		public View getView(int group, int position, View convertView) {
			//因为position每组都会从0开始计数，得加上前面几组的position
			position += group * getCount(group - 1);
			
			TextView text;
			if(convertView == null){
				text = new TextView(UIUtils.getContext());
			}else{
				text = (TextView) convertView;
			}
			final String keyword = data.get(position);
			text.setText(keyword);
			
			Random random = new Random();
			//随机大小 (16-25)
			int size = 16 + random.nextInt(9);
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			
			//随机颜色 rgb(0-255) 颜色不能太深或太暗（30-230）
			int red = 30 + random.nextInt(200);
			int green = 30 + random.nextInt(200);
			int blue = 30 + random.nextInt(200);
			text.setTextColor(Color.rgb(red, green, blue));
			
			text.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
				}
			});
			
			return text;
		}

		/** 返回下一组的id， isZoomIn为true则为下滑，false则为上滑 */
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			if(isZoomIn){
				//往下滑加载上一页
				if(group > 0){
					group--;
				}else{   //跳到最后一页
					group = getGroupCount() - 1;
				}
			}else{
				//往上滑加载下一页
				if(group < getGroupCount() - 1){
					group++;
				}else{  //跳到第一页
					group = 0;
				}
			}
			return group;
		}
		
	}

}
