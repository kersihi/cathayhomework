package com.example.homework.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework.R;
import com.example.homework.adapter.UserListAdapter;
import com.example.homework.api.ApiCallback;
import com.example.homework.api.UserApi;
import com.example.homework.model.UsersList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;

public class FirstFragment extends Fragment implements UserListAdapter.ItemClickListener{

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        gson = new Gson();
        adapter = new UserListAdapter(getContext(),usersList);
        adapter.setClickListener(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.usersList);
        recyclerView.setAdapter(adapter);
        ApiCallback apiCallback = new ApiCallback() {
            @Override
            public void showMsg(String msg) {

            }

            @Override
            public void loadString(String response) {

                JsonArray jojo = new JsonParser().parse(response).getAsJsonArray();


                if(jojo != null){
                    Type collectionType = new TypeToken<List<UsersList>>() {
                    }.getType();
                    usersList = gson.fromJson(jojo, collectionType);
                }

                if(usersList.size() > 0)
                    adapter.refreshData(usersList);

            }

            @Override
            public void loadComplete(Response response) {

            }
        };
        UserApi.callListUsers(apiCallback);

//
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    Gson gson;
    UserListAdapter adapter;
    List<UsersList> usersList;
    RecyclerView recyclerView;

    @Override
    public void onItemClick(View view, int position) {
        FragmentTransaction ft;
        if (getFragmentManager() != null) {
            ft = getFragmentManager().beginTransaction();
            try {
                int pos = (position - 1);
                Fragment target = new DetailFragment();
                Bundle data = new Bundle();
                data.putString("login", usersList.get(pos).login);
                target.setArguments(data);

                ft.replace(R.id.main_activity, target).addToBackStack(null).commit();
            } catch (Exception ignored) {
                Log.d("Exception",ignored.toString());
            }
        }
    }
}