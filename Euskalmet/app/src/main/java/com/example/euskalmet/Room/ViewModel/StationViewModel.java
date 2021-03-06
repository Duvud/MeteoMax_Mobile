package com.example.euskalmet.Room.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import java.util.List;

public class StationViewModel extends AndroidViewModel {
    private LiveData<List<Station>> stationList;
    private MeteoController meteoController;

    public StationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMeteoController(MeteoController meteoController) {
        this.meteoController = meteoController;
    }

    public LiveData<List<Station>> getStations() {
        stationList = meteoController.getLiveStations();
        if(stationList.getValue() != null && stationList.getValue().size() == 0){
            stationList = meteoController.getLiveStations();
        }
        return stationList;
    }
}
