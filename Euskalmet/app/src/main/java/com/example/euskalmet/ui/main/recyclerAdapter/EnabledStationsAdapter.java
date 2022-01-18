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

public class EnabledStationsAdapter extends RecyclerView.Adapter<EnabledStationsAdapter.ViewHolder> implements View.OnClickListener{
    private Boolean firstExec = true;
    private List<Station> stationList;
    private LayoutInflater layoutInflater;
    private View.OnClickListener clickListener;
    private Context context;
    private MeteoController meteoController;
    private ServerRequest serverRequest;

    public EnabledStationsAdapter(Context context, List<Station> dataSet) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final Switch stationSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.enabledStationTextView);
            imageView = itemView.findViewById(R.id.enabledStationImgView);
            stationSwitch = itemView.findViewById(R.id.enabledStationSwitch);
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

    public void updateEnabledStations(List<Station> stationList) {
        Log.d("", "Updating enabled station list");
        this.stationList = stationList;
        notifyDataSetChanged();
    }

    Station getItem(int id) {
        return stationList.get(id);
    }


    @NonNull
    @Override
    public EnabledStationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.from(viewGroup.getContext()).inflate(R.layout.frame_enabled_station, viewGroup, false);
        view.setOnClickListener(this);
        return new EnabledStationsAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull EnabledStationsAdapter.ViewHolder viewHolder, int position) {
        if (stationList != null && stationList.get(position) != null && viewHolder.getTextView() != null) {
            viewHolder.getTextView().setText(stationList.get(position).name);
            if (firstExec) {
                viewHolder.getStationSwitch().setChecked(stationList.get(position).enabled);
            }
            viewHolder.getStationSwitch().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firstExec = false;
                }
            });
            viewHolder.getStationSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!firstExec) {
                        meteoController.changeEnabled(
                                stationList.get(viewHolder.getAdapterPosition()).id,
                                isChecked
                        );
                    }
                    if(isChecked) {
                        //serverRequest.getStationData(stationList.get(viewHolder.getAdapterPosition()).id);
                    }
                }
            });
        }
    }


    @Override public int getItemViewType(int position) { return position; }
    @Override
    public int getItemCount() {
        if (stationList != null) {
            return stationList.size();
        }
        return 0;
    }
}
