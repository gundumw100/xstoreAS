package com.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.xstore.App;
import com.app.xstore.R;
import com.app.xstore.SettingPrinterActivity;
import com.app.xstore.ThemeChangeActivity;
import com.app.xstore.mine.BusinessSettingActivity;
import com.app.xstore.mine.StoreSettingActivity;


/**
 * 试衣
 * @author pythoner
 *
 */
public class Main3Fragment extends BaseFragment implements View.OnClickListener {

	
	public static Main3Fragment newInstance(String param1) {
		Main3Fragment fragment = new Main3Fragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_main_3, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
//		updateTheme(view);
	}
	
	@Override
	public void initViews(View view){
		TextView tv_name=$(view,R.id.tv_name);
		TextView tv_storeId=$(view,R.id.tv_storeId);
		TextView tv_storeName=$(view,R.id.tv_storeName);

		$(view,R.id.btn_store_setting).setOnClickListener(this);
		$(view,R.id.btn_business_setting).setOnClickListener(this);

		tv_name.setText(App.user.getUserInfo().getUser_name()+"("+App.user.getUserInfo().getUser_code()+")");
		tv_storeId.setText("门店ID："+App.user.getShopInfo().getShop_code());
		tv_storeName.setText("门店名称："+App.user.getShopInfo().getShop_name());

		$(view,R.id.tv_skin).setOnClickListener(this);

		TextView tv_local_ip=(TextView)view.findViewById(R.id.tv_local_ip);
		tv_local_ip.setText("本机IP:  "+(TextUtils.isEmpty(getIP())?"未知":getIP()));

		TextView tv_version=(TextView)view.findViewById(R.id.tv_version);
		tv_version.setText("软件版本:  "+App.config.getVersionName());
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
			case R.id.btn_store_setting:
				intent=new Intent(context,StoreSettingActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_business_setting:
				intent=new Intent(context,BusinessSettingActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_print:
				intent=new Intent(context,SettingPrinterActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_email:
				break;
			case R.id.tv_skin:
				intent=new Intent(context,ThemeChangeActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}

	}

	@Override
	public void updateViews(Object obj) {
	}
	
//	@Subscriber
//	void updateByEventBus(String event) {
//		if(event.equals(App.EVENT_SAVE_FITTING)){
//		}
//	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
	}
	
	/**
	 * 获得本机IP
	 *
	 * @return
	 */
	public String getIP() {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();// 获取本机IP地址
			return intToIp(ipAddress);
		}
		return null;
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

}
