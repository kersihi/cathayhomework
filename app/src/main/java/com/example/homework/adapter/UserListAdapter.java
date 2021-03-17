package com.example.homework.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework.R;
import com.example.homework.model.UsersList;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {


    private ItemClickListener mClickListener;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<UsersList> mData;


    public UserListAdapter(Context context, List<UsersList> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    public void refreshData(List<UsersList> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        new Thread(new Runnable() {
            Drawable drawable = loadImageFromNetwork( mData.get(position).avatar_url);
            @Override

            public void run() {
                holder.avatar.post(() -> {
                    //TODO Auto-generated method stub
                    holder.avatar.setImageDrawable(drawable);
                });

            }
        }).start();


        holder.name.setText(mData.get(position).login);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView avatar;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.first_avatar);
            name = itemView.findViewById(R.id.first_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
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

}
