package com.rixin.appstore.ui.holder;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.domain.AppInfo.SafeInfo;
import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.utils.BitmapHelper;
import com.rixin.appstore.utils.UIUtils;

/**
 * 应用详情页 - 安全模块
 * 
 * @author 飘渺云轩
 * @date 2016-10-06
 */
public class DetailsSafeHolder extends BaseHolder<AppInfo> {

	private ImageView[] safeIcons; // 安全标识图片
	private ImageView[] desIcons; // 安全描述图片
	private TextView[] safeDes; // 安全描述文字
	private ImageView ivArrow;
	private LinearLayout[] safeBars; // 安全描述条目
	private BitmapUtils bitmap;
	private RelativeLayout rlDes;
	private LinearLayout llDes;
	private LinearLayout.LayoutParams params;

	private int desHeight;
	private boolean isOpen = false; // 标记安全描述开关状态

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_details_safeinfo);

		safeIcons = new ImageView[4];
		safeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
		safeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
		safeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
		safeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);

		desIcons = new ImageView[4];
		desIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
		desIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
		desIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
		desIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);

		safeDes = new TextView[4];
		safeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
		safeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
		safeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
		safeDes[3] = (TextView) view.findViewById(R.id.tv_des4);

		safeBars = new LinearLayout[4];
		safeBars[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
		safeBars[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
		safeBars[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
		safeBars[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

		bitmap = BitmapHelper.getBitmapUtils();

		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		rlDes = (RelativeLayout) view.findViewById(R.id.rl_des_root);
		llDes = (LinearLayout) view.findViewById(R.id.ll_des_root);

		rlDes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}

		});

		return view;
	}

	// 打开或关闭安全描述信息模块
	private void toggle() {
		// 属性动画
		ValueAnimator animator = null;
		if (isOpen) {
			isOpen = false;
			animator = ValueAnimator.ofInt(desHeight, 0);
		} else {
			isOpen = true;
			animator = ValueAnimator.ofInt(0, desHeight);
		}

		// 动画更新的监听
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// 获取最新的高度值
				Integer height = (Integer) animation.getAnimatedValue();

				// 重新修改布局高度
				params.height = height;
				llDes.setLayoutParams(params);
			}
		});
		
		animator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				//动画结束的事件
				if(isOpen){
					ivArrow.setImageResource(R.drawable.arrow_up);
				}else{
					ivArrow.setImageResource(R.drawable.arrow_down);
				}
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		animator.setDuration(500);
		animator.start();
	}

	@Override
	public void refreshView(AppInfo data) {
		ArrayList<SafeInfo> safe = data.safe;
		for (int i = 0; i < 4; i++) {
			if (i < safe.size()) {
				SafeInfo info = safe.get(i);

				bitmap.display(safeIcons[i], HttpHelper.URL + "image?name="
						+ info.safeUrl);
				bitmap.display(desIcons[i], HttpHelper.URL + "image?name="
						+ info.safeDesUrl);
				safeDes[i].setText(info.safeDes);
			} else {
				// 剩下的ImageView隐藏
				safeIcons[i].setVisibility(View.GONE);
				safeBars[i].setVisibility(View.GONE);
			}
		}

		// 获取安全描述的完整高度
		llDes.measure(0, 0);
		desHeight = llDes.getMeasuredHeight();

		// 修改安全描述布局高度为0，达到隐藏效果
		params = (LinearLayout.LayoutParams) llDes
				.getLayoutParams();
		params.height = 0;
		llDes.setLayoutParams(params);

	}

}
