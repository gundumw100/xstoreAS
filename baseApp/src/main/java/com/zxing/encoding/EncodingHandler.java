package com.zxing.encoding;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
/**
 * @author Ryan Tang
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/** 
     * 创建条形码的方法 
     * @return 
     * @throws Exception  
     */  
    public static Bitmap createOneQRCode(String content) throws Exception {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败  
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 300, 120);
        //矩阵的宽度  
        int width = matrix.getWidth();  
        //矩阵的高度  
        int height = matrix.getHeight();  
        //矩阵像素数组  
        int[] pixels =  new int[width * height];  
        //双重循环遍历每一个矩阵点  
        for(int y = 0;y<height;y++){  
            for(int x = 0;x<width;x++){  
                if(matrix.get(x, y)){  
                    //设置矩阵像素点的值  
                    pixels[y * width +x] = 0xff000000;  
                }  
            }  
        }  
        //根据颜色数组来创建位图  
        /** 
         * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图， 
         * 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。 
         * config是一个枚举，可以用它来指定位图“质量”。 
         */  
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  
        // 通过像素数组生成bitmap,具体参考api  
        bm.setPixels(pixels, 0, width, 0, 0, width, height);  
        //将生成的条形码返回给调用者  
        return bm;  
    }
}
