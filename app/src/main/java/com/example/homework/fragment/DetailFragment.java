package com.example.homework.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.R;
import com.example.homework.api.ApiCallback;
import com.example.homework.api.UserApi;
import com.example.homework.model.UserDetail;
import com.example.homework.model.UsersList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import okhttp3.Response;

public class DetailFragment extends Fragment {




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_detail,container,false);

        gson = new Gson();

        Bundle bundle = getArguments();
        if (bundle != null) {
            login = bundle.getString("login");
        }

        ApiCallback callback = new ApiCallback() {
            @Override
            public void showMsg(String msg) {

            }

            @Override
            public void loadString(String response) {
                JsonObject jojo = new JsonParser().parse(response).getAsJsonObject();


                if(jojo != null){
                    Type collectionType = new TypeToken<UserDetail>() {
                    }.getType();
                    userDetail = gson.fromJson(jojo, collectionType);
                }

            }

            @Override
            public void loadComplete(Response response) {

            }
        };

        UserApi.callUserDetail(login,callback);


        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.first_name);
        location = view.findViewById(R.id.location);
        website = view.findViewById(R.id.website);
        back_btn = view.findViewById(R.id.back_btn);


        new Thread(new Runnable() {
            Drawable drawable = loadImageFromNetwork( userDetail.avatar_url);
            @Override

            public void run() {
                avatar.post(() -> {
                    //TODO Auto-generated method stub
                    avatar.setImageDrawable(drawable);
                });

            }
        }).start();

        name.setText(userDetail.name);
        location.setText(userDetail.location);
        website.setText(userDetail.blog);

        back_btn.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        return view;
    }

    private Drawable loadImageFromNetwork(String urlAddress) {

// TODO Auto-generated method stub

        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(urlAddress).openStream(), "image.jpg");
        } catch (IOException e) {

        }
//
//        if (drawable == null) {
//        } else {
//        }
        return drawable;
    }

    ImageView avatar;
    TextView name;
    TextView location;
    TextView website;
    ImageView back_btn;
    String login;
    UserDetail userDetail;
    Gson gson;
}
