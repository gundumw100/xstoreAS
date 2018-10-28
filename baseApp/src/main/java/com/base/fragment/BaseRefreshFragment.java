package com.base.fragment;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.R;

/**
 * 带下拉刷新的基类，需要有此功能的fragment继承此类，提供一些工具方法
 * 
 * @author pythoner
 * 
 */
public class BaseRefreshFragment extends BaseAppFragment
{
	private SwipeRefreshLayout swipeRefreshLayout;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 初始化SwipeRefreshLayout，一般在子类的onCreateView调用
     * 且SwipeRefreshLayout的id必须为swipeRefreshLayout
     * @param view
     * @param onRefreshListener
     */
    public void initSwipeRefreshLayout(View view,final OnRefreshListener onRefreshListener){
    	swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    	if(swipeRefreshLayout!=null){
			swipeRefreshLayout.setColorScheme(R.color.scheme_green, R.color.scheme_yellow, R.color.scheme_gray, R.color.scheme_red);
			swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
	
	            @Override
	            public void onRefresh()
	            {
	                // TODO Auto-generated method stub
	            	if(onRefreshListener!=null){
	            		onRefreshListener.onRefresh();
	            	}
	            }
	        });
    	}
    }
    
    private void closeRefreshing() {
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
    
    public boolean isSuccess(JSONObject response){
    	closeRefreshing();
    	return context.isSuccess(response);
    }
    
    public OnRefreshListener onRefreshListener;
    
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public interface OnRefreshListener{
    	void onRefresh();
    }
	
}
