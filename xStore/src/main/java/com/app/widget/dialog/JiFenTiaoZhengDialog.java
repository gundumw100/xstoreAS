package com.app.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.model.Member;
import com.app.widget.BaseDialog;
import com.app.xstore.App;
import com.app.xstore.R;

/**
 * 积分调整
 * @author pythoner
 * 
 */
public class JiFenTiaoZhengDialog extends BaseDialog {

	private TextView tv_name;
	private TextView tv_mobile;
	private TextView tv_userCode;

	private EditText et_jifen,et_reseason;
	private RadioGroup rg;
	private Member member;

	public JiFenTiaoZhengDialog(Context context, Member member) {
		this(context, R.style.Theme_Dialog_NoTitle,member);
		// TODO Auto-generated constructor stub
	}

	public JiFenTiaoZhengDialog(Context context, int theme, Member member) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.member=member;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_member_jifentiaozheng);
		initViews();
	}

	private void initViews() {
		if(member==null){
			return;
		}
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		tv_userCode = (TextView) findViewById(R.id.tv_userCode);
		et_jifen = (EditText) findViewById(R.id.et_jifen);
		et_reseason = (EditText) findViewById(R.id.et_reseason);
		rg = (RadioGroup) findViewById(R.id.rg);
		rg.check(R.id.rb_add);

		tv_name.setText("会员姓名："+member.getName());
		tv_mobile.setText("手机号码："+member.getMobile());
		tv_userCode.setText("操作人："+ App.user.getUserInfo().getUser_code());

		findViewById(R.id.btn_left).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
		findViewById(R.id.btn_right).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String text = et_jifen.getText().toString().trim();
						if (text.length() == 0) {
							doShake(et_jifen);
							return;
						}

						String reseason = et_reseason.getText().toString().trim();
						if (reseason.length() == 0) {
							doShake(et_reseason);
							return;
						}

						int points=Integer.parseInt(text);

						if (onOkClickListener != null) {
							onOkClickListener.onOkClick(v, points);
						}
						dismiss();
					}
				});
	}

	private OnOkClickListener onOkClickListener;

	public interface OnOkClickListener {
		public void onOkClick(View v, int points);
	}

	public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
		this.onOkClickListener = onOkClickListener;
	}


}
