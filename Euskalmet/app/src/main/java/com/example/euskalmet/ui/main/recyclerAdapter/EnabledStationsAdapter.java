package com.example.euskalmet.ui.main.recyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.euskalmet.EuskalmetData.ServerRequest;
import com.example.euskalmet.R;
import com.example.euskalmet.Activity.ReadingDataActivity;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import java.util.HashMap;
import java.util.List;

public class EnabledStationsAdapter extends RecyclerView.Adapter<EnabledStationsAdapter.ViewHolder> implements View.OnClickListener{
    private Boolean firstExec = true;
    private List<Station> stationList;
    private List<Reading> readingList;
    private LayoutInflater layoutInflater;
    private HashMap<String, Double> precipitationMap;
    private HashMap<String, Double> temperatureMap;
    private HashMap<String, Double> humidityMap;
    private HashMap<String, Double> speedMap;
    boolean hasPrep, hasHumidity, hasSpeed, hasTemperature;


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
        private final TextView precipitationView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.enabledStationTextView);
            precipitationView = itemView.findViewById(R.id.readingsTextView);
            imageView = itemView.findViewById(R.id.enabledStationImgView);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getPrecipitationView() {
            return precipitationView;
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    public void updateEnabledStations(List<Station> stationList) {
        Log.d("", "Updating enabled station list");
        this.stationList = stationList;
        notifyDataSetChanged();
    }

    public void updateReadings(List<Reading> readingList) {
        this.readingList = readingList;
        loadReadings(readingList);
        notifyDataSetChanged();
        //notifyAll();
    }

    public void loadReadings(List<Reading> readingList) {
        precipitationMap = new HashMap<>();
        temperatureMap = new HashMap<>();
        humidityMap = new HashMap<>();
        speedMap = new HashMap<>();

        for(int i=0; i<readingList.size(); i++) {
            Reading currentReading = readingList.get(i);
            switch (currentReading.readingType) {
                case "temperature" :
                    temperatureMap.put(currentReading.stationId, currentReading.readingData);
                break;
                case "precipitation":
                    precipitationMap.put(currentReading.stationId, currentReading.readingData);
                break;
                case "humidity" :
                    humidityMap.put(currentReading.stationId, currentReading.readingData);
                break;
                case "speed":
                    speedMap.put(currentReading.stationId, currentReading.readingData);
                break;
            }
        }
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
        Double precipitation = null, humidity = null, speed = null, temperature = null;
        if (stationList != null && stationList.get(position) != null && viewHolder.getTextView() != null) {
            viewHolder.getTextView().setText(stationList.get(position).name);
            viewHolder.precipitationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent readingDataActivityIntent;
                    readingDataActivityIntent =  new Intent(context, ReadingDataActivity.class);
                    readingDataActivityIntent.putExtra("stationId", stationList.get(viewHolder.getAdapterPosition()).id);
                    readingDataActivityIntent.putExtra("stationName", stationList.get(viewHolder.getAdapterPosition()).name);
                    context.startActivity(readingDataActivityIntent);
                }
            });
            String readingsString = "";
            if (precipitationMap != null && precipitationMap.get(stationList.get(position).id) != null) {
                readingsString +=
                        "Precipitation : " + precipitationMap.get(stationList.get(position).id).toString() + "mm\n" ;
                hasPrep = true;
                precipitation = precipitationMap.get(stationList.get(position).id);
            } else {
                hasPrep = false;
                readingsString +=  "Precipitation : N/A \n";
            }

            if (temperatureMap != null && temperatureMap.get(stationList.get(position).id) != null) {
                readingsString +=
                                "Temperature : " + temperatureMap.get(stationList.get(position).id).toString() + "°C\n";
                hasTemperature = true;
                temperature = temperatureMap.get(stationList.get(position).id);
            } else {
                hasTemperature = false;
                readingsString += "Temperature : N/A \n";
            }

            if (humidityMap != null && humidityMap.get(stationList.get(position).id) != null) {
                readingsString +=
                                "Humidity : " + humidityMap.get(stationList.get(position).id).toString() + "%\n" ;
                hasHumidity = true;
                humidity = humidityMap.get(stationList.get(position).id);
            } else {
                hasHumidity = false;
                readingsString += "Humidity : N/A \n";
            }
            if (speedMap != null && speedMap.get(stationList.get(position).id) != null) {
                readingsString +=
                        "Air speed : " + speedMap.get(stationList.get(position).id).toString() + "km/h" ;
                hasSpeed = true;
                speed = speedMap.get(stationList.get(position).id);
            } else {
                readingsString += "Air speed : N/A ";
                hasSpeed = false;
            }
            viewHolder.getPrecipitationView().setText(readingsString);
            setImage(viewHolder, precipitation);
        }
    }

    public void setImage(@NonNull EnabledStationsAdapter.ViewHolder viewHolder, Double precipitation) {
        if(precipitation == null){
            viewHolder.getImageView().setBackgroundResource(R.drawable.error);
            return;
        }
        if(precipitation != null && precipitation == 0){
            viewHolder.getImageView().setBackgroundResource(R.drawable.sunny);
            return;
        }
        if(precipitation != null && precipitation != 0){
            viewHolder.getImageView().setBackgroundResource(R.drawable.rain);
            return;
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
