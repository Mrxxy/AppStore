package com.rixin.appstore.utils;

import android.R;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * 生成图形的工具类
 * 
 * @author 飘渺云轩
 * @date 2016-10-04
 */
public class DrawableUtils {

	/**
	 * 生成圆角矩形
	 * @param color
	 * @param radius
	 * @return
	 */
	public static GradientDrawable getGradientDrawable(int color, int radius){
		//xml中定义的shape标签对应此类
		GradientDrawable shape = new GradientDrawable();
		shape.setShape(GradientDrawable.RECTANGLE); //矩形
		shape.setCornerRadius(radius);  //圆角
		shape.setColor(color);
		
		return shape;
	}
	
	/**
	 * 获取状态选择器
	 * @param normal
	 * @param press
	 * @return
	 */
	public static StateListDrawable getSelector(Drawable normal, Drawable pressed){
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[]{android.R.attr.state_pressed}, pressed); //按下时图片
		selector.addState(new int[]{}, normal);  //默认图片
		
		return selector;
	}
	
	/**
	 * 获取状态选择器，（封装至此）终极奥义
	 * @param normal
	 * @param pressed
	 * @param radius
	 * @return
	 */
	public static StateListDrawable getSelector(int normal, int pressed, int radius){
		GradientDrawable bgNormal = getGradientDrawable(normal, radius);
		GradientDrawable bgPressed = getGradientDrawable(pressed, radius);
		StateListDrawable selector = getSelector(bgNormal, bgPressed);
		
		return selector;
	}
	
}
