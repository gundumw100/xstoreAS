package com.app.xstore.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 商家设置
 * @author pythoner
 * 
 */
public class BusinessSettingActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_business_setting);
		initActionBar("商家设置", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub

		RadioGroup rg_level=$(R.id.rg_level);
		final LinearLayout container=$(R.id.container);
		rg_level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_use_level){
					container.setVisibility(View.VISIBLE);
				}else if(checkedId==R.id.rb_unuse_level){
					container.setVisibility(View.GONE);
				}
			}
		});

		rg_level.check(R.id.rb_use_level);
	}
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}
}
