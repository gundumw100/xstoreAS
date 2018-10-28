package com.base.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response.Listener;
import com.app.base.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.model.Pagination;
import com.base.net.VolleyHelper;
import com.widget.slideExpandableListView.SlideExpandableListAdapter;
import com.widget.view.LoadMoreListView.OnLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 带下拉刷新(继承自BaseRefreshFragment)和上拉加载更多的基类，需要有此功能的fragment继承此类后 需要实现parseList和createItem；
 * parseList：用于网络返回数据后解析数据到成ArrayList<T> beans ；
 * createItem：用于在Adapter中生成ItemView；
 * 在子类中需要初始化url，map，layoutId,listViewType
 * url：连接服务端的地址
 * map： 需要发送给服务端的参数集合
 * layoutId： item布局
 * listViewType:ListView的显示类型，目前只有两种:NORMAL-普通ListView，EXPANDABLE-SlideExpandableListView
 * 如果使用参数EXPANDABLE，需要SlideExpandableListView扩展包支持
 * 
 * @author pythoner
 * 
 */
@SuppressWarnings("unchecked")
public abstract class BaseRefreshListFragment<T> extends BaseRefreshFragment {

	private com.widget.view.LoadMoreListView listView;
	private CommonAdapter<T> adapter;
	private Pagination pagination = new Pagination();//分页
	private ArrayList<T> beans = new ArrayList<T>();

	// 各种参数，需要在子类初始化
	public String url;// 连接服务端的地址
	public Map<String, String> map;// 需要发送给服务端的参数集合
	public int layoutId;// item布局
	public int listViewType=0;//ListView的显示类型，0，普通ListView，1：SlideExpandableListView

	public static final int NORMAL=0;//普通ListView
	public static final int EXPANDABLE=1;//SlideExpandableListView
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 初始化ListView，一般在子类的onCreateView调用 且ListView的id必须为listView
	 * 
	 * @param view
	 */
	public void initListView(View view) {

		initSwipeRefreshLayout(view, new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				reloadItems();
			}
		});

		listView = (com.widget.view.LoadMoreListView) view.findViewById(R.id.listView);
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				pagination.page++;
				doCommandForList(url, map);
			}
		});
		if(TextUtils.isEmpty(url)){
			Log.i("tag", "BaseRefreshListFragment-->No url found");
			return;
		}
		doCommandForList(url, map);
	}

	private void doCommandForList(String url, Map<String, String> map) {
		doCommandForList(url, map,true); 
	}
	
	private void doCommandForList(String url, Map<String, String> map,boolean showLoading) {
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put("page", String.valueOf(pagination.page));
		map.put("size", String.valueOf(pagination.size));
		VolleyHelper.execPostRequest(context,null, url, map,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
//						 Log.i("tag", response.toString());
						if (isSuccess(response)) {
							updateViews(parseToList(response));
						}
					}

				},showLoading);

	}

	private void updateViews(Object obj) {
		if (obj == null) {
			return;
		}
		ArrayList<T> list = (ArrayList<T>) obj;
		listView.onLoadMoreComplete();
		if (list.size() == 0) {
			listView.removeLoadMoreListener();
			return;
		}
		beans.addAll(list);
		if(layoutId==0){
			Log.i("tag", "BaseRefreshListFragment-->No layoutId found");
			return;
		}
		notifyDataSetChanged();
	}

	protected void notifyDataSetChanged() {
		if (adapter == null) {
			adapter = new CommonAdapter<T>(context, beans, layoutId) {
				@Override
				public void setValues(ViewHolder helper, T item, int position) {
					createItem(helper, item, position);
				}
			};
			if(listViewType==NORMAL){
				listView.setAdapter(adapter);
			}else if(listViewType==EXPANDABLE){
				listView.setAdapter(new SlideExpandableListAdapter(adapter,
					R.id.expandable_toggle_button, R.id.expandable));
			}
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 从第一页开始，重新加载数据
	 * 用于刷新界面
	 */
	public void reloadItems(){
		refreshListView();
		doCommandForList(url, map,false);
	}
	
	private void refreshListView() {
		listView.resetLoadMoreListener();
		pagination.reset();
		beans.clear();
	}

	public ArrayList<T> getItems() {
		return beans;
	}

	public T getItemAt(int position){
		return beans.get(position);
	}
	
	public com.widget.view.LoadMoreListView getListView() {
		return listView;
	}

	/**
	 * 用于网络返回数据后解析数据到成ArrayList<T> beans
	 * 
	 * @param response
	 * @return
	 */
	public abstract ArrayList<T> parseToList(JSONObject response);

	/**
	 * 用于在Adapter中生成ItemView
	 * 
	 * @param helper
	 * @param item
	 * @param position
	 */
	public abstract void createItem(ViewHolder helper, final T item,
			final int position);

}
