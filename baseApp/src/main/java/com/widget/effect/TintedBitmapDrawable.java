package com.widget.effect;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

/**
 * 为drawable着色
 * 
 * 优点和提示:
	对白色和透明图片有效。
	
	需要支持多个主题的时候，无需为同一图标准备多个drawable，减小了apk占用的空间。
	
	与谷歌的material图标集完美搭配，只需下载白的的 .png 然后相应着色。
	
	也完美适用于 Palette library.
	
	如果和list的item使用，请缓存drawable.
	
	如果是代码编写的而不是使用menu.xml，也同样可以使用在ToolBar上. 。
	
	可以用它来创建一个StateListDrawable，不同状态下使用同一图标做到不同颜色，从而减小apk体积。

 * @author pythoner
 * @see http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0824/3356.html
 *
 */
public final class TintedBitmapDrawable extends BitmapDrawable {
	private int tint;
	private int alpha;

	public TintedBitmapDrawable(final Resources res, final Bitmap bitmap,
			final int tint) {
		super(res, bitmap);
		this.tint = tint;
		this.alpha = Color.alpha(tint);
	}

	public TintedBitmapDrawable(final Resources res, final int resId,
			final int tint) {
		super(res, BitmapFactory.decodeResource(res, resId));
		this.tint = tint;
		this.alpha = Color.alpha(tint);
	}

	public void setTint(final int tint) {
		this.tint = tint;
		this.alpha = Color.alpha(tint);
	}

	@Override
	public void draw(final Canvas canvas) {
		final Paint paint = getPaint();
		if (paint.getColorFilter() == null) {
			paint.setColorFilter(new LightingColorFilter(tint, 0));
			paint.setAlpha(alpha);
		}
		super.draw(canvas);
	}
}
