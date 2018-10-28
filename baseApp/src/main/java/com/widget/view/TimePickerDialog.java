package com.widget.view;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.base.R;
import com.widget.wheel.NumericWheelAdapter;
import com.widget.wheel.WheelView;

/**
 * 
 * TimePickerDialog d=new TimePickerDialog(context);
			d.setOnButtonClickListener(new TimePickerDialog.OnButtonClickListener() {
				
				@Override
				public void onOKClick(String hour, String minute) {
					// TODO Auto-generated method stub
					showToast(hour+":"+minute);
				}

			});
			d.show();
			
 * 可以试试这个：https://github.com/jjobes/SlideDateTimePicker
 * 
 * @author pythoner
 * 
 */
public class TimePickerDialog extends Dialog {

	private Button btn_left, btn_right;
	private WheelView hour, minute;
	private String date;// 初始化显示的日期，默认为当日

	public TimePickerDialog(Context context) {
		// super(context);
		this(context, R.style.Theme_Dialog_NoTitle, null);
		// TODO Auto-generated constructor stub
	}

	public TimePickerDialog(Context context, String date) {
		this(context, R.style.Theme_Dialog_NoTitle , date);
	}

	public TimePickerDialog(Context context, int theme,String date) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.date = date;
		init();
	}

	private void init() {
		this.setCanceledOnTouchOutside(true);
		this.setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_time_picker);
		initViews();
		initValues();
	}

	private void initViews() {
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_left.setOnClickListener(clickListener);
		btn_right.setOnClickListener(clickListener);

		int length="yyyy-MM-dd HH:mm:ss".length();
		int icurHour,icurMinute;
		Calendar c = Calendar.getInstance();
		if (date == null || date.length() < length) {// 格式不正确
			icurHour = c.get(Calendar.HOUR_OF_DAY)-1;
			icurMinute = c.get(Calendar.MINUTE)-1;
		}else{
			icurHour = Integer.parseInt(date.substring(11, 13))-1;
			icurMinute = Integer.parseInt(date.substring(14, 16))-1;
		}

		hour = (WheelView) findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(1, 23));
		hour.setCyclic(true);
		hour.setCurrentItem(icurHour);
		hour.setLabel("时");

		minute = (WheelView) findViewById(R.id.minute);
		minute.setAdapter(new NumericWheelAdapter(1, 59));// "%02d"
		minute.setLabel("分");
		minute.setCyclic(true);
		minute.setCurrentItem(icurMinute);
	}

	private void initValues() {

	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			if (id == R.id.btn_left) {
				dismiss();
			} else if (id == R.id.btn_right) {
				String mHour = hour.getAdapter().getItem(hour.getCurrentItem());
				String mMinute = minute.getAdapter().getItem(minute.getCurrentItem());
				if (Integer.parseInt(mHour) < 10) {
					mHour = "0" + mHour;
				}
				if (Integer.parseInt(mMinute) < 10) {
					mMinute = "0" + mMinute;
				}
				if (onClickListener != null) {
					onClickListener.onOKClick(mHour, mMinute);
				}
				dismiss();
			}
		}

	};

	private OnButtonClickListener onClickListener;

	public interface OnButtonClickListener {
		public void onOKClick(String hour, String minute);
	}

	public void setOnButtonClickListener(OnButtonClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
}
