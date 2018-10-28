package com.base.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * http://blog.csdn.net/bboyfeiyu/article/details/42266869
 * 
 * @author minrui
 * @date 2014-10-23
 */
@Deprecated
public class MultipartRequest extends Request<JSONObject>
{

    private MultipartRequestParams params = null;

    private HttpEntity httpEntity = null;

    private Listener<JSONObject> listener;

//    public MultipartRequest(String url, MultipartRequestParams params,Listener<JSONObject> listener, ErrorListener errorListener)
//    {
//        this(Method.POST, url, params, errorListener);
//        mListener = listener;
//    }

    public MultipartRequest(String url, MultipartRequestParams params, Listener<JSONObject> listener, ErrorListener errorListener)
    {
        super(Request.Method.POST, url, errorListener);
        this.params = params;
        this.listener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (params != null)
        {
            httpEntity = params.getEntity();
            if (httpEntity == null)
            {
                return null;
            }
            try
            {
                httpEntity.writeTo(baos);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
//            String str = new String(baos.toByteArray());
            // Log.d("tag", "bodyString is :" + str);
        }
        return baos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(response));
        }
        catch (JSONException je)
        {
            return Response.error(new ParseError(response));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap()))
        {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    @Override
    public String getBodyContentType()
    {
        if (httpEntity == null)
        {
            return super.getBodyContentType();
        }
        return httpEntity.getContentType().getValue();
    }

    @Override
    protected void deliverResponse(JSONObject response)
    {
        if (listener != null)
        {
        	listener.onResponse(response);
        }
    }
}