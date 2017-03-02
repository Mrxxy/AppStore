package com.rixin.appstore.ui.view;

import com.rixin.appstore.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 自定义控件，按照比例来决定布局高度
 * 
 * @author 飘渺云轩
 * @date 2016-10-02
 */
public class RatioLayout extends FrameLayout {

	private float ratio;

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/** 获取属性值 (当自定义属性时，系统会自动生成属性相关id，通过R.styleabel.xxx引用) */
		// //1. 第一种方法（简单）
		// ratio = attrs.getAttributeFloatValue("rixin", "ratio", -1);

		// 2. 第二种方法（google大牛使用的方法）
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.ratioLayout);
		ratio = typedArray.getFloat(R.styleable.ratioLayout_ratio, -1);
		typedArray.recycle(); // 回收typedArray，提高性能
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatioLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/** 1.获取宽度 2.根据宽度和比例ratio，计算控件的高度 3.重新测量控件 */
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// MeasureSpec.AT_MOST: 至多模式，控件有多大就显示多大，wrap_content
		// MeasureSpec.EXACTLY: 确定模式，类似宽高写死成dip， match_parent
		// MeasureSpec.UNSPECIFIED: 为指定模式

		// 宽度确定，高度不确定，ratio合法 才计算高度值
		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode == MeasureSpec.UNSPECIFIED && ratio > 0) {

			// 图片宽度 = 控件宽度 - 左右内边距
			int imageWidth = width - getPaddingLeft() - getPaddingRight();

			// 图片高度 = 图片宽度 / 宽高比例
			int imageHeight = (int) (imageWidth / ratio + 0.5f);

			// 控件高度 = 图片高度 + 上下内边距
			height = imageHeight + getPaddingBottom() + getPaddingTop();

			// 带模式的控件高度值
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
