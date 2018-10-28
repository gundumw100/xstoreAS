package com.app.xstore.member;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.app.model.Member;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 会员详情
 * 
 * @author pythoner
 * 
 */
public class MemberDetailActivity extends BaseActivity implements OnClickListener{

	private Member member;
	private TagFlowLayout flowLayout_labels;
	private LineChartView lineChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_detail);
		member=getIntent().getParcelableExtra("Member");
		initActionBar("会员详情", null, null);
		initViews();

		doCommandGetVipLabelMappingList(member.getVipCode());
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub

		TextView tv_name=$(R.id.tv_name);
		TextView tv_jifen=$(R.id.tv_jifen);
		TextView tv_yue=$(R.id.tv_yue);
		TextView tv_zhuceriqi=$(R.id.tv_zhuceriqi);
		tv_name.setText(member.getName());
		tv_jifen.setText("积分："+member.getTotalPoints());
		tv_yue.setText("账户余额：");
		tv_zhuceriqi.setText("注册日期："+member.getCreatetimeStr());

		final TextView btn_labels_toggle=$(R.id.btn_labels_toggle);
		btn_labels_toggle.setText("[显示标签]");
		flowLayout_labels=(TagFlowLayout)findViewById(R.id.flowLayout_labels);
		flowLayout_labels.setEnabled(false);
		flowLayout_labels.setVisibility(View.GONE);
		btn_labels_toggle.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				String text=btn_labels_toggle.getText().toString();
				if(text.contains("显示")){
					flowLayout_labels.setVisibility(View.VISIBLE);
					btn_labels_toggle.setText("[隐藏标签]");
				}else{
					flowLayout_labels.setVisibility(View.GONE);
					btn_labels_toggle.setText("[显示标签]");
				}
			}
		});

		$(R.id.btn_call).setOnClickListener(this);
		$(R.id.btn_wx).setOnClickListener(this);
		$(R.id.btn_watch).setOnClickListener(this);
		$(R.id.btn_recharge).setOnClickListener(this);
		$(R.id.btn_label).setOnClickListener(this);
		$(R.id.btn_archive).setOnClickListener(this);
		$(R.id.btn_jifenguanli).setOnClickListener(this);

		lineChart=$(R.id.lineChart);

		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(0, 2));
		values.add(new PointValue(1, 4));
		values.add(new PointValue(2, 3));
		values.add(new PointValue(3, 4));

		//In most cased you can call data model methods in builder-pattern-like manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);

		LineChartView chart = new LineChartView(context);
		lineChart.setLineChartData(data);

	}


	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
		case R.id.btn_call:
			if(isEmpty(member.getMobile())){
				showToast("该会员未注册电话号码");
				return;
			}
			doDial(member.getMobile());
			break;
		case R.id.btn_wx:
			intent=new Intent(Intent.ACTION_SEND);                            
			intent.setType("text/plain");                            
			intent.setPackage("com.tencent.mm");//intent.setPackage("com.sina.weibo");                            
			intent.putExtra(Intent.EXTRA_TEXT,  "无花果消息");
			startActivity(Intent.createChooser(intent, "请选择"));
			break;
		case R.id.btn_watch:
			break;
		case R.id.btn_recharge:
			break;
		case R.id.btn_label:
			intent=new Intent(context,MemberLabelsActivity.class);
			intent.putExtra("VipCode",member.getVipCode());
			startActivity(intent);
			break;
		case R.id.btn_archive:
			intent=new Intent(context,ZhuanShuDangAnActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_jifenguanli:
			intent=new Intent(context,JiFenGuanLiActivity.class);
			intent.putExtra("Member",member);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void doCommandGetVipLabelMappingList(String goodsCode){
		Commands.doCommandGetVipLabelMappingList(context, goodsCode,new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetVipLabelListResponse obj=mapperToObject(response, GetVipLabelListResponse.class);
					if(obj.getInfo()!=null){
						flowLayout_labels.setAdapter(new TagAdapter<VipLabel>(obj.getInfo()){
							@Override
							public View getView(FlowLayout parent, int position, VipLabel item){
								TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text_checked,flowLayout_labels, false);
								tv.setText(item.getDescription());
//								tv.setText(item.getLabelDesc());
								tv.setTag(item);
								return tv;
							}
						});
					}

				}
			}
		});
	}


	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_REFRESH)){
			doCommandGetVipLabelMappingList(member.getVipCode());
		}
	}


}
