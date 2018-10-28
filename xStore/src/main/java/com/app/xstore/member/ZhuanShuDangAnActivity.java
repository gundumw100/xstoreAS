package com.app.xstore.member;

import android.os.Bundle;
import android.view.View;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 专属档案
 * @author pythoner
 * 
 */
public class ZhuanShuDangAnActivity extends BaseActivity implements View.OnClickListener{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_zhuanshudangan);
		initActionBar("专属档案", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
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
	

	
}
