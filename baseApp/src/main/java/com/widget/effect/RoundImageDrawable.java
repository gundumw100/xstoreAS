package com.widget.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

/**
 * 圆角效果图
 * 
 * RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
	drawable.setCircular(true);
	http://blog.csdn.net/ys743276112/article/details/52316588
	
 * @author Administrator
 * 
 */
public class RoundImageDrawable extends Drawable {

	private Paint mPaint;
	private Bitmap mBitmap;

	private RectF rectF;

	private int corner;
	public RoundImageDrawable(Bitmap bitmap) {
		this(bitmap, 15);
	}
	public RoundImageDrawable(Bitmap bitmap,int corner) {
		mBitmap = bitmap;
		this.corner = corner;
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
	}

	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
		rectF = new RectF(left, top, right, bottom);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRoundRect(rectF, corner, corner, mPaint);
	}

	@Override
	public int getIntrinsicWidth() {
		return mBitmap.getWidth();
	}

	@Override
	public int getIntrinsicHeight() {
		return mBitmap.getHeight();
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

}
