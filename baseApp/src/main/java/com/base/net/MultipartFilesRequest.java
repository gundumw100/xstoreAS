package com.base.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * volley上传文件，需要httpmime.jar扩展包支持
 * 返回的是Json格式数据
 * 
 * @author pythoner
 *
 */
public class MultipartFilesRequest extends Request<JSONObject> {
	private final String TAG = "MultipartFilesRequest";

	private MultipartEntity entity = new MultipartEntity();

	private final Response.Listener<JSONObject> mListener;

	private List<File> mFileParts;
	private String mFilePartName;
	private Map<String, String> mParams;
	private AccessToken mAccessToken;

	/**
	 * 单个文件
	 * 
	 * @param url
	 * @param errorListener
	 * @param listener
	 * @param filePartName
	 * @param file
	 * @param params
	 */
	public MultipartFilesRequest(String url, String filePartName, File file,
			Map<String, String> params, Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mFileParts = new ArrayList<File>();
		if (file != null) {
			mFileParts.add(file);
		}
		mFilePartName = filePartName;
		mListener = listener;
		mParams = params;
		buildMultipartEntity();
	}
	public MultipartFilesRequest(AccessToken accessToken,String url, String filePartName, File file,
			Map<String, String> params, Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mFileParts = new ArrayList<File>();
		if (file != null) {
			mFileParts.add(file);
		}
		mAccessToken = accessToken;
		mFilePartName = filePartName;
		mListener = listener;
		mParams = params;
		buildMultipartEntity();
	}

	/**
	 * 多个文件，对应一个key
	 * 
	 * @param url
	 * @param errorListener
	 * @param listener
	 * @param filePartName
	 * @param files
	 * @param params
	 */
	public MultipartFilesRequest(String url, String filePartName,
			List<File> files, Map<String, String> params,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mFilePartName = filePartName;
		mListener = listener;
		mFileParts = files;
		mParams = params;
		buildMultipartEntity();
	}
	public MultipartFilesRequest(AccessToken accessToken,String url, String filePartName,
			List<File> files, Map<String, String> params,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mAccessToken = accessToken;
		mFilePartName = filePartName;
		mListener = listener;
		mFileParts = files;
		mParams = params;
		buildMultipartEntity();
	}

	private void buildMultipartEntity() {
		if (mFileParts != null && mFileParts.size() > 0) {
			for (File file : mFileParts) {
				entity.addPart(mFilePartName, new FileBody(file));
			}
			long l = entity.getContentLength();
			Log.i(TAG, mFileParts.size() + "个，长度：" + l);
		}

		try {
			if (mParams != null && mParams.size() > 0) {
				for (Map.Entry<String, String> entry : mParams.entrySet()) {
					entity.addPart(
							entry.getKey(),
							new StringBody(entry.getValue(), Charset
									.forName("UTF-8")));
				}
			}
		} catch (UnsupportedEncodingException e) {
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (JSONException je)
        {
            return Response.error(new ParseError(je));
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#getHeaders()
	 */
//	@Override
//	public Map<String, String> getHeaders() throws AuthFailureError {
//		VolleyLog.d("getHeaders");
//		Map<String, String> headers = super.getHeaders();
//
//		if (headers == null || headers.equals(Collections.emptyMap())) {
//			headers = new HashMap<String, String>();
//		}
//		if(mAccessToken!=null){
//	        headers.put("token", mAccessToken.getToken());
//	        headers.put("userCode", mAccessToken.getUserCode());
//	        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
//	        headers.put("appId", mAccessToken.getAppId());
//	        
//	        String sign=sign(headers,mAccessToken.getSecretKey());
//	        
//	        headers.put("sign", sign);
//		}
//		return headers;
//	}
//
//	/**
//	   * 数字签名算法
//	   * 
//	   * */
//	public static String sign(Map<String, String> params, String secretKey) {
//		String md5Hex=null;
//		try {
//			Collection<String> keyset = params.keySet();
//			List<String> list = new ArrayList<String>(keyset);
//			Collections.sort(list);
//			// 对传入的参数进行字典排序
//			String str = "";
//			// 按照规则对字符串进行拼接
//			for (int i = 0; i < list.size(); i++) {
//				str += list.get(i) + "=" + params.get(list.get(i)) + "&";
//			}
//			str = secretKey + "+" + str + "+" + secretKey;
////			Log.i("tag", "str="+str);
//			// Md5Hash
//			md5Hex = DigestUtils.md5Hex(str.getBytes("UTF-8"));
////			Log.i("tag", "md5Hex="+md5Hex);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return md5Hex;
//	}
	  
	
	@Override
	protected void deliverResponse(JSONObject response) {
		if(mListener!=null){
			mListener.onResponse(response);
		}
	}
}
