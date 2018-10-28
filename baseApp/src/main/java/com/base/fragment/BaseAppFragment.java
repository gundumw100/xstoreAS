package com.base.fragment;

import org.simple.eventbus.EventBus;

import com.base.app.BaseAppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基类，所有fragment必须继承此类，提供一些工具方法
 * 
 * @author pythoner
 * 
 */
public class BaseAppFragment extends Fragment {
	public static final String tag = "tags";

	public BaseAppActivity context;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (BaseAppActivity) getActivity();
		EventBus.getDefault().register(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	public void doLeftButtonClick() {
		if (getActivity() != null && !isDetached()) {
			getActivity().finish();
		}
	}

	public void doRightButtonClick() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
