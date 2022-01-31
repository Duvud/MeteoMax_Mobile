package com.example.euskalmet.ui.main.recyclerAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.euskalmet.EuskalmetData.ServerRequest;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import java.util.List;

public class StationListRecyclerAdapter extends RecyclerView.Adapter<StationListRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private Boolean firstExec = true;
    private List<Station> stationList;
    private LayoutInflater layoutInflater;
    private View.OnClickListener clickListener;
    private Context context;
    private MeteoController meteoController;
    private ServerRequest serverRequest;


    public StationListRecyclerAdapter(Context context, List<Station> dataSet) {
        this.layoutInflater = LayoutInflater.from(context);
        meteoController = MeteoController.getMeteoController(context);
        stationList = dataSet;
        this.context = context;
        this.serverRequest = ServerRequest.getServerRequest(context, stationList);
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
        private final Switch stationSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.stationTextView);
            imageView = itemView.findViewById(R.id.stationImgView);
            stationSwitch = itemView.findViewById(R.id.stationSwitch);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public Switch getStationSwitch() {
            return stationSwitch;
        }
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (stationList != null && stationList.get(position) != null && viewHolder.getTextView() != null) {
            viewHolder.getStationSwitch().setChecked(stationList.get(position).enabled);
            viewHolder.getTextView().setText(stationList.get(position).name);
            viewHolder.getStationSwitch().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean isChecked = viewHolder.getStationSwitch().isChecked();
                    meteoController.changeEnabled(
                            stationList.get(viewHolder.getAdapterPosition()).id,
                            isChecked
                    );
                    if(isChecked){
                        serverRequest.getStationData(stationList.get(viewHolder.getAdapterPosition()).id);
                    }else{
                        meteoController.deleteReadingsFromStation(stationList.get(viewHolder.getAdapterPosition()).id);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (stationList != null) {
            return stationList.size();
        }
        return 0;
    }
}
