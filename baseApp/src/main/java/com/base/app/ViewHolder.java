package com.base.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.net.RequestManager;
import com.base.net.VolleyHelper;
import com.base.net.VolleyHelper.OnLoadImageListener;

/**
 * 通用的ViewHolder，通常与CommonAdapter<T>一起使用
 * 
 * @author pythoner
 * 
 */
@SuppressWarnings("unchecked")
public class ViewHolder {
	private final SparseArray<View> mViews;

	private int mPosition;

	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}
	/**
	 * 为TextView设置文字颜色
	 * @author Tan Chenglong
	 * @param viewId
	 * @param textColor
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int textColor) {
		TextView view = getView(viewId);
		view.setTextColor(textColor);
		return this;
	}
	/**
	 * 设置指定View是否可见
	 * @author Tan Chenglong
	 * @param viewId
	 * @param visibility
	 * @return
	 */
	public ViewHolder setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}
	/**
	 * 设置指定CompoundButton(RadioButton,CheckBox)选中状态
	 * @param viewId
	 * @param checked
	 * @return
	 */
	public ViewHolder setChecked(int viewId, boolean checked){
		CompoundButton compoundButton = getView(viewId);
		compoundButton.setChecked(checked);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片,直角
	 * 
	 * @param viewId
	 * @param url
	 * @param defaultImageResId
	 * @return
	 */
	public ViewHolder setImageUrl(int viewId, String url, int defaultImageResId) {
		ImageView iv = getView(viewId);
		VolleyHelper.loadImageByVolley(url, iv, defaultImageResId, 0);
		return this;
	}

	/**
	 * 为任意View设置图片，具体操作由OnLoadImageListener回调onLoadImageSuccess(View
	 * view,ImageContainer imageContainer)决定
	 * 
	 * @param viewId
	 * @param url
	 * @param onLoadImageListener
	 * @return
	 */
	public ViewHolder setImageUrl(int viewId, String url, OnLoadImageListener onLoadImageListener) {
		VolleyHelper.loadImageByVolley(url, getView(viewId), onLoadImageListener);
		return this;
	}

	/**
	 * 从网络获取图片然后为NetworkImageView设置图片
	 * 
	 * @param viewId
	 * @param defaultResId
	 * @return
	 */
	public ViewHolder setImageToNetworkImageView(int viewId, String url, int defaultResId) {
		com.android.volley.toolbox.NetworkImageView v = (com.android.volley.toolbox.NetworkImageView) getView(viewId);
		v.setDefaultImageResId(defaultResId);
		v.setErrorImageResId(defaultResId);
		v.setImageUrl(url, RequestManager.getImageLoader());
		return this;
	}

	public int getPosition() {
		return mPosition;
	}

}
