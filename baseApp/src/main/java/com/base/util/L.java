package com.base.util;

import android.util.Log;

/**
 * Log统一管理类
 * 
 */
/**
 * //用法 
 * public void testLog() { 
 * 		L.i(TAG, L.getThreadName()+" say something"); 
 * }
 * 
 * @author Administrator
 *
 */
public class L {
	private static String TAG = "tag";

	// public static boolean DEBUG = Log.isLoggable(TAG, Log.VERBOSE);
	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

	private L() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	public static void setTag(String tag) {
		TAG = tag;
	}

	public static void i(String tag, String content) {
		if (isDebug)
			Log.d(TAG + "_" + tag, content);
	}
	public static void w(String content) {
		if (isDebug)
			Log.w(TAG, content);
	}

	/**
	 * 得到调用此方法的线程的线程名
	 * 
	 * @return
	 */
	public static String getThreadName() {
		StringBuffer sb = new StringBuffer();
		sb.append(Thread.currentThread().getName());
		sb.append("-> ");
		sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
		sb.append("()");
		sb.append(" ");
		return sb.toString();
	}

}