package com.widget.common.recycler;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.net.RequestManager;
import com.base.net.VolleyHelper;

/**
 * RecyclerView适配器
 * 
 * @author pythoner
 * 
 */
@SuppressWarnings("unchecked")
public class ViewHolder {
	private SparseArray<View> viewHolder;
	private View view;
	private int viewType;
	
	public static ViewHolder getViewHolder(View view,int viewType) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		if (viewHolder == null) {
			viewHolder = new ViewHolder(view);
			viewHolder.setViewType(viewType);
			view.setTag(viewHolder);
		}
		return viewHolder;
	}

	private ViewHolder(View view) {
		this.view = view;
		viewHolder = new SparseArray<View>();
		view.setTag(viewHolder);
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	public <T extends View> T getView(int id) {
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

	public View getConvertView() {
		return view;
	}

	public TextView getTextView(int id) {
		return getView(id);
	}

	public Button getButton(int id) {
		return getView(id);
	}

	public ImageView getImageView(int id) {
		return getView(id);
	}

	public ViewHolder setText(int id, CharSequence charSequence) {
		getTextView(id).setText(charSequence);
		return this;
	}

	public ViewHolder setImageResource(int id, int resID) {
		getImageView(id).setImageResource(resID);
		return this;
	}

	public ViewHolder setImageBitmap(int id, Bitmap bm) {
		getImageView(id).setImageBitmap(bm);
		return this;
	}

	/**
     * 从网络获取图片然后为ImageView设置图片
     * 
     */
    @Deprecated
    public ViewHolder setImageByUrl(int viewId, String url,int defaultResId){
    	VolleyHelper.loadImageByVolley(url,  (ImageView) getView(viewId), defaultResId,defaultResId,200, 200,0);
//        ImageLoader.getInstance(3, Type.LIFO).loadImage(url, (ImageView) get(viewId));
        return this;
    }
    
	/**
	 * 从网络获取图片然后为NetworkImageView设置图片
	 * 
	 */
	public ViewHolder setImageUrl(int viewId, String url, int defaultResId) {
		com.android.volley.toolbox.NetworkImageView v = (com.android.volley.toolbox.NetworkImageView) getView(viewId);
		v.setDefaultImageResId(defaultResId);
		v.setErrorImageResId(defaultResId);
		v.setImageUrl(url, RequestManager.getImageLoader());
		return this;
	}

	/**
	 * 显示指定View
	 * @param viewId
	 * @return
	 */
	public ViewHolder showView(int viewId){
		getView(viewId).setVisibility(View.VISIBLE);
		return this;
	}
	/**
	 * 显示View集合中的所有实例
	 * @param ids
	 * @return
	 */
	public ViewHolder showViews(int ... ids){
		if(ids != null && ids.length > 0){
			for (int i = 0; i < ids.length; i++) {
				getView(ids[i]).setVisibility(View.VISIBLE);
			}
		}
		return this;
	}
	/**
	 * 隐藏指定View
	 * @param viewId
	 * @return
	 */
	public ViewHolder hideView(int viewId){
		getView(viewId).setVisibility(View.GONE);
		return this;
	}
	/**
	 * 隐藏View集合中的所有实例
	 * @param ids
	 * @return
	 */
	public ViewHolder hideViews(int ... ids){
		if(ids != null && ids.length > 0){
			for (int i = 0; i < ids.length; i++) {
				getView(ids[i]).setVisibility(View.GONE);
			}
		}
		return this;
	}
}
