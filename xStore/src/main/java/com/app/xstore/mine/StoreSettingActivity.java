package com.app.xstore.mine;

import android.os.Bundle;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 门店设置
 * @author pythoner
 * 
 */
public class StoreSettingActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_store_setting);
		initActionBar("门店设置", null, null);
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
}
