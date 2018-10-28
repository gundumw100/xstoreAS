package com.widget.flowlayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.View;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class TagAdapter<T> {
	private List<T> mTagDatas;
	private OnDataChangedListener mOnDataChangedListener;

	public TagAdapter(List<T> datas) {
		mTagDatas = datas;
	}

	public TagAdapter(T[] datas) {
		mTagDatas = new ArrayList<T>(Arrays.asList(datas));
	}

	public int getCount() {
		return mTagDatas == null ? 0 : mTagDatas.size();
	}

	public void notifyDataChanged() {
		mOnDataChangedListener.onChanged();
	}

	public T getItem(int position) {
		if(position>mTagDatas.size()-1){
			return null;
		}
		return mTagDatas.get(position);
	}

	public abstract View getView(FlowLayout parent, int position, T t);

	static interface OnDataChangedListener {
		void onChanged();
	}

	void setOnDataChangedListener(OnDataChangedListener listener) {
		mOnDataChangedListener = listener;
	}

}