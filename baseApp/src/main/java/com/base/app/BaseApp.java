package com.base.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.litepal.LitePalApplication;

import com.base.model.Config;
import com.base.net.RequestManager;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 启动类，初始化App所必需的参数等数据
 * 
 * @author pythoner
 * 
 */
public class BaseApp extends LitePalApplication {
	
	public static final String TAG = "tag";
	public static boolean DEBUG = true;//开发阶段
//	public static boolean needsCatchCrash = true;//捕获异常，开发阶段不需要（打印异常于控制台），发布版本需要
	public static boolean hasNetwork = true;
	public static DisplayMetrics dm;
	public static Resources res;//
	public static Config config = new Config();

	@Override
	public void onCreate() {
		super.onCreate();
		initConfig();
		initVolley();
	}

	private void initVolley() {
		RequestManager.init(this);
	}

	private void initConfig() {
		res = getResources();
		dm = res.getDisplayMetrics();
		// 初始化系统信息
		config.setTotalMemory(this.getTotalMemory());
		config.setAvailMemory(this.getAvailMemory());
		config.setDensityDpi(dm.densityDpi);
		config.setScreenWidth(dm.widthPixels);
		config.setScreenHeight(dm.heightPixels);
		config.setStatusBarHeight(getStatusBarHeight());
		config.setModel(android.os.Build.MODEL);
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		config.setDeviceId(tm.getDeviceId());//android.os.Build.SERIAL
		config.setSdk(android.os.Build.VERSION.SDK_INT);
		config.setRelease(android.os.Build.VERSION.RELEASE);
		String androidID = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);
		config.setAndroidID(androidID);
		try {
			PackageInfo packInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			config.setVersionCode(packInfo.versionCode);
			config.setVersionName(packInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, config.toString());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	private String getAvailMemory() {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
	}

	private String getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {

		}
		return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @return
	 */
	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
