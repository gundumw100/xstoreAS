package com.widget.common.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 
 * @author pythoner
 * 
 */
public class BaseHolder extends RecyclerView.ViewHolder {

	private ViewHolder viewHolder;

	public BaseHolder(View itemView,int viewType) {
		super(itemView);
		viewHolder = ViewHolder.getViewHolder(itemView,viewType);
	}

	public ViewHolder getViewHolder() {
		return viewHolder;
	}

}
