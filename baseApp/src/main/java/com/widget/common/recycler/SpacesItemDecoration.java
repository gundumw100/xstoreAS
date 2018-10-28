package com.widget.common.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置RecycleView中item之间的间隔（透明）
 * 可根据需要设置不同item之间的上下左右的间距
 * 
 * @author pythoner
 *
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
	
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    	//这里可以使用parent.getChildPosition(view)获得item的position
    	//然后可以分别设置不同item之间的上下左右间距
    	//比如：
    	/*if(parent.getChildPosition(view)%2 == 0){
    		outRect.top = space;
    	}*/
    	
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
    }
}
