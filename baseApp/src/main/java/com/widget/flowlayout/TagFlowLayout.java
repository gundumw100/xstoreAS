package com.widget.flowlayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.app.base.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class TagFlowLayout extends FlowLayout implements TagAdapter.OnDataChangedListener {
	private static final String TAG = "TagFlowLayout";
	private TagAdapter mTagAdapter;
	private boolean mSupportMulSelected = true;//单选or多选模式，默认多选
	private int mSelectedMax = -1;// 设置为多选模式时，可以选择的最大数量，-1为不限制数量
	private int oldPosition = -1;//设置为单选时，上一次选中项的位置；此时adapter.setCheckedAt()中只能填入一个参数或长度为1的List，否则抛参数异常
	private Set<Integer> mSelectedSet = new HashSet<Integer>();//选中的位置集合

	public TagFlowLayout(Context context) {
		this(context, null);
	}
	public TagFlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
		mSupportMulSelected = ta.getBoolean(R.styleable.TagFlowLayout_multi_suppout, true);
		mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
		ta.recycle();
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int cCount = getChildCount();
//
//		for (int i = 0; i < cCount; i++) {
//			TagView tagView = (TagView) getChildAt(i);
//			if (tagView.getVisibility() == View.GONE)
//				continue;
//			if (tagView.getTagView().getVisibility() == View.GONE) {
//				tagView.setVisibility(View.GONE);
//			}
//		}
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//	}

	public interface OnTagClickListener {
		boolean onTagClick(FlowLayout parent, View view, int position);
	}

	private OnTagClickListener mOnTagClickListener;

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		mOnTagClickListener = onTagClickListener;
	}
	public interface OnTagLongClickListener {
		boolean onTagLongClick(FlowLayout parent, View view, int position);
	}
	private OnTagLongClickListener mOnTagLongClickListener;

	public void setmOnTagLongClickListener(
			OnTagLongClickListener mOnTagLongClickListener) {
		this.mOnTagLongClickListener = mOnTagLongClickListener;
	}
	public void setAdapter(TagAdapter adapter) {
		mTagAdapter = adapter;
		mTagAdapter.setOnDataChangedListener(this);
		changeAdapter();
	}

	private void changeAdapter() {
		removeAllViews();
		TagAdapter adapter = mTagAdapter;
		TagView tagView = null;
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(this, i, adapter.getItem(i));
			view.setDuplicateParentStateEnabled(true);//子视图与父控件perssed 等状态保持一致

			tagView = new TagView(getContext());
			tagView.setLayoutParams(view.getLayoutParams());
			tagView.addView(view);
			final int pos = i;
			tagView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					doSelect((TagView) view, pos);
				}
			});
			tagView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View view) {
					// TODO Auto-generated method stub
//					doSelect((TagView) view, pos);
					if (mOnTagLongClickListener != null) {
						mOnTagLongClickListener.onTagLongClick(TagFlowLayout.this, view, pos);
					}
					return true;
				}
			});
			addView(tagView);

			if (mSupportMulSelected) {
				if (mSelectedSet.contains(i)) {
					tagView.setChecked(true);
				}
			} else {
				if (mSelectedSet.size() > 1) {
					throw new IllegalArgumentException("单选按钮只能有一个预设值");
				} else {
					if (mSelectedSet.contains(i)) {
						oldPosition = i;
						tagView.setChecked(true);
					}
				}
			}
		}
	}
	public void setMaxSelectCount(int count) {
		if (mSelectedSet.size() > count) {
			Log.w(TAG, "you has already select more than " + count + " views , so it will be clear .");
			mSelectedSet.clear();
		}
		mSelectedMax = count;
	}

	public int getMaxSelectCount(){
		return mSelectedMax;
	}
	
	public Set<Integer> getCheckedPositions() {
		return mSelectedSet;
	}
	public <T> ArrayList<T> getCheckedItems() {
		ArrayList<T> items=new ArrayList<T>();
		for(int position:mSelectedSet){
			T t=(T)mTagAdapter.getItem(position);
			if(t!=null){
				items.add(t);
			}
		}
		return items;
	}

	private void doSelect(TagView child, int position) {
		if (mSupportMulSelected) {
			if (child.isChecked()) {
				child.setChecked(false);
				mSelectedSet.remove(position);
				if (mOnTagClickListener != null) {
					mOnTagClickListener.onTagClick(TagFlowLayout.this, child, position);
				}
			} else {
				if (mSelectedMax > 0 && mSelectedSet.size() >= mSelectedMax)//超过最大选择，不执行事件
					return;
				child.setChecked(true);
				mSelectedSet.add(position);
				if (mOnTagClickListener != null) {
					mOnTagClickListener.onTagClick(TagFlowLayout.this, child, position);
				}
			}

		} else {
			if (child.isChecked()) {
				child.setChecked(false);
				mSelectedSet.clear();
				oldPosition = -1;
			} else {
				if (oldPosition >= 0) {
					TagView tagView=(TagView) getChildAt(oldPosition);
					if(tagView!=null){
						tagView.setChecked(false);
					}
				}
				child.setChecked(true);
				mSelectedSet.clear();
				mSelectedSet.add(position);
				oldPosition = position;
			}
			if (mOnTagClickListener != null) {
				mOnTagClickListener.onTagClick(TagFlowLayout.this, child, position);
			}
		}
	}

	/**
	 * 设置预设项，必须在setAdapter之前调用
	 * @param positions
	 */
	public void setCheckedAt(int... positions) {
		if (mTagAdapter != null) {
			mSelectedSet.clear();// must be cleared
			for (int i = 0; i < positions.length; i++) {
				mSelectedSet.add(positions[i]);
				TagView tagView = (TagView) getChildAt(positions[i]);
				tagView.setChecked(true);
			}
			mTagAdapter.notifyDataChanged();
		}
	}
	
	public boolean isChecked(int position){
		TagView tagView=(TagView) getChildAt(position);
		if(tagView==null){
			return false;
		}
		return tagView.isChecked();
	}
	
	public void notifyDataChanged(){
		if(mTagAdapter!=null){
			mTagAdapter.notifyDataChanged();
		}
	}
	/**
	 * 设置预设项，必须在setAdapter之后调用
	 * @param positions
	 */
	public void setCheckedAt(HashSet<Integer> positions) {
		if(mTagAdapter!=null&&positions!=null&&positions.size()>0){
			mSelectedSet.clear();//must be cleared
			mSelectedSet.addAll(positions);
			Iterator iterator=positions.iterator();
			while(iterator.hasNext()){
				TagView tagView = (TagView) getChildAt((Integer)iterator.next());
				tagView.setChecked(true);
			}
			mTagAdapter.notifyDataChanged();
		}
	}

	private static final String KEY_CHOOSE_POS = "key_choose_pos";
	@Override
	protected Parcelable onSaveInstanceState() {
		super.onSaveInstanceState();
		Bundle bundle = new Bundle();

		String selectPos = "";
		if (mSelectedSet.size() > 0) {
			for (int key : mSelectedSet) {
				selectPos += key + "|";
			}
			selectPos = selectPos.substring(0, selectPos.length() - 1);
		}
		bundle.putString(KEY_CHOOSE_POS, selectPos);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
			if (!TextUtils.isEmpty(mSelectPos)) {
				mSelectedSet.clear();
				String[] split = mSelectPos.split("\\|");
				for (String pos : split) {
					int index = Integer.parseInt(pos);
					mSelectedSet.add(index);

					TagView tagView = (TagView) getChildAt(index);
					tagView.setChecked(true);
				}

			}
		}
	}

	@Override
	public void onChanged() {
		changeAdapter();
	}

}
