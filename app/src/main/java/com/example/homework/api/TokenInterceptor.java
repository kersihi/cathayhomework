package com.example.homework.api;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (isTokenExpired(response)) {
            HttpHelper mHttpHelper = HttpHelper.getInstance();
            try {
                if (mHttpHelper.mContext != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mHttpHelper.mContext);
                        builder.setMessage("連線超時");
                        builder.setCancelable(false);
                        builder.setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        builder.show();
                    });
                }
            } catch (Exception ignored) {
            }
        }
        return response;
    }


    private boolean isTokenExpired(Response response) {
        return response.code() == 401;
    }
}