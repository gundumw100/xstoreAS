package com.base.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @author pythoner
 * 
 */
public class FaceUtil {

	private String[] items = new String[] { "图库", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faces.jpg";
	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int SELECT_PIC_KITKAT = 3;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private Activity context;
	private String uploadUrl;

	private final int ST_UPLOAD_SUCCESS=0;
	private final int ST_UPLOAD_FAIL=1;
	
	Handler handler = new Handler(){   
        public void handleMessage(Message msg) { 
        	super.handleMessage(msg);  
            switch (msg.what) {      
            case ST_UPLOAD_SUCCESS:
            	if (onFaceUploadListener != null) {
            		DataWrap datas=(DataWrap)msg.obj;
            		try {
	                	JSONObject response = new JSONObject(datas.data);
	                	JSONObject result = response.optJSONObject("result");
	        			int code = result.optInt("code");
	        			if (code == 2000) {
	        				JSONObject data = response.optJSONObject("data");
	        				JSONObject urls = data.optJSONObject("urls");
	        				String orignUrl = urls.optString("origin", null);
	        				String imgUrl = urls.optString("100_100", null);
	        				String circleImgUrl = urls.optString("100_100_circle", null);
	        				onFaceUploadListener.onFaceUploadSuccess(datas.photo, orignUrl,imgUrl, circleImgUrl);
	        			}else{
	        				onFaceUploadListener.onFaceUploadFailed(datas.photo);
	        			}
            		} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						onFaceUploadListener.onFaceUploadFailed(datas.photo);
					}
        		}
            	
                break;  
            case ST_UPLOAD_FAIL:
            	if (onFaceUploadListener != null) {
            		DataWrap datas=(DataWrap)msg.obj;
            		onFaceUploadListener.onFaceUploadFailed(datas.photo);
            	}
            	break;
            	default:
            		break;
            }      
            
        }  
          
    };  
    
	public FaceUtil(Activity context,String uploadUrl) {
		this.context = context;
		this.uploadUrl = uploadUrl;
//		String uploadUrl=Commands.URL_SYSTEMS_UPLOAD_FILE+"?user_session_key="+BaseApp.user.getUser_session_key()+"&file_category=1";
	}

	public void showSettingFaceDialog() {
		new AlertDialog.Builder(context)
				.setTitle("图片来源")
				.setCancelable(true)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent=null;
						switch (which) {
						case 0:// Local Image
							intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
								context.startActivityForResult(intent, SELECT_PIC_KITKAT);
							} else {
								context.startActivityForResult(intent, IMAGE_REQUEST_CODE);
							}
							break;
						case 1:// Take Picture
							intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (hasSdcard()) {
								///storage/sdcard0
								Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME));
								intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
							}
							context.startActivityForResult(intent, CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case FaceUtil.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case FaceUtil.SELECT_PIC_KITKAT:
				startPhotoZoom(data.getData());
				break;
			case FaceUtil.CAMERA_REQUEST_CODE:
				File tempFile = new File(
						Environment.getExternalStorageDirectory(),
						FaceUtil.IMAGE_FILE_NAME);
				if (tempFile.exists()) {
					startPhotoZoom(Uri.fromFile(tempFile));
				}

				break;
			case FaceUtil.RESULT_REQUEST_CODE:
				if (data != null) {
					setData(data);
				}
				break;

			}

		}
	}

	private boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
			return;
		}

		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = getPath(context, uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}

		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 480);
		intent.putExtra("return-data", true);
		context.startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setData(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			if (photo != null) {
				
//				if(onFaceUploadListener!=null){
//					onFaceUploadListener.onFaceUploadSuccess(photo, null,null, null);
//				}
				
				//这里是保存图片到本地
				saveBitmap(photo);
				//然后上传图片到服务端保存;
				//此处每个人实现是不一样的，一般需要重写uploadFace方法;
				//我这里没有抽成接口，为了快速迭代使用；
				//但是，我在uploadFace成功后回调onFaceUploadSuccess方法；
				uploadFace(photo);
			}

		}
	}

	private OnFaceUploadListener onFaceUploadListener;

	public void setOnFaceUploadListener(
			OnFaceUploadListener onFaceUploadListener) {
		this.onFaceUploadListener = onFaceUploadListener;
	}

	public interface OnFaceUploadListener {
		void onFaceUploadSuccess(Bitmap photo, String orignUrl,
				String imgUrl, String circleImgUrl);
		void onFaceUploadFailed(Bitmap photo);
	}

	private void saveBitmap(Bitmap bitmap) {
		File f = new File(Environment.getExternalStorageDirectory(),
				IMAGE_FILE_NAME);
		try {
			f.createNewFile();
			FileOutputStream fos = null;
			fos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void uploadFace(final Bitmap photo) {
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
				String filePath = file.getAbsolutePath();
				uploadFile(photo,filePath,uploadUrl);
			}
		};
		Timer t=new Timer();
		t.schedule(task, 5);
		
		//使用volley上传，该方法可能会报错
		/*Commands.uploadFile(context, 1, App.user, filePath,
				new VolleyHelper.OnFileUploadListener() {
					@Override
					public void onFileUploadSuccess(String orignUrl,
							String imgUrl, String circleImgUrl) {
						// TODO Auto-generated method stub
						if (onFaceUploadSuccessListener != null) {
							onFaceUploadSuccessListener
									.onFaceUploadSuccess(view, photo, orignUrl,
											imgUrl, circleImgUrl);
						}
					}

				});*/
	}

	
	/**
	 * 上传文件至Server
	 * @param photo
	 * @param filePath 文件路径
	 * @param urlString 连接地址
	 */
    private void uploadFile(Bitmap photo,String filePath,String urlString)
    {
	if(TextUtils.isEmpty(urlString)||!urlString.startsWith("http")){
		Log.i("tag", "上传失败，无效的url") ;
		return;
	}
      String end ="\r\n";
      String twoHyphens ="--";
      String boundary ="*****";
      try
      {
    	  URL url =new URL(urlString);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        DataOutputStream dos =new DataOutputStream(con.getOutputStream());
        dos.writeBytes(twoHyphens + boundary + end);
        dos.writeBytes("Content-Disposition: form-data; "+"name=\"file1\";filename=\""+IMAGE_FILE_NAME +"\""+ end);
        dos.writeBytes(end);  
        FileInputStream fis =new FileInputStream(filePath);
        int bufferSize =1024;
        byte[] buffer =new byte[bufferSize];
        int length =-1;
        /* 从文件读取数据至缓冲区 */
        while((length = fis.read(buffer)) !=-1)
        {
        	dos.write(buffer, 0, length);
        }
        dos.writeBytes(end);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
        dos.flush();
        dos.close();
        fis.close();
        /* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 )
        {
          b.append( (char)ch );
        }
        is.close();
        
        DataWrap obj=new DataWrap(photo,b.toString());
        sendMessage(ST_UPLOAD_SUCCESS,obj);
      }
      catch(Exception e)
      {
    	  e.printStackTrace();
    	  DataWrap obj=new DataWrap(photo,null);
    	  sendMessage(ST_UPLOAD_FAIL,obj);
      }
    
    }
    
    private void sendMessage(int st,Object obj){
    	Message message = new Message();      
        message.what = st;     
        message.obj=obj;
        handler.sendMessage(message);
    }
    
    class DataWrap{
    	Bitmap photo;
    	String data;
    	DataWrap(Bitmap photo,String data){
    		this.photo=photo;
    		this.data=data;
    	}
    }
    
	@SuppressLint("NewApi")
	private static String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	private static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

}
