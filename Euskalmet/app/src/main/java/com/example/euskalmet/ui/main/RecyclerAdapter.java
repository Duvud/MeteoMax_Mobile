package com.example.euskalmet.ui.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.euskalmet.MainActivity;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private List<Station> stationList;
    private LayoutInflater layoutInflater;
    private View.OnClickListener clickListener;
    private Context context;


    public RecyclerAdapter(Context context, List<Station> dataSet) {
        this.layoutInflater = LayoutInflater.from(context);
        stationList = dataSet;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onClick(view);
        }
    }

    public void updateMessageList(List<Station> messageList) {
        Log.d("", "Updating station list");
        this.stationList = messageList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.stationTextView);
            imageView = itemView.findViewById(R.id.stationImgView);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public void removeItem(int position) {
        stationList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, stationList.size());
    }

    Station getItem(int id) {
        return stationList.get(id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.frame_station, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    void setClickListener(View.OnClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (stationList != null && viewHolder.getTextView() != null) {
            viewHolder.getTextView().setText(stationList.get(position).name);
        }
    }

    @Override
    public int getItemCount() {
        if (stationList != null) {
            return stationList.size();
        }
        return 0;
    }
}
