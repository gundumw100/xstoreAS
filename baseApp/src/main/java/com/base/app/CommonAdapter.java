package com.base.app;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * // 通用的适配器，用法： mListView.setAdapter(mAdapter = new CommonAdapter<Bean>(
 * getApplicationContext(), mDatas, R.layout.item_list){
 * 
 * @Override public void setValues(ViewHolder helper, Bean item){
 *           helper.setText(R.id.tv_title, item.getTitle());
 *           helper.setText(R.id.tv_describe, item.getDesc());
 *           helper.setText(R.id.tv_phone, item.getPhone());
 *           helper.setText(R.id.tv_time, item.getTime());
 * 
 *           helper.getView(R.id.tv_title).setOnClickListener(l) }
 * 
 *           });
 * @author pythoner
 * 
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;

	protected Context mContext;

	protected List<T> mDatas;

	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		setValues(viewHolder, getItem(position), position);
		return viewHolder.getConvertView();
	}

	public abstract void setValues(ViewHolder helper, T item, int position);

	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}

}
