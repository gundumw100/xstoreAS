package com.app.xstore.member;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.widget.dialog.SimpleEditTextSingleDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.D;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 会员标签管理
 * @author pythoner
 * 
 */
public class MemberLabelsActivity extends BaseActivity implements View.OnClickListener{

	private String vipCode;
	private final String classification="VIP_A";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_labels);
		initActionBar("会员标签", null, null);
		initViews();
		vipCode=getIntent().getStringExtra("VipCode");
		if(vipCode==null){
			showToast("VipCode can't be null");
			return;
		}
		doCommandGetVipLabelList();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.tv_a).setOnClickListener(this);
		$(R.id.tv_b).setOnClickListener(this);
		$(R.id.tv_c).setOnClickListener(this);
		$(R.id.tv_d).setOnClickListener(this);
		$(R.id.tv_e).setOnClickListener(this);
		$(R.id.tv_zhiye).setOnClickListener(this);
		$(R.id.tv_jinjishili).setOnClickListener(this);
		$(R.id.tv_kehuleixing).setOnClickListener(this);
		$(R.id.tv_chuanyifenge).setOnClickListener(this);
		$(R.id.tv_xihuanyanse).setOnClickListener(this);

		initZhiYeFlowLayout();
		initJinjishiliFlowLayout();
		initKehuleixingFlowLayout();
		initChuanyifengeFlowLayout();
		initXihuanyanseFlowLayout();

		initAFlowLayout();
		initBFlowLayout();
		initCFlowLayout();
		initDFlowLayout();
		initEFlowLayout();
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private TagFlowLayout flowLayout_zhiye;
	private TagAdapter<VipLabel> zhiyeAdapter;
	private List<VipLabel> zhiyeList=new ArrayList<VipLabel>();
	private void initZhiYeFlowLayout(){
		flowLayout_zhiye=(TagFlowLayout)findViewById(R.id.flowLayout_zhiye);
		flowLayout_zhiye.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_zhiye,zhiyeList, position);
				return true;
			}
		});
		flowLayout_zhiye.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {

			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(zhiyeList.get(position));
				return true;
			}
		});

		notifyDataChangedZhiye();
	}

	private void notifyDataChangedZhiye(){
		if(zhiyeAdapter==null){
			flowLayout_zhiye.setAdapter(zhiyeAdapter=new TagAdapter<VipLabel>(zhiyeList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_zhiye, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_zhiye.notifyDataChanged();
		}
	}

	private TagFlowLayout flowLayout_jinjishili;
	private TagAdapter<VipLabel> jinjishiliAdapter;
	private List<VipLabel> jinjishiliList=new ArrayList<VipLabel>();
	private void initJinjishiliFlowLayout(){
		flowLayout_jinjishili=(TagFlowLayout)findViewById(R.id.flowLayout_jinjishili);
		flowLayout_jinjishili.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_jinjishili,jinjishiliList, position);
				return true;
			}
		});
		flowLayout_jinjishili.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {

			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(jinjishiliList.get(position));
				return true;
			}
		});

		notifyDataChangedJinjishili();
	}

	private void notifyDataChangedJinjishili(){
		if(jinjishiliAdapter==null){
			flowLayout_jinjishili.setAdapter(jinjishiliAdapter=new TagAdapter<VipLabel>(jinjishiliList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_jinjishili, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_jinjishili.notifyDataChanged();
		}
	}

	private TagFlowLayout flowLayout_kehuleixing;
	private TagAdapter<VipLabel> kehuleixingAdapter;
	private List<VipLabel> kehuleixingList=new ArrayList<VipLabel>();
	private void initKehuleixingFlowLayout(){
		flowLayout_kehuleixing=(TagFlowLayout)findViewById(R.id.flowLayout_kehuleixing);
		flowLayout_kehuleixing.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_kehuleixing,kehuleixingList, position);
				return true;
			}
		});
		flowLayout_kehuleixing.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {

			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(kehuleixingList.get(position));
				return true;
			}
		});

		notifyDataChangedKehuleixing();
	}

	private void notifyDataChangedKehuleixing(){
		if(kehuleixingAdapter==null){
			flowLayout_kehuleixing.setAdapter(kehuleixingAdapter=new TagAdapter<VipLabel>(kehuleixingList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_kehuleixing, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_kehuleixing.notifyDataChanged();
		}
	}

	private TagFlowLayout flowLayout_chuanyifenge;
	private TagAdapter<VipLabel> chuanyifengeAdapter;
	private List<VipLabel> chuanyifengeList=new ArrayList<VipLabel>();
	private void initChuanyifengeFlowLayout(){
		flowLayout_chuanyifenge=(TagFlowLayout)findViewById(R.id.flowLayout_chuanyifenge);
		flowLayout_chuanyifenge.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_chuanyifenge,chuanyifengeList, position);
				return true;
			}
		});
		flowLayout_chuanyifenge.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {

			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(chuanyifengeList.get(position));
				return true;
			}
		});

		notifyDataChangedChuanyifenge();
	}

	private void notifyDataChangedChuanyifenge(){
		if(chuanyifengeAdapter==null){
			flowLayout_chuanyifenge.setAdapter(chuanyifengeAdapter=new TagAdapter<VipLabel>(chuanyifengeList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_chuanyifenge, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_chuanyifenge.notifyDataChanged();
		}
	}

	private TagFlowLayout flowLayout_xihuanyanse;
	private TagAdapter<VipLabel> xihuanyanseAdapter;
	private List<VipLabel> xihuanyanseList=new ArrayList<VipLabel>();
	private void initXihuanyanseFlowLayout(){
		flowLayout_xihuanyanse=(TagFlowLayout)findViewById(R.id.flowLayout_xihuanyanse);
		flowLayout_xihuanyanse.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_xihuanyanse,xihuanyanseList, position);
				return true;
			}
		});
		flowLayout_xihuanyanse.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {

			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(xihuanyanseList.get(position));
				return true;
			}
		});

		notifyDataChangedXihuanyanse();
	}

	private void notifyDataChangedXihuanyanse(){
		if(xihuanyanseAdapter==null){
			flowLayout_xihuanyanse.setAdapter(xihuanyanseAdapter=new TagAdapter<VipLabel>(xihuanyanseList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_xihuanyanse, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_xihuanyanse.notifyDataChanged();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private TagFlowLayout flowLayout_a;
	private TagAdapter<VipLabel> aAdapter;
	private List<VipLabel> aList=new ArrayList<VipLabel>();
	private void initAFlowLayout(){
		flowLayout_a=(TagFlowLayout)findViewById(R.id.flowLayout_a);
		flowLayout_a.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_a,aList, position);
				return true;
			}
		});
		flowLayout_a.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(aList.get(position));
				return true;
			}
		});
		
		notifyDataChangedA();
	}
	
	private void notifyDataChangedA(){
		if(aAdapter==null){
			flowLayout_a.setAdapter(aAdapter=new TagAdapter<VipLabel>(aList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_a, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_a.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_b;
	private TagAdapter<VipLabel> bAdapter;
	private List<VipLabel> bList=new ArrayList<VipLabel>();
	private void initBFlowLayout(){
		flowLayout_b=(TagFlowLayout)findViewById(R.id.flowLayout_b);
		flowLayout_b.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_b,bList, position);
				return true;
			}
		});
		flowLayout_b.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(bList.get(position));
				return true;
			}
		});

		notifyDataChangedB();
	}
	
	private void notifyDataChangedB(){
		if(bAdapter==null){
			flowLayout_b.setAdapter(bAdapter=new TagAdapter<VipLabel>(bList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_b, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_b.notifyDataChanged();
		}
	}
	
	
	private TagFlowLayout flowLayout_c;
	private TagAdapter<VipLabel> cAdapter;
	private List<VipLabel> cList=new ArrayList<VipLabel>();
	private void initCFlowLayout(){
		flowLayout_c=(TagFlowLayout)findViewById(R.id.flowLayout_c);
		flowLayout_c.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_c,cList, position);
				return true;
			}
		});
		flowLayout_c.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(cList.get(position));
				return true;
			}
		});
		notifyDataChangedC();
	}
	
	private void notifyDataChangedC(){
		if(cAdapter==null){
			flowLayout_c.setAdapter(cAdapter=new TagAdapter<VipLabel>(cList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_c, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_c.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_d;
	private TagAdapter<VipLabel> dAdapter;
	private List<VipLabel> dList=new ArrayList<VipLabel>();
	private void initDFlowLayout(){
		flowLayout_d=(TagFlowLayout)findViewById(R.id.flowLayout_d);
		flowLayout_d.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_d,dList, position);
				return true;
			}
		});
		flowLayout_d.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(dList.get(position));
				return true;
			}
		});
		notifyDataChangedD();
	}
	
	private void notifyDataChangedD(){
		if(dAdapter==null){
			flowLayout_d.setAdapter(dAdapter=new TagAdapter<VipLabel>(dList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_d, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_d.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_e;
	private TagAdapter<VipLabel> eAdapter;
	private List<VipLabel> eList=new ArrayList<VipLabel>();
	private void initEFlowLayout(){
		flowLayout_e=(TagFlowLayout)findViewById(R.id.flowLayout_e);
		flowLayout_e.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_e,eList, position);
				return true;
			}
		});
		flowLayout_e.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(eList.get(position));
				return true;
			}
		});
		notifyDataChangedE();
	}
	
	private void notifyDataChangedE(){
		if(eAdapter==null){
			flowLayout_e.setAdapter(eAdapter=new TagAdapter<VipLabel>(eList){
				@Override
				public View getView(FlowLayout parent, int position, VipLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_e, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_e.notifyDataChanged();
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().post(App.EVENT_REFRESH);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showDialog(v);
	}
	
	private void showDialog(View v){
		final TextView tv=(TextView)v;
		SimpleEditTextSingleDialog d=new SimpleEditTextSingleDialog(context, "", "请输入标签名称");
		d.setOnClickListener(new SimpleEditTextSingleDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				String property=(String)tv.getTag();
				if(property==null){
					showToast("标签未定义");
					return;
				}
				doCommandAddVipLabel(property,text);
			}
		});
		d.show();
	}
	

	private void doCommandAddVipLabel(final String property,final String description){
		Commands.doCommandAddVipLabel(context, classification, property, description, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					AddVipLabelResponse obj=context.mapperToObject(response, AddVipLabelResponse.class);
					if(obj.getInfo()!=null){
						VipLabel bean=new VipLabel();
						bean.setClassification(classification);
						bean.setProperty(property);
//						bean.setLabelCodeFullName(classification+property+obj.getInfo());
						bean.setLabelCode(obj.getInfo());
//						bean.setLabelDesc(description);
						bean.setDescription(description);

						addOrDeleteLabel(true,bean);
					}

				}
			}
		});

	}
	
	private void doCommandDeleteVipLabel(final VipLabel bean){
		Commands.doCommandDeleteVipLabel(context, bean.getLabelCode(), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					addOrDeleteLabel(false,bean);
				}
			}
		});
	}
	
	private void addOrDeleteLabel(boolean isAdd,VipLabel bean){
		if("职业".equals(bean.getProperty())){
			if(isAdd){
				zhiyeList.add(bean);
			}else{
				zhiyeList.remove(bean);
			}
			notifyDataChangedZhiye();
		}else if("经济实力".equals(bean.getProperty())){
			if(isAdd){
				jinjishiliList.add(bean);
			}else{
				jinjishiliList.remove(bean);
			}
			notifyDataChangedJinjishili();
		}else if("客户类型".equals(bean.getProperty())){
			if(isAdd){
				kehuleixingList.add(bean);
			}else{
				kehuleixingList.remove(bean);
			}
			notifyDataChangedKehuleixing();
		}else if("穿衣风格".equals(bean.getProperty())){
			if(isAdd){
				chuanyifengeList.add(bean);
			}else{
				chuanyifengeList.remove(bean);
			}
			notifyDataChangedChuanyifenge();
		}else if("喜欢颜色".equals(bean.getProperty())){
			if(isAdd){
				xihuanyanseList.add(bean);
			}else{
				xihuanyanseList.remove(bean);
			}
			notifyDataChangedXihuanyanse();
		}
		else if("A".equals(bean.getProperty())){
			if(isAdd){
				aList.add(bean);
			}else{
				aList.remove(bean);
			}
			notifyDataChangedA();
		}else if("B".equals(bean.getProperty())){
			if(isAdd){
				bList.add(bean);
			}else{
				bList.remove(bean);
			}
			notifyDataChangedB();
		}else if("C".equals(bean.getProperty())){
			if(isAdd){
				cList.add(bean);
			}else{
				cList.remove(bean);
			}
			notifyDataChangedC();
		}else if("D".equals(bean.getProperty())){
			if(isAdd){
				dList.add(bean);
			}else{
				dList.remove(bean);
			}
			notifyDataChangedD();
		}else if("E".equals(bean.getProperty())){
			if(isAdd){
				eList.add(bean);
			}else{
				eList.remove(bean);
			}
			notifyDataChangedE();
		}
	}
	
	private void doCommandGetVipLabelList(){
		Commands.doCommandGetVipLabelList(context, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
//				{"ErrMessage":"","Result":true,"Info":[{"enabled":1,"property":"A","LabelCode":"0001","description":"学生","classification":"VIP_A","companyCode":"C001"},{"enabled":1,"property":"A","LabelCode":"0002","description":"同事","classification":"VIP_A","companyCode":"C001"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}
				if (context.isSuccess(response)) {
					GetVipLabelListResponse obj=context.mapperToObject(response, GetVipLabelListResponse.class);
					if(obj.getInfo()!=null){
						for(VipLabel item:obj.getInfo()){
							if(classification.equals(item.getClassification())){
								if("职业".equals(item.getProperty())){
									zhiyeList.add(item);
								}else if("经济实力".equals(item.getProperty())){
									jinjishiliList.add(item);
								}else if("客户类型".equals(item.getProperty())){
									kehuleixingList.add(item);
								}else if("穿衣风格".equals(item.getProperty())){
									chuanyifengeList.add(item);
								}else if("喜欢颜色".equals(item.getProperty())){
									xihuanyanseList.add(item);
								}
								else if("A".equals(item.getProperty())){
									aList.add(item);
								}else if("B".equals(item.getProperty())){
									bList.add(item);
								}else if("C".equals(item.getProperty())){
									cList.add(item);
								}else if("D".equals(item.getProperty())){
									dList.add(item);
								}else if("E".equals(item.getProperty())){
									eList.add(item);
								}
							}
						}
						
						notifyDataChangedZhiye();
						notifyDataChangedJinjishili();
						notifyDataChangedKehuleixing();
						notifyDataChangedChuanyifenge();
						notifyDataChangedXihuanyanse();
						notifyDataChangedA();
						notifyDataChangedB();
						notifyDataChangedC();
						notifyDataChangedD();
						notifyDataChangedE();

						doCommandGetVipLabelMappingList();
						
					}
					
				}
			}
		});
	}
	
	private void showDeleteDialog(final VipLabel bean){
		String message="确定要删除吗？";
		D.showDialog(this, message, "确定", "取消", new D.OnPositiveListener() {
			
			@Override
			public void onPositive() {
				// TODO Auto-generated method stub
				doCommandDeleteVipLabel(bean);
			}
		});
	}
	
	private void doCommandAddVipLabelMapping(String labelCode){
		Commands.doCommandAddVipLabelMapping(context, vipCode, labelCode,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
				}
			}
		});
	}
	private void doCommandDeleteVipLabelMapping(String labelCode){
		Commands.doCommandDeleteVipLabelMapping(context,  vipCode, labelCode,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
				}
			}
		});
	}
	
	private void doCommandGetVipLabelMappingList(){
		Commands.doCommandGetVipLabelMappingList(context, vipCode,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					GetVipLabelListResponse obj=context.mapperToObject(response, GetVipLabelListResponse.class);
					List<VipLabel> checkedBeans=obj.getInfo();
					if(!isEmptyList(checkedBeans)){
						setCheckToFlowLayout(flowLayout_zhiye,zhiyeList,checkedBeans);
						setCheckToFlowLayout(flowLayout_jinjishili,jinjishiliList,checkedBeans);
						setCheckToFlowLayout(flowLayout_kehuleixing,kehuleixingList,checkedBeans);
						setCheckToFlowLayout(flowLayout_chuanyifenge,chuanyifengeList,checkedBeans);
						setCheckToFlowLayout(flowLayout_xihuanyanse,xihuanyanseList,checkedBeans);
						setCheckToFlowLayout(flowLayout_a,aList,checkedBeans);
						setCheckToFlowLayout(flowLayout_b,bList,checkedBeans);
						setCheckToFlowLayout(flowLayout_c,cList,checkedBeans);
						setCheckToFlowLayout(flowLayout_d,dList,checkedBeans);
						setCheckToFlowLayout(flowLayout_e,eList,checkedBeans);
					}

				}
			}
		});
	}
	
	private void setCheckToFlowLayout(TagFlowLayout flowLayout,List<VipLabel> allBeans,List<VipLabel> checkedBeans){
		HashSet<Integer> aSet=new HashSet<Integer>();
		for(int i=0;i<allBeans.size();i++){
			for(VipLabel checkedBean:checkedBeans){
				if(allBeans.get(i).getLabelCode().equals(checkedBean.getLabelCode())){
					aSet.add(i);
				}
			}
		}
		flowLayout.setCheckedAt(aSet);
	}
	
	private void addOrDeleteLabelMapping(TagFlowLayout flowLayout,List<VipLabel> list,int position){
		VipLabel bean=list.get(position);
		if(flowLayout.isChecked(position)){
			doCommandAddVipLabelMapping(bean.getLabelCode());
		}else{
			doCommandDeleteVipLabelMapping(bean.getLabelCode());
		}
	}
	
}
