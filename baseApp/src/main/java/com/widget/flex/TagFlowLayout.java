package com.widget.flex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 水平方向流布局
 * @author Ni Guijun
 *
 */
public class TagFlowLayout<T> extends FlexboxLayout{

	public static final int CHOICE_MULTIPLE=0;//多选模式
	public static final int CHOICE_SINGLE=1;//单选模式
	private int choiceMode=CHOICE_MULTIPLE;
	private ItemAdapter<T> adapter;
	private Map<Integer,T> selectedItem=new HashMap<Integer,T>();
	public TagFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);
		setFlexWrap(FlexboxLayout.FLEX_WRAP_WRAP);
	}

	public void setAdapter(ItemAdapter<T> adapter){
		this.adapter=adapter;
		removeAllViews();
		for(int i=0;i<adapter.getCount();i++){
			View view=adapter.getView(this, i, adapter.getItem(i));
			addView(view);
			setClick(view,i);
		}
	}
	
	private void setClick(final View view,final int position){
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onItemClickListener!=null){
					onItemClickListener.onItemClick(view, position);
				}
				if(choiceMode==CHOICE_MULTIPLE){
					if(selectedItem.containsKey(position)){
						selectedItem.remove(position);
						adapter.onUnselect(view, position);
					}else{
						selectedItem.put(position,adapter.getItem(position));
						adapter.onSelect(view, position);
					}
				}else if(choiceMode==CHOICE_SINGLE){//单选模式
					if(selectedItem.containsKey(position)){
						//单选模式下，不支持选中后再取消选择
					}else{
						//如果已经选过一个了，将上一个的状态置为非选状态
						if(selectedItem.size()>0){
							Set<Integer> set=selectedItem.keySet();
							Iterator<Integer> iterator=set.iterator();
							while(iterator.hasNext()){
								Integer lastPosition=iterator.next();//上一个Item的位置
								selectedItem.remove(lastPosition);
								adapter.onUnselect(null, lastPosition);//上一个View拿不到，也不需要，故null
							}
						}
						//然后加入当前选中的Item
						selectedItem.put(position,adapter.getItem(position));
						adapter.onSelect(view, position);
					}
				}
			}
		});
	}
	
	/**
	 * 获得所有选中项的位置
	 * @return
	 */
	public Map<Integer,T> getSelectedItemPosition(){
		return selectedItem;
	}
	
	public int getChoiceMode() {
		return choiceMode;
	}

	public void setChoiceMode(int choiceMode) {
		this.choiceMode = choiceMode;
	}

	private OnItemClickListener onItemClickListener;
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener{
		public void onItemClick(View v,int position);
	}
	
}
