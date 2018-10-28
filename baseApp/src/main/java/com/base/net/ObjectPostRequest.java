package com.base.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 
 * @author pythoner
 * 
 */
public class ObjectPostRequest extends Request<JSONObject> {

	private Map<String, String> mMap;
	private Listener<JSONObject> mListener;
	private AccessToken mAccessToken;

	public ObjectPostRequest(AccessToken accessToken,String url, Map<String, String> map,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(Request.Method.POST, url, errorListener);
		mAccessToken = accessToken;
		mListener = listener;
		mMap = map;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	// 传送
	@Override
	protected void deliverResponse(JSONObject response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}

	// 此处因为response返回值是json数据,和JsonObjectRequest类一样即可
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		String data=null;
		try {
			data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(data), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
//			IOUtil.writeToSDCard("data.txt","UnsupportedEncodingException-->\n"+data);
			return Response.error(new ParseError(e));
		} catch (JSONException e) {
//			IOUtil.writeToSDCard("data.txt","JSONException-->\n"+data);
			return Response.error(new ParseError(e));
		}
	}
	
//	@Override  
//    public Map<String, String> getHeaders() throws AuthFailureError {
//		Map<String, String> headers = null ;
//		headers = new HashMap<String, String>();
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
//        return headers;  
//    }  
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
	  
}
