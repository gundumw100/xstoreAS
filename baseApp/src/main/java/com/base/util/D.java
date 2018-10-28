package com.base.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.base.R;
/**
 * Dialogs
 * 
 * @author pythoner
 *
 */
public class D {

	private D()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
	/**
	 * 接口，用于处理Dialog中按下Positive发生的事件
	 * 
	 * @author pythoner
	 * 
	 */
	public interface OnPositiveListener {
		void onPositive();
	}

	/**
	 * 接口,用于处理Dialog中按下Negative发生的事件
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnNegativeListener {
		void onNegative();
	}
	
	/**
	 * 
	 * @param activity
	 * @param message
	 * @param positiveBtnStr
	 */
	public static void showDialog(final Activity activity,String message, String positiveBtnStr) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positiveBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog dialog=builder.create();
		setTitleView(activity,dialog);
		dialog.show();
		setTextSize(dialog);
	}
	
	/**
	 * 
	 * @param activity
	 * @param message
	 * @param positiveBtnStr
	 * @param onPositiveListener
	 */
	public static void showDialog(final Activity activity,String message, String positiveBtnStr,final OnPositiveListener onPositiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positiveBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (onPositiveListener != null) {
									onPositiveListener.onPositive();
								}
							}
						});
		AlertDialog dialog=builder.create();
		setTitleView(activity,dialog);
		dialog.show();
		setTextSize(dialog);
	}
	
	/**
	 * 提供一个简单快捷的Dialog
	 * 自定义内容区域显示文字,两个按钮显示文字,只需要实现OnPositiveListener即可自定义PositiveButton事件，NegativeButton直接为关闭dialog
	 * 
	 * @param activity
	 * @param message
	 * @param positiveBtnStr
	 * @param negativeBtnStr
	 * @param onPositiveListener
	 */
	public static void showDialog(final Activity activity,String message, String positiveBtnStr, String negativeBtnStr, final OnPositiveListener onPositiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positiveBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (onPositiveListener != null) {
									onPositiveListener.onPositive();
								}
							}
						})
				.setNegativeButton(negativeBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog dialog=builder.create();
		setTitleView(activity,dialog);
		dialog.show();
		setTextSize(dialog);
	}
	
	
	/**
	 * 提供一个简单快捷的Dialog
	 * 		自定义内容区域显示文字,两个按钮显示文字,两个按钮的点击事件
	 * 
	 * @param messageId
	 * @param positiveBtnStr
	 * @param negativeBtnStr
	 * @param onPositiveListener
	 * @param negativeListener
	 */
	public static void showDialog(final Activity activity,String message, String positiveBtnStr, String negativeBtnStr, final OnPositiveListener onPositiveListener, final OnNegativeListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positiveBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (onPositiveListener != null) {
									onPositiveListener.onPositive();
								}
							}
						})
				.setNegativeButton(negativeBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if(negativeListener != null){
									negativeListener.onNegative();
								}
							}
						});
		AlertDialog dialog=builder.create();
		setTitleView(activity,dialog);
		dialog.show();
		setTextSize(dialog);
	}
	/**
	 * 提供一个简单快捷的Dialog
	 * 		自定义内容区域显示文字,两个按钮显示文字,两个按钮的点击事件,及取消事件监听
	 *  
	 * @param activity
	 * @param message
	 * @param positiveBtnStr
	 * @param negativeBtnStr
	 * @param onPositiveListener
	 * @param negativeListener
	 * @param cancelListener
	 */
	public static void showDialog(final Activity activity,String message, String positiveBtnStr, String negativeBtnStr, final OnPositiveListener onPositiveListener, final OnNegativeListener negativeListener, DialogInterface.OnCancelListener cancelListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setCancelable(true)
				.setPositiveButton(positiveBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (onPositiveListener != null) {
									onPositiveListener.onPositive();
								}
							}
						})
				.setNegativeButton(negativeBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if(negativeListener != null){
									negativeListener.onNegative();
								}
							}
						});
		AlertDialog dialog=builder.create();
		dialog.setOnCancelListener(cancelListener);
		setTitleView(activity,dialog);
		dialog.show();
		setTextSize(dialog);
	}
	
	/**
	 * 提供一个简单快捷的Dialog，实现OnPositiveListener即可自定义事件
	 * 
	 * @param messageId
	 * @param onPositiveListener
	 */
	public static void showDialog(final Activity activity,int messageId,final OnPositiveListener onPositiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(messageId)
				.setCancelable(true)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (onPositiveListener != null) {
									onPositiveListener.onPositive();
								}
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog dialog=builder.create();
		setTitleView(activity,dialog);
		dialog.show();
		setTextSize(dialog);
	}
	
	/**
	 * 内存不足
	 * @param activity
	 */
	public static void showNoEnoughMemoryDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(R.string.alert_no_enough_memory)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								activity.finish();
							}
						});
		builder.create().show();
	}
	
	/**
	 * 检查是否有内容，如果有，弹出对话框确认是否需要关闭activity
	 * 
	 * @param content
	 * @return
	 */
	public static boolean checkContent(final Activity activity,String content) {
		if (!TextUtils.isEmpty(content)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(R.string.alert_no_content)
					.setCancelable(true)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									activity.finish();
								}
							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog dialog=builder.create();
			setTitleView(activity,dialog);
			dialog.show();
			setTextSize(dialog);
			return true;
		}
		return false;
	}
	
	/**
	 * 打开网络设置
	 * @param activity
	 */
	public static void openWirelessSet(final Activity activity) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
		dialogBuilder
				.setTitle(R.string.prompt)
				.setMessage(activity.getString(R.string.alert_check_connection))
				.setPositiveButton(R.string.setting,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								Intent intent = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								activity.startActivity(intent);
							}
						})
				.setNegativeButton(R.string.close,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});
		dialogBuilder.create().show();
	}
	
//	/**
//	 * 提供一个简单快捷的多选的Dialog
//	 * @param activity
//	 * @param items
//	 * @param checkedItems
//	 * @param onPositiveListener
//	 */
//	public static void showDialog(Activity activity,String[] items,final boolean[] checkedItems ,final OnItemSelectListener onItemSelectListener) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//		builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener(){
//			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//				checkedItems[which]=isChecked;
//				if(onItemSelectListener!=null){
//					onItemSelectListener.onItemSelect(which);
//				}
//            }
//		})
//				.setCancelable(true);
//		AlertDialog dialog=builder.create();
//		setTitleView(activity,dialog);
//		dialog.show();
//		setTextSize(dialog);
//	}
	/**
	 * 提供一个简单快捷的单选的Dialog
	 * @param activity
	 * @param items
	 * @param checkedItems
	 * @param onPositiveListener
	 */
	public static void showDialog(Activity activity,String[] items,int checkedItems ,final OnItemSelectListener onItemSelectListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setSingleChoiceItems(items,checkedItems ,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(onItemSelectListener!=null){
					onItemSelectListener.onItemSelect(which);
				}
			}
		})
		.setCancelable(true);
		AlertDialog dialog=builder.create();
		dialog.show();
		setTextSize(dialog);
	}
//	
//	/**
//	 * 提供一个简单快捷的列表Dialog
//	 * @param activity
//	 * @param items
//	 * @param onPositiveListener
//	 */
//	public static void showDialog(Activity activity,String[] items,final OnItemSelectListener onItemSelectListener) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//		builder.setItems(items, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				if(onItemSelectListener!=null){
//					onItemSelectListener.onItemSelect(which);
//				}
//			}
//		})
//		.setCancelable(true);
//		AlertDialog dialog=builder.create();
//		dialog.show();
//		setTextSize(dialog);
//	}
	
	public interface OnItemSelectListener {
		void onItemSelect(int which);
	}
	
	
	/**
	 * 
	 * @param activity
	 * @param message
	 * @param positiveBtnStr
	 * @param onPositiveListener
	 */
	public static void showForceDialog(final Activity activity,String message, String positiveBtnStr,final OnPositiveListener onPositiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(positiveBtnStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								if (onPositiveListener != null) {
									onPositiveListener.onPositive();
								}
							}
						});
		builder.create().show();
	}
	
	private static void setTextSize(AlertDialog dialog){
		try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            mMessageView.setPadding(16, 32, 16, 0);
            mMessageView.setGravity(Gravity.CENTER_VERTICAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
		
		setLine(dialog);
		setButtons(dialog);
	}
	
	private static void setTitleView(Activity activity,AlertDialog dialog){
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		View mTitleView = layoutInflater.inflate(R.layout.dialog_comm_title, null);
		dialog.setCustomTitle(mTitleView);
	}
	
	private static void setLine(AlertDialog dialog){
		int divierId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null); 
		View divider = dialog.findViewById(divierId); 
		divider.setBackgroundColor(0x00000000); 
	}
	
	private static void setButtons(AlertDialog dialog){
		Button buttonPositive=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		buttonPositive.setPadding(16, 16, 16, 16);
		buttonPositive.setBackgroundResource(R.drawable.bg_btn_dialog);
		
		Button buttonNegative=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		buttonNegative.setPadding(16, 16, 16, 16);
		buttonNegative.setBackgroundResource(R.drawable.bg_btn_dialog);
	}
	
}
