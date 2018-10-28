package com.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CListView extends ListView {

	public CListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	
	/**  
     * 设置不滚动  
     */    
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)    
    {    
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,    
                MeasureSpec.AT_MOST);    
        super.onMeasure(widthMeasureSpec, expandSpec);    
    
    }   

}