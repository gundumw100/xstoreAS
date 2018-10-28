package com.base.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.base.R;
import com.base.util.IOUtil;

public class CustomErrorListener implements Response.ErrorListener{
	private Context context;
	public CustomErrorListener(Context context){
		this.context = context;
	}
	@Override
	public void onErrorResponse(VolleyError e) {
		IOUtil.writeToSDCard("data.txt","VolleyError:\n"+e.getMessage()+"\n"+e.getLocalizedMessage()+"\n");
		e.printStackTrace();
		String errorMessage = context.getResources().getString(R.string.error_json);
		if (e instanceof TimeoutError) {
			errorMessage = context.getResources().getString(R.string.error_timeout);
		} else if (e instanceof AuthFailureError) {
			errorMessage = context.getResources().getString(R.string.error_auth_failure);
		} else if (e instanceof ServerError) {
			errorMessage = context.getResources().getString(R.string.error_server);
		} else if (e instanceof NetworkError) {
			errorMessage = context.getResources().getString(R.string.error_network);
		} else if (e instanceof NoConnectionError) {
			errorMessage = context.getResources().getString(R.string.error_no_connection);
		} else {
			errorMessage = context.getResources().getString(R.string.error_json);
		}
		if(this.errorListener != null){
			this.errorListener.returnErrorMessage(errorMessage);
		}
	}
	private currErrorListener errorListener;
	public void setErrorListener(currErrorListener errorListener) {
		this.errorListener = errorListener;
	}
	public interface currErrorListener {
		void returnErrorMessage(String errorMessage);
	}
}