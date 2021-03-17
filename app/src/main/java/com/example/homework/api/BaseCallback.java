package com.example.homework.api;

import okhttp3.Request;
import okhttp3.Response;

public interface BaseCallback<T> {

    void onFailure(Request request, Exception e);

    void onSuccess(Response response, T t);

    void onFalse(Response response, String err_msg);

    void onError(Response response, int errorCode, Exception e);
}