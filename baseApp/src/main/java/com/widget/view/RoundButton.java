/*
 * Copyright (C) 2014 BitGriff LLC. 
 * All rights reserved.
 *
 * Proprietary/confidential.
 */
package com.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.app.base.R;

/**
 * http://www.open-open.com/lib/view/open1437363895396.html
 * 
 * 
 * <com.widget.view.RoundButton
        android:id="@+id/button"
        app:radius="0"
        app:image="@drawable/ic_addtomedialibrary"
        app:bgcolor="@color/transparent"
        app:highlightMode="image"
        android:layout_width="40dip"
        android:layout_height="80dip"
        />
        
    roundButton = new RoundButton(context);
	roundButton.init(image, bgcolor, text);//image can be null
 * @author Administrator
 *
 */
public class RoundButton extends View {
	public interface OnCheckedChangeListener {
		void onCheckedChanged(RoundButton roundButton, boolean checked);
	}
	
	public enum HighlightMode {
		None,
		Image,
		Background;
		
		public static HighlightMode getValue(int i) {
			for (HighlightMode value : values()) {
				if (value.ordinal() == i)
					return value;
			}
			return HighlightMode.None;
		}
	}
	
	private Drawable checkedImage;
	
	private Drawable image;
	private Paint bgpaint;
	private Paint pressedBgpaint;//按下闪背景色
	private Paint highlightPaint;//常亮高亮色
	
	private String text;
	private Paint textPaint;
	
	private float radius = 16.0f;
	private float textSize = 16.0f;
	
	private float spacing = 16.0f;//文字和图片的间距
	
	private HighlightMode highlightMode;
	
	//指定的高亮颜色，如果highlightMode为none，则highlightColor无效，点击后的颜色值由brighter(bgcolor)决定！
	//如果highlightMode为image,则highlightColor为点击后图片的高亮色；
	//如果highlightMode为background,则highlightColor为点击后background的高亮色,不由brighter(bgcolor)决定！
	private int highlightColor;
	
	private Drawable highlightImage;
	
	private boolean checkable;
	
	private boolean checked;
	private boolean highlighted;
	
	private OnCheckedChangeListener onCheckedChangeListener;

	private boolean pressed;
	
	public RoundButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	public RoundButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public RoundButton(Context context) {
		super(context);
		init(null, 0);
	}

	private void init(AttributeSet attrs, int defStyle) {
		Drawable image;
		int bgcolor;
		String text;
		
		if (attrs != null) {
			final TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.RoundButton, defStyle, 0);

			image = a.getDrawable(R.styleable.RoundButton_image);

			bgcolor = a.getColor(R.styleable.RoundButton_bgcolor, 0xffffffff);
			
			text = a.getString(R.styleable.RoundButton_text);
			
			radius = a.getFloat(R.styleable.RoundButton_radius, 12.0f);
			
			highlightMode = HighlightMode.getValue(a.getInt(R.styleable.RoundButton_highlightMode, HighlightMode.None.ordinal()));
			
			//如果highlightMode设置为none，则highlightColor无效
			highlightColor = a.getColor(R.styleable.RoundButton_highlightColor, 0xff00b5ff);
			
			a.recycle();
		}
		else {
			image = null;
			
			text = "";
			
			bgcolor = 0xff808080;
			
			radius = 12.0f;
			
			highlightMode = HighlightMode.None;
			
			highlightColor = 0xff00b5ff;
		}
		
		init(image, bgcolor, text);
	}
	
	public void init(Drawable image, int bgcolor, String text) {
		this.image = image;
		
		bgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bgpaint.setColor(bgcolor);
		
		pressedBgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pressedBgpaint.setColor(brighter(bgcolor));
//		pressedBgpaint.setColor(highlightColor);
		
		if (text == null)
			text = "";

		this.text = text;
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(0xffffffff);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(pixelsToSp(getContext(), textSize));

		if (highlightMode == HighlightMode.Background) {
			highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			highlightPaint.setColor(highlightColor);
		}
		else if (highlightMode == HighlightMode.Image) {
			highlightImage = createHighlightImage();
		}
		
		setClickable(true);
	}
	
	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	public boolean isCheckable() {
		return checkable;
	}

	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
		
		if (checkable) {
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (RoundButton.this.checkable) {
						setChecked(!checked);
						
						if (onCheckedChangeListener != null)
							onCheckedChangeListener.onCheckedChanged(RoundButton.this, checked);
					}
				}
			});
		}
	}

	public boolean isChecked() {
		return checked;
	}
		
	public void setText(int id) {
		setText(getContext().getString(id));
	}
	
	public void setText(String text) {
		this.text = text;
		
		invalidate();
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(int id) {
		setImage(getResources().getDrawable(id));
	}
	
	public void setImage(Drawable image) {
		this.image = image;
		if (highlightMode == HighlightMode.Image) {
			highlightImage = createHighlightImage();
		}
		
		invalidate();
	}
	
	public void setBgColor(int bgcolor) {
		bgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bgpaint.setColor(bgcolor);
		
		pressedBgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pressedBgpaint.setColor(brighter(bgcolor));
		
		invalidate();
	}

	public void setChecked(boolean checked) {
		if (this.checked != checked) {
			this.checked = checked;

			invalidate();
		}
	}
	
	public void setHighlighted(boolean highlighted) {
		if (this.highlighted != highlighted) {
			this.highlighted = highlighted;

			invalidate();
		}
	}

	public void setRadius(float radius) {
		this.radius = radius;
		
		invalidate();
	}
	
	private Drawable createHighlightImage() {
		int width = image.getIntrinsicWidth();
		int height = image.getIntrinsicHeight();
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		image.setBounds(0, 0, width, height);
		image.draw(canvas);
		
		int count = bitmap.getWidth() * bitmap.getHeight();
		int pixels[] = new int[count];
		
		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for (int n = 0; n < count; n++) {
			boolean v = (Color.alpha(pixels[n])) != 0;
			
			if (v) {
				int pixel = pixels[n];
				
				int alpha = Color.alpha(pixel);
				int red = Color.red(highlightColor);
				int green = Color.green(highlightColor);
				int blue = Color.blue(highlightColor);
				
				int color = Color.argb(alpha, red, green, blue);
				
				pixels[n] = color;
			}
		}
		
		bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		
		return new BitmapDrawable(getResources(), bitmap);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			pressed = true;
			invalidate();
			break;			
		case MotionEvent.ACTION_UP:
			pressed = false;
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_HOVER_EXIT:
			pressed = false;
			invalidate();
			break;
		}
		
		return super.onTouchEvent(event);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		RectF bounds = new RectF(0, 0, getWidth(), getHeight());
		
		Drawable image = null;
		Paint bgPaint = null;
		
		switch (highlightMode) {
		case None:
			image = this.image;
			bgPaint = pressed ? pressedBgpaint : this.bgpaint;
			break;
		case Background:
			image = this.image;
			bgPaint = pressed ? highlightPaint : this.bgpaint;
			break;
		case Image:
			image = pressed ? highlightImage : this.image;
			bgPaint = pressed ? pressedBgpaint : this.bgpaint;
			break;
		}
		
		if (radius != 0.0f)
			canvas.drawRoundRect(bounds, radius, radius, bgPaint);
		else 
			canvas.drawRect(bounds, bgPaint);
		
		Rect textBounds = new Rect();
		if (text.length() > 0)
			textPaint.getTextBounds(text, 0, text.length(), textBounds);
		
		float h_dst = ((image != null) ? image.getMinimumHeight() + ((text.length() > 0) ? spacing : 0) : 0) + textBounds.height();
		
		float xd = (bounds.width() - ((image != null) ? image.getMinimumWidth() : 0)) / 2;
		float yd = (bounds.height() - h_dst) / 2; 
		
		if (image != null) {
			image.setBounds((int) xd, (int) yd, (int) (xd + image.getMinimumWidth()), (int) (yd + image.getMinimumHeight()));
			image.draw(canvas);
		}
		
		float xt = (bounds.width() - 0 * textBounds.width()) / 2;
		float yt = yd + ((image != null) ? image.getMinimumHeight() + ((text.length() > 0) ? (spacing+textBounds.height()) : 0) : textBounds.height());// + textBounds.height();
		
		canvas.drawText(text, xt, yt, textPaint);
		
		if (checked && checkable && checkedImage != null) {
			checkedImage.setBounds((int) (bounds.width() - checkedImage.getMinimumWidth()), (int) (bounds.height() - checkedImage.getMinimumHeight()),
					(int) bounds.width(), (int) bounds.height());
			checkedImage.draw(canvas);
		}
	}
	
	public static float pixelsToSp(Context context, float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px*scaledDensity;
	}

	public void setOnCheckedChangeListener(
			OnCheckedChangeListener onCheckedChangeListener) {

		this.onCheckedChangeListener = onCheckedChangeListener;
	}
	
	/**
	 * 颜色加深处理
	 * @param color
	 * @return
	 */
	public static int brighter(int color) {
		float flag=0.8f;
//		int alpha = color >> 24;
//		int red = color >> 16 & 0xFF;
//		int green = color >> 8 & 0xFF;
//		int blue = color & 0xFF;
		int r = Color.red(color);//
		int g = Color.green(color);
		int b = Color.blue(color);
		
		r = (int) Math.floor(r * flag);
		g = (int) Math.floor(g * flag);
		b = (int) Math.floor(b * flag);
		
		return Color.rgb(r, g, b);
	}

	public void resetState() {
		pressed = false;
		highlighted = false;
		checked = false;
		
		invalidate();
	}
}
