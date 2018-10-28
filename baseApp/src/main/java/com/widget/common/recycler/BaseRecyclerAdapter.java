package com.widget.common.recycler;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * public class MyAdapter<Entity> extends BaseRecyclerAdapter {
    public MyAdapter(Context context, List<Entity> list) {
        super(context, list);
    }

	@Override
	public int getItemViewType(int position) {//可选
		if(position==0){
			return 0;
		}else{
			return 1;
		}
	}
			
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,Entity item, int position) {

        vh.getTextView(R.id.name).setText(item.getName());
        vh.getTextView(R.id.age).setText(item.getAge());
        vh.setText(R.id.height,item.getHeight()); 
    }
}
 * @author pythoner
 *
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

	public List<T> list;

	private Context context;

	public BaseRecyclerAdapter(Context context, List<T> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//解决RecyclerView 中的 item 居中问题，传入parent，false参数即可
		//https://segmentfault.com/q/1010000004375841
		View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), parent,false);
		return new BaseHolder(view,viewType);
	}

	public abstract int onCreateViewLayoutID(int viewType);
	public abstract void onBindViewHolder(ViewHolder holder, T item, int position);
	
	@Override
	public void onViewRecycled(final BaseHolder holder) {
		super.onViewRecycled(holder);
	}

	@Override
	public void onBindViewHolder(final BaseHolder holder, final int position) {
		onBindViewHolder(holder.getViewHolder(), list.get(position), position);
		if (onItemClickListener != null) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClickListener.onItemClick(null, v,holder.getPosition(), holder.getItemId());
				}
			});
		}

		if(onItemLongClickListener!=null){
			holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					onItemLongClickListener.onItemLongClick(null, v, holder.getPosition(), holder.getItemId());
					return false;
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	private AdapterView.OnItemClickListener onItemClickListener;
	private AdapterView.OnItemLongClickListener onItemLongClickListener;

	//no needs
//	public AdapterView.OnItemClickListener getOnItemClickListener() {
//		return onItemClickListener;
//	}

	public void setOnItemLongClickListener(
			AdapterView.OnItemLongClickListener onItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener;
	}

	public void setOnItemClickListener(
			AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	
	//以下方法尚未测试
	//http://blog.csdn.net/dywailly/article/details/47169355
	//
	public void addItem(T item){
		list.add(item);
        notifyItemInserted(list.size()-1);
	}
	
	public void addItems(List<T> items){
		list.addAll(items);
		notifyItemRangeInserted(list.size()-1, items.size());
//		notifyItemRangeChanged(list.size()-1, items.size());//???
//		notifyItemRangeChanged(position + addStrs.size(), getItemCount()-addStrs.size());
	}
	public void removeItem(int position) {
		list.remove(position);
        notifyItemRemoved(position);
	}
	public void removeItem(T item) {
		list.remove(item);
		notifyItemRemoved(list.indexOf(item));
	}
	public void removeAllItem() {
		int itemCount=list.size();
		list.clear();
		notifyItemRangeRemoved(0, itemCount);
	}
	public void updateItem(int position,T item){
		list.set(position, item);
		notifyItemChanged(position);
	}
}
