package com.base.app;

import android.view.View;

/**
 * 用于包装原始View对象，间接为其提供get和set方法 ，用法：
 * 
 * private void doSliding(View view, int maxHeight) { ViewWrapper wrapper = new
 * ViewWrapper(view); if (view.getHeight()== 0) { ObjectAnimator.ofInt(wrapper,
 * "height", maxHeight).setDuration(400).start(); } else {
 * ObjectAnimator.ofInt(wrapper, "height", 0).setDuration(400).start(); } }
 * 
 * @author pythoner
 * 
 */
public class ViewWrapper {

	private View view;

	public ViewWrapper(View view) {
		this.view = view;
	}

	public int getWidth() {
		return view.getLayoutParams().width;
	}

	public void setWidth(int width) {
		view.getLayoutParams().width = width;
		view.requestLayout();
	}

	public int getHeight() {
		return view.getLayoutParams().height;
	}

	public void setHeight(int height) {
		view.getLayoutParams().height = height;
		view.requestLayout();
	}
}
