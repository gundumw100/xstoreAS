package com.base.app;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.base.R;
import com.base.net.RequestManager;
import com.base.util.L;

/**
 * 基类，所有Activity必须继承此类，提供一些工具方法
 * 
 * @author pythoner
 * 
 */
public abstract class BaseAppActivity extends FragmentActivity{

	public static final String tag = "tags";
	protected Context context;
	public BaseApp app;
	private TextView btn_left, btn_right;
	private TextView tv_title;
	private View actionView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		app = (BaseApp) getApplication();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * findViewById的简便写法，直接一个$方法搞定，类似JQuery语法
	 * @param viewID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends View> T $(int viewID) {
	    return (T) findViewById(viewID);
	}
	
	/**
	 * 左右两边分别带一个按钮的通用的ActionBar
	 * 
	 * @param leftBtn
	 * @param drawableForLeftBtn
	 * @param title
	 * @param rightBtn
	 * @param drawableForRightBtn
	 */
	protected void initActionBar(String leftBtn, Drawable drawableForLeftBtn,
			String title, String rightBtn, Drawable drawableForRightBtn) {
		
		actionView =findViewById(R.id.actionbar);
		tv_title = (TextView) findViewById(R.id.tv_title);
		if (tv_title != null) {
			tv_title.setText(title);
		}
		btn_left = (TextView) findViewById(R.id.btn_left);
		if (btn_left != null) {
			if(leftBtn==null&&drawableForLeftBtn==null){
				btn_left.setVisibility(View.GONE);
			}else{
				btn_left.setVisibility(View.VISIBLE);
				btn_left.setText(leftBtn==null?"返回":leftBtn);
				if (drawableForLeftBtn != null) {
					btn_left.setCompoundDrawablePadding(8);
					drawableForLeftBtn.setBounds(0, 0,
							drawableForLeftBtn.getMinimumWidth(),
							drawableForLeftBtn.getMinimumHeight());
					btn_left.setCompoundDrawables(drawableForLeftBtn, null, null,
							null);
				}else{
					btn_left.setCompoundDrawables(null, null, null,null);
				}
	
				btn_left.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doLeftButtonClick(v);
					}
				});
			}
		}

		btn_right = (TextView) findViewById(R.id.btn_right);
		if (btn_right != null) {
			if(rightBtn==null&&drawableForRightBtn==null){
				btn_right.setVisibility(View.GONE);
			}else{
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText(rightBtn==null?"":rightBtn);
	
				if (drawableForRightBtn != null) {
					btn_right.setCompoundDrawablePadding(8);
					drawableForRightBtn.setBounds(0, 0,
							drawableForRightBtn.getMinimumWidth(),
							drawableForRightBtn.getMinimumHeight());
					btn_right.setCompoundDrawables(null, null, drawableForRightBtn,
							null);
				}else{
					btn_right.setCompoundDrawables(null, null, null,null);
				}
	
				btn_right.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doRightButtonClick(v);
					}
				});
			}
		}
		
	}

	public View getActionView() {
//		actionView.setVisibility(View.VISIBLE);
		return actionView;
	}
	public TextView getTitleView() {
//		actionView.setVisibility(View.VISIBLE);
		return tv_title;
	}
	public TextView getRightButton() {
		btn_right.setVisibility(View.VISIBLE);
		return btn_right;
	}

	public TextView getLeftButton() {
		btn_left.setVisibility(View.VISIBLE);
		return btn_left;
	}

	/**
	 * 用于重设ActionBar中的标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		tv_title.setText(text);
	}

	/**
	 * 按下右上角执行该方法，需要override该方法
	 */
	public void doRightButtonClick(View v) {

	}

	/**
	 * 按下左上角执行该方法，默认关闭该activity，必要时需要override该方法
	 */
	public void doLeftButtonClick(View v) {
		finish();
	}

	/**
	 * 执行动画
	 * 
	 * @param context
	 * @param view
	 * @param animId
	 */
	public void doAnimation(Context context, View view, @AnimRes int animId) {
		Animation animation = AnimationUtils.loadAnimation(context, animId);
		view.startAnimation(animation);
	}

	/**
	 * 滑动条滑动动画
	 * 
	 * @param v
	 * @param oldPos
	 * @param newPos
	 * @param width
	 */
	public void doAnimation(View v, int oldPos, int newPos, int width) {
		TranslateAnimation animation = new TranslateAnimation(oldPos * width,
				newPos * width, 0, 0);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(200);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

	/**
	 * 显示软键盘
	 * 
	 * @param view
	 */
	public void showSoftInput(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 隐藏软键盘（不依赖具体的EditText）
	 */
	public void hideSoftInput() {
		View v = ((Activity) context).getCurrentFocus();
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	protected Dialog dialog;

	/**
	 * 网络连接时显示Loading
	 */
	public void showProgress() {
		if(isFinishing()){
			return;
		}
		if (dialog == null) {
			dialog = new Dialog(this, R.style.Theme_TransparentDialog);
			dialog.setContentView(R.layout.custom_progress_dialog);
//			dialog.setContentView(new ProgressBar(getApplicationContext()));
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	public void closeProgress() {
		if (dialog != null&&!isFinishing()) {
			dialog.cancel();
			dialog = null;
		}
	}

	private static Toast toast;

	public void showToast(String text) {
		try{
			if (null == toast) {
				toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
			} else {
				toast.setText(text);
			}
			toast.show();
		}catch(Exception e){
			
		}
	}

	public void showToast(@StringRes int resId) {
		try{
			if (null == toast) {
				toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
			} else {
				toast.setText(resId);
			}
			toast.show();
		}catch(Exception e){
			
		}
	}

	/**
	 * 为TextView设置CompoundDrawable
	 * 
	 * @param resId
	 * @param tv
	 * @param ltrb
	 */
	public void setCompoundDrawable(@DrawableRes int resId, TextView tv, int ltrb) {
		Drawable drawable = getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		switch (ltrb) {
		case 0:
			tv.setCompoundDrawables(drawable, null, null, null);
			break;
		case 1:
			tv.setCompoundDrawables(null, drawable, null, null);
			break;
		case 2:
			tv.setCompoundDrawables(null, null, drawable, null);
			break;
		case 3:
			tv.setCompoundDrawables(null, null, null, drawable);
			break;

		default:
			tv.setCompoundDrawables(null, null, null, null);
			break;
		}
	}

	/**
	 * 网络请求返回成功与否
	 * 每家公司返回的数据结构可能不一样，需要override该方法
	 * @param response
	 * @return
	 */
	public boolean isSuccess(JSONObject response) {
		closeProgress();
		JSONObject result = response.optJSONObject("result");
		String status = result.optString("status");
		if ("OK".equalsIgnoreCase(status)) {
			return true;
		} else {
			String message = result.optString("message");
			String code = result.optString("code");
			//

			L.i("code=" + code + ";message=" + message);
			if (code.equals("4000")) {// 用户会话过期，请重新登录
										// dosomething
			} else {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}

//	/**
//	 * 为一个TextView设置多种字体（大小，颜色，背景色等）
//	 * 
//	 * @param tv
//	 * @param title
//	 * @param content
//	 */
//	public void createSpannableTextView(SpannableTextView tv, String title,
//			String content) {
//		// clear pieces
//		tv.reset();
//		// Add the first piece
//		tv.addPiece(new SpannableTextView.Piece.Builder(title)
//				.textColor(BaseApp.res.getColor(R.color.primary))
//				.textSize((int) BaseApp.res.getDimension(R.dimen.font_xbig))
//				.build());
//
//		// Add the second piece
//		tv.addPiece(new SpannableTextView.Piece.Builder(content)
//				.textColor(BaseApp.res.getColor(R.color.primary))
//				.textSize((int) BaseApp.res.getDimension(R.dimen.font_middle))
//				.build());
//
//		// Display the final, styled text
//		tv.display();
//	}
//
//	/**
//	 * 
//	 * @param v
//	 */
//	public void extractMention2Link(TextView v) {
//		String text1 = "登陆";
//		SpannableString spannableString1 = new SpannableString(text1);
//		spannableString1.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(View widget) {
//				// Intent intent = new Intent(context, SiginActivity.class);
//				// startActivity(intent);
//			}
//		}, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		String text2 = "注册";
//		SpannableString spannableString2 = new SpannableString(text2);
//		spannableString2.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(View widget) {
//				// Intent intent = new Intent(context, RegisterActivity.class);
//				// intent.putExtra("isRegister", false);
//				// context.startActivity(intent);
//			}
//		}, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		v.append("您还未登陆，请");
//		v.append(spannableString1);
//		v.append("或者");
//		v.append(spannableString2);
//		v.setMovementMethod(LinkMovementMethod.getInstance());
//	}
//
//	/**
//	 * 使用TransitionDrawable实现图片显示的时候有渐变效果,比使用AlphaAnimation效果要好，可避免出现闪烁问题
//	 * 
//	 * @param imageView
//	 * @param bitmap
//	 */
//	@SuppressWarnings("deprecation")
//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//	public void setImageBitmap(ImageView imageView, Bitmap bitmap) {
//		// Use TransitionDrawable to fade in.
//		TransitionDrawable td = new TransitionDrawable(new Drawable[] {
//				new ColorDrawable(android.R.color.transparent),
//				new BitmapDrawable(context.getResources(), bitmap) });
//		// noinspection deprecation
//		if(android.os.Build.VERSION.SDK_INT>=16){
//			imageView.setBackground(imageView.getDrawable());
//		}else{
//			imageView.setBackgroundDrawable(imageView.getDrawable());
//		}
//		imageView.setImageDrawable(td);
//		td.startTransition(200);
//	}

	/**
	 * 格式化Money，结构为0.00，传入的是Number
	 * @param money
	 * @return
	 */
	public String formatMoney(Number money){
    	return formatNumber(money,"###0.00");
    }
	
	/**
	 * 格式化Money，结构为0.00，传入的是String
	 * @param money
	 * @return
	 */
	public String formatMoney(String money){
		if(TextUtils.isEmpty(money)){
			return "0.00";
		}
		return formatMoney(Double.parseDouble(money));
	}
	
	/**
	 * 格式化数字，格式通过formatString自定义
	 * @param money
	 * @return
	 */
	public String formatNumber(Number number,String formatString){
    	if(number==null){
    		return null;
    	}
    	if(TextUtils.isEmpty(formatString)){
    		return String.valueOf(number);
    	}
    	DecimalFormat format = new DecimalFormat(formatString);
    	return format.format(number);
    }
	
	/**
	 * android自带的DateUtils获得时间
	 * @param millis
	 * @param flag a string containing the formatted date/time.
	 * @return
	 */
	public String formatDate(long millis,int flag){
		return DateUtils.formatDateTime(context, millis, flag);
	}
	
	/**
	 * 获得当前的时间，格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String formatNow(){
		return formatNow("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获得当前的时间，格式由format指定
	 * @param format
	 * @return
	 */
	public String formatNow(String format){
		Calendar c=Calendar.getInstance();
		return formatTime(c.getTimeInMillis(),format);
	}
	
	/**
	 * long类型时间格式化,格式yyyy-MM-dd HH:mm:ss
	 * @param millis
	 * @return
	 */
	public String formatTime(long millis) {
		return formatTime(millis,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * long类型时间格式化，式化由format指定
	 * @param millis
	 * @param format
	 * @return
	 */
	public String formatTime(long millis,String format) {
		SimpleDateFormat df = new SimpleDateFormat(format,Locale.CHINA);
		Date date = new Date(millis);
		return df.format(date);
	}
	
	/**
	 * 是否开启了GPS
	 * @return
	 */
	public boolean isGPSAvailable() {
		LocationManager alm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		return alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
	}
	
	/**
	 * 判断某App是否已安装
	 * @param packageName
	 * @return
	 */
	public boolean isAppInstalled(String packageName) {
		try {
			getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 将文件的大小由字节转换成KB、MB甚至G。再也不用手动去和1024较真了
	 * @param number
	 * @return
	 */
	public String formatSize(long number){
		return Formatter.formatFileSize(context,number);
	}
	
	/**
	 * 可以很方便的创建视频的缩略图，甚至还可以指定缩略图的尺寸
	 * @param source
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap extractThumbnail(Bitmap source, int width, int height){
		return ThumbnailUtils.extractThumbnail(source, width, height);
	}
	
	/**
	 * 判断字符创是否为空或null
	 * @param text
	 * @return
	 */
	public boolean isEmpty(String text){
		return TextUtils.isEmpty(text);
	}
	
	/**
	 * 处理url链接的方法，通过key获取参数
	 * @param url
	 * @param key
	 * @return
	 */
	public String getParam(String url, String key) {
		UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
		sanitizer.setAllowUnregisteredParamaters(true);
		return sanitizer.getValue(key);
	}
	
	/**
	 * PhoneNumverUtils提供了一系列方法用来格式化电话号码
	 * @param phoneNumber
	 * @return
	 */
	public String formatPhoneNumber(String phoneNumber) {
		return PhoneNumberUtils.formatNumber(phoneNumber);
	}

	/**
	 * 跳转到拨号界面
	 * @param phoneNumber
	 */
	public void doDial(String phoneNumber){
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
