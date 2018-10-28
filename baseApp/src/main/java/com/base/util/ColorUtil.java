package com.base.util;

import android.graphics.Color;

/**
 * 
 * @author Ni Guijun
 *
 */
public class ColorUtil {

	private static final double COLOR_THRESHOLD = 180.0;
	
	/**
     * 使用方差来计算这个颜色是否近似某个颜色
     *
     * @param baseColor
     * @param color
     * @return
     */
    public static boolean isColorSimilar(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < COLOR_THRESHOLD) {
            return true;
        }
        return false;
    }
    /**
     * 使用方差来计算这个颜色是否近似黑色
     * 
     * @param color
     * @return
     */
    public static boolean isColorSimilar(int color) {
    	return isColorSimilar(Color.BLACK,color);
    }
}
