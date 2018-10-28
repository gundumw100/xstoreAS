package com.widget.effect;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * @author Administrator
 *
 */
public class RotatePageTransformer implements ViewPager.PageTransformer{
	
	private static final float ROT_MAX = 20.0f;
	private float mRot;
	
	public void transformPage(View view, float position){
		if (position < -1){ // [-Infinity,-1)
			// This page is way off-screen to the left.
//			ViewHelper.setRotation(view, 0);
			view.setRotation(0);

		} else if (position <= 1){ //[-1,1] a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
			// Modify the default slide transition to shrink the page as well

			mRot = (ROT_MAX * position);
			view.setPivotX(view.getMeasuredWidth() * 0.5f);
			view.setPivotY(view.getMeasuredWidth());
			view.setRotation(mRot);
//			ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
//			ViewHelper.setPivotY(view, view.getMeasuredHeight());
//			ViewHelper.setRotation(view, mRot);

		} else{ // (1,+Infinity]
			// This page is way off-screen to the right.
//			ViewHelper.setRotation(view, 0);
			view.setRotation(0);
		}
	}
}
