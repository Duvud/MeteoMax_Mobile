package com.example.euskalmet.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  implements View.OnClickListener{

    private List<Station> stationList;
    private LayoutInflater layoutInflater;
    private View.OnClickListener clickListener;

    public RecyclerAdapter(Context context, List<Station> dataSet){
        this.layoutInflater = LayoutInflater.from(context);
        stationList = dataSet;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onClick(view);
        }
    }

    public void updateMessageList(List<Station> messageList){
        this.stationList = messageList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView1;

        ViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.stationTextView);
        }

        public TextView getTextView1() {
            return textView1;
        }
    }

    public void removeItem(int position){
        stationList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, stationList.size());
    }

    public void onItemMove(int fromPosition, int toPosition) {
        stationList.add(toPosition, stationList.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    Station getItem(int id) {
        return stationList.get(id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.station_list_fragment,viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    void setClickListener(View.OnClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if(stationList != null && viewHolder.getTextView1() != null) {
            viewHolder.getTextView1().setText(stationList.get(position).name);
        }
    }

    @Override
    public int getItemCount() {
        if(stationList != null){
            return stationList.size();
        }
        return 0;
    }
}
