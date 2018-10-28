package com.base.net;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.app.base.R;
import com.base.app.BaseApp;
import com.base.app.BaseAppActivity;
import com.base.util.IOUtil;
import com.base.util.T;
import com.widget.effect.CircleImageDrawable;
import com.widget.effect.RoundImageDrawable;

/**
 * 
 * @author pythoner
 * 
 */
public class VolleyHelper {

	public static final int TYPE_RECT = 0;// 直角
	public static final int TYPE_RECT_ROUND = 1;// 圆角
	public static final int TYPE_CIRCLE = 2;// 圆形

	private VolleyHelper() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 执行Post请求，默认显示loading进度
	 * 
	 * @param context
	 * @param url
	 * @param map
	 * @param onSucListener
	 */
	public static void execPostRequest(Context context, AccessToken accessToken,String url, Map<String, String> map,
			Listener<JSONObject> onSuccessListener) {
		execPostRequest(context,accessToken, url, map, onSuccessListener, true);
	}
	
	/**
	 * 执行Post请求，可控制loading的有无
	 * 
	 * @param context
	 * @param url
	 * @param map
	 * @param onSucListener
	 * @param showProgress
	 */
	public static void execPostRequest(Context context, AccessToken accessToken,String url, Map<String, String> map,
			Listener<JSONObject> onSuccessListener, boolean showProgress) {
		int SOCKET_TIMEOUT = 30 * 1000;//超时时间
		ObjectPostRequest request = new ObjectPostRequest(accessToken,url, map, onSuccessListener, errorListener(context));
		// 对Request设置重试策略，加长默认超时时间，以防服务器端响应缓慢时发送多次请求的问题
//		DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
		request.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT, 0,// DefaultRetryPolicy.DEFAULT_MAX_RETRIES
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, showProgress);
	}
	
	public static void execPostRequest(Context context, AccessToken accessToken,String url, Map<String, String> map,
			Listener<JSONObject> onSuccessListener,Response.ErrorListener errorListener) {
		int SOCKET_TIMEOUT = 30 * 1000;//超时时间
		ObjectPostRequest request = new ObjectPostRequest(accessToken,url, map, onSuccessListener, errorListener);
		// 对Request设置重试策略，加长默认超时时间，以防服务器端响应缓慢时发送多次请求的问题
		request.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT, 0,// DefaultRetryPolicy.DEFAULT_MAX_RETRIES
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, false);
	}
	public static void execPostRequest(Context context, AccessToken accessToken,String url, Map<String, String> map,
			Listener<JSONObject> onSuccessListener,Response.ErrorListener errorListener, boolean showProgress) {
		int SOCKET_TIMEOUT = 30 * 1000;//超时时间
		ObjectPostRequest request = new ObjectPostRequest(accessToken,url, map, onSuccessListener, errorListener);
		// 对Request设置重试策略，加长默认超时时间，以防服务器端响应缓慢时发送多次请求的问题
		request.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT, 0,// DefaultRetryPolicy.DEFAULT_MAX_RETRIES
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, showProgress);
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param onSuccessListener
	 */
	public static void execPostRequest(Context context, String url, JSONObject params,
			Listener<JSONObject> onSuccessListener) {
		execPostRequest(context, url, params, onSuccessListener, true);
	}
	/**
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param onSuccessListener
	 * @param showProgress
	 */
	public static void execPostRequest(Context context, String url, JSONObject params,
			Listener<JSONObject> onSuccessListener, boolean showProgress) {
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url, params,
				onSuccessListener, errorListener(context));
		// 对Request设置重试策略，加长默认超时时间，以防服务器端响应缓慢时发送多次请求的问题
		//http://blog.sina.com.cn/s/blog_4b5bc0110102vrb2.html
		//http://stackoverflow.com/questions/22428343/android-volley-double-post-when-have-slow-request
//		request.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
//				 0,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
//				 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, showProgress);
	}
	
	/**
	 * 发送Get请求
	 * @author Tan Chenglong
	 * @param context
	 * @param url
	 * @param params
	 * @param onSuccessListener
	 * @param showProgress
	 */
//	public static void execGetRequest(Context context, String url,Listener<String> onSuccessListener, boolean showProgress) {
//		StringRequest request = new StringRequest(Request.Method.GET,url, onSuccessListener, errorListener(context));
//		request.setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		execRequest(request, context, url, showProgress);
//	}
	public static void execGetRequest(Context context, String url,Listener<JSONObject> onSuccessListener, boolean showProgress) {
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, null, onSuccessListener, errorListener(context));
		request.setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, showProgress);
	}
	
	/**
	 * 加入请求到队列
	 * 
	 * @param request
	 * @param context
	 * @param tag
	 * @param showProgress
	 */
	private static <T> void execRequest(Request<T> request, Context context, String tag, boolean showProgress) {
		if (BaseApp.hasNetwork) {
			if (showProgress) {
				BaseAppActivity activity=((BaseAppActivity) context);
				activity.showProgress();
			}
			RequestManager.addRequest(request, tag);
		} else {
			Toast.makeText(context, R.string.error_no_network, Toast.LENGTH_SHORT).show();
		}
	}

	// ----------------------------uploadFile part--------------------------------

	/**
	 * 上传单个文件
	 * 
	 * @param context
	 * @param url
	 * @param filePartName
	 *            文件域名
	 * @param file
	 *            文件File对象
	 * @param map
	 *            其他文本字段
	 * @param onSuccessListener
	 *            成功后回调
	 * @param showProgress
	 */
	public static void execPostRequest(Context context, String url, String filePartName, File file,
			Map<String, String> map, Listener<JSONObject> onSuccessListener, boolean showProgress) {
		MultipartFilesRequest request = new MultipartFilesRequest(url, filePartName, file, map, onSuccessListener,
				errorListener(context));
		execRequest(request, context, url, showProgress);
	}

	public static void execPostRequest(Context context, AccessToken accessToken,String url, String filePartName, File file,
			Map<String, String> map, Listener<JSONObject> onSuccessListener, boolean showProgress) {
		MultipartFilesRequest request = new MultipartFilesRequest(accessToken,url, filePartName, file, map, onSuccessListener,
				errorListener(context));
		execRequest(request, context, url, showProgress);
	}
	/**
	 * 上传多个文件，对应一个key
	 * 
	 * @param context
	 * @param url
	 * @param filePartName
	 *            文件域名
	 * @param files
	 *            文件File对象列表
	 * @param map
	 *            其他文本字段
	 * @param onSuccessListener
	 * @param showProgress
	 */
	public static void execPostRequest(Context context, String url, String filePartName, List<File> files,
			Map<String, String> map, Listener<JSONObject> onSuccessListener, boolean showProgress) {
		MultipartFilesRequest request = new MultipartFilesRequest(url, filePartName, files, map, onSuccessListener,
				errorListener(context));
		request.setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, showProgress);
	}
	public static void execPostRequest(Context context,AccessToken accessToken, String url, String filePartName, List<File> files,
			Map<String, String> map, Listener<JSONObject> onSuccessListener, boolean showProgress) {
		MultipartFilesRequest request = new MultipartFilesRequest(accessToken,url, filePartName, files, map, onSuccessListener,
				errorListener(context));
		request.setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		execRequest(request, context, url, showProgress);
	}
	// public interface OnFileUploadListener {
	// public void onFileUploadSuccess(String orignUrl, String imgUrl,
	// String circleImgUrl);
	// }
	//
	// public static void uploadFile(Context context, String url, int fileType,
	// String sessionKey, String filePath, final OnFileUploadListener
	// onFileUploadListener) {
	// MultipartRequestParams params = new MultipartRequestParams();
	// params.put("user_session_key", sessionKey);
	// params.put("file_category", String.valueOf(fileType));
	//// params.put("platform", "2");
	// File file = new File(filePath);
	// if (file.exists()) {
	// if (fileType == 1) {
	// params.put("img", file);
	// } else {
	// params.put("audio", file);
	// }
	// } else {
	// Log.i("tag", "File is not exist.");
	// return;
	// }
	// Listener<JSONObject> successListener = new Listener<JSONObject>() {
	//
	// @Override
	// public void onResponse(JSONObject response) {
	//// Log.i("tag", "response=" + response.toString());
	// JSONObject result = response.optJSONObject("result");
	// int code = result.optInt("code");
	// if (code == 2000) {
	// JSONObject data = response.optJSONObject("data");
	// JSONObject url = data.optJSONObject("urls");
	// String orignUrl = url.optString("origin", null);
	// String imgUrl = url.optString("100_100", null);
	// String circleImgUrl = url.optString("100_100_circle", null);
	// if (onFileUploadListener != null) {
	// onFileUploadListener.onFileUploadSuccess(orignUrl, imgUrl,circleImgUrl);
	// }
	// }
	// }
	// };
	//
	// MultipartRequest request = new MultipartRequest(url, params,
	// successListener,errorListener(context));
	// execRequest(request, context, filePath, false);
	// }

	// ----------------------------load image part--------------------------------

	public interface OnLoadImageListener {
		void onLoadImageSuccess(View view, ImageContainer imageContainer);
		void onLoadImageFail(View view);
	}

	/**
	 * 重构，利用Volley异步加载图片到任意View,
	 * 加载完成后的具体操作由OnLoadImageListener回调onLoadImageSuccess(View
	 * view,ImageContainer imageContainer)决定 图片大小由View决定
	 * 
	 * @param imageUrl
	 * @param view
	 * @param onLoadImageSuccessListener
	 */
	public static void loadImageByVolley(String imageUrl, final View view,
			final OnLoadImageListener onLoadImageListener) {
		ImageSize imageSize = getImageViewWidth(view);
		int reqWidth = imageSize.width;
		int reqHeight = imageSize.height;
		loadImageByVolley(imageUrl, view, reqWidth, reqHeight, onLoadImageListener);
	}

	/**
	 * 利用Volley异步加载图片到任意View,加载完成后的具体操作由OnLoadImageListener回调onLoadImageSuccess(
	 * View view,ImageContainer imageContainer)决定
	 * 
	 * @param imageUrl
	 * @param view
	 * @param maxWidth
	 * @param maxHeight
	 * @param onLoadImageSuccessListener
	 */
	public static void loadImageByVolley(String imageUrl, final View view, final int maxWidth, final int maxHeight,
			final OnLoadImageListener onLoadImageListener) {
		if (TextUtils.isEmpty(imageUrl) || !imageUrl.startsWith("http")) {
			if (onLoadImageListener != null) {
				onLoadImageListener.onLoadImageFail(view);
			}
			return;
		}
		RequestManager.getImageLoader().get(imageUrl, new ImageListener() {

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				if (onLoadImageListener != null) {
					onLoadImageListener.onLoadImageSuccess(view, response);
				}
			}

			@Override
			public void onErrorResponse(VolleyError e) {
				if (onLoadImageListener != null) {
					onLoadImageListener.onLoadImageFail(view);
				}
			}
		}, maxWidth, maxHeight);
	}

	/**
	 * 重构，利用Volley异步加载图片到ImageView,可显示0直角 1圆角 2圆形 图片大小由ImageView决定
	 * 注意：图片过大可能会出错，最好采用指定大小的ImageView
	 * 
	 * @param imageUrl
	 * @param view
	 * @param defaultImageResId
	 * @param type
	 */
	public static void loadImageByVolley(String imageUrl, final ImageView view,
			@DrawableRes final int defaultImageResId, final int type) {
		ImageSize imageSize = getImageViewWidth(view);
		int reqWidth = imageSize.width;
		int reqHeight = imageSize.height;
		loadImageByVolley(imageUrl, view, defaultImageResId, defaultImageResId, reqWidth, reqHeight, type);
	}

	/**
	 * 利用Volley异步加载图片到ImageView,可显示0直角 1圆角 2圆形
	 * 
	 * @param imageUrl
	 * @param iv
	 * @param defaultImageResId
	 * @param errorImageResId
	 * @param maxHeight
	 * @param maxWidth
	 * @param type：0直角 1圆形 2圆角
	 * 
	 */
	public static void loadImageByVolley(String imageUrl, final ImageView iv, @DrawableRes final int defaultImageResId,
			@DrawableRes final int errorImageResId, final int maxWidth, final int maxHeight, final int type) {
		if (TextUtils.isEmpty(imageUrl) || !imageUrl.startsWith("http")) {
			iv.setImageResource(defaultImageResId);
			return;
		}
		RequestManager.getImageLoader().get(imageUrl, new ImageListener() {

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				String imageUrl = response.getRequestUrl();
				Bitmap bmp = response.getBitmap();
				if (bmp != null) {
					if (type == TYPE_RECT) {
						iv.setImageBitmap(bmp);
					} else if (type == TYPE_RECT_ROUND) {
						// Bitmap b = null;
						// b = ImageUtil.toRoundConerImage(bmp, 10);
						// iv.setImageBitmap(b);
						iv.setImageDrawable(new RoundImageDrawable(bmp));
					} else if (type == TYPE_CIRCLE) {
						// b = ImageUtil.toCircleBitmap(bmp);
						// iv.setImageBitmap(b);
						iv.setImageDrawable(new CircleImageDrawable(bmp));
					}

				} else {
					iv.setImageResource(defaultImageResId);
				}
			}

			@Override
			public void onErrorResponse(VolleyError e) {
				iv.setImageResource(errorImageResId);
			}
		}, maxWidth, maxHeight);

	}

	/**
	 * 利用Volley异步加载图片到ImageView,直角
	 * 
	 * @param imageUrl
	 * @param iv
	 * @param defaultImageResId
	 * @param errorImageResId
	 */
	public static void loadImageByVolley(String imageUrl, ImageView iv, @DrawableRes int defaultImageResId) {
		ImageListener listener = ImageLoader.getImageListener(iv, defaultImageResId, defaultImageResId);
		RequestManager.getImageLoader().get(imageUrl, listener);
	}

	/**
	 * 利用NetworkImageView显示网络图片
	 * 
	 * @param imageUrl
	 * @param networkImageView
	 * @param defaultImageResId
	 */
	public static void loadImageByNetworkImageView(String imageUrl, NetworkImageView networkImageView,
			@DrawableRes int defaultImageResId) {
		loadImageByNetworkImageView(imageUrl, networkImageView, defaultImageResId, defaultImageResId);
	}

	/**
	 * 利用NetworkImageView显示网络图片
	 * 
	 * @param imageUrl
	 * @param networkImageView
	 * @param defaultImageResId
	 * @param errorImageResId
	 */
	public static void loadImageByNetworkImageView(String imageUrl, NetworkImageView networkImageView,
			@DrawableRes int defaultImageResId, @DrawableRes int errorImageResId) {
		networkImageView.setDefaultImageResId(defaultImageResId);
		networkImageView.setErrorImageResId(errorImageResId);
		networkImageView.setTag(imageUrl);
		networkImageView.setImageUrl(imageUrl, RequestManager.getImageLoader());
	}

	/**
	 * 网络连接出错后的统一处理方式
	 * 
	 * @param context
	 * @return
	 */
	public static Response.ErrorListener errorListener(final Context context) {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError e) {
				((BaseAppActivity) context).closeProgress();
				IOUtil.writeToSDCard("data.txt","VolleyError:\n"+e.getMessage()+"\n"+e.getLocalizedMessage()+"\n");
				e.printStackTrace();
				if (e instanceof TimeoutError) {
					T.showToast(context, R.string.error_timeout);
				} else if (e instanceof AuthFailureError) {
					T.showToast(context, R.string.error_auth_failure);
				} else if (e instanceof ServerError) {
					T.showToast(context, R.string.error_server);
				} else if (e instanceof NetworkError) {
					T.showToast(context, R.string.error_network);
				} else if (e instanceof NoConnectionError) {
					T.showToast(context, R.string.error_no_connection);
				} else {// com.android.volley.ParseError
					// T.showToast(context, e.getMessage());
					T.showToast(context, R.string.error_json);
				}
			}
		};
	}
	/**
	 * 网络连接出错后的统一处理方式
	 * 
	 * @param context
	 * @param errorEventTag
	 * @return
	 */
	public static Response.ErrorListener errorListener(final Context context, final String errorEventTag) {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError e) {
				((BaseAppActivity) context).closeProgress();
				EventBus.getDefault().post(errorEventTag);
				IOUtil.writeToSDCard("data.txt","VolleyError:\n"+e.getMessage()+"\n"+e.getLocalizedMessage()+"\n");
				e.printStackTrace();
				if (e instanceof TimeoutError) {
					T.showToast(context, R.string.error_timeout);
				} else if (e instanceof AuthFailureError) {
					T.showToast(context, R.string.error_auth_failure);
				} else if (e instanceof ServerError) {
					T.showToast(context, R.string.error_server);
				} else if (e instanceof NetworkError) {
					T.showToast(context, R.string.error_network);
				} else if (e instanceof NoConnectionError) {
					T.showToast(context, R.string.error_no_connection);
				} else {// com.android.volley.ParseError
					// T.showToast(context, e.getMessage());
					T.showToast(context, R.string.error_json);
				}
			}
		};
	}

	private static class ImageSize {
		int width;
		int height;
	}

	/**
	 * 根据ImageView获得适当的压缩的宽和高
	 * 
	 * @param view
	 * @return
	 */
	private static ImageSize getImageViewWidth(View view) {
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
		final LayoutParams params = view.getLayoutParams();

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : view.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getImageViewFieldValue(view, "mMaxWidth"); // Check maxWidth parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : view.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(view, "mMaxHeight"); // Check maxHeight parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		imageSize.width = width;
		imageSize.height = height;
//		Log.i("tag", "imageSize="+width+","+height);
		return imageSize;
	}

	/**
	 * 反射获得ImageView设置的最大宽度和高度
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
//				Log.e("TAG", value + "");
			}
		} catch (Exception e) {
		}
		return value;
	}
}
