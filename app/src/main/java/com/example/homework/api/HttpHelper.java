package com.example.homework.api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.homework.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {
    private static OkHttpClient mClientInstance;
    private volatile static HttpHelper mHttpHelperInstance;
    private Handler mHandler;
    public Context mContext;
    private JsonObject returnData;
    private static final Class STRING_TYPE = String.class;
    private static Map<String, String> ActivityImageList;
    private String userID;
    final String basicAuth = "Basic " + Base64.encodeToString("ThinkpowerArthur:tt32145+".getBytes(), Base64.NO_WRAP);

    public static final String API_BASE_URL = "https://api.github.com/";

    private HttpHelper() {

        mClientInstance = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new TokenInterceptor())
                .build();

        mHandler = new Handler(Looper.getMainLooper());
    }

    public static HttpHelper getInstance() {

        if (mHttpHelperInstance == null) {
            synchronized (HttpHelper.class) {
                if (mHttpHelperInstance == null) {
                    mHttpHelperInstance = new HttpHelper();
                }

                if (ActivityImageList == null) {
                    ActivityImageList = new TreeMap<>();
                }
            }
        }
        return mHttpHelperInstance;
    }
    public void setContext(Context ctx) {
        mContext = ctx;
    }

//    public String getRefreshToken() {
//        return refreshToken;
//    }

    public void setActivityImage(Map<String, String> ActivityImageList) {
        HttpHelper.ActivityImageList = ActivityImageList;
    }

    public Map<String, String> getActivityImage() {
        return ActivityImageList;
    }

    //暫存
    public void setUserId(String id) {
        userID = id;
    }

    public String getUserId() {
        return userID;
    }

    public JsonObject getReturnData() {
        return returnData;
    }

    public void setReturnData(JsonObject res) {
        returnData = res;
    }

    private void request(final Request request, final Class type, final BaseCallback callback) {

        Log.e("AndroidR",request.toString());

        mClientInstance.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    Exception customException = new Exception("errorMsg");
                    callbackFailure(request, callback, customException);
                } catch (Exception ex) {
                    callbackFailure(request, callback, e);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resString = null;
                String err_msg;
                String returnMsg = null;
                if (response.body() != null) {
                    if (response.header("Content-Type") != null) {
                        if (response.header("Content-Type").contains("image")) {
                            byte[] pbytes = response.body().bytes();
                            resString = Base64.encodeToString(pbytes, Base64.NO_WRAP);
                        } else {
                            resString = response.body().string();
                        }
                    } else {
                        resString = response.body().string();
                    }
                }
                if (response.isSuccessful()) {
                    if (resString != null) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(resString);
                            returnMsg = jsonObject.getString("returnMsg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callbackError(response, callback, e);
                        }

                        callbackSuccess(response,resString,callback);

                    }
                } else {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(resString);

//                        if (response.code() == 700) {
//                            err_msg = String.format(
//                                    mContext.getString(R.string.intenal_server_errmsg), 700);
//                        } else {
//                            err_msg = jsonObject.getString("returnMsg");
//                        }
                        err_msg = jsonObject.getString("returnMsg");

                        if((response.code() == 500) || (response.code() == 400))
                            err_msg = String.format("errorMsg", response.code());

                        callbackFalse(response, err_msg, callback);
                    } catch (JSONException e) {
                        if ((response.code() == 500) || (response.code() == 400)) {
                            String err = String.format("errorMsg", response.code());
                            callbackFalse(response, err, callback);
                        } else {
                            callbackError(response, callback, e);
                        }
                    }
                }
            }
        });
    }

    private void callbackSuccess(final Response response, final Object o, final BaseCallback callback) {
        mHandler.post(() -> callback.onSuccess(response, o));
    }

    private void callbackError(final Response response, final BaseCallback callback, final Exception e) {
        mHandler.post(() -> callback.onError(response, response.code(), e));
    }

    private void callbackFailure(final Request request, final BaseCallback callback, final Exception e) {
        mHandler.post(() -> callback.onFailure(request, e));
    }

    private void callbackFalse(final Response response, final String err_msg, final BaseCallback callback) {
        mHandler.post(() -> callback.onFalse(response, err_msg));
    }

//    public void get(String url, String token, Class type, BaseCallback callback) {
//        Request request = buildRequest(url, token, null, HttpMethodType.GET);
//        request(request, type, callback);
//    }
//
//    public void get(String url, String token, String header, Class type, BaseCallback callback) {
//        Request request = buildRequest(url, token, header, null, HttpMethodType.GET);
//        request(request, type, callback);
//    }

    public void get(String url, String token, Class type, Map<String, Object> params, BaseCallback callback) {
        Request request = buildRequest(url, token, params, HttpMethodType.GET);
        request(request, type, callback);
    }

//    public void post(String url, String token, Class type, Map<String, Object> params,
//                     BaseCallback callback) {
//        Request request = buildRequest(url, token, params, HttpMethodType.POST);
//        request(request, type, callback);
//    }


    private Request buildRequest(String url, String token, Map<String, Object> params, HttpMethodType type) {
        Request.Builder builder = new Request.Builder();
        builder.url(API_BASE_URL+url)
                .addHeader("Authorization", basicAuth)
                .addHeader("host","api.github.com")
                .addHeader("Accept", "application/vnd.github.v3+json");
        if (type == HttpMethodType.GET) {
            builder.get();
        } else if (type == HttpMethodType.POST) {
            RequestBody body = buildRequestBody(params, url, "POST");
            builder.post(body);
        }
        return builder.build();
    }


    private RequestBody buildRequestBody(final Map<String, Object> params, String url, String method) {
        String route;

        route = ("users/"+url.replace(API_BASE_URL, ""));

        JSONObject jsonObject = new JSONObject();
        try {
            if (params != null) {
                for (Map.Entry<String, Object> next : params.entrySet()) {
                    String key = next.getKey();
                    Object value = next.getValue();
                    jsonObject.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Method", method);
            jsonBody.put("Route", route);
            if (params != null) {
                jsonBody.put("Body", jsonObject.toString());
            } else {
                jsonBody.put("Body", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String bodyString = jsonBody.toString();
        return RequestBody.create(JSON, bodyString);
    }

    enum HttpMethodType {
        GET,
        POST
    }
}
