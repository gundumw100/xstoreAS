package com.widget.effect.text;

import android.graphics.Color;

/**
 * ArgbEvaluator.evaluate(float fraction, Object startValue, Object endValue);
 * 根据一个起始颜色值和一个结束颜色值以及一个偏移量生成一个新的颜色，分分钟实现类似于微信底部栏滑动颜色渐变。
这里提供另一个颜色渐变的版本
 * @author http://blog.danlew.net/2014/03/30/android-tips-round-up-part-1/
 *
 */
public class ColorUtil {

	/**
    * Blend {@code color1} and {@code color2} using the given ratio.
    *
    * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
    *              0.0 will return {@code color2}.
    */
   public static int blendColors(int color1, int color2, float ratio) {
       final float inverseRation = 1f - ratio;
       float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
       float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
       float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
       return Color.rgb((int) r, (int) g, (int) b);
   }
	   
}
