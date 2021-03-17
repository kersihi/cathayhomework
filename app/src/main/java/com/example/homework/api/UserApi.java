package com.example.homework.api;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;

public class UserApi {
    private static Map<String, Long> mapTimeStamp = new HashMap<>();

    private  HttpHelper mHttpHelper = HttpHelper.getInstance();

    public static void callListUsers(ApiCallback mCallback) {
        long timeStamp = Calendar.getInstance().getTimeInMillis();

        if (!isCallApiFrequently("ListUsers", timeStamp)) {
            String api = "users";

            Map<String, Object> paramMap = new HashMap<>();
//            try {
//                paramMap.put("", "");
//                paramMap.put("","");
//            } catch (Exception e) {
//                paramMap.put("", "");
//                paramMap.put("", "");
//            }

            HttpHelper mHttpHelper = HttpHelper.getInstance();
            mHttpHelper.get(api, null, String[].class, paramMap, new BaseCallback<String>() {
                @Override
                public void onFailure(Request request, Exception e) {
                    mCallback.showMsg(e.getMessage());
                }

                @Override
                public void onSuccess(Response response, String s) {
                    String result = null;
                    if (s != null) {
                        try {
                            result = s;
                            Log.e("AndroidR",s);
                            mCallback.loadString(result);
                        } catch (Exception e) {
                            mCallback.loadString(result);
                        }
                    }
                }

                @Override
                public void onFalse(Response response, String err_msg) {
                    mCallback.showMsg(err_msg);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    mCallback.showMsg(response.message());
                }
            });
        } else {
            mCallback.showMsg("");
        }
    }


    public static void callUserDetail(String login, ApiCallback mCallback){
        long timeStamp = Calendar.getInstance().getTimeInMillis();

        if (!isCallApiFrequently("ListUsers", timeStamp)) {
            String api = "users"+"/"+login;

            Map<String, Object> paramMap = new HashMap<>();
//            try {
//                paramMap.put("", "");
//                paramMap.put("","");
//            } catch (Exception e) {
//                paramMap.put("", "");
//                paramMap.put("", "");
//            }

            HttpHelper mHttpHelper = HttpHelper.getInstance();
            mHttpHelper.get(api, null, String[].class, paramMap, new BaseCallback<String>() {
                @Override
                public void onFailure(Request request, Exception e) {
                    mCallback.showMsg(e.getMessage());
                }

                @Override
                public void onSuccess(Response response, String s) {
                    String result = null;
                    if (s != null) {
                        try {
                            result = s;
                            Log.e("AndroidR",s);
                            mCallback.loadString(result);
                        } catch (Exception e) {
                            mCallback.loadString(result);
                        }
                    }
                }

                @Override
                public void onFalse(Response response, String err_msg) {
                    mCallback.showMsg(err_msg);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    mCallback.showMsg(response.message());
                }
            });
        } else {
            mCallback.showMsg("");
        }
    }



    public static boolean isCallApiFrequently(String key, long newTimeStamp) {
        boolean res;

        if (!mapTimeStamp.containsKey(key)) {
            mapTimeStamp.put(key, newTimeStamp);
            res = false;
        } else {
            long oldTimeStamp = mapTimeStamp.get(key);
            long periodTime = (newTimeStamp - oldTimeStamp);

            if (periodTime <= 1000) {
                res = true;
            } else {
                mapTimeStamp.put(key, newTimeStamp);
                res = false;
            }
        }
        return res;
    }
}
