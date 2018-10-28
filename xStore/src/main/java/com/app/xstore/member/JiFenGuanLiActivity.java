package com.app.xstore.member;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.app.model.Member;
import com.app.widget.dialog.JiFenTiaoZhengDialog;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分管理
 * @author pythoner
 * 
 */
public class JiFenGuanLiActivity extends BaseActivity implements View.OnClickListener{

	private Member member;
	private ListView listView;
	private CommonAdapter<Member> adapter;
	private List<Member> beans=new ArrayList<Member>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_jifenguanli);
		member=getIntent().getParcelableExtra("Member");
		initActionBar("积分调整单", "新增", null);
		beans.add(new Member());
		beans.add(new Member());
		beans.add(new Member());
		beans.add(new Member());
		initViews();
		updateViews(beans);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_jifen=$(R.id.tv_jifen);
		tv_jifen.setText("当前积分余额："+member.getTotalPoints());
		listView=$(R.id.listView);
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(beans.size()==0){
			if(listView.getEmptyView()==null){
				setEmptyView(listView, "暂无数据");
			}
		}
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<Member>(
					context, beans, R.layout.item_member_jifenguanli) {

				@Override
				public void setValues(ViewHolder helper,
									  final Member item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_danhao, "零售单号：");
					helper.setText(R.id.item_mobile, "调整单号：");
					helper.setText(R.id.item_createtimeStr, "调整日期：");
					helper.setText(R.id.item_totalPoints, "积分：");
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		EventBus.getDefault().post(App.EVENT_REFRESH);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doRightButtonClick(View v) {
		JiFenTiaoZhengDialog dialog=new JiFenTiaoZhengDialog(context,member);
		dialog.show();
	}
}
