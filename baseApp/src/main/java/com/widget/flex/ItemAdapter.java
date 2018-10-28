package com.widget.flex;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

/**
 * 适配器抽象类
 * @author Ni Guijun
 *
 * @param <T>
 */
public abstract class ItemAdapter<T> {

	private List<T> datas;
	public ItemAdapter(List<T> datas){
		this.datas=datas;
	}
	
	public abstract View getView(ViewGroup parent,int position,T t);
	public abstract T getItem(int position);
	
	public int getCount(){
		return datas.size();
	}
	
	public void onSelect(View view,int position){
		
	}
	public void onUnselect(View view,int position){
		
	}
}
