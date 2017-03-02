package com.rixin.appstore.ui.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rixin.appstore.R;
import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.utils.UIUtils;

/**
 * 应用详情页 - 简介模块
 * 
 * @author 飘渺云轩
 * @date 2016-10-06
 */
public class DetailsDesHolder extends BaseHolder<AppInfo> {

	private ImageView ivArrow;
	private TextView tvDes;
	private RelativeLayout rlToggle;
	private TextView tvAuthor;

	private boolean isOpen = false; // 标记是否展开全部简介内容

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_details_desinfo);

		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
		rlToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
		tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);

		rlToggle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		return view;
	}

	private void toggle() {
		// 属性动画
		ValueAnimator animator = null;
		if (isOpen) {
			isOpen = false;
			if (getShortHeight() < getLongHeight()) { // 大于3行才初始化动画
				animator = ValueAnimator.ofInt(getLongHeight(),
						getShortHeight());
			}
		} else {
			isOpen = true;
			if (getShortHeight() < getLongHeight()) { // 大于3行才初始化动画
				animator = ValueAnimator.ofInt(getShortHeight(),
						getLongHeight());
			}
		}
		if (animator != null) {
			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// 获取最新高度
					Integer height = (Integer) animation.getAnimatedValue();

					tvDes.setHeight(height);
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

					final ScrollView scrollView = getScrollView();
					//为了更稳定和安全，传到消息队列中执行
					scrollView.post((new Runnable() {

						@Override
						public void run() {
							scrollView.fullScroll(ScrollView.FOCUS_DOWN); //滑动到底部
						}
					}));

					if (isOpen) {
						ivArrow.setImageResource(R.drawable.arrow_up);
					} else {
						ivArrow.setImageResource(R.drawable.arrow_down);
					}
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			});
			animator.setDuration(200);
			animator.start();
		}
	}

	@Override
	public void refreshView(AppInfo data) {
		tvDes.setText(data.des);
		tvAuthor.setText(data.author);

		// 设置简介内容显示3行
		tvDes.post(new Runnable() {
			//放在消息队列中执行，解决当内容小于3行时textView还是显示3行的高度
			@Override
			public void run() {
				tvDes.setHeight(getShortHeight());
			}
		});
	}

	/**
	 * 获取3行textView内容的高度
	 * 
	 * @return
	 */
	private int getShortHeight() {
		// 模拟一个textView，设置最大行为3行，计算该虚拟textview的高度，从而得知tvDes在展示7行时的高度
		int width = tvDes.getMeasuredWidth();

		TextView tv = new TextView(UIUtils.getContext());
		tv.setText(getData().des);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		int padding = UIUtils.dipTopx(5);
		tv.setPadding(padding, padding, padding, padding);
		tv.setMaxLines(3); // 最大行数为7行

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY); // match_parent
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST); // wrap_content
										// ，当包裹内容时，参数1表示尺寸为最大值，暂写为2000
		// 开始测量
		tv.measure(widthMeasureSpec, heightMeasureSpec);

		return tv.getMeasuredHeight();
	}

	/**
	 * 获取完整textView内容的高度
	 * 
	 * @return
	 */
	private int getLongHeight() {
		// 模拟一个textView，计算该虚拟textview的高度，从而得知tvDes的高度
		int width = tvDes.getMeasuredWidth();

		TextView tv = new TextView(UIUtils.getContext());
		tv.setText(getData().des);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		int padding = UIUtils.dipTopx(5);
		tv.setPadding(padding, padding, padding, padding);

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY); // match_parent
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000,
				MeasureSpec.AT_MOST); // wrap_content
										// ，当包裹内容时，参数1表示尺寸为最大值，暂写为2000
		// 开始测量
		tv.measure(widthMeasureSpec, heightMeasureSpec);

		return tv.getMeasuredHeight();
	}

	/**
	 * 获取ScrollView，一层一层往上找，注意保证其祖宗有ScrollView，否则陷入死循环
	 * 
	 * @return
	 */
	private ScrollView getScrollView() {
		ViewParent parent = tvDes.getParent();
		while (!(parent instanceof ScrollView)) {
			parent = parent.getParent();
		}
		return (ScrollView) parent;
	}

}
