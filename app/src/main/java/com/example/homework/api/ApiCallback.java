package com.example.homework.api;

import okhttp3.Response;

public interface ApiCallback {
    void showMsg(String msg);
    void loadString(String response);
    void loadComplete(Response response);
}
